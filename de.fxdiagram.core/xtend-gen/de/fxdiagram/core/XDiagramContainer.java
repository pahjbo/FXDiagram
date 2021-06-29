package de.fxdiagram.core;

import de.fxdiagram.core.XDiagram;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;

/**
 * Interface for {@XNode}s that contain diagrams.
 */
@SuppressWarnings("all")
public interface XDiagramContainer {
  XDiagram getInnerDiagram();
  
  void setInnerDiagram(final XDiagram diagram);
  
  ObjectProperty<XDiagram> innerDiagramProperty();
  
  boolean isInnerDiagramActive();
  
  Insets getInsets();
}
