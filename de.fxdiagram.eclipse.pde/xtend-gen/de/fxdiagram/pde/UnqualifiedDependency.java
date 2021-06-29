package de.fxdiagram.pde;

import de.fxdiagram.pde.BundleDependency;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.osgi.framework.Version;

@Data
@SuppressWarnings("all")
public class UnqualifiedDependency extends BundleDependency {
  private final BundleDescription dependency;
  
  @Override
  public boolean isReexport() {
    return false;
  }
  
  @Override
  public boolean isOptional() {
    return false;
  }
  
  @Override
  public VersionRange getVersionRange() {
    Version _version = this.dependency.getVersion();
    Version _version_1 = this.dependency.getVersion();
    return new VersionRange(_version, true, _version_1, true);
  }
  
  @Override
  public BundleDependency.Kind getKind() {
    return BundleDependency.Kind.UNQUALIFIED;
  }
  
  public UnqualifiedDependency(final BundleDescription owner, final BundleDescription dependency) {
    super(owner);
    this.dependency = dependency;
  }
  
  @Override
  @Pure
  public int hashCode() {
    return 31 * super.hashCode() + ((this.dependency== null) ? 0 : this.dependency.hashCode());
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
    UnqualifiedDependency other = (UnqualifiedDependency) obj;
    if (this.dependency == null) {
      if (other.dependency != null)
        return false;
    } else if (!this.dependency.equals(other.dependency))
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
  public BundleDescription getDependency() {
    return this.dependency;
  }
}
