package de.fxdiagram.pde;

import de.fxdiagram.pde.BundleDependency;
import org.eclipse.osgi.service.resolver.BaseDescription;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class RequireBundle extends BundleDependency {
  private final BundleSpecification required;
  
  @Override
  public BundleDependency.Kind getKind() {
    return BundleDependency.Kind.REQUIRE_BUNDLE;
  }
  
  @Override
  public BundleDescription getDependency() {
    BaseDescription _supplier = this.required.getSupplier();
    BundleDescription _supplier_1 = null;
    if (_supplier!=null) {
      _supplier_1=_supplier.getSupplier();
    }
    return _supplier_1;
  }
  
  @Override
  public VersionRange getVersionRange() {
    VersionRange _elvis = null;
    VersionRange _versionRange = this.required.getVersionRange();
    if (_versionRange != null) {
      _elvis = _versionRange;
    } else {
      _elvis = VersionRange.emptyRange;
    }
    return _elvis;
  }
  
  @Override
  public boolean isReexport() {
    return this.required.isExported();
  }
  
  @Override
  public boolean isOptional() {
    return this.required.isOptional();
  }
  
  public RequireBundle(final BundleDescription owner, final BundleSpecification required) {
    super(owner);
    this.required = required;
  }
  
  @Override
  @Pure
  public int hashCode() {
    return 31 * super.hashCode() + ((this.required== null) ? 0 : this.required.hashCode());
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
    RequireBundle other = (RequireBundle) obj;
    if (this.required == null) {
      if (other.required != null)
        return false;
    } else if (!this.required.equals(other.required))
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
  public BundleSpecification getRequired() {
    return this.required;
  }
}
