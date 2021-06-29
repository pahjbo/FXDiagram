package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.command.AnimationCommand;

/**
 * A behavior to compare the shown state of a shape with its current domain model.
 */
@SuppressWarnings("all")
public interface ReconcileBehavior extends Behavior {
  interface UpdateAcceptor {
    void delete(final XShape shape, final XDiagram diagram);
    
    void add(final XShape shape, final XDiagram diagram);
    
    void morph(final AnimationCommand command);
  }
  
  DirtyState getDirtyState();
  
  void showDirtyState(final DirtyState dirtyState);
  
  void hideDirtyState();
  
  void reconcile(final ReconcileBehavior.UpdateAcceptor acceptor);
}
