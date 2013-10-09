package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.mongodb.DBObject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.examples.lcars.LcarsAccess;
import de.fxdiagram.examples.lcars.LcarsDiagram;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.lcars.LcarsField;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsQueryTask extends Task<Void> {
  private LcarsField host;
  
  private String fieldName;
  
  private String fieldValue;
  
  private Function2<? super XNode,? super XNode,? extends XConnection> connectionFactory;
  
  public LcarsQueryTask(final LcarsField host, final String fieldName, final String fieldValue, final Function2<? super XNode,? super XNode,? extends XConnection> connectionFactory) {
    this.host = host;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
    this.connectionFactory = connectionFactory;
  }
  
  protected Void call() throws Exception {
    Void _xblockexpression = null;
    {
      LcarsDiagram _lcarsDiagram = LcarsExtensions.getLcarsDiagram(this.host);
      LcarsAccess _lcarsAccess = _lcarsDiagram.getLcarsAccess();
      final List<DBObject> siblings = _lcarsAccess.query(this.fieldName, this.fieldValue);
      final LcarsNode lcarsNode = this.host.getLcarsNode();
      CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(lcarsNode, Pos.BOTTOM_CENTER);
      final CoverFlowChooser chooser = _coverFlowChooser;
      chooser.setConnectionFactory(this.connectionFactory);
      final Function1<DBObject,Boolean> _function = new Function1<DBObject,Boolean>() {
        public Boolean apply(final DBObject it) {
          Object _get = it.get("_id");
          String _string = _get.toString();
          String _dbId = lcarsNode.getDbId();
          boolean _notEquals = (!Objects.equal(_string, _dbId));
          return Boolean.valueOf(_notEquals);
        }
      };
      Iterable<DBObject> _filter = IterableExtensions.<DBObject>filter(siblings, _function);
      final Function1<DBObject,LcarsNode> _function_1 = new Function1<DBObject,LcarsNode>() {
        public LcarsNode apply(final DBObject it) {
          LcarsNode _lcarsNode = new LcarsNode(it);
          final Procedure1<LcarsNode> _function = new Procedure1<LcarsNode>() {
            public void apply(final LcarsNode it) {
              double _width = lcarsNode.getWidth();
              it.setWidth(_width);
              double _height = lcarsNode.getHeight();
              it.setHeight(_height);
            }
          };
          LcarsNode _doubleArrow = ObjectExtensions.<LcarsNode>operator_doubleArrow(_lcarsNode, _function);
          return _doubleArrow;
        }
      };
      Iterable<LcarsNode> _map = IterableExtensions.<DBObject, LcarsNode>map(_filter, _function_1);
      chooser.operator_add(_map);
      final Runnable _function_2 = new Runnable() {
        public void run() {
          XRoot _root = CoreExtensions.getRoot(LcarsQueryTask.this.host);
          _root.setCurrentTool(chooser);
          LcarsQueryTask.this.host.resetColors();
        }
      };
      Platform.runLater(_function_2);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
