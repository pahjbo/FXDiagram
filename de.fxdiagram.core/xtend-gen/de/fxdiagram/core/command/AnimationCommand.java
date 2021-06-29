package de.fxdiagram.core.command;

import de.fxdiagram.core.command.CommandContext;
import javafx.animation.Animation;

/**
 * Base interface for commands.
 * 
 * FXDiagram extends the command pattern by the idea that everything should be animated
 * in order to give a better user experience. So instead of changing some diagram model,
 * we apply {@link Animations} to the scenegraph directly.
 * 
 * Consider overrding {@link AbstractAnimationCommand} instead.
 */
@SuppressWarnings("all")
public interface AnimationCommand {
  Animation getExecuteAnimation(final CommandContext context);
  
  Animation getUndoAnimation(final CommandContext context);
  
  Animation getRedoAnimation(final CommandContext context);
  
  boolean clearRedoStackOnExecute();
  
  /**
   * Consider package private. Clients should not override this.
   */
  void skipViewportRestore();
  
  static final AnimationCommand NOOP = new AnimationCommand() {
    @Override
    public Animation getExecuteAnimation(final CommandContext context) {
      return null;
    }
    
    @Override
    public Animation getUndoAnimation(final CommandContext context) {
      return null;
    }
    
    @Override
    public Animation getRedoAnimation(final CommandContext context) {
      return null;
    }
    
    @Override
    public boolean clearRedoStackOnExecute() {
      return false;
    }
    
    @Override
    public void skipViewportRestore() {
    }
  };
}
