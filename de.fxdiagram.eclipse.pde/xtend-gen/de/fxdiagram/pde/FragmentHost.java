package de.fxdiagram.pde;

import de.fxdiagram.pde.BundleDependency;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class FragmentHost extends BundleDependency {
  private final BundleDescription fragment;
  
  @Override
  public BundleDependency.Kind getKind() {
    return BundleDependency.Kind.FRAGMENT_HOST;
  }
  
  @Override
  public BundleDescription getDependency() {
    return this.fragment;
  }
  
  @Override
  public VersionRange getVersionRange() {
    return VersionRange.emptyRange;
  }
  
  @Override
  public boolean isReexport() {
    return false;
  }
  
  @Override
  public boolean isOptional() {
    return false;
  }
  
  public FragmentHost(final BundleDescription owner, final BundleDescription fragment) {
    super(owner);
    this.fragment = fragment;
  }
  
  @Override
  @Pure
  public int hashCode() {
    return 31 * super.hashCode() + ((this.fragment== null) ? 0 : this.fragment.hashCode());
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
    if (!super.equals(obj))
      return false;
    FragmentHost other = (FragmentHost) obj;
    if (this.fragment == null) {
      if (other.fragment != null)
        return false;
    } else if (!this.fragment.equals(other.fragment))
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
  public BundleDescription getFragment() {
    return this.fragment;
  }
}
