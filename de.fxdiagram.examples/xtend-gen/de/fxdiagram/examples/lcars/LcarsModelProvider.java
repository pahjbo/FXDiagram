package de.fxdiagram.examples.lcars;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.examples.lcars.LcarsConnectionDescriptor;
import de.fxdiagram.examples.lcars.LcarsEntryDescriptor;
import java.util.List;
import org.bson.types.ObjectId;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class LcarsModelProvider implements DomainObjectProvider {
  private DB db;
  
  private DBCollection lcars;
  
  public LcarsModelProvider() {
    try {
      final Mongo mongo = new Mongo();
      this.db = mongo.getDB("startrek");
      this.lcars = this.db.getCollection("lcars");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public List<DBObject> query(final String fieldName, final Object fieldValue) {
    BasicDBObject _basicDBObject = new BasicDBObject();
    final Procedure1<BasicDBObject> _function = (BasicDBObject it) -> {
      it.put(fieldName, fieldValue);
    };
    BasicDBObject _doubleArrow = ObjectExtensions.<BasicDBObject>operator_doubleArrow(_basicDBObject, _function);
    DBCursor _find = this.lcars.find(_doubleArrow);
    return IterableExtensions.<DBObject>toList(((Iterable<DBObject>) _find));
  }
  
  public <T extends Object> DBObject resolveDomainObject(final LcarsEntryDescriptor descriptor) {
    BasicDBObject _basicDBObject = new BasicDBObject();
    final Procedure1<BasicDBObject> _function = (BasicDBObject it) -> {
      String _id = descriptor.getId();
      ObjectId _objectId = new ObjectId(_id);
      it.put("_id", _objectId);
    };
    BasicDBObject _doubleArrow = ObjectExtensions.<BasicDBObject>operator_doubleArrow(_basicDBObject, _function);
    return this.lcars.findOne(_doubleArrow);
  }
  
  public boolean canConnect() {
    try {
      return this.db.collectionExists("lcars");
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        return false;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  @Override
  public DomainObjectDescriptor createDescriptor(final Object it) {
    boolean _matched = false;
    if (it instanceof DBObject) {
      _matched=true;
      return this.createLcarsEntryDescriptor(((DBObject)it));
    }
    if (!_matched) {
      if (it instanceof String) {
        _matched=true;
        return this.createLcarsConnectionDescriptor(((String)it));
      }
    }
    throw new IllegalArgumentException("LcarsModelProvider only knows about DBObjects");
  }
  
  public LcarsEntryDescriptor createLcarsEntryDescriptor(final DBObject it) {
    String _string = it.get("_id").toString();
    String _string_1 = it.get("name").toString();
    return new LcarsEntryDescriptor(_string, _string_1, this);
  }
  
  public LcarsConnectionDescriptor createLcarsConnectionDescriptor(final String fieldName) {
    return new LcarsConnectionDescriptor(fieldName, this);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
