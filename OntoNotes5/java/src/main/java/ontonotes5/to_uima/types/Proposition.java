

/* First created by JCasGen Fri Mar 14 09:40:47 EST 2014 */
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
public class Proposition extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Proposition.class);
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
  protected Proposition() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Proposition(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Proposition(JCas jcas) {
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
  //* Feature: encoded

  /** getter for encoded - gets 
   * @generated
   * @return value of the feature 
   */
  public String getEncoded() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_encoded == null)
      jcasType.jcas.throwFeatMissing("encoded", "ontonotes5.to_uima.types.Proposition");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_encoded);}
    
  /** setter for encoded - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEncoded(String v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_encoded == null)
      jcasType.jcas.throwFeatMissing("encoded", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_encoded, v);}    
   
    
  //*--------------*
  //* Feature: quality

  /** getter for quality - gets 
   * @generated
   * @return value of the feature 
   */
  public String getQuality() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_quality == null)
      jcasType.jcas.throwFeatMissing("quality", "ontonotes5.to_uima.types.Proposition");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_quality);}
    
  /** setter for quality - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setQuality(String v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_quality == null)
      jcasType.jcas.throwFeatMissing("quality", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_quality, v);}    
   
    
  //*--------------*
  //* Feature: kind

  /** getter for kind - gets 
   * @generated
   * @return value of the feature 
   */
  public String getKind() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.Proposition");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_kind);}
    
  /** setter for kind - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKind(String v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_kind, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLemma() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "ontonotes5.to_uima.types.Proposition");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLemma(String v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: pbSenseNum

  /** getter for pbSenseNum - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPbSenseNum() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_pbSenseNum == null)
      jcasType.jcas.throwFeatMissing("pbSenseNum", "ontonotes5.to_uima.types.Proposition");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_pbSenseNum);}
    
  /** setter for pbSenseNum - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPbSenseNum(String v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_pbSenseNum == null)
      jcasType.jcas.throwFeatMissing("pbSenseNum", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setStringValue(addr, ((Proposition_Type)jcasType).casFeatCode_pbSenseNum, v);}    
   
    
  //*--------------*
  //* Feature: leaf

  /** getter for leaf - gets 
   * @generated
   * @return value of the feature 
   */
  public ParseNode getLeaf() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_leaf == null)
      jcasType.jcas.throwFeatMissing("leaf", "ontonotes5.to_uima.types.Proposition");
    return (ParseNode)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_leaf)));}
    
  /** setter for leaf - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLeaf(ParseNode v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_leaf == null)
      jcasType.jcas.throwFeatMissing("leaf", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_leaf, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: predParts

  /** getter for predParts - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getPredParts() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_predParts == null)
      jcasType.jcas.throwFeatMissing("predParts", "ontonotes5.to_uima.types.Proposition");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_predParts)));}
    
  /** setter for predParts - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPredParts(FSArray v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_predParts == null)
      jcasType.jcas.throwFeatMissing("predParts", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_predParts, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for predParts - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public PropPredPart getPredParts(int i) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_predParts == null)
      jcasType.jcas.throwFeatMissing("predParts", "ontonotes5.to_uima.types.Proposition");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_predParts), i);
    return (PropPredPart)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_predParts), i)));}

  /** indexed setter for predParts - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setPredParts(int i, PropPredPart v) { 
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_predParts == null)
      jcasType.jcas.throwFeatMissing("predParts", "ontonotes5.to_uima.types.Proposition");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_predParts), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_predParts), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: argGroups

  /** getter for argGroups - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getArgGroups() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_argGroups == null)
      jcasType.jcas.throwFeatMissing("argGroups", "ontonotes5.to_uima.types.Proposition");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_argGroups)));}
    
  /** setter for argGroups - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setArgGroups(FSArray v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_argGroups == null)
      jcasType.jcas.throwFeatMissing("argGroups", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_argGroups, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for argGroups - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public PropArgGroup getArgGroups(int i) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_argGroups == null)
      jcasType.jcas.throwFeatMissing("argGroups", "ontonotes5.to_uima.types.Proposition");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_argGroups), i);
    return (PropArgGroup)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_argGroups), i)));}

  /** indexed setter for argGroups - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setArgGroups(int i, PropArgGroup v) { 
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_argGroups == null)
      jcasType.jcas.throwFeatMissing("argGroups", "ontonotes5.to_uima.types.Proposition");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_argGroups), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_argGroups), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: linkGroups

  /** getter for linkGroups - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getLinkGroups() {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_linkGroups == null)
      jcasType.jcas.throwFeatMissing("linkGroups", "ontonotes5.to_uima.types.Proposition");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_linkGroups)));}
    
  /** setter for linkGroups - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLinkGroups(FSArray v) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_linkGroups == null)
      jcasType.jcas.throwFeatMissing("linkGroups", "ontonotes5.to_uima.types.Proposition");
    jcasType.ll_cas.ll_setRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_linkGroups, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for linkGroups - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public PropLinkGroup getLinkGroups(int i) {
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_linkGroups == null)
      jcasType.jcas.throwFeatMissing("linkGroups", "ontonotes5.to_uima.types.Proposition");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_linkGroups), i);
    return (PropLinkGroup)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_linkGroups), i)));}

  /** indexed setter for linkGroups - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setLinkGroups(int i, PropLinkGroup v) { 
    if (Proposition_Type.featOkTst && ((Proposition_Type)jcasType).casFeat_linkGroups == null)
      jcasType.jcas.throwFeatMissing("linkGroups", "ontonotes5.to_uima.types.Proposition");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_linkGroups), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Proposition_Type)jcasType).casFeatCode_linkGroups), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    