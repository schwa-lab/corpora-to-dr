

/* First created by JCasGen Fri Mar 14 09:56:36 EST 2014 */
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
public class ParseNode extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(ParseNode.class);
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
  protected ParseNode() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public ParseNode(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public ParseNode(JCas jcas) {
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
  //* Feature: tag

  /** getter for tag - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTag() {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_tag == null)
      jcasType.jcas.throwFeatMissing("tag", "ontonotes5.to_uima.types.ParseNode");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ParseNode_Type)jcasType).casFeatCode_tag);}
    
  /** setter for tag - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTag(String v) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_tag == null)
      jcasType.jcas.throwFeatMissing("tag", "ontonotes5.to_uima.types.ParseNode");
    jcasType.ll_cas.ll_setStringValue(addr, ((ParseNode_Type)jcasType).casFeatCode_tag, v);}    
   
    
  //*--------------*
  //* Feature: pos

  /** getter for pos - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPos() {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "ontonotes5.to_uima.types.ParseNode");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ParseNode_Type)jcasType).casFeatCode_pos);}
    
  /** setter for pos - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPos(String v) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "ontonotes5.to_uima.types.ParseNode");
    jcasType.ll_cas.ll_setStringValue(addr, ((ParseNode_Type)jcasType).casFeatCode_pos, v);}    
   
    
  //*--------------*
  //* Feature: token

  /** getter for token - gets 
   * @generated
   * @return value of the feature 
   */
  public Token getToken() {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_token == null)
      jcasType.jcas.throwFeatMissing("token", "ontonotes5.to_uima.types.ParseNode");
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_token)));}
    
  /** setter for token - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setToken(Token v) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_token == null)
      jcasType.jcas.throwFeatMissing("token", "ontonotes5.to_uima.types.ParseNode");
    jcasType.ll_cas.ll_setRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_token, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: phraseType

  /** getter for phraseType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPhraseType() {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_phraseType == null)
      jcasType.jcas.throwFeatMissing("phraseType", "ontonotes5.to_uima.types.ParseNode");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ParseNode_Type)jcasType).casFeatCode_phraseType);}
    
  /** setter for phraseType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPhraseType(String v) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_phraseType == null)
      jcasType.jcas.throwFeatMissing("phraseType", "ontonotes5.to_uima.types.ParseNode");
    jcasType.ll_cas.ll_setStringValue(addr, ((ParseNode_Type)jcasType).casFeatCode_phraseType, v);}    
   
    
  //*--------------*
  //* Feature: functionTags

  /** getter for functionTags - gets 
   * @generated
   * @return value of the feature 
   */
  public String getFunctionTags() {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_functionTags == null)
      jcasType.jcas.throwFeatMissing("functionTags", "ontonotes5.to_uima.types.ParseNode");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ParseNode_Type)jcasType).casFeatCode_functionTags);}
    
  /** setter for functionTags - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFunctionTags(String v) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_functionTags == null)
      jcasType.jcas.throwFeatMissing("functionTags", "ontonotes5.to_uima.types.ParseNode");
    jcasType.ll_cas.ll_setStringValue(addr, ((ParseNode_Type)jcasType).casFeatCode_functionTags, v);}    
   
    
  //*--------------*
  //* Feature: corefSection

  /** getter for corefSection - gets 
   * @generated
   * @return value of the feature 
   */
  public int getCorefSection() {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_corefSection == null)
      jcasType.jcas.throwFeatMissing("corefSection", "ontonotes5.to_uima.types.ParseNode");
    return jcasType.ll_cas.ll_getIntValue(addr, ((ParseNode_Type)jcasType).casFeatCode_corefSection);}
    
  /** setter for corefSection - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCorefSection(int v) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_corefSection == null)
      jcasType.jcas.throwFeatMissing("corefSection", "ontonotes5.to_uima.types.ParseNode");
    jcasType.ll_cas.ll_setIntValue(addr, ((ParseNode_Type)jcasType).casFeatCode_corefSection, v);}    
   
    
  //*--------------*
  //* Feature: syntacticLink

  /** getter for syntacticLink - gets 
   * @generated
   * @return value of the feature 
   */
  public ParseNode getSyntacticLink() {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_syntacticLink == null)
      jcasType.jcas.throwFeatMissing("syntacticLink", "ontonotes5.to_uima.types.ParseNode");
    return (ParseNode)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_syntacticLink)));}
    
  /** setter for syntacticLink - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSyntacticLink(ParseNode v) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_syntacticLink == null)
      jcasType.jcas.throwFeatMissing("syntacticLink", "ontonotes5.to_uima.types.ParseNode");
    jcasType.ll_cas.ll_setRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_syntacticLink, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: children

  /** getter for children - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getChildren() {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_children == null)
      jcasType.jcas.throwFeatMissing("children", "ontonotes5.to_uima.types.ParseNode");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_children)));}
    
  /** setter for children - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setChildren(FSArray v) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_children == null)
      jcasType.jcas.throwFeatMissing("children", "ontonotes5.to_uima.types.ParseNode");
    jcasType.ll_cas.ll_setRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_children, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for children - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public ParseNode getChildren(int i) {
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_children == null)
      jcasType.jcas.throwFeatMissing("children", "ontonotes5.to_uima.types.ParseNode");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_children), i);
    return (ParseNode)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_children), i)));}

  /** indexed setter for children - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setChildren(int i, ParseNode v) { 
    if (ParseNode_Type.featOkTst && ((ParseNode_Type)jcasType).casFeat_children == null)
      jcasType.jcas.throwFeatMissing("children", "ontonotes5.to_uima.types.ParseNode");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_children), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((ParseNode_Type)jcasType).casFeatCode_children), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    