package de.fxdiagram.eclipse.changes;

import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import org.eclipse.ui.IWorkbenchPart;

@SuppressWarnings("all")
public interface IChangeSource {
  void addChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker);
  
  void removeChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker);
}
