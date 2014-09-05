

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
public class PropLinkPart extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PropLinkPart.class);
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
  protected PropLinkPart() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public PropLinkPart(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public PropLinkPart(JCas jcas) {
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
    if (PropLinkPart_Type.featOkTst && ((PropLinkPart_Type)jcasType).casFeat_encoded == null)
      jcasType.jcas.throwFeatMissing("encoded", "ontonotes5.to_uima.types.PropLinkPart");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PropLinkPart_Type)jcasType).casFeatCode_encoded);}
    
  /** setter for encoded - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEncoded(String v) {
    if (PropLinkPart_Type.featOkTst && ((PropLinkPart_Type)jcasType).casFeat_encoded == null)
      jcasType.jcas.throwFeatMissing("encoded", "ontonotes5.to_uima.types.PropLinkPart");
    jcasType.ll_cas.ll_setStringValue(addr, ((PropLinkPart_Type)jcasType).casFeatCode_encoded, v);}    
   
    
  //*--------------*
  //* Feature: node

  /** getter for node - gets 
   * @generated
   * @return value of the feature 
   */
  public ParseNode getNode() {
    if (PropLinkPart_Type.featOkTst && ((PropLinkPart_Type)jcasType).casFeat_node == null)
      jcasType.jcas.throwFeatMissing("node", "ontonotes5.to_uima.types.PropLinkPart");
    return (ParseNode)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PropLinkPart_Type)jcasType).casFeatCode_node)));}
    
  /** setter for node - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setNode(ParseNode v) {
    if (PropLinkPart_Type.featOkTst && ((PropLinkPart_Type)jcasType).casFeat_node == null)
      jcasType.jcas.throwFeatMissing("node", "ontonotes5.to_uima.types.PropLinkPart");
    jcasType.ll_cas.ll_setRefValue(addr, ((PropLinkPart_Type)jcasType).casFeatCode_node, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    