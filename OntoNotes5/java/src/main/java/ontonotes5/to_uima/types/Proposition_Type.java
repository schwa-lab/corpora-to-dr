
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
public class Proposition_Type extends AnnotationBase_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Proposition_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Proposition_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Proposition(addr, Proposition_Type.this);
  			   Proposition_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Proposition(addr, Proposition_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Proposition.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ontonotes5.to_uima.types.Proposition");
 
  /** @generated */
  final Feature casFeat_encoded;
  /** @generated */
  final int     casFeatCode_encoded;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getEncoded(int addr) {
        if (featOkTst && casFeat_encoded == null)
      jcas.throwFeatMissing("encoded", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getStringValue(addr, casFeatCode_encoded);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEncoded(int addr, String v) {
        if (featOkTst && casFeat_encoded == null)
      jcas.throwFeatMissing("encoded", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setStringValue(addr, casFeatCode_encoded, v);}
    
  
 
  /** @generated */
  final Feature casFeat_quality;
  /** @generated */
  final int     casFeatCode_quality;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getQuality(int addr) {
        if (featOkTst && casFeat_quality == null)
      jcas.throwFeatMissing("quality", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getStringValue(addr, casFeatCode_quality);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setQuality(int addr, String v) {
        if (featOkTst && casFeat_quality == null)
      jcas.throwFeatMissing("quality", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setStringValue(addr, casFeatCode_quality, v);}
    
  
 
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
      jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getStringValue(addr, casFeatCode_kind);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setKind(int addr, String v) {
        if (featOkTst && casFeat_kind == null)
      jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setStringValue(addr, casFeatCode_kind, v);}
    
  
 
  /** @generated */
  final Feature casFeat_lemma;
  /** @generated */
  final int     casFeatCode_lemma;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getLemma(int addr) {
        if (featOkTst && casFeat_lemma == null)
      jcas.throwFeatMissing("lemma", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getStringValue(addr, casFeatCode_lemma);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLemma(int addr, String v) {
        if (featOkTst && casFeat_lemma == null)
      jcas.throwFeatMissing("lemma", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setStringValue(addr, casFeatCode_lemma, v);}
    
  
 
  /** @generated */
  final Feature casFeat_pbSenseNum;
  /** @generated */
  final int     casFeatCode_pbSenseNum;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPbSenseNum(int addr) {
        if (featOkTst && casFeat_pbSenseNum == null)
      jcas.throwFeatMissing("pbSenseNum", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getStringValue(addr, casFeatCode_pbSenseNum);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPbSenseNum(int addr, String v) {
        if (featOkTst && casFeat_pbSenseNum == null)
      jcas.throwFeatMissing("pbSenseNum", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setStringValue(addr, casFeatCode_pbSenseNum, v);}
    
  
 
  /** @generated */
  final Feature casFeat_leaf;
  /** @generated */
  final int     casFeatCode_leaf;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getLeaf(int addr) {
        if (featOkTst && casFeat_leaf == null)
      jcas.throwFeatMissing("leaf", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getRefValue(addr, casFeatCode_leaf);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLeaf(int addr, int v) {
        if (featOkTst && casFeat_leaf == null)
      jcas.throwFeatMissing("leaf", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setRefValue(addr, casFeatCode_leaf, v);}
    
  
 
  /** @generated */
  final Feature casFeat_predParts;
  /** @generated */
  final int     casFeatCode_predParts;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getPredParts(int addr) {
        if (featOkTst && casFeat_predParts == null)
      jcas.throwFeatMissing("predParts", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getRefValue(addr, casFeatCode_predParts);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPredParts(int addr, int v) {
        if (featOkTst && casFeat_predParts == null)
      jcas.throwFeatMissing("predParts", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setRefValue(addr, casFeatCode_predParts, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getPredParts(int addr, int i) {
        if (featOkTst && casFeat_predParts == null)
      jcas.throwFeatMissing("predParts", "ontonotes5.to_uima.types.Proposition");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_predParts), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_predParts), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_predParts), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setPredParts(int addr, int i, int v) {
        if (featOkTst && casFeat_predParts == null)
      jcas.throwFeatMissing("predParts", "ontonotes5.to_uima.types.Proposition");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_predParts), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_predParts), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_predParts), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_argGroups;
  /** @generated */
  final int     casFeatCode_argGroups;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getArgGroups(int addr) {
        if (featOkTst && casFeat_argGroups == null)
      jcas.throwFeatMissing("argGroups", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getRefValue(addr, casFeatCode_argGroups);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setArgGroups(int addr, int v) {
        if (featOkTst && casFeat_argGroups == null)
      jcas.throwFeatMissing("argGroups", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setRefValue(addr, casFeatCode_argGroups, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getArgGroups(int addr, int i) {
        if (featOkTst && casFeat_argGroups == null)
      jcas.throwFeatMissing("argGroups", "ontonotes5.to_uima.types.Proposition");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_argGroups), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_argGroups), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_argGroups), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setArgGroups(int addr, int i, int v) {
        if (featOkTst && casFeat_argGroups == null)
      jcas.throwFeatMissing("argGroups", "ontonotes5.to_uima.types.Proposition");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_argGroups), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_argGroups), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_argGroups), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_linkGroups;
  /** @generated */
  final int     casFeatCode_linkGroups;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getLinkGroups(int addr) {
        if (featOkTst && casFeat_linkGroups == null)
      jcas.throwFeatMissing("linkGroups", "ontonotes5.to_uima.types.Proposition");
    return ll_cas.ll_getRefValue(addr, casFeatCode_linkGroups);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLinkGroups(int addr, int v) {
        if (featOkTst && casFeat_linkGroups == null)
      jcas.throwFeatMissing("linkGroups", "ontonotes5.to_uima.types.Proposition");
    ll_cas.ll_setRefValue(addr, casFeatCode_linkGroups, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getLinkGroups(int addr, int i) {
        if (featOkTst && casFeat_linkGroups == null)
      jcas.throwFeatMissing("linkGroups", "ontonotes5.to_uima.types.Proposition");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_linkGroups), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_linkGroups), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_linkGroups), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setLinkGroups(int addr, int i, int v) {
        if (featOkTst && casFeat_linkGroups == null)
      jcas.throwFeatMissing("linkGroups", "ontonotes5.to_uima.types.Proposition");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_linkGroups), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_linkGroups), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_linkGroups), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Proposition_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_encoded = jcas.getRequiredFeatureDE(casType, "encoded", "uima.cas.String", featOkTst);
    casFeatCode_encoded  = (null == casFeat_encoded) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_encoded).getCode();

 
    casFeat_quality = jcas.getRequiredFeatureDE(casType, "quality", "uima.cas.String", featOkTst);
    casFeatCode_quality  = (null == casFeat_quality) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_quality).getCode();

 
    casFeat_kind = jcas.getRequiredFeatureDE(casType, "kind", "uima.cas.String", featOkTst);
    casFeatCode_kind  = (null == casFeat_kind) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_kind).getCode();

 
    casFeat_lemma = jcas.getRequiredFeatureDE(casType, "lemma", "uima.cas.String", featOkTst);
    casFeatCode_lemma  = (null == casFeat_lemma) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lemma).getCode();

 
    casFeat_pbSenseNum = jcas.getRequiredFeatureDE(casType, "pbSenseNum", "uima.cas.String", featOkTst);
    casFeatCode_pbSenseNum  = (null == casFeat_pbSenseNum) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_pbSenseNum).getCode();

 
    casFeat_leaf = jcas.getRequiredFeatureDE(casType, "leaf", "ontonotes5.to_uima.types.ParseNode", featOkTst);
    casFeatCode_leaf  = (null == casFeat_leaf) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_leaf).getCode();

 
    casFeat_predParts = jcas.getRequiredFeatureDE(casType, "predParts", "uima.cas.FSArray", featOkTst);
    casFeatCode_predParts  = (null == casFeat_predParts) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_predParts).getCode();

 
    casFeat_argGroups = jcas.getRequiredFeatureDE(casType, "argGroups", "uima.cas.FSArray", featOkTst);
    casFeatCode_argGroups  = (null == casFeat_argGroups) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_argGroups).getCode();

 
    casFeat_linkGroups = jcas.getRequiredFeatureDE(casType, "linkGroups", "uima.cas.FSArray", featOkTst);
    casFeatCode_linkGroups  = (null == casFeat_linkGroups) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_linkGroups).getCode();

  }
}



    