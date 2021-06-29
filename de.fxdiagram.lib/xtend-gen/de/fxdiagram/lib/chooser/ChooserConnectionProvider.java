package de.fxdiagram.lib.chooser;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;

@SuppressWarnings("all")
public interface ChooserConnectionProvider {
  XConnection getConnection(final XNode host, final XNode choice, final DomainObjectDescriptor choiceInfo);
}
