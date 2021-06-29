package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XShape;

/**
 * Base interface for any kind of user interoperability or liveness an {@link XShape} could have.
 * 
 * By using this approach, FXDiagram enforces the composability of behavior instead of implementing
 * everything in the shapes themselves.
 */
@SuppressWarnings("all")
public interface Behavior extends XActivatable {
  Class<? extends Behavior> getBehaviorKey();
}
