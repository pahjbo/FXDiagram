package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.MoveCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class MoveBehavior<T extends XShape> extends AbstractHostBehavior<T> {
  @Data
  public static class DragContext {
    private final double initialX;
    
    private final double initialY;
    
    private final double mouseAnchorX;
    
    private final double mouseAnchorY;
    
    private final Point2D initialPosInScene;
    
    public DragContext(final double initialX, final double initialY, final double mouseAnchorX, final double mouseAnchorY, final Point2D initialPosInScene) {
      super();
      this.initialX = initialX;
      this.initialY = initialY;
      this.mouseAnchorX = mouseAnchorX;
      this.mouseAnchorY = mouseAnchorY;
      this.initialPosInScene = initialPosInScene;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (Double.doubleToLongBits(this.initialX) ^ (Double.doubleToLongBits(this.initialX) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.initialY) ^ (Double.doubleToLongBits(this.initialY) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.mouseAnchorX) ^ (Double.doubleToLongBits(this.mouseAnchorX) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.mouseAnchorY) ^ (Double.doubleToLongBits(this.mouseAnchorY) >>> 32));
      return prime * result + ((this.initialPosInScene== null) ? 0 : this.initialPosInScene.hashCode());
    }
    
    @Override
    @Pure
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      MoveBehavior.DragContext other = (MoveBehavior.DragContext) obj;
      if (Double.doubleToLongBits(other.initialX) != Double.doubleToLongBits(this.initialX))
        return false; 
      if (Double.doubleToLongBits(other.initialY) != Double.doubleToLongBits(this.initialY))
        return false; 
      if (Double.doubleToLongBits(other.mouseAnchorX) != Double.doubleToLongBits(this.mouseAnchorX))
        return false; 
      if (Double.doubleToLongBits(other.mouseAnchorY) != Double.doubleToLongBits(this.mouseAnchorY))
        return false; 
      if (this.initialPosInScene == null) {
        if (other.initialPosInScene != null)
          return false;
      } else if (!this.initialPosInScene.equals(other.initialPosInScene))
        return false;
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("initialX", this.initialX);
      b.add("initialY", this.initialY);
      b.add("mouseAnchorX", this.mouseAnchorX);
      b.add("mouseAnchorY", this.mouseAnchorY);
      b.add("initialPosInScene", this.initialPosInScene);
      return b.toString();
    }
    
    @Pure
    public double getInitialX() {
      return this.initialX;
    }
    
    @Pure
    public double getInitialY() {
      return this.initialY;
    }
    
    @Pure
    public double getMouseAnchorX() {
      return this.mouseAnchorX;
    }
    
    @Pure
    public double getMouseAnchorY() {
      return this.mouseAnchorY;
    }
    
    @Pure
    public Point2D getInitialPosInScene() {
      return this.initialPosInScene;
    }
  }
  
  private MoveBehavior.DragContext dragContext;
  
  public MoveBehavior(final T host) {
    super(host);
  }
  
  @Override
  public void doActivate() {
  }
  
  public boolean hasMoved() {
    return ((!Objects.equal(this.dragContext, null)) && ((this.dragContext.initialX != this.getHost().getLayoutX()) || (this.dragContext.initialY != this.getHost().getLayoutY())));
  }
  
  public MoveBehavior.DragContext reset() {
    return this.dragContext = null;
  }
  
  protected AnimationCommand createMoveCommand() {
    T _host = this.getHost();
    boolean _manuallyPlaced = this.getHost().getManuallyPlaced();
    double _layoutX = this.getHost().getLayoutX();
    double _layoutY = this.getHost().getLayoutY();
    return new MoveCommand(_host, 
      this.dragContext.initialX, this.dragContext.initialY, _manuallyPlaced, _layoutX, _layoutY);
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return MoveBehavior.class;
  }
  
  public void mousePressed(final MouseEvent it) {
    this.startDrag(it.getScreenX(), it.getScreenY());
  }
  
  public void startDrag(final double screenX, final double screenY) {
    final Point2D initialPositionInScene = this.getHost().getParent().localToScene(this.getHost().getLayoutX(), this.getHost().getLayoutY());
    double _layoutX = this.getHost().getLayoutX();
    double _layoutY = this.getHost().getLayoutY();
    MoveBehavior.DragContext _dragContext = new MoveBehavior.DragContext(_layoutX, _layoutY, screenX, screenY, initialPositionInScene);
    this.dragContext = _dragContext;
    T _host = this.getHost();
    if ((_host instanceof XNode)) {
      T _host_1 = this.getHost();
      final XNode node = ((XNode) _host_1);
      ObservableList<XConnection> _incomingConnections = node.getIncomingConnections();
      ObservableList<XConnection> _outgoingConnections = node.getOutgoingConnections();
      final Consumer<XConnection> _function = (XConnection it) -> {
        ConnectionRouter _connectionRouter = it.getConnectionRouter();
        _connectionRouter.setSplineShapeKeeperEnabled(true);
      };
      Iterables.<XConnection>concat(_incomingConnections, _outgoingConnections).forEach(_function);
    }
  }
  
  public void mouseDragged(final MouseEvent it) {
    double _x = this.dragContext.initialPosInScene.getX();
    double _screenX = it.getScreenX();
    double _plus = (_x + _screenX);
    double _minus = (_plus - this.dragContext.mouseAnchorX);
    double _y = this.dragContext.initialPosInScene.getY();
    double _screenY = it.getScreenY();
    double _plus_1 = (_y + _screenY);
    double _minus_1 = (_plus_1 - this.dragContext.mouseAnchorY);
    final Point2D newPositionInScene = new Point2D(_minus, _minus_1);
    final Point2D newPositionInDiagram = this.getHost().getParent().sceneToLocal(newPositionInScene);
    final boolean useGrid = (CoreExtensions.getDiagram(this.getHost()).getGridEnabled() ^ it.isShortcutDown());
    boolean _isShortcutDown = it.isShortcutDown();
    final boolean useAuxlines = (!_isShortcutDown);
    this.dragTo(CoreExtensions.getDiagram(this.getHost()).getSnappedPosition(newPositionInDiagram, this.getHost(), useGrid, useAuxlines));
  }
  
  public void mouseReleased(final MouseEvent it) {
    boolean _hasMoved = this.hasMoved();
    if (_hasMoved) {
      final AnimationCommand moveCommand = this.createMoveCommand();
      boolean _notEquals = (!Objects.equal(moveCommand, null));
      if (_notEquals) {
        CoreExtensions.getRoot(this.getHost()).getCommandStack().execute(moveCommand);
        this.reset();
      }
    }
  }
  
  protected void dragTo(final Point2D newPositionInDiagram) {
    boolean _notEquals = (!Objects.equal(newPositionInDiagram, null));
    if (_notEquals) {
      T _host = this.getHost();
      _host.setLayoutX(newPositionInDiagram.getX());
      T _host_1 = this.getHost();
      _host_1.setLayoutY(newPositionInDiagram.getY());
    }
  }
}
