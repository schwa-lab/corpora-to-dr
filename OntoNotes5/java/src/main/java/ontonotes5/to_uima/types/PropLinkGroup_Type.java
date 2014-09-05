
/* First created by JCasGen Fri Mar 14 09:40:47 EST 2014 */
package ontonotes5.to_uima.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.AnnotationBase_Type;

/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * @generated */
public class PropLinkGroup_Type extends AnnotationBase_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (PropLinkGroup_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = PropLinkGroup_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new PropLinkGroup(addr, PropLinkGroup_Type.this);
  			   PropLinkGroup_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new PropLinkGroup(addr, PropLinkGroup_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = PropLinkGroup.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ontonotes5.to_uima.types.PropLinkGroup");
 
  /** @generated */
  final Feature casFeat_kind;
  /** @generated */
  final int     casFeatCode_kind;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getKind(int addr) {
        if (featOkTst && casFeat_kind == null)
      jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.PropLinkGroup");
    return ll_cas.ll_getStringValue(addr, casFeatCode_kind);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setKind(int addr, String v) {
        if (featOkTst && casFeat_kind == null)
      jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.PropLinkGroup");
    ll_cas.ll_setStringValue(addr, casFeatCode_kind, v);}
    
  
 
  /** @generated */
  final Feature casFeat_associatedArgumentId;
  /** @generated */
  final int     casFeatCode_associatedArgumentId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAssociatedArgumentId(int addr) {
        if (featOkTst && casFeat_associatedArgumentId == null)
      jcas.throwFeatMissing("associatedArgumentId", "ontonotes5.to_uima.types.PropLinkGroup");
    return ll_cas.ll_getStringValue(addr, casFeatCode_associatedArgumentId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAssociatedArgumentId(int addr, String v) {
        if (featOkTst && casFeat_associatedArgumentId == null)
      jcas.throwFeatMissing("associatedArgumentId", "ontonotes5.to_uima.types.PropLinkGroup");
    ll_cas.ll_setStringValue(addr, casFeatCode_associatedArgumentId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_links;
  /** @generated */
  final int     casFeatCode_links;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getLinks(int addr) {
        if (featOkTst && casFeat_links == null)
      jcas.throwFeatMissing("links", "ontonotes5.to_uima.types.PropLinkGroup");
    return ll_cas.ll_getRefValue(addr, casFeatCode_links);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLinks(int addr, int v) {
        if (featOkTst && casFeat_links == null)
      jcas.throwFeatMissing("links", "ontonotes5.to_uima.types.PropLinkGroup");
    ll_cas.ll_setRefValue(addr, casFeatCode_links, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getLinks(int addr, int i) {
        if (featOkTst && casFeat_links == null)
      jcas.throwFeatMissing("links", "ontonotes5.to_uima.types.PropLinkGroup");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_links), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_links), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_links), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setLinks(int addr, int i, int v) {
        if (featOkTst && casFeat_links == null)
      jcas.throwFeatMissing("links", "ontonotes5.to_uima.types.PropLinkGroup");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_links), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_links), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_links), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public PropLinkGroup_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_kind = jcas.getRequiredFeatureDE(casType, "kind", "uima.cas.String", featOkTst);
    casFeatCode_kind  = (null == casFeat_kind) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_kind).getCode();

 
    casFeat_associatedArgumentId = jcas.getRequiredFeatureDE(casType, "associatedArgumentId", "uima.cas.String", featOkTst);
    casFeatCode_associatedArgumentId  = (null == casFeat_associatedArgumentId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_associatedArgumentId).getCode();

 
    casFeat_links = jcas.getRequiredFeatureDE(casType, "links", "uima.cas.FSArray", featOkTst);
    casFeatCode_links  = (null == casFeat_links) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_links).getCode();

  }
}



    