package de.fxdiagram.eclipse.xtext.hyperlink;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class FXDiagramHyperlinkHelper extends HyperlinkHelper {
  @Data
  public static class FXDiagramHyperlink implements IHyperlink {
    private final IMappedElementDescriptor<EObject> descriptor;
    
    private final EntryCall<EObject> entryCall;
    
    private final ITextRegion region;
    
    private final IEditorPart editor;
    
    @Override
    public IRegion getHyperlinkRegion() {
      int _offset = this.region.getOffset();
      int _length = this.region.getLength();
      return new Region(_offset, _length);
    }
    
    @Override
    public String getHyperlinkText() {
      String _text = this.entryCall.getText();
      return ("Show in FXDiagram as " + _text);
    }
    
    @Override
    public String getTypeLabel() {
      return "FXDiagram";
    }
    
    @Override
    public void open() {
      try {
        IWorkbenchPartSite _site = null;
        if (this.editor!=null) {
          _site=this.editor.getSite();
        }
        IWorkbenchWindow _workbenchWindow = null;
        if (_site!=null) {
          _workbenchWindow=_site.getWorkbenchWindow();
        }
        IWorkbench _workbench = null;
        if (_workbenchWindow!=null) {
          _workbench=_workbenchWindow.getWorkbench();
        }
        IWorkbenchWindow _activeWorkbenchWindow = null;
        if (_workbench!=null) {
          _activeWorkbenchWindow=_workbench.getActiveWorkbenchWindow();
        }
        IWorkbenchPage _activePage = null;
        if (_activeWorkbenchWindow!=null) {
          _activePage=_activeWorkbenchWindow.getActivePage();
        }
        IViewPart _showView = null;
        if (_activePage!=null) {
          _showView=_activePage.showView("de.fxdiagram.eclipse.FXDiagramView");
        }
        final IViewPart view = _showView;
        if ((view instanceof FXDiagramView)) {
          final Function1<EObject, Object> _function = (EObject it) -> {
            Object _xblockexpression = null;
            {
              ((FXDiagramView)view).<EObject>revealElement(it, this.entryCall, this.editor);
              _xblockexpression = null;
            }
            return _xblockexpression;
          };
          this.descriptor.<Object>withDomainObject(_function);
        }
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    }
    
    public FXDiagramHyperlink(final IMappedElementDescriptor<EObject> descriptor, final EntryCall<EObject> entryCall, final ITextRegion region, final IEditorPart editor) {
      super();
      this.descriptor = descriptor;
      this.entryCall = entryCall;
      this.region = region;
      this.editor = editor;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.descriptor== null) ? 0 : this.descriptor.hashCode());
      result = prime * result + ((this.entryCall== null) ? 0 : this.entryCall.hashCode());
      result = prime * result + ((this.region== null) ? 0 : this.region.hashCode());
      return prime * result + ((this.editor== null) ? 0 : this.editor.hashCode());
    }
    
    @Override
    @Pure
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      FXDiagramHyperlinkHelper.FXDiagramHyperlink other = (FXDiagramHyperlinkHelper.FXDiagramHyperlink) obj;
      if (this.descriptor == null) {
        if (other.descriptor != null)
          return false;
      } else if (!this.descriptor.equals(other.descriptor))
        return false;
      if (this.entryCall == null) {
        if (other.entryCall != null)
          return false;
      } else if (!this.entryCall.equals(other.entryCall))
        return false;
      if (this.region == null) {
        if (other.region != null)
          return false;
      } else if (!this.region.equals(other.region))
        return false;
      if (this.editor == null) {
        if (other.editor != null)
          return false;
      } else if (!this.editor.equals(other.editor))
        return false;
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("descriptor", this.descriptor);
      b.add("entryCall", this.entryCall);
      b.add("region", this.region);
      b.add("editor", this.editor);
      return b.toString();
    }
    
    @Pure
    public IMappedElementDescriptor<EObject> getDescriptor() {
      return this.descriptor;
    }
    
    @Pure
    public EntryCall<EObject> getEntryCall() {
      return this.entryCall;
    }
    
    @Pure
    public ITextRegion getRegion() {
      return this.region;
    }
    
    @Pure
    public IEditorPart getEditor() {
      return this.editor;
    }
  }
  
  public static final String DELEGATE = "de.fxdiagram.eclipse.xtext.FXDiagramHyperlinkHelper.Delegate";
  
  @Inject
  @Named(FXDiagramHyperlinkHelper.DELEGATE)
  private IHyperlinkHelper delegate;
  
  @Inject
  private IWorkbench workbench;
  
  @Inject
  private ILocationInFileProvider locationInFileProvider;
  
  @Override
  public IHyperlink[] createHyperlinksByOffset(final XtextResource resource, final int offset, final boolean createMultipleHyperlinks) {
    Iterable<IHyperlink> _xblockexpression = null;
    {
      List<IHyperlink> _emptyListIfNull = this.<IHyperlink>emptyListIfNull(this.delegate.createHyperlinksByOffset(resource, offset, createMultipleHyperlinks));
      List<IHyperlink> _emptyListIfNull_1 = this.<IHyperlink>emptyListIfNull(super.createHyperlinksByOffset(resource, offset, createMultipleHyperlinks));
      final Iterable<IHyperlink> hyperlinks = Iterables.<IHyperlink>concat(_emptyListIfNull, _emptyListIfNull_1);
      Iterable<IHyperlink> _xifexpression = null;
      boolean _isEmpty = IterableExtensions.isEmpty(hyperlinks);
      if (_isEmpty) {
        _xifexpression = null;
      } else {
        _xifexpression = hyperlinks;
      }
      _xblockexpression = _xifexpression;
    }
    return ((IHyperlink[])Conversions.unwrapArray(_xblockexpression, IHyperlink.class));
  }
  
  protected <T extends Object> List<T> emptyListIfNull(final T[] array) {
    List<T> _elvis = null;
    if (((List<T>) Conversions.doWrapArray(array)) != null) {
      _elvis = ((List<T>) Conversions.doWrapArray(array));
    } else {
      List<T> _emptyList = CollectionLiterals.<T>emptyList();
      _elvis = _emptyList;
    }
    return _elvis;
  }
  
  @Override
  public void createHyperlinksByOffset(final XtextResource resource, final int offset, final IHyperlinkAcceptor acceptor) {
    final EObject selectedElement = this.getEObjectAtOffsetHelper().resolveElementAt(resource, offset);
    IWorkbenchWindow _activeWorkbenchWindow = null;
    if (this.workbench!=null) {
      _activeWorkbenchWindow=this.workbench.getActiveWorkbenchWindow();
    }
    IWorkbenchPage _activePage = null;
    if (_activeWorkbenchWindow!=null) {
      _activePage=_activeWorkbenchWindow.getActivePage();
    }
    IEditorPart _activeEditor = null;
    if (_activePage!=null) {
      _activeEditor=_activePage.getActiveEditor();
    }
    final IEditorPart editor = _activeEditor;
    if ((selectedElement != null)) {
      final Function1<XDiagramConfig, Iterable<? extends EntryCall<EObject>>> _function = (XDiagramConfig it) -> {
        return it.<EObject>getEntryCalls(selectedElement);
      };
      final Iterable<EntryCall<EObject>> entryCalls = Iterables.<EntryCall<EObject>>concat(IterableExtensions.map(XDiagramConfig.Registry.getInstance().getConfigurations(), _function));
      boolean _isEmpty = IterableExtensions.isEmpty(entryCalls);
      boolean _not = (!_isEmpty);
      if (_not) {
        final ITextRegion region = this.locationInFileProvider.getSignificantTextRegion(selectedElement);
        for (final EntryCall<EObject> entryCall : entryCalls) {
          {
            final IMappedElementDescriptorProvider domainObjectProvider = entryCall.getConfig().getDomainObjectProvider();
            if ((domainObjectProvider instanceof XtextDomainObjectProvider)) {
              IMappedElementDescriptorProvider _domainObjectProvider = entryCall.getConfig().getDomainObjectProvider();
              XDiagramConfig _config = entryCall.getConfig();
              NodeMapping<EObject> _nodeMapping = new NodeMapping<EObject>(_config, "dummy", "dummy");
              final IMappedElementDescriptor<EObject> descriptor = _domainObjectProvider.<EObject>createMappedElementDescriptor(selectedElement, _nodeMapping);
              FXDiagramHyperlinkHelper.FXDiagramHyperlink _fXDiagramHyperlink = new FXDiagramHyperlinkHelper.FXDiagramHyperlink(descriptor, entryCall, region, editor);
              acceptor.accept(_fXDiagramHyperlink);
            }
          }
        }
      }
    }
  }
}
