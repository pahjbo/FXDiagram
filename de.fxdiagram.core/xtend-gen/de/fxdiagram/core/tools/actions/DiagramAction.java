package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.scene.input.KeyEvent;

/**
 * Some action that can happen on an {@link XDiagram}.
 * 
 * Usually triggered by the diagram's menu or a keystroke.
 */
@SuppressWarnings("all")
public interface DiagramAction {
  boolean matches(final KeyEvent event);
  
  SymbolType getSymbol();
  
  String getTooltip();
  
  void perform(final XRoot root);
}
