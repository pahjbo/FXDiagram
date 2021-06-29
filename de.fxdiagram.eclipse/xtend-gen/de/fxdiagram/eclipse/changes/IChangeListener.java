package de.fxdiagram.eclipse.changes;

import org.eclipse.ui.IWorkbenchPart;

@SuppressWarnings("all")
public interface IChangeListener {
  void partChanged(final IWorkbenchPart part);
}
