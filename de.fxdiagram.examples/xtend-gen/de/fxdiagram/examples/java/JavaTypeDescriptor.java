package de.fxdiagram.examples.java;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.CachedDomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.java.JavaModelProvider;
import org.eclipse.xtext.xbase.lib.Exceptions;

@ModelNode({ "id", "name", "provider" })
@SuppressWarnings("all")
public class JavaTypeDescriptor extends CachedDomainObjectDescriptor<Class<?>> {
  public JavaTypeDescriptor(final Class<?> javaClass, final JavaModelProvider provider) {
    super(javaClass, javaClass.getCanonicalName(), javaClass.getCanonicalName(), provider);
  }
  
  public Class<?> resolveDomainObject() {
    try {
      String _id = this.getId();
      return Class.forName(_id);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public JavaTypeDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(idProperty(), String.class);
    modelElement.addProperty(nameProperty(), String.class);
    modelElement.addProperty(providerProperty(), DomainObjectProvider.class);
  }
}
