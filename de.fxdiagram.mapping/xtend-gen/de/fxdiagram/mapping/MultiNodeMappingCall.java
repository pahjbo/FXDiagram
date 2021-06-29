package de.fxdiagram.mapping;

import de.fxdiagram.mapping.AbstractNodeMappingCall;
import de.fxdiagram.mapping.NodeMapping;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class MultiNodeMappingCall<RESULT extends Object, ARG extends Object> extends AbstractNodeMappingCall<RESULT, ARG> {
  private final Function1<? super ARG, ? extends Iterable<? extends RESULT>> selector;
  
  private final NodeMapping<RESULT> nodeMapping;
  
  public MultiNodeMappingCall(final Function1<? super ARG, ? extends Iterable<? extends RESULT>> selector, final NodeMapping<RESULT> nodeMapping) {
    super();
    this.selector = selector;
    this.nodeMapping = nodeMapping;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.selector== null) ? 0 : this.selector.hashCode());
    return prime * result + ((this.nodeMapping== null) ? 0 : this.nodeMapping.hashCode());
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
    MultiNodeMappingCall<?, ?> other = (MultiNodeMappingCall<?, ?>) obj;
    if (this.selector == null) {
      if (other.selector != null)
        return false;
    } else if (!this.selector.equals(other.selector))
      return false;
    if (this.nodeMapping == null) {
      if (other.nodeMapping != null)
        return false;
    } else if (!this.nodeMapping.equals(other.nodeMapping))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    return new ToStringBuilder(this)
    	.addAllFields()
    	.toString();
  }
  
  @Pure
  public Function1<? super ARG, ? extends Iterable<? extends RESULT>> getSelector() {
    return this.selector;
  }
  
  @Pure
  public NodeMapping<RESULT> getNodeMapping() {
    return this.nodeMapping;
  }
}
