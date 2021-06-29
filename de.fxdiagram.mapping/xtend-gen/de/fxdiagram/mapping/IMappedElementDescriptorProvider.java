package de.fxdiagram.mapping;

import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;

/**
 * A {@link DomainObjectProvider} that translates between domain objects with a mapping
 * and {@link IMappedElementDescriptor}s.
 */
@SuppressWarnings("all")
public interface IMappedElementDescriptorProvider extends DomainObjectProvider, XModelProvider {
  <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<? extends T> mapping);
  
  @Override
  default boolean isTransient() {
    return true;
  }
  
  @Override
  default void populate(final ModelElementImpl element) {
  }
}
