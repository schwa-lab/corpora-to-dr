
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
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * @generated */
public class Sentence_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Sentence_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Sentence_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Sentence(addr, Sentence_Type.this);
  			   Sentence_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Sentence(addr, Sentence_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Sentence.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ontonotes5.to_uima.types.Sentence");
 
  /** @generated */
  final Feature casFeat_parse;
  /** @generated */
  final int     casFeatCode_parse;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getParse(int addr) {
        if (featOkTst && casFeat_parse == null)
      jcas.throwFeatMissing("parse", "ontonotes5.to_uima.types.Sentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_parse);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setParse(int addr, int v) {
        if (featOkTst && casFeat_parse == null)
      jcas.throwFeatMissing("parse", "ontonotes5.to_uima.types.Sentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_parse, v);}
    
  
 
  /** @generated */
  final Feature casFeat_startTime;
  /** @generated */
  final int     casFeatCode_startTime;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public double getStartTime(int addr) {
        if (featOkTst && casFeat_startTime == null)
      jcas.throwFeatMissing("startTime", "ontonotes5.to_uima.types.Sentence");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_startTime);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setStartTime(int addr, double v) {
        if (featOkTst && casFeat_startTime == null)
      jcas.throwFeatMissing("startTime", "ontonotes5.to_uima.types.Sentence");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_startTime, v);}
    
  
 
  /** @generated */
  final Feature casFeat_endTime;
  /** @generated */
  final int     casFeatCode_endTime;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public double getEndTime(int addr) {
        if (featOkTst && casFeat_endTime == null)
      jcas.throwFeatMissing("endTime", "ontonotes5.to_uima.types.Sentence");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_endTime);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEndTime(int addr, double v) {
        if (featOkTst && casFeat_endTime == null)
      jcas.throwFeatMissing("endTime", "ontonotes5.to_uima.types.Sentence");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_endTime, v);}
    
  
 
  /** @generated */
  final Feature casFeat_speakers;
  /** @generated */
  final int     casFeatCode_speakers;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getSpeakers(int addr) {
        if (featOkTst && casFeat_speakers == null)
      jcas.throwFeatMissing("speakers", "ontonotes5.to_uima.types.Sentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_speakers);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSpeakers(int addr, int v) {
        if (featOkTst && casFeat_speakers == null)
      jcas.throwFeatMissing("speakers", "ontonotes5.to_uima.types.Sentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_speakers, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getSpeakers(int addr, int i) {
        if (featOkTst && casFeat_speakers == null)
      jcas.throwFeatMissing("speakers", "ontonotes5.to_uima.types.Sentence");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_speakers), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_speakers), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_speakers), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setSpeakers(int addr, int i, int v) {
        if (featOkTst && casFeat_speakers == null)
      jcas.throwFeatMissing("speakers", "ontonotes5.to_uima.types.Sentence");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_speakers), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_speakers), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_speakers), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_propositions;
  /** @generated */
  final int     casFeatCode_propositions;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getPropositions(int addr) {
        if (featOkTst && casFeat_propositions == null)
      jcas.throwFeatMissing("propositions", "ontonotes5.to_uima.types.Sentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_propositions);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPropositions(int addr, int v) {
        if (featOkTst && casFeat_propositions == null)
      jcas.throwFeatMissing("propositions", "ontonotes5.to_uima.types.Sentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_propositions, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getPropositions(int addr, int i) {
        if (featOkTst && casFeat_propositions == null)
      jcas.throwFeatMissing("propositions", "ontonotes5.to_uima.types.Sentence");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_propositions), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_propositions), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_propositions), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setPropositions(int addr, int i, int v) {
        if (featOkTst && casFeat_propositions == null)
      jcas.throwFeatMissing("propositions", "ontonotes5.to_uima.types.Sentence");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_propositions), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_propositions), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_propositions), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Sentence_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_parse = jcas.getRequiredFeatureDE(casType, "parse", "ontonotes5.to_uima.types.ParseNode", featOkTst);
    casFeatCode_parse  = (null == casFeat_parse) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_parse).getCode();

 
    casFeat_startTime = jcas.getRequiredFeatureDE(casType, "startTime", "uima.cas.Double", featOkTst);
    casFeatCode_startTime  = (null == casFeat_startTime) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_startTime).getCode();

 
    casFeat_endTime = jcas.getRequiredFeatureDE(casType, "endTime", "uima.cas.Double", featOkTst);
    casFeatCode_endTime  = (null == casFeat_endTime) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_endTime).getCode();

 
    casFeat_speakers = jcas.getRequiredFeatureDE(casType, "speakers", "uima.cas.FSArray", featOkTst);
    casFeatCode_speakers  = (null == casFeat_speakers) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_speakers).getCode();

 
    casFeat_propositions = jcas.getRequiredFeatureDE(casType, "propositions", "uima.cas.FSArray", featOkTst);
    casFeatCode_propositions  = (null == casFeat_propositions) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_propositions).getCode();

  }
}



    