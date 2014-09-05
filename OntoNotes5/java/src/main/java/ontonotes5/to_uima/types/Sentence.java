

/* First created by JCasGen Fri Mar 14 09:40:47 EST 2014 */
package ontonotes5.to_uima.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * XML source: /Users/tim/repos/tim.dawborn/phd/ontonotes5/java/desc/DBTypes.xml
 * @generated */
public class Sentence extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Sentence.class);
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
  protected Sentence() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Sentence(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Sentence(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Sentence(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
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
  //* Feature: parse

  /** getter for parse - gets 
   * @generated
   * @return value of the feature 
   */
  public ParseNode getParse() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_parse == null)
      jcasType.jcas.throwFeatMissing("parse", "ontonotes5.to_uima.types.Sentence");
    return (ParseNode)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_parse)));}
    
  /** setter for parse - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParse(ParseNode v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_parse == null)
      jcasType.jcas.throwFeatMissing("parse", "ontonotes5.to_uima.types.Sentence");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_parse, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: startTime

  /** getter for startTime - gets 
   * @generated
   * @return value of the feature 
   */
  public double getStartTime() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_startTime == null)
      jcasType.jcas.throwFeatMissing("startTime", "ontonotes5.to_uima.types.Sentence");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Sentence_Type)jcasType).casFeatCode_startTime);}
    
  /** setter for startTime - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setStartTime(double v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_startTime == null)
      jcasType.jcas.throwFeatMissing("startTime", "ontonotes5.to_uima.types.Sentence");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Sentence_Type)jcasType).casFeatCode_startTime, v);}    
   
    
  //*--------------*
  //* Feature: endTime

  /** getter for endTime - gets 
   * @generated
   * @return value of the feature 
   */
  public double getEndTime() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_endTime == null)
      jcasType.jcas.throwFeatMissing("endTime", "ontonotes5.to_uima.types.Sentence");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Sentence_Type)jcasType).casFeatCode_endTime);}
    
  /** setter for endTime - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEndTime(double v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_endTime == null)
      jcasType.jcas.throwFeatMissing("endTime", "ontonotes5.to_uima.types.Sentence");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Sentence_Type)jcasType).casFeatCode_endTime, v);}    
   
    
  //*--------------*
  //* Feature: speakers

  /** getter for speakers - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getSpeakers() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_speakers == null)
      jcasType.jcas.throwFeatMissing("speakers", "ontonotes5.to_uima.types.Sentence");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_speakers)));}
    
  /** setter for speakers - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSpeakers(FSArray v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_speakers == null)
      jcasType.jcas.throwFeatMissing("speakers", "ontonotes5.to_uima.types.Sentence");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_speakers, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for speakers - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public Speaker getSpeakers(int i) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_speakers == null)
      jcasType.jcas.throwFeatMissing("speakers", "ontonotes5.to_uima.types.Sentence");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_speakers), i);
    return (Speaker)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_speakers), i)));}

  /** indexed setter for speakers - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setSpeakers(int i, Speaker v) { 
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_speakers == null)
      jcasType.jcas.throwFeatMissing("speakers", "ontonotes5.to_uima.types.Sentence");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_speakers), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_speakers), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: propositions

  /** getter for propositions - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getPropositions() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_propositions == null)
      jcasType.jcas.throwFeatMissing("propositions", "ontonotes5.to_uima.types.Sentence");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_propositions)));}
    
  /** setter for propositions - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPropositions(FSArray v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_propositions == null)
      jcasType.jcas.throwFeatMissing("propositions", "ontonotes5.to_uima.types.Sentence");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_propositions, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for propositions - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public Proposition getPropositions(int i) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_propositions == null)
      jcasType.jcas.throwFeatMissing("propositions", "ontonotes5.to_uima.types.Sentence");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_propositions), i);
    return (Proposition)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_propositions), i)));}

  /** indexed setter for propositions - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setPropositions(int i, Proposition v) { 
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_propositions == null)
      jcasType.jcas.throwFeatMissing("propositions", "ontonotes5.to_uima.types.Sentence");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_propositions), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_propositions), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    