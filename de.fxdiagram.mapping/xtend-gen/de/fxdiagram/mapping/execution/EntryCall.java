package de.fxdiagram.mapping.execution;

import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;

@SuppressWarnings("all")
public interface EntryCall<ARG extends Object> {
  XDiagramConfig getConfig();
  
  void execute(final ARG domainObject, final XDiagramConfigInterpreter interpreter, final InterpreterContext context);
  
  String getText();
}
