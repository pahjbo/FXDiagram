package de.fxdiagram.core;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import javafx.beans.property.ReadOnlyObjectProperty;

@SuppressWarnings("all")
public interface XDomainObjectOwner {
  DomainObjectDescriptor getDomainObjectDescriptor();
  
  ReadOnlyObjectProperty<DomainObjectDescriptor> domainObjectDescriptorProperty();
}
