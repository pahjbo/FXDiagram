package de.fxdiagram.core.model;

import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;

@SuppressWarnings("all")
public interface ModelElement {
  List<? extends Property<?>> getProperties();
  
  List<? extends ListProperty<?>> getListProperties();
  
  Class<?> getType(final Property<?> property);
  
  boolean isPrimitive(final Property<?> property);
  
  Object getNode();
}
