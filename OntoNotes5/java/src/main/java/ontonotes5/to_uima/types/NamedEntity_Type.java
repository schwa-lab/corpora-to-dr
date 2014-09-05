
/* First created by JCasGen Fri Mar 14 09:40:31 EST 2014 */
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
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * @generated */
public class NamedEntity_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (NamedEntity_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = NamedEntity_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new NamedEntity(addr, NamedEntity_Type.this);
  			   NamedEntity_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new NamedEntity(addr, NamedEntity_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = NamedEntity.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ontonotes5.to_uima.types.NamedEntity");
 
  /** @generated */
  final Feature casFeat_tag;
  /** @generated */
  final int     casFeatCode_tag;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTag(int addr) {
        if (featOkTst && casFeat_tag == null)
      jcas.throwFeatMissing("tag", "ontonotes5.to_uima.types.NamedEntity");
    return ll_cas.ll_getStringValue(addr, casFeatCode_tag);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTag(int addr, String v) {
        if (featOkTst && casFeat_tag == null)
      jcas.throwFeatMissing("tag", "ontonotes5.to_uima.types.NamedEntity");
    ll_cas.ll_setStringValue(addr, casFeatCode_tag, v);}
    
  
 
  /** @generated */
  final Feature casFeat_startOffset;
  /** @generated */
  final int     casFeatCode_startOffset;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getStartOffset(int addr) {
        if (featOkTst && casFeat_startOffset == null)
      jcas.throwFeatMissing("startOffset", "ontonotes5.to_uima.types.NamedEntity");
    return ll_cas.ll_getIntValue(addr, casFeatCode_startOffset);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setStartOffset(int addr, int v) {
        if (featOkTst && casFeat_startOffset == null)
      jcas.throwFeatMissing("startOffset", "ontonotes5.to_uima.types.NamedEntity");
    ll_cas.ll_setIntValue(addr, casFeatCode_startOffset, v);}
    
  
 
  /** @generated */
  final Feature casFeat_endOffset;
  /** @generated */
  final int     casFeatCode_endOffset;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getEndOffset(int addr) {
        if (featOkTst && casFeat_endOffset == null)
      jcas.throwFeatMissing("endOffset", "ontonotes5.to_uima.types.NamedEntity");
    return ll_cas.ll_getIntValue(addr, casFeatCode_endOffset);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEndOffset(int addr, int v) {
        if (featOkTst && casFeat_endOffset == null)
      jcas.throwFeatMissing("endOffset", "ontonotes5.to_uima.types.NamedEntity");
    ll_cas.ll_setIntValue(addr, casFeatCode_endOffset, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public NamedEntity_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_tag = jcas.getRequiredFeatureDE(casType, "tag", "uima.cas.String", featOkTst);
    casFeatCode_tag  = (null == casFeat_tag) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tag).getCode();

 
    casFeat_startOffset = jcas.getRequiredFeatureDE(casType, "startOffset", "uima.cas.Integer", featOkTst);
    casFeatCode_startOffset  = (null == casFeat_startOffset) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_startOffset).getCode();

 
    casFeat_endOffset = jcas.getRequiredFeatureDE(casType, "endOffset", "uima.cas.Integer", featOkTst);
    casFeatCode_endOffset  = (null == casFeat_endOffset) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_endOffset).getCode();

  }
}



    