package de.fxdiagram.core.model;

import de.fxdiagram.core.model.ModelElement;

/**
 * Adapter interface for serializing instances of classes that don't implement
 * {@link XModelProvider}.
 */
@SuppressWarnings("all")
public interface ValueAdapter<T extends Object> extends ModelElement {
  T getValueObject();
}
