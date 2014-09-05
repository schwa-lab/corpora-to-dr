

/* First created by JCasGen Fri Mar 14 09:40:47 EST 2014 */
package ontonotes5.to_uima.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.AnnotationBase;


/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * XML source: /Users/tim/repos/tim.dawborn/phd/ontonotes5/java/desc/DBTypes.xml
 * @generated */
public class Speaker extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Speaker.class);
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
  protected Speaker() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Speaker(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Speaker(JCas jcas) {
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
  //* Feature: name

  /** getter for name - gets 
   * @generated
   * @return value of the feature 
   */
  public String getName() {
    if (Speaker_Type.featOkTst && ((Speaker_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "ontonotes5.to_uima.types.Speaker");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Speaker_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setName(String v) {
    if (Speaker_Type.featOkTst && ((Speaker_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "ontonotes5.to_uima.types.Speaker");
    jcasType.ll_cas.ll_setStringValue(addr, ((Speaker_Type)jcasType).casFeatCode_name, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated
   * @return value of the feature 
   */
  public String getGender() {
    if (Speaker_Type.featOkTst && ((Speaker_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "ontonotes5.to_uima.types.Speaker");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Speaker_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGender(String v) {
    if (Speaker_Type.featOkTst && ((Speaker_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "ontonotes5.to_uima.types.Speaker");
    jcasType.ll_cas.ll_setStringValue(addr, ((Speaker_Type)jcasType).casFeatCode_gender, v);}    
   
    
  //*--------------*
  //* Feature: competence

  /** getter for competence - gets 
   * @generated
   * @return value of the feature 
   */
  public String getCompetence() {
    if (Speaker_Type.featOkTst && ((Speaker_Type)jcasType).casFeat_competence == null)
      jcasType.jcas.throwFeatMissing("competence", "ontonotes5.to_uima.types.Speaker");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Speaker_Type)jcasType).casFeatCode_competence);}
    
  /** setter for competence - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCompetence(String v) {
    if (Speaker_Type.featOkTst && ((Speaker_Type)jcasType).casFeat_competence == null)
      jcasType.jcas.throwFeatMissing("competence", "ontonotes5.to_uima.types.Speaker");
    jcasType.ll_cas.ll_setStringValue(addr, ((Speaker_Type)jcasType).casFeatCode_competence, v);}    
  }

    