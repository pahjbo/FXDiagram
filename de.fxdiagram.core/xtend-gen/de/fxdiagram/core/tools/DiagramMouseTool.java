package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.viewport.ViewportTransform;
import java.util.Set;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class DiagramMouseTool implements XDiagramTool {
  public static class DragContext {
    @Accessors
    private double sceneX;
    
    @Accessors
    private double sceneY;
    
    @Accessors
    private double screenX;
    
    @Accessors
    private double screenY;
    
    @Accessors
    private double previousScale = 1;
    
    @Accessors
    private Point2D pivotInDiagram;
    
    @Pure
    public double getSceneX() {
      return this.sceneX;
    }
    
    public void setSceneX(final double sceneX) {
      this.sceneX = sceneX;
    }
    
    @Pure
    public double getSceneY() {
      return this.sceneY;
    }
    
    public void setSceneY(final double sceneY) {
      this.sceneY = sceneY;
    }
    
    @Pure
    public double getScreenX() {
      return this.screenX;
    }
    
    public void setScreenX(final double screenX) {
      this.screenX = screenX;
    }
    
    @Pure
    public double getScreenY() {
      return this.screenY;
    }
    
    public void setScreenY(final double screenY) {
      this.screenY = screenY;
    }
    
    @Pure
    public double getPreviousScale() {
      return this.previousScale;
    }
    
    public void setPreviousScale(final double previousScale) {
      this.previousScale = previousScale;
    }
    
    @Pure
    public Point2D getPivotInDiagram() {
      return this.pivotInDiagram;
    }
    
    public void setPivotInDiagram(final Point2D pivotInDiagram) {
      this.pivotInDiagram = pivotInDiagram;
    }
  }
  
  public enum State {
    SCROLL,
    
    ZOOM_IN,
    
    ZOOM_OUT,
    
    MARQUEE,
    
    MARQUEE_XOR;
  }
  
  private XRoot root;
  
  private DiagramMouseTool.DragContext dragContext;
  
  private EventHandler<MouseEvent> pressedHandler;
  
  private EventHandler<MouseEvent> dragHandler;
  
  private EventHandler<MouseEvent> releasedHandler;
  
  private static final int ZOOM_SENSITIVITY = 30;
  
  private boolean hasDragged = false;
  
  private DiagramMouseTool.State currentState;
  
  private static final ImageCursor zoomInCursor = new ImageCursor(ImageCache.get().getImage(DiagramMouseTool.class, "zoom_in.png"));
  
  private static final ImageCursor zoomOutCursor = new ImageCursor(ImageCache.get().getImage(DiagramMouseTool.class, "zoom_out.png"));
  
  private Rectangle marquee = new Rectangle();
  
  private Set<XShape> previousSelection;
  
  public DiagramMouseTool(final XRoot root) {
    this.root = root;
    final EventHandler<MouseEvent> _function = (MouseEvent event) -> {
      this.hasDragged = false;
      this.applyToState(event);
      event.consume();
    };
    this.pressedHandler = _function;
    final EventHandler<MouseEvent> _function_1 = (MouseEvent it) -> {
      boolean _notEquals = (!Objects.equal(this.dragContext, null));
      if (_notEquals) {
        this.hasDragged = true;
        boolean _applyToState = this.applyToState(it);
        if (_applyToState) {
          return;
        }
        final DiagramMouseTool.State currentState = this.currentState;
        if (currentState != null) {
          switch (currentState) {
            case SCROLL:
              ViewportTransform _viewportTransform = root.getViewportTransform();
              double _sceneX = it.getSceneX();
              double _plus = (this.dragContext.sceneX + _sceneX);
              _viewportTransform.setTranslateX(_plus);
              ViewportTransform _viewportTransform_1 = root.getViewportTransform();
              double _sceneY = it.getSceneY();
              double _plus_1 = (this.dragContext.sceneY + _sceneY);
              _viewportTransform_1.setTranslateY(_plus_1);
              break;
            case ZOOM_IN:
            case ZOOM_OUT:
              double _screenX = it.getScreenX();
              double _minus = (_screenX - this.dragContext.screenX);
              double _screenY = it.getScreenY();
              double _minus_1 = (_screenY - this.dragContext.screenY);
              double _norm = Point2DExtensions.norm(_minus, _minus_1);
              double _divide = (_norm / DiagramMouseTool.ZOOM_SENSITIVITY);
              double totalZoomFactor = (1 + _divide);
              boolean _equals = Objects.equal(this.currentState, DiagramMouseTool.State.ZOOM_OUT);
              if (_equals) {
                totalZoomFactor = (1 / totalZoomFactor);
              }
              final double scale = (totalZoomFactor / this.dragContext.previousScale);
              root.getViewportTransform().scaleRelative(scale);
              final Point2D pivotInScene = root.getDiagram().localToScene(this.dragContext.pivotInDiagram);
              ViewportTransform _viewportTransform_2 = root.getViewportTransform();
              double _sceneX_1 = it.getSceneX();
              double _x = pivotInScene.getX();
              double _minus_2 = (_sceneX_1 - _x);
              double _sceneY_1 = it.getSceneY();
              double _y = pivotInScene.getY();
              double _minus_3 = (_sceneY_1 - _y);
              _viewportTransform_2.translateRelative(_minus_2, _minus_3);
              this.dragContext.previousScale = totalZoomFactor;
              break;
            case MARQUEE:
            case MARQUEE_XOR:
              final Point2D position = root.getDiagramCanvas().screenToLocal(this.dragContext.screenX, this.dragContext.screenY);
              double _screenX_1 = it.getScreenX();
              final double newWidth = (_screenX_1 - this.dragContext.screenX);
              if ((newWidth < 0)) {
                double _x_1 = position.getX();
                double _plus_2 = (_x_1 + newWidth);
                this.marquee.setX(_plus_2);
                this.marquee.setWidth((-newWidth));
              } else {
                this.marquee.setX(position.getX());
                this.marquee.setWidth(newWidth);
              }
              double _screenY_1 = it.getScreenY();
              final double newHeight = (_screenY_1 - this.dragContext.screenY);
              if ((newHeight < 0)) {
                double _y_1 = position.getY();
                double _plus_3 = (_y_1 + newHeight);
                this.marquee.setY(_plus_3);
                this.marquee.setHeight((-newHeight));
              } else {
                this.marquee.setY(position.getY());
                this.marquee.setHeight(newHeight);
              }
              final Bounds marqueeBounds = root.getDiagram().sceneToLocal(this.marquee.localToScene(this.marquee.getBoundsInLocal()));
              final Consumer<XNode> _function_2 = (XNode it_1) -> {
                it_1.setSelected(this.isMarqueeSelected(it_1, marqueeBounds));
              };
              root.getDiagram().getNodes().forEach(_function_2);
              ObservableList<XConnection> _connections = root.getDiagram().getConnections();
              for (final XConnection connection : _connections) {
                boolean _isSelectable = connection.isSelectable();
                if (_isSelectable) {
                  final Function1<XControlPoint, Boolean> _function_3 = (XControlPoint it_1) -> {
                    return Boolean.valueOf(this.isMarqueeSelected(it_1, marqueeBounds));
                  };
                  final Set<XControlPoint> selectedControlPoints = IterableExtensions.<XControlPoint>toSet(IterableExtensions.<XControlPoint>filter(connection.getControlPoints(), _function_3));
                  boolean _isEmpty = selectedControlPoints.isEmpty();
                  boolean _not = (!_isEmpty);
                  connection.setSelected(_not);
                  boolean _selected = connection.getSelected();
                  if (_selected) {
                    final Consumer<XControlPoint> _function_4 = (XControlPoint it_1) -> {
                      it_1.setSelected(selectedControlPoints.contains(it_1));
                    };
                    connection.getControlPoints().forEach(_function_4);
                  } else {
                    final Consumer<XControlPoint> _function_5 = (XControlPoint it_1) -> {
                      it_1.setSelected(false);
                    };
                    connection.getControlPoints().forEach(_function_5);
                  }
                }
              }
              break;
            default:
              break;
          }
        }
        it.consume();
      }
    };
    this.dragHandler = _function_1;
    final EventHandler<MouseEvent> _function_2 = (MouseEvent it) -> {
      if (this.hasDragged) {
        this.hasDragged = false;
        this.dragContext = null;
        it.consume();
      }
      CoreExtensions.<Rectangle>safeDelete(root.getDiagramCanvas().getChildren(), this.marquee);
      this.currentState = null;
      Scene _scene = root.getScene();
      _scene.setCursor(Cursor.DEFAULT);
    };
    this.releasedHandler = _function_2;
  }
  
  protected boolean isMarqueeSelected(final XShape shape, final Bounds marqueeBounds) {
    boolean _isSelectable = shape.isSelectable();
    boolean _not = (!_isSelectable);
    if (_not) {
      return false;
    }
    boolean _contains = marqueeBounds.contains(CoreExtensions.localToDiagram(shape, BoundsExtensions.center(shape.getBoundsInLocal())));
    if (_contains) {
      boolean _equals = Objects.equal(this.currentState, DiagramMouseTool.State.MARQUEE_XOR);
      if (_equals) {
        boolean _contains_1 = this.previousSelection.contains(shape);
        return (!_contains_1);
      } else {
        return true;
      }
    } else {
      boolean _equals_1 = Objects.equal(this.currentState, DiagramMouseTool.State.MARQUEE_XOR);
      if (_equals_1) {
        return this.previousSelection.contains(shape);
      } else {
        return false;
      }
    }
  }
  
  protected boolean applyToState(final MouseEvent event) {
    DiagramMouseTool.State _xifexpression = null;
    boolean _isShortcutDown = event.isShortcutDown();
    if (_isShortcutDown) {
      DiagramMouseTool.State _xifexpression_1 = null;
      boolean _isShiftDown = event.isShiftDown();
      if (_isShiftDown) {
        _xifexpression_1 = DiagramMouseTool.State.ZOOM_OUT;
      } else {
        _xifexpression_1 = DiagramMouseTool.State.ZOOM_IN;
      }
      _xifexpression = _xifexpression_1;
    } else {
      DiagramMouseTool.State _xifexpression_2 = null;
      if ((Objects.equal(event.getButton(), MouseButton.PRIMARY) || event.isPrimaryButtonDown())) {
        DiagramMouseTool.State _xifexpression_3 = null;
        boolean _isShiftDown_1 = event.isShiftDown();
        if (_isShiftDown_1) {
          _xifexpression_3 = DiagramMouseTool.State.MARQUEE_XOR;
        } else {
          _xifexpression_3 = DiagramMouseTool.State.MARQUEE;
        }
        _xifexpression_2 = _xifexpression_3;
      } else {
        _xifexpression_2 = DiagramMouseTool.State.SCROLL;
      }
      _xifexpression = _xifexpression_2;
    }
    final DiagramMouseTool.State newState = _xifexpression;
    boolean _notEquals = (!Objects.equal(this.currentState, newState));
    if (_notEquals) {
      this.currentState = newState;
      this.startDragContext(event);
      Scene _scene = this.root.getScene();
      Cursor _switchResult = null;
      if (newState != null) {
        switch (newState) {
          case SCROLL:
            _switchResult = Cursor.OPEN_HAND;
            break;
          case MARQUEE:
            _switchResult = Cursor.CROSSHAIR;
            break;
          case MARQUEE_XOR:
            _switchResult = Cursor.CROSSHAIR;
            break;
          case ZOOM_IN:
            _switchResult = DiagramMouseTool.zoomInCursor;
            break;
          case ZOOM_OUT:
            _switchResult = DiagramMouseTool.zoomOutCursor;
            break;
          default:
            break;
        }
      }
      _scene.setCursor(_switchResult);
      if ((Objects.equal(newState, DiagramMouseTool.State.MARQUEE) || Objects.equal(newState, DiagramMouseTool.State.MARQUEE_XOR))) {
        final Point2D position = this.root.getDiagramCanvas().screenToLocal(this.dragContext.screenX, this.dragContext.screenY);
        final Procedure1<Rectangle> _function = (Rectangle it) -> {
          it.setX(position.getX());
          it.setY(position.getY());
          it.setWidth(0);
          it.setHeight(0);
          it.setArcWidth(5);
          it.setArcHeight(5);
          it.setFill(null);
          it.setStroke(Color.DARKGRAY);
          it.setStrokeWidth(2);
          it.setStrokeLineCap(StrokeLineCap.ROUND);
          it.getStrokeDashArray().setAll(Double.valueOf(10.0), Double.valueOf(5.0), Double.valueOf(5.0), Double.valueOf(5.0));
          Timeline _timeline = new Timeline();
          final Procedure1<Timeline> _function_1 = (Timeline it_1) -> {
            ObservableList<KeyFrame> _keyFrames = it_1.getKeyFrames();
            Duration _millis = DurationExtensions.millis(0);
            DoubleProperty _strokeDashOffsetProperty = this.marquee.strokeDashOffsetProperty();
            KeyValue _keyValue = new <Number>KeyValue(_strokeDashOffsetProperty, Integer.valueOf(0));
            KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
            _keyFrames.add(_keyFrame);
            ObservableList<KeyFrame> _keyFrames_1 = it_1.getKeyFrames();
            Duration _millis_1 = DurationExtensions.millis(300);
            DoubleProperty _strokeDashOffsetProperty_1 = this.marquee.strokeDashOffsetProperty();
            final Function2<Double, Double, Double> _function_2 = (Double $0, Double $1) -> {
              return Double.valueOf(DoubleExtensions.operator_plus($0, $1));
            };
            Double _reduce = IterableExtensions.<Double>reduce(this.marquee.getStrokeDashArray(), _function_2);
            KeyValue _keyValue_1 = new <Number>KeyValue(_strokeDashOffsetProperty_1, ((Double) _reduce));
            KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
            _keyFrames_1.add(_keyFrame_1);
            it_1.setCycleCount(Animation.INDEFINITE);
            it_1.play();
          };
          ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function_1);
        };
        ObjectExtensions.<Rectangle>operator_doubleArrow(
          this.marquee, _function);
        CoreExtensions.<Rectangle>safeAdd(this.root.getDiagramCanvas().getChildren(), this.marquee);
        this.previousSelection = CollectionLiterals.<XShape>newHashSet();
        boolean _equals = Objects.equal(newState, DiagramMouseTool.State.MARQUEE_XOR);
        if (_equals) {
          Iterable<XShape> _currentSelection = this.root.getCurrentSelection();
          Iterables.<XShape>addAll(this.previousSelection, _currentSelection);
        }
      } else {
        CoreExtensions.<Rectangle>safeDelete(this.root.getDiagramCanvas().getChildren(), this.marquee);
      }
      return true;
    } else {
      return false;
    }
  }
  
  protected DiagramMouseTool.DragContext startDragContext(final MouseEvent event) {
    DiagramMouseTool.DragContext _dragContext = new DiagramMouseTool.DragContext();
    final Procedure1<DiagramMouseTool.DragContext> _function = (DiagramMouseTool.DragContext it) -> {
      double _translateX = this.root.getViewportTransform().getTranslateX();
      double _sceneX = event.getSceneX();
      double _minus = (_translateX - _sceneX);
      it.sceneX = _minus;
      double _translateY = this.root.getViewportTransform().getTranslateY();
      double _sceneY = event.getSceneY();
      double _minus_1 = (_translateY - _sceneY);
      it.sceneY = _minus_1;
      it.screenX = event.getScreenX();
      it.screenY = event.getScreenY();
      it.pivotInDiagram = this.root.getDiagram().sceneToLocal(event.getSceneX(), event.getSceneY());
    };
    DiagramMouseTool.DragContext _doubleArrow = ObjectExtensions.<DiagramMouseTool.DragContext>operator_doubleArrow(_dragContext, _function);
    return this.dragContext = _doubleArrow;
  }
  
  @Override
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      final Scene scene = this.root.getScene();
      scene.<MouseEvent>addEventHandler(MouseEvent.MOUSE_PRESSED, this.pressedHandler);
      scene.<MouseEvent>addEventHandler(MouseEvent.MOUSE_DRAGGED, this.dragHandler);
      this.root.<MouseEvent>addEventHandler(MouseEvent.MOUSE_RELEASED, this.releasedHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      final Scene scene = this.root.getScene();
      scene.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_PRESSED, this.pressedHandler);
      scene.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_DRAGGED, this.dragHandler);
      this.root.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_RELEASED, this.releasedHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
}
