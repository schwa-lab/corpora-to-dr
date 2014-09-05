

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
public class PropLink extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PropLink.class);
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
  protected PropLink() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public PropLink(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public PropLink(JCas jcas) {
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
  //* Feature: parts

  /** getter for parts - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getParts() {
    if (PropLink_Type.featOkTst && ((PropLink_Type)jcasType).casFeat_parts == null)
      jcasType.jcas.throwFeatMissing("parts", "ontonotes5.to_uima.types.PropLink");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PropLink_Type)jcasType).casFeatCode_parts)));}
    
  /** setter for parts - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParts(FSArray v) {
    if (PropLink_Type.featOkTst && ((PropLink_Type)jcasType).casFeat_parts == null)
      jcasType.jcas.throwFeatMissing("parts", "ontonotes5.to_uima.types.PropLink");
    jcasType.ll_cas.ll_setRefValue(addr, ((PropLink_Type)jcasType).casFeatCode_parts, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for parts - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public PropLinkPart getParts(int i) {
    if (PropLink_Type.featOkTst && ((PropLink_Type)jcasType).casFeat_parts == null)
      jcasType.jcas.throwFeatMissing("parts", "ontonotes5.to_uima.types.PropLink");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((PropLink_Type)jcasType).casFeatCode_parts), i);
    return (PropLinkPart)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((PropLink_Type)jcasType).casFeatCode_parts), i)));}

  /** indexed setter for parts - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setParts(int i, PropLinkPart v) { 
    if (PropLink_Type.featOkTst && ((PropLink_Type)jcasType).casFeat_parts == null)
      jcasType.jcas.throwFeatMissing("parts", "ontonotes5.to_uima.types.PropLink");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((PropLink_Type)jcasType).casFeatCode_parts), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((PropLink_Type)jcasType).casFeatCode_parts), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    