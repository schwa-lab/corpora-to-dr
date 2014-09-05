
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
public class CorefMention_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CorefMention_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CorefMention_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CorefMention(addr, CorefMention_Type.this);
  			   CorefMention_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CorefMention(addr, CorefMention_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CorefMention.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ontonotes5.to_uima.types.CorefMention");
 
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
      jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.CorefMention");
    return ll_cas.ll_getStringValue(addr, casFeatCode_kind);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setKind(int addr, String v) {
        if (featOkTst && casFeat_kind == null)
      jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.CorefMention");
    ll_cas.ll_setStringValue(addr, casFeatCode_kind, v);}
    
  
 
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
      jcas.throwFeatMissing("startOffset", "ontonotes5.to_uima.types.CorefMention");
    return ll_cas.ll_getIntValue(addr, casFeatCode_startOffset);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setStartOffset(int addr, int v) {
        if (featOkTst && casFeat_startOffset == null)
      jcas.throwFeatMissing("startOffset", "ontonotes5.to_uima.types.CorefMention");
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
      jcas.throwFeatMissing("endOffset", "ontonotes5.to_uima.types.CorefMention");
    return ll_cas.ll_getIntValue(addr, casFeatCode_endOffset);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEndOffset(int addr, int v) {
        if (featOkTst && casFeat_endOffset == null)
      jcas.throwFeatMissing("endOffset", "ontonotes5.to_uima.types.CorefMention");
    ll_cas.ll_setIntValue(addr, casFeatCode_endOffset, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public CorefMention_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_kind = jcas.getRequiredFeatureDE(casType, "kind", "uima.cas.String", featOkTst);
    casFeatCode_kind  = (null == casFeat_kind) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_kind).getCode();

 
    casFeat_startOffset = jcas.getRequiredFeatureDE(casType, "startOffset", "uima.cas.Integer", featOkTst);
    casFeatCode_startOffset  = (null == casFeat_startOffset) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_startOffset).getCode();

 
    casFeat_endOffset = jcas.getRequiredFeatureDE(casType, "endOffset", "uima.cas.Integer", featOkTst);
    casFeatCode_endOffset  = (null == casFeat_endOffset) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_endOffset).getCode();

  }
}



    