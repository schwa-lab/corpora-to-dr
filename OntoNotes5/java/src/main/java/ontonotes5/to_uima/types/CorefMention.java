

/* First created by JCasGen Fri Mar 14 09:40:31 EST 2014 */
package ontonotes5.to_uima.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * XML source: /Users/tim/repos/tim.dawborn/phd/ontonotes5/java/desc/DBTypes.xml
 * @generated */
public class CorefMention extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CorefMention.class);
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
  protected CorefMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public CorefMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CorefMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public CorefMention(JCas jcas, int begin, int end) {
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
  //* Feature: kind

  /** getter for kind - gets 
   * @generated
   * @return value of the feature 
   */
  public String getKind() {
    if (CorefMention_Type.featOkTst && ((CorefMention_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.CorefMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CorefMention_Type)jcasType).casFeatCode_kind);}
    
  /** setter for kind - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKind(String v) {
    if (CorefMention_Type.featOkTst && ((CorefMention_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.CorefMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((CorefMention_Type)jcasType).casFeatCode_kind, v);}    
   
    
  //*--------------*
  //* Feature: startOffset

  /** getter for startOffset - gets 
   * @generated
   * @return value of the feature 
   */
  public int getStartOffset() {
    if (CorefMention_Type.featOkTst && ((CorefMention_Type)jcasType).casFeat_startOffset == null)
      jcasType.jcas.throwFeatMissing("startOffset", "ontonotes5.to_uima.types.CorefMention");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CorefMention_Type)jcasType).casFeatCode_startOffset);}
    
  /** setter for startOffset - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setStartOffset(int v) {
    if (CorefMention_Type.featOkTst && ((CorefMention_Type)jcasType).casFeat_startOffset == null)
      jcasType.jcas.throwFeatMissing("startOffset", "ontonotes5.to_uima.types.CorefMention");
    jcasType.ll_cas.ll_setIntValue(addr, ((CorefMention_Type)jcasType).casFeatCode_startOffset, v);}    
   
    
  //*--------------*
  //* Feature: endOffset

  /** getter for endOffset - gets 
   * @generated
   * @return value of the feature 
   */
  public int getEndOffset() {
    if (CorefMention_Type.featOkTst && ((CorefMention_Type)jcasType).casFeat_endOffset == null)
      jcasType.jcas.throwFeatMissing("endOffset", "ontonotes5.to_uima.types.CorefMention");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CorefMention_Type)jcasType).casFeatCode_endOffset);}
    
  /** setter for endOffset - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEndOffset(int v) {
    if (CorefMention_Type.featOkTst && ((CorefMention_Type)jcasType).casFeat_endOffset == null)
      jcasType.jcas.throwFeatMissing("endOffset", "ontonotes5.to_uima.types.CorefMention");
    jcasType.ll_cas.ll_setIntValue(addr, ((CorefMention_Type)jcasType).casFeatCode_endOffset, v);}    
  }

    