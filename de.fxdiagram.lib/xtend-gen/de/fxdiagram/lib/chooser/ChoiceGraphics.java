package de.fxdiagram.lib.chooser;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.chooser.AbstractBaseChooser;
import javafx.scene.Node;

@SuppressWarnings("all")
public interface ChoiceGraphics extends XActivatable {
  void setInterpolatedPosition(final double interpolatedPosition);
  
  void nodeChosen(final XNode choice);
  
  void relocateButtons(final Node minusButton, final Node plusButton);
  
  boolean hasButtons();
  
  void setChooser(final AbstractBaseChooser chooser);
}
