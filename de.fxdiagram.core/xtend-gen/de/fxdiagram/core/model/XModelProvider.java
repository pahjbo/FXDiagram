package de.fxdiagram.core.model;

import de.fxdiagram.core.model.ModelElementImpl;

/**
 * Something that provides a model, i.e. can be serializable.
 * 
 * @see {@link ModelNode}
 */
@SuppressWarnings("all")
public interface XModelProvider {
  void populate(final ModelElementImpl element);
  
  /**
   * Implementing classes can return <code>true</code> if the specific implementation should not be serialized.
   */
  default boolean isTransient() {
    return false;
  }
  
  void postLoad();
}
