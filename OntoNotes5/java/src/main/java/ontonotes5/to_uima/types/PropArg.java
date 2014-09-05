

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
public class PropArg extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PropArg.class);
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
  protected PropArg() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public PropArg(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public PropArg(JCas jcas) {
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
    if (PropArg_Type.featOkTst && ((PropArg_Type)jcasType).casFeat_parts == null)
      jcasType.jcas.throwFeatMissing("parts", "ontonotes5.to_uima.types.PropArg");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PropArg_Type)jcasType).casFeatCode_parts)));}
    
  /** setter for parts - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParts(FSArray v) {
    if (PropArg_Type.featOkTst && ((PropArg_Type)jcasType).casFeat_parts == null)
      jcasType.jcas.throwFeatMissing("parts", "ontonotes5.to_uima.types.PropArg");
    jcasType.ll_cas.ll_setRefValue(addr, ((PropArg_Type)jcasType).casFeatCode_parts, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for parts - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public PropArgPart getParts(int i) {
    if (PropArg_Type.featOkTst && ((PropArg_Type)jcasType).casFeat_parts == null)
      jcasType.jcas.throwFeatMissing("parts", "ontonotes5.to_uima.types.PropArg");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((PropArg_Type)jcasType).casFeatCode_parts), i);
    return (PropArgPart)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((PropArg_Type)jcasType).casFeatCode_parts), i)));}

  /** indexed setter for parts - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setParts(int i, PropArgPart v) { 
    if (PropArg_Type.featOkTst && ((PropArg_Type)jcasType).casFeat_parts == null)
      jcasType.jcas.throwFeatMissing("parts", "ontonotes5.to_uima.types.PropArg");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((PropArg_Type)jcasType).casFeatCode_parts), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((PropArg_Type)jcasType).casFeatCode_parts), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    