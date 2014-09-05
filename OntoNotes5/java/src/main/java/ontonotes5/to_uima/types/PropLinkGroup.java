

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
public class PropLinkGroup extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PropLinkGroup.class);
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
  protected PropLinkGroup() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public PropLinkGroup(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public PropLinkGroup(JCas jcas) {
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
    if (PropLinkGroup_Type.featOkTst && ((PropLinkGroup_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.PropLinkGroup");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_kind);}
    
  /** setter for kind - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKind(String v) {
    if (PropLinkGroup_Type.featOkTst && ((PropLinkGroup_Type)jcasType).casFeat_kind == null)
      jcasType.jcas.throwFeatMissing("kind", "ontonotes5.to_uima.types.PropLinkGroup");
    jcasType.ll_cas.ll_setStringValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_kind, v);}    
   
    
  //*--------------*
  //* Feature: associatedArgumentId

  /** getter for associatedArgumentId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAssociatedArgumentId() {
    if (PropLinkGroup_Type.featOkTst && ((PropLinkGroup_Type)jcasType).casFeat_associatedArgumentId == null)
      jcasType.jcas.throwFeatMissing("associatedArgumentId", "ontonotes5.to_uima.types.PropLinkGroup");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_associatedArgumentId);}
    
  /** setter for associatedArgumentId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAssociatedArgumentId(String v) {
    if (PropLinkGroup_Type.featOkTst && ((PropLinkGroup_Type)jcasType).casFeat_associatedArgumentId == null)
      jcasType.jcas.throwFeatMissing("associatedArgumentId", "ontonotes5.to_uima.types.PropLinkGroup");
    jcasType.ll_cas.ll_setStringValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_associatedArgumentId, v);}    
   
    
  //*--------------*
  //* Feature: links

  /** getter for links - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getLinks() {
    if (PropLinkGroup_Type.featOkTst && ((PropLinkGroup_Type)jcasType).casFeat_links == null)
      jcasType.jcas.throwFeatMissing("links", "ontonotes5.to_uima.types.PropLinkGroup");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_links)));}
    
  /** setter for links - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLinks(FSArray v) {
    if (PropLinkGroup_Type.featOkTst && ((PropLinkGroup_Type)jcasType).casFeat_links == null)
      jcasType.jcas.throwFeatMissing("links", "ontonotes5.to_uima.types.PropLinkGroup");
    jcasType.ll_cas.ll_setRefValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_links, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for links - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public PropLink getLinks(int i) {
    if (PropLinkGroup_Type.featOkTst && ((PropLinkGroup_Type)jcasType).casFeat_links == null)
      jcasType.jcas.throwFeatMissing("links", "ontonotes5.to_uima.types.PropLinkGroup");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_links), i);
    return (PropLink)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_links), i)));}

  /** indexed setter for links - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setLinks(int i, PropLink v) { 
    if (PropLinkGroup_Type.featOkTst && ((PropLinkGroup_Type)jcasType).casFeat_links == null)
      jcasType.jcas.throwFeatMissing("links", "ontonotes5.to_uima.types.PropLinkGroup");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_links), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((PropLinkGroup_Type)jcasType).casFeatCode_links), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    