package de.fxdiagram.mapping.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.mapping.reconcile.MappingLabelListener;
import de.fxdiagram.mapping.reconcile.NodeReconcileBehavior;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Base implementation for a {@link XNode} with a nested {@link XDiagram} that belongs to an
 * {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector.
 */
@ModelNode
@SuppressWarnings("all")
public class BaseDiagramNode<T extends Object> extends OpenableDiagramNode {
  public static final String NODE_HEADING = "diagramNodeHeading";
  
  public BaseDiagramNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public IMappedElementDescriptor<T> getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  protected Node createNode() {
    RectangleBorderPane _xblockexpression = null;
    {
      RectangleBorderPane _pane = this.getPane();
      final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
        Color _rgb = Color.rgb(242, 236, 181);
        Stop _stop = new Stop(0, _rgb);
        Color _rgb_1 = Color.rgb(255, 248, 202);
        Stop _stop_1 = new Stop(1, _rgb_1);
        LinearGradient _linearGradient = new LinearGradient(
          0, 0, 1, 1, 
          true, CycleMethod.NO_CYCLE, 
          Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop, _stop_1)));
        it.setBackgroundPaint(_linearGradient);
      };
      ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_pane, _function);
      ObservableList<XLabel> _labels = this.getLabels();
      RectangleBorderPane _pane_1 = this.getPane();
      Pair<String, Pane> _mappedTo = Pair.<String, Pane>of(BaseDiagramNode.NODE_HEADING, _pane_1);
      MappingLabelListener.<XLabel>addMappingLabelListener(_labels, _mappedTo);
      _xblockexpression = this.getPane();
    }
    return _xblockexpression;
  }
  
  @Override
  public Node getTextNode() {
    final Function1<XLabel, Boolean> _function = (XLabel it) -> {
      String _type = it.getType();
      return Boolean.valueOf(Objects.equal(_type, BaseDiagramNode.NODE_HEADING));
    };
    return IterableExtensions.<XLabel>findFirst(this.getLabels(), _function);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    LazyConnectionMappingBehavior.<T>addLazyBehavior(this, this.getDomainObjectDescriptor());
    NodeReconcileBehavior<Object> _nodeReconcileBehavior = new NodeReconcileBehavior<Object>(this);
    this.addBehavior(_nodeReconcileBehavior);
    XDiagram _innerDiagram = this.getInnerDiagram();
    _innerDiagram.setLayoutOnActivate(true);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BaseDiagramNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
