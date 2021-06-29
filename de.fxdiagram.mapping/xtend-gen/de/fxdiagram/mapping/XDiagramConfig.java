package de.fxdiagram.mapping;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XDomainObjectOwner;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.mapping.AbstractDiagramConfig;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.execution.EntryCall;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * Stores a set of {@link AbstractMapping}s for a sepecific domain.
 * 
 * {@link XDiagramConfig}s must be registered to the {@link Registry}
 * to be picked up by the runtime using the extension point
 * <code>de.fxdiagram.eclipse.fxDiagramConfig</code>.
 * 
 * Consider inheriting from {@link AbstractDiagramConfig} instead of directly
 * implementing this interface.
 */
@SuppressWarnings("all")
public interface XDiagramConfig {
  @Logging
  class Registry {
    private static XDiagramConfig.Registry instance;
    
    private Map<String, XDiagramConfig> configs = CollectionLiterals.<String, XDiagramConfig>newHashMap();
    
    public static XDiagramConfig.Registry getInstance() {
      XDiagramConfig.Registry _elvis = null;
      if (XDiagramConfig.Registry.instance != null) {
        _elvis = XDiagramConfig.Registry.instance;
      } else {
        XDiagramConfig.Registry _registry = new XDiagramConfig.Registry();
        XDiagramConfig.Registry _instance = (XDiagramConfig.Registry.instance = _registry);
        _elvis = _instance;
      }
      return _elvis;
    }
    
    private Registry() {
      this.addStaticConfigurations();
    }
    
    public Iterable<? extends XDiagramConfig> getConfigurations() {
      return this.configs.values();
    }
    
    protected void addStaticConfigurations() {
      boolean _isEquinox = ClassLoaderExtensions.isEquinox();
      if (_isEquinox) {
        final Consumer<IConfigurationElement> _function = (IConfigurationElement it) -> {
          try {
            Object _createExecutableExtension = it.createExecutableExtension("class");
            final AbstractDiagramConfig config = ((AbstractDiagramConfig) _createExecutableExtension);
            final String id = it.getAttribute("id");
            config.setID(id);
            config.setLabel(it.getAttribute("label"));
            this.addConfig(config);
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception exc = (Exception)_t;
              XDiagramConfig.Registry.LOG.severe(exc.getMessage());
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        };
        ((List<IConfigurationElement>)Conversions.doWrapArray(Platform.getExtensionRegistry().getConfigurationElementsFor("de.fxdiagram.mapping.fxDiagramConfig"))).forEach(_function);
      }
    }
    
    public XDiagramConfig addConfig(final XDiagramConfig config) {
      XDiagramConfig _xblockexpression = null;
      {
        final String id = config.getID();
        XDiagramConfig _xifexpression = null;
        boolean _containsKey = this.configs.containsKey(id);
        if (_containsKey) {
          XDiagramConfig.Registry.LOG.severe(("Duplicate fxDiagramConfig id=" + id));
        } else {
          _xifexpression = this.configs.put(id, config);
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    }
    
    public XDiagramConfig getConfigByID(final String configID) {
      return this.configs.get(configID);
    }
    
    private static Logger LOG = Logger.getLogger("de.fxdiagram.mapping.XDiagramConfig.Registry");
      ;
  }
  
  /**
   * @return all possible calls to add a diagram element for the given domain object
   */
  <ARG extends Object> Iterable<? extends EntryCall<ARG>> getEntryCalls(final ARG domainObject);
  
  Iterable<? extends AbstractMapping<?>> getMappings();
  
  AbstractMapping<?> getMappingByID(final String mappingID);
  
  String getID();
  
  String getLabel();
  
  <ARG extends Object> void addMapping(final AbstractMapping<ARG> mapping);
  
  IMappedElementDescriptorProvider getDomainObjectProvider();
  
  /**
   * Hook for common configuration for a specific kind of shape, e.g. force a certain
   * connection kind.
   * 
   * All base shapes call this in {@link XModelProvider#postLoad()} as such allow to override
   * the saved configuration.
   */
  void initialize(final XDomainObjectOwner shape);
}
