

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
public class PropArgGroup extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PropArgGroup.class);
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
  protected PropArgGroup() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public PropArgGroup(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public PropArgGroup(JCas jcas) {
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
  //* Feature: kind

  /** getter for kind - gets 
   * @generated
   * @return value of the feature 
   */
  public String getKind() {
    if (PropArgGroup_Type.featOkTst && ((PropArgGroup_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.PropArgGroup");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PropArgGroup_Type)jcasType).casFeatCode_kind);}
    
  /** setter for kind - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKind(String v) {
    if (PropArgGroup_Type.featOkTst && ((PropArgGroup_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.PropArgGroup");
    jcasType.ll_cas.ll_setStringValue(addr, ((PropArgGroup_Type)jcasType).casFeatCode_kind, v);}    
   
    
  //*--------------*
  //* Feature: args

  /** getter for args - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getArgs() {
    if (PropArgGroup_Type.featOkTst && ((PropArgGroup_Type)jcasType).casFeat_args == null)
      jcasType.jcas.throwFeatMissing("args", "ontonotes5.to_uima.types.PropArgGroup");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PropArgGroup_Type)jcasType).casFeatCode_args)));}
    
  /** setter for args - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setArgs(FSArray v) {
    if (PropArgGroup_Type.featOkTst && ((PropArgGroup_Type)jcasType).casFeat_args == null)
      jcasType.jcas.throwFeatMissing("args", "ontonotes5.to_uima.types.PropArgGroup");
    jcasType.ll_cas.ll_setRefValue(addr, ((PropArgGroup_Type)jcasType).casFeatCode_args, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for args - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public PropArg getArgs(int i) {
    if (PropArgGroup_Type.featOkTst && ((PropArgGroup_Type)jcasType).casFeat_args == null)
      jcasType.jcas.throwFeatMissing("args", "ontonotes5.to_uima.types.PropArgGroup");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((PropArgGroup_Type)jcasType).casFeatCode_args), i);
    return (PropArg)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((PropArgGroup_Type)jcasType).casFeatCode_args), i)));}

  /** indexed setter for args - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setArgs(int i, PropArg v) { 
    if (PropArgGroup_Type.featOkTst && ((PropArgGroup_Type)jcasType).casFeat_args == null)
      jcasType.jcas.throwFeatMissing("args", "ontonotes5.to_uima.types.PropArgGroup");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((PropArgGroup_Type)jcasType).casFeatCode_args), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((PropArgGroup_Type)jcasType).casFeatCode_args), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    