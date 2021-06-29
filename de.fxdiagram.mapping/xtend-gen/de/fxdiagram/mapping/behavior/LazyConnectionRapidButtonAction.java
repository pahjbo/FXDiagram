package de.fxdiagram.mapping.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.chooser.CarusselChoice;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractLabelMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LazyConnectionRapidButtonAction<MODEL extends Object, ARG extends Object> extends RapidButtonAction {
  private XDiagramConfigInterpreter configInterpreter;
  
  private AbstractConnectionMappingCall<MODEL, ARG> mappingCall;
  
  private boolean hostIsSource;
  
  public LazyConnectionRapidButtonAction(final AbstractConnectionMappingCall<MODEL, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource) {
    this.mappingCall = mappingCall;
    this.configInterpreter = configInterpreter;
    this.hostIsSource = hostIsSource;
  }
  
  @Override
  public boolean isEnabled(final XNode host) {
    DomainObjectDescriptor _domainObjectDescriptor = host.getDomainObjectDescriptor();
    final IMappedElementDescriptor<ARG> hostDescriptor = ((IMappedElementDescriptor<ARG>) _domainObjectDescriptor);
    final XDiagram diagram = CoreExtensions.getDiagram(host);
    boolean _equals = Objects.equal(diagram, null);
    if (_equals) {
      return false;
    }
    final Function1<XConnection, DomainObjectDescriptor> _function = (XConnection it) -> {
      return it.getDomainObjectDescriptor();
    };
    final Set<DomainObjectDescriptor> existingConnectionDescriptors = IterableExtensions.<DomainObjectDescriptor>toSet(ListExtensions.<XConnection, DomainObjectDescriptor>map(diagram.getConnections(), _function));
    try {
      final Function1<ARG, Boolean> _function_1 = (ARG domainArgument) -> {
        final Iterable<MODEL> connectionDomainObjects = this.configInterpreter.<MODEL, ARG>select(this.mappingCall, domainArgument);
        for (final MODEL connectionDomainObject : connectionDomainObjects) {
          {
            final IMappedElementDescriptor<MODEL> connectionDescriptor = this.configInterpreter.<MODEL>getDescriptor(connectionDomainObject, this.mappingCall.getConnectionMapping());
            boolean _add = existingConnectionDescriptors.add(connectionDescriptor);
            if (_add) {
              NodeMappingCall<?, MODEL> _xifexpression = null;
              if (this.hostIsSource) {
                _xifexpression = this.mappingCall.getConnectionMapping().getTarget();
              } else {
                _xifexpression = this.mappingCall.getConnectionMapping().getSource();
              }
              final NodeMappingCall<?, MODEL> nodeMappingCall = _xifexpression;
              final Iterable<?> nodeDomainObjects = this.configInterpreter.select(nodeMappingCall, connectionDomainObject);
              if (((!Objects.equal(nodeDomainObjects, null)) && (!IterableExtensions.isEmpty(nodeDomainObjects)))) {
                return Boolean.valueOf(true);
              }
            }
          }
        }
        return Boolean.valueOf(false);
      };
      final Boolean result = hostDescriptor.<Boolean>withDomainObject(_function_1);
      return (result).booleanValue();
    } catch (final Throwable _t) {
      if (_t instanceof NoSuchElementException) {
        return false;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  @Override
  public void perform(final RapidButton button) {
    final ConnectedNodeChooser chooser = this.createChooser(button);
    this.populateChooser(chooser, button.getHost());
    XRoot _root = CoreExtensions.getRoot(button.getHost());
    _root.setCurrentTool(chooser);
  }
  
  protected ConnectedNodeChooser createChooser(final RapidButton button) {
    ConnectedNodeChooser _xblockexpression = null;
    {
      final Side position = button.getPosition();
      ConnectedNodeChooser _xifexpression = null;
      boolean _isVertical = position.isVertical();
      if (_isVertical) {
        XNode _host = button.getHost();
        CarusselChoice _carusselChoice = new CarusselChoice();
        _xifexpression = new ConnectedNodeChooser(_host, position, _carusselChoice);
      } else {
        XNode _host_1 = button.getHost();
        CoverFlowChoice _coverFlowChoice = new CoverFlowChoice();
        _xifexpression = new ConnectedNodeChooser(_host_1, position, _coverFlowChoice);
      }
      final ConnectedNodeChooser chooser = _xifexpression;
      _xblockexpression = chooser;
    }
    return _xblockexpression;
  }
  
  protected Object populateChooser(final ConnectedNodeChooser chooser, final XNode host) {
    Object _xblockexpression = null;
    {
      DomainObjectDescriptor _domainObjectDescriptor = host.getDomainObjectDescriptor();
      final IMappedElementDescriptor<ARG> hostDescriptor = ((IMappedElementDescriptor<ARG>) _domainObjectDescriptor);
      final Function1<XConnection, DomainObjectDescriptor> _function = (XConnection it) -> {
        return it.getDomainObjectDescriptor();
      };
      final Set<DomainObjectDescriptor> existingConnectionDescriptors = IterableExtensions.<DomainObjectDescriptor>toSet(ListExtensions.<XConnection, DomainObjectDescriptor>map(CoreExtensions.getDiagram(host).getConnections(), _function));
      final Function1<ARG, Object> _function_1 = (ARG domainArgument) -> {
        Object _xblockexpression_1 = null;
        {
          final Iterable<MODEL> connectionDomainObjects = this.configInterpreter.<MODEL, ARG>select(this.mappingCall, domainArgument);
          final Consumer<MODEL> _function_2 = (MODEL connectionDomainObject) -> {
            final IMappedElementDescriptor<MODEL> connectionDescriptor = this.configInterpreter.<MODEL>getDescriptor(connectionDomainObject, this.mappingCall.getConnectionMapping());
            boolean _add = existingConnectionDescriptors.add(connectionDescriptor);
            if (_add) {
              NodeMappingCall<?, MODEL> _xifexpression = null;
              if (this.hostIsSource) {
                _xifexpression = this.mappingCall.getConnectionMapping().getTarget();
              } else {
                _xifexpression = this.mappingCall.getConnectionMapping().getSource();
              }
              final NodeMappingCall<?, MODEL> nodeMappingCall = _xifexpression;
              final Iterable<?> nodeDomainObjects = this.configInterpreter.select(nodeMappingCall, connectionDomainObject);
              final Consumer<Object> _function_3 = (Object it) -> {
                chooser.addChoice(this.<Object>createNode(it, nodeMappingCall.getNodeMapping()), connectionDescriptor);
              };
              nodeDomainObjects.forEach(_function_3);
            }
          };
          connectionDomainObjects.forEach(_function_2);
          final ChooserConnectionProvider _function_3 = (XNode thisNode, XNode thatNode, DomainObjectDescriptor connectionDesc) -> {
            XConnection _xblockexpression_2 = null;
            {
              final IMappedElementDescriptor<MODEL> descriptor = ((IMappedElementDescriptor<MODEL>) connectionDesc);
              XConnection _createConnection = this.mappingCall.getConnectionMapping().createConnection(descriptor);
              final Procedure1<XConnection> _function_4 = (XConnection it) -> {
                if (this.hostIsSource) {
                  it.setSource(thisNode);
                  it.setTarget(thatNode);
                } else {
                  it.setSource(thatNode);
                  it.setTarget(thisNode);
                }
                final Consumer<AbstractLabelMappingCall<?, MODEL>> _function_5 = (AbstractLabelMappingCall<?, MODEL> labelMappingCall) -> {
                  ObservableList<XConnectionLabel> _labels = it.getLabels();
                  final Function1<MODEL, Iterable<? extends XLabel>> _function_6 = (MODEL it_1) -> {
                    return this.configInterpreter.execute(labelMappingCall, it_1);
                  };
                  Iterable<XConnectionLabel> _filter = Iterables.<XConnectionLabel>filter(descriptor.<Iterable<? extends XLabel>>withDomainObject(_function_6), XConnectionLabel.class);
                  Iterables.<XConnectionLabel>addAll(_labels, _filter);
                };
                this.mappingCall.getConnectionMapping().getLabels().forEach(_function_5);
                this.mappingCall.getConnectionMapping().getConfig().initialize(it);
              };
              _xblockexpression_2 = ObjectExtensions.<XConnection>operator_doubleArrow(_createConnection, _function_4);
            }
            return _xblockexpression_2;
          };
          chooser.setConnectionProvider(_function_3);
          _xblockexpression_1 = null;
        }
        return _xblockexpression_1;
      };
      _xblockexpression = hostDescriptor.<Object>withDomainObject(_function_1);
    }
    return _xblockexpression;
  }
  
  protected <NODE extends Object> XNode createNode(final NODE nodeDomainObject, final NodeMapping<?> nodeMapping) {
    XNode _xifexpression = null;
    boolean _isApplicable = nodeMapping.isApplicable(nodeDomainObject);
    if (_isApplicable) {
      XNode _xblockexpression = null;
      {
        final NodeMapping<NODE> nodeMappingCasted = ((NodeMapping<NODE>) nodeMapping);
        final IMappedElementDescriptor<NODE> descriptor = this.configInterpreter.<NODE>getDescriptor(((NODE) nodeDomainObject), nodeMappingCasted);
        final XNode node = nodeMappingCasted.createNode(descriptor);
        final Consumer<AbstractLabelMappingCall<?, NODE>> _function = (AbstractLabelMappingCall<?, NODE> it) -> {
          ObservableList<XLabel> _labels = node.getLabels();
          Iterable<? extends XLabel> _execute = this.configInterpreter.execute(it, ((NODE) nodeDomainObject));
          Iterables.<XLabel>addAll(_labels, _execute);
        };
        nodeMappingCasted.getLabels().forEach(_function);
        nodeMappingCasted.getConfig().initialize(node);
        _xblockexpression = node;
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
}
