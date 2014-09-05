

/* First created by JCasGen Fri Mar 14 09:40:31 EST 2014 */
package ontonotes5.to_uima.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.AnnotationBase;
import org.apache.uima.jcas.cas.FSList;


/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * XML source: /Users/tim/repos/tim.dawborn/phd/ontonotes5/java/desc/DBTypes.xml
 * @generated */
public class CorefChain extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CorefChain.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CorefChain() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public CorefChain(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CorefChain(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets 
   * @generated
   * @return value of the feature 
   */
  public String getId() {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "ontonotes5.to_uima.types.CorefChain");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CorefChain_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "ontonotes5.to_uima.types.CorefChain");
    jcasType.ll_cas.ll_setStringValue(addr, ((CorefChain_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: section

  /** getter for section - gets 
   * @generated
   * @return value of the feature 
   */
  public int getSection() {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_section == null)
      jcasType.jcas.throwFeatMissing("section", "ontonotes5.to_uima.types.CorefChain");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CorefChain_Type)jcasType).casFeatCode_section);}
    
  /** setter for section - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSection(int v) {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_section == null)
      jcasType.jcas.throwFeatMissing("section", "ontonotes5.to_uima.types.CorefChain");
    jcasType.ll_cas.ll_setIntValue(addr, ((CorefChain_Type)jcasType).casFeatCode_section, v);}    
   
    
  //*--------------*
  //* Feature: kind

  /** getter for kind - gets 
   * @generated
   * @return value of the feature 
   */
  public String getKind() {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.CorefChain");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CorefChain_Type)jcasType).casFeatCode_kind);}
    
  /** setter for kind - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKind(String v) {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.CorefChain");
    jcasType.ll_cas.ll_setStringValue(addr, ((CorefChain_Type)jcasType).casFeatCode_kind, v);}    
   
    
  //*--------------*
  //* Feature: speaker

  /** getter for speaker - gets 
   * @generated
   * @return value of the feature 
   */
  public Speaker getSpeaker() {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_speaker == null)
      jcasType.jcas.throwFeatMissing("speaker", "ontonotes5.to_uima.types.CorefChain");
    return (Speaker)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CorefChain_Type)jcasType).casFeatCode_speaker)));}
    
  /** setter for speaker - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSpeaker(Speaker v) {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_speaker == null)
      jcasType.jcas.throwFeatMissing("speaker", "ontonotes5.to_uima.types.CorefChain");
    jcasType.ll_cas.ll_setRefValue(addr, ((CorefChain_Type)jcasType).casFeatCode_speaker, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: mentions

  /** getter for mentions - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getMentions() {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_mentions == null)
      jcasType.jcas.throwFeatMissing("mentions", "ontonotes5.to_uima.types.CorefChain");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CorefChain_Type)jcasType).casFeatCode_mentions)));}
    
  /** setter for mentions - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMentions(FSArray v) {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_mentions == null)
      jcasType.jcas.throwFeatMissing("mentions", "ontonotes5.to_uima.types.CorefChain");
    jcasType.ll_cas.ll_setRefValue(addr, ((CorefChain_Type)jcasType).casFeatCode_mentions, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for mentions - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public CorefMention getMentions(int i) {
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_mentions == null)
      jcasType.jcas.throwFeatMissing("mentions", "ontonotes5.to_uima.types.CorefChain");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CorefChain_Type)jcasType).casFeatCode_mentions), i);
    return (CorefMention)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CorefChain_Type)jcasType).casFeatCode_mentions), i)));}

  /** indexed setter for mentions - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setMentions(int i, CorefMention v) { 
    if (CorefChain_Type.featOkTst && ((CorefChain_Type)jcasType).casFeat_mentions == null)
      jcasType.jcas.throwFeatMissing("mentions", "ontonotes5.to_uima.types.CorefChain");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CorefChain_Type)jcasType).casFeatCode_mentions), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CorefChain_Type)jcasType).casFeatCode_mentions), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    