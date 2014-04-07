package de.fxdiagram.core.model;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@ModelNode({ "name" })
@SuppressWarnings("all")
public class StringDescriptor implements DomainObjectDescriptor {
  public StringDescriptor(final String name) {
    this.nameProperty.set(name);
  }
  
  public String getId() {
    return this.getName();
  }
  
  private SimpleStringProperty nameProperty = new SimpleStringProperty(this, "name",_initName());
  
  private static final String _initName() {
    return null;
  }
  
  public String getName() {
    return this.nameProperty.get();
  }
  
  public void setName(final String name) {
    this.nameProperty.set(name);
  }
  
  public StringProperty nameProperty() {
    return this.nameProperty;
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public StringDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(nameProperty, String.class);
  }
}
