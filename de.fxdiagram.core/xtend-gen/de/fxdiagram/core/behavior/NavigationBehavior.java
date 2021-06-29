package de.fxdiagram.core.behavior;

import de.fxdiagram.core.behavior.Behavior;

@SuppressWarnings("all")
public interface NavigationBehavior extends Behavior {
  boolean next();
  
  boolean previous();
}
