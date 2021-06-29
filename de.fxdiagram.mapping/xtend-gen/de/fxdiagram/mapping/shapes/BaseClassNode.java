package de.fxdiagram.mapping.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.SequentialAnimationCommand;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.mapping.reconcile.MappingLabelListener;
import de.fxdiagram.mapping.reconcile.NodeReconcileBehavior;
import de.fxdiagram.mapping.shapes.INodeWithLazyMappings;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@ModelNode({ "showPackage", "showAttributes", "showMethods", "bgColor" })
@SuppressWarnings("all")
public class BaseClassNode<T extends Object> extends FlipNode implements INodeWithLazyMappings {
  public static class ReconcileBehavior<T extends Object> extends NodeReconcileBehavior<T> {
    public ReconcileBehavior(final BaseClassNode<T> host) {
      super(host);
    }
    
    @Override
    public BaseClassNode<T> getHost() {
      XNode _host = super.getHost();
      return ((BaseClassNode<T>) _host);
    }
    
    @Override
    protected void reconcile(final AbstractMapping<T> mapping, final T domainObject, final de.fxdiagram.core.behavior.ReconcileBehavior.UpdateAcceptor acceptor) {
      final de.fxdiagram.core.behavior.ReconcileBehavior.UpdateAcceptor fakeAcceptor = new de.fxdiagram.core.behavior.ReconcileBehavior.UpdateAcceptor() {
        @Override
        public void delete(final XShape shape, final XDiagram diagram) {
          acceptor.delete(shape, diagram);
        }
        
        @Override
        public void add(final XShape shape, final XDiagram diagram) {
          acceptor.add(shape, diagram);
        }
        
        @Override
        public void morph(final AnimationCommand command) {
          SequentialAnimationCommand _sequentialAnimationCommand = new SequentialAnimationCommand();
          final Procedure1<SequentialAnimationCommand> _function = (SequentialAnimationCommand it) -> {
            AbstractAnimationCommand _deflateCommand = ReconcileBehavior.this.getHost().inflator.getDeflateCommand();
            it.operator_add(_deflateCommand);
            it.operator_add(command);
            AbstractAnimationCommand _inflateCommand = ReconcileBehavior.this.getHost().inflator.getInflateCommand();
            it.operator_add(_inflateCommand);
          };
          SequentialAnimationCommand _doubleArrow = ObjectExtensions.<SequentialAnimationCommand>operator_doubleArrow(_sequentialAnimationCommand, _function);
          acceptor.morph(_doubleArrow);
        }
      };
      super.reconcile(mapping, domainObject, fakeAcceptor);
    }
  }
  
  public static final String CLASS_NAME = "className";
  
  public static final String FILE_NAME = "fileName";
  
  public static final String PACKAGE = "package";
  
  public static final String ATTRIBUTE = "attribute";
  
  public static final String OPERATION = "operation";
  
  protected CheckBox packageBox;
  
  protected CheckBox attributesBox;
  
  protected CheckBox methodsBox;
  
  private VBox contentArea;
  
  private VBox packageArea;
  
  private VBox attributeCompartment;
  
  private VBox methodCompartment;
  
  private VBox nameArea;
  
  private VBox fileArea;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private Inflator inflator;
  
  public BaseClassNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public void postLoad() {
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    AbstractMapping<T> _mapping = null;
    if (_domainObjectDescriptor!=null) {
      _mapping=_domainObjectDescriptor.getMapping();
    }
    XDiagramConfig _config = null;
    if (_mapping!=null) {
      _config=_mapping.getConfig();
    }
    if (_config!=null) {
      _config.initialize(this);
    }
  }
  
  public Color getDefaultBgPaint() {
    return Color.web("#ffe6cc");
  }
  
  @Override
  public Node createNode() {
    Node _xblockexpression = null;
    {
      final Node pane = super.createNode();
      this.setBgColor(this.getDefaultBgPaint());
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
        TooltipExtensions.setTooltip(it, "Right-click to configure");
        it.backgroundPaintProperty().bind(this.bgColorProperty);
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function_1 = (VBox it_1) -> {
          Insets _insets = new Insets(10, 20, 10, 20);
          it_1.setPadding(_insets);
          ObservableList<Node> _children_1 = it_1.getChildren();
          VBox _vBox_1 = new VBox();
          final Procedure1<VBox> _function_2 = (VBox it_2) -> {
            it_2.setAlignment(Pos.CENTER);
          };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_2);
          VBox _nameArea = (this.nameArea = _doubleArrow);
          _children_1.add(_nameArea);
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
        VBox _contentArea = (this.contentArea = _doubleArrow);
        _children.add(_contentArea);
      };
      RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
      this.setFront(_doubleArrow);
      RectangleBorderPane _rectangleBorderPane_1 = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function_1 = (RectangleBorderPane it) -> {
        TooltipExtensions.setTooltip(it, "Right-click to show node");
        it.backgroundPaintProperty().bind(this.bgColorProperty);
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function_2 = (VBox it_1) -> {
          Insets _insets = new Insets(10, 20, 10, 20);
          it_1.setPadding(_insets);
          it_1.setSpacing(5);
          ObservableList<Node> _children_1 = it_1.getChildren();
          VBox _vBox_1 = new VBox();
          VBox _fileArea = (this.fileArea = _vBox_1);
          _children_1.add(_fileArea);
          ObservableList<Node> _children_2 = it_1.getChildren();
          CheckBox _checkBox = new CheckBox("Package");
          CheckBox _packageBox = (this.packageBox = _checkBox);
          _children_2.add(_packageBox);
          ObservableList<Node> _children_3 = it_1.getChildren();
          CheckBox _checkBox_1 = new CheckBox("Attributes");
          CheckBox _attributesBox = (this.attributesBox = _checkBox_1);
          _children_3.add(_attributesBox);
          ObservableList<Node> _children_4 = it_1.getChildren();
          CheckBox _checkBox_2 = new CheckBox("Methods");
          CheckBox _methodsBox = (this.methodsBox = _checkBox_2);
          _children_4.add(_methodsBox);
          ObservableList<Node> _children_5 = it_1.getChildren();
          ColorPicker _colorPicker = new ColorPicker();
          final Procedure1<ColorPicker> _function_3 = (ColorPicker it_2) -> {
            it_2.valueProperty().bindBidirectional(this.bgColorProperty);
          };
          ColorPicker _doubleArrow_1 = ObjectExtensions.<ColorPicker>operator_doubleArrow(_colorPicker, _function_3);
          _children_5.add(_doubleArrow_1);
        };
        VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_2);
        _children.add(_doubleArrow_1);
      };
      RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane_1, _function_1);
      this.setBack(_doubleArrow_1);
      Inflator _inflator = new Inflator(this, this.contentArea);
      this.inflator = _inflator;
      VBox _vBox = new VBox();
      final Procedure1<VBox> _function_2 = (VBox it) -> {
        it.setAlignment(Pos.CENTER);
        this.addInflatable(it, this.showPackageProperty, this.packageBox, 0, this.inflator);
      };
      VBox _doubleArrow_2 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_2);
      this.packageArea = _doubleArrow_2;
      VBox _vBox_1 = new VBox();
      final Procedure1<VBox> _function_3 = (VBox c) -> {
        Insets _insets = new Insets(10, 0, 0, 0);
        c.setPadding(_insets);
        this.addInflatable(c, this.showAttributesProperty, this.attributesBox, this.contentArea.getChildren().size(), this.inflator);
      };
      VBox _doubleArrow_3 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_3);
      this.attributeCompartment = _doubleArrow_3;
      VBox _vBox_2 = new VBox();
      final Procedure1<VBox> _function_4 = (VBox c) -> {
        Insets _insets = new Insets(10, 0, 0, 0);
        c.setPadding(_insets);
        this.addInflatable(c, this.showMethodsProperty, this.methodsBox, this.contentArea.getChildren().size(), this.inflator);
      };
      VBox _doubleArrow_4 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_2, _function_4);
      this.methodCompartment = _doubleArrow_4;
      ListProperty<XLabel> _labelsProperty = this.labelsProperty();
      Pair<String, Pane> _mappedTo = Pair.<String, Pane>of(BaseClassNode.CLASS_NAME, this.nameArea);
      Pair<String, Pane> _mappedTo_1 = Pair.<String, Pane>of(BaseClassNode.ATTRIBUTE, this.attributeCompartment);
      Pair<String, Pane> _mappedTo_2 = Pair.<String, Pane>of(BaseClassNode.OPERATION, this.methodCompartment);
      Pair<String, Pane> _mappedTo_3 = Pair.<String, Pane>of(BaseClassNode.FILE_NAME, this.fileArea);
      Pair<String, Pane> _mappedTo_4 = Pair.<String, Pane>of(BaseClassNode.PACKAGE, this.packageArea);
      MappingLabelListener.<XLabel>addMappingLabelListener(_labelsProperty, _mappedTo, _mappedTo_1, _mappedTo_2, _mappedTo_3, _mappedTo_4);
      _xblockexpression = pane;
    }
    return _xblockexpression;
  }
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 6, 6);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    BaseClassNode.ReconcileBehavior<T> _reconcileBehavior = new BaseClassNode.ReconcileBehavior<T>(this);
    this.addBehavior(_reconcileBehavior);
    final Function0<Integer> _function = () -> {
      return Integer.valueOf(0);
    };
    this.bindCheckbox(this.showPackageProperty, this.packageBox, this.packageArea, _function, this.inflator);
    final Function0<Integer> _function_1 = () -> {
      int _xifexpression = (int) 0;
      boolean _showPackage = this.getShowPackage();
      if (_showPackage) {
        _xifexpression = 2;
      } else {
        _xifexpression = 1;
      }
      return Integer.valueOf(_xifexpression);
    };
    this.bindCheckbox(this.showAttributesProperty, this.attributesBox, this.attributeCompartment, _function_1, this.inflator);
    final Function0<Integer> _function_2 = () -> {
      return Integer.valueOf(this.contentArea.getChildren().size());
    };
    this.bindCheckbox(this.showMethodsProperty, this.methodsBox, this.methodCompartment, _function_2, this.inflator);
    this.inflator.getInflateAnimation().play();
    LazyConnectionMappingBehavior.<T>addLazyBehavior(this, this.getDomainObjectDescriptor());
  }
  
  @Override
  public Dimension2D getAutoLayoutDimension() {
    return this.inflator.getInflatedSize();
  }
  
  protected Rectangle addInflatable(final VBox compartment, final BooleanProperty showProperty, final CheckBox box, final int index, final Inflator inflator) {
    Rectangle _xifexpression = null;
    boolean _get = showProperty.get();
    if (_get) {
      _xifexpression = inflator.addInflatable(compartment, index);
    }
    return _xifexpression;
  }
  
  protected void bindCheckbox(final BooleanProperty property, final CheckBox box, final VBox compartment, final Function0<? extends Integer> index, final Inflator inflator) {
    box.selectedProperty().bindBidirectional(property);
    final ChangeListener<Boolean> _function = (ObservableValue<? extends Boolean> p, Boolean o, Boolean show) -> {
      boolean _contains = this.contentArea.getChildren().contains(compartment);
      if (_contains) {
        if ((!(show).booleanValue())) {
          inflator.removeInflatable(compartment);
        }
      } else {
        if ((show).booleanValue()) {
          inflator.addInflatable(compartment, (index.apply()).intValue());
        }
      }
    };
    property.addListener(_function);
  }
  
  @Override
  public IMappedElementDescriptor<T> getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  public void registerOnClick() {
    final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
      MouseButton _button = it.getButton();
      boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
      if (_equals) {
        if (((!Objects.equal(this.getFront(), null)) && (!Objects.equal(this.getBack(), null)))) {
          this.flip(this.isHorizontal(it));
          it.consume();
        }
      }
    };
    this.<MouseEvent>addEventHandler(MouseEvent.MOUSE_CLICKED, _function);
  }
  
  @Override
  public List<Side> getButtonSides(final ConnectionMapping<?> mapping) {
    return Collections.<Side>unmodifiableList(CollectionLiterals.<Side>newArrayList(Side.TOP, Side.BOTTOM, Side.LEFT, Side.RIGHT));
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BaseClassNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(showPackageProperty, Boolean.class);
    modelElement.addProperty(showAttributesProperty, Boolean.class);
    modelElement.addProperty(showMethodsProperty, Boolean.class);
    modelElement.addProperty(bgColorProperty, Color.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleBooleanProperty showPackageProperty = new SimpleBooleanProperty(this, "showPackage",_initShowPackage());
  
  private static final boolean _initShowPackage() {
    return false;
  }
  
  public boolean getShowPackage() {
    return this.showPackageProperty.get();
  }
  
  public void setShowPackage(final boolean showPackage) {
    this.showPackageProperty.set(showPackage);
  }
  
  public BooleanProperty showPackageProperty() {
    return this.showPackageProperty;
  }
  
  private SimpleBooleanProperty showAttributesProperty = new SimpleBooleanProperty(this, "showAttributes",_initShowAttributes());
  
  private static final boolean _initShowAttributes() {
    return true;
  }
  
  public boolean getShowAttributes() {
    return this.showAttributesProperty.get();
  }
  
  public void setShowAttributes(final boolean showAttributes) {
    this.showAttributesProperty.set(showAttributes);
  }
  
  public BooleanProperty showAttributesProperty() {
    return this.showAttributesProperty;
  }
  
  private SimpleBooleanProperty showMethodsProperty = new SimpleBooleanProperty(this, "showMethods",_initShowMethods());
  
  private static final boolean _initShowMethods() {
    return true;
  }
  
  public boolean getShowMethods() {
    return this.showMethodsProperty.get();
  }
  
  public void setShowMethods(final boolean showMethods) {
    this.showMethodsProperty.set(showMethods);
  }
  
  public BooleanProperty showMethodsProperty() {
    return this.showMethodsProperty;
  }
  
  private SimpleObjectProperty<Color> bgColorProperty = new SimpleObjectProperty<Color>(this, "bgColor");
  
  public Color getBgColor() {
    return this.bgColorProperty.get();
  }
  
  public void setBgColor(final Color bgColor) {
    this.bgColorProperty.set(bgColor);
  }
  
  public ObjectProperty<Color> bgColorProperty() {
    return this.bgColorProperty;
  }
  
  @Pure
  public Inflator getInflator() {
    return this.inflator;
  }
}
