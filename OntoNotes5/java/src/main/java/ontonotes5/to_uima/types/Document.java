

/* First created by JCasGen Fri Mar 14 09:40:47 EST 2014 */
package ontonotes5.to_uima.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.DocumentAnnotation;


/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * XML source: /Users/tim/repos/tim.dawborn/phd/ontonotes5/java/desc/DBTypes.xml
 * @generated */
public class Document extends DocumentAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Document.class);
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
  protected Document() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Document(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Document(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Document(JCas jcas, int begin, int end) {
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
  //* Feature: docId

  /** getter for docId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getDocId() {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_docId == null)
      jcasType.jcas.throwFeatMissing("docId", "ontonotes5.to_uima.types.Document");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Document_Type)jcasType).casFeatCode_docId);}
    
  /** setter for docId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDocId(String v) {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_docId == null)
      jcasType.jcas.throwFeatMissing("docId", "ontonotes5.to_uima.types.Document");
    jcasType.ll_cas.ll_setStringValue(addr, ((Document_Type)jcasType).casFeatCode_docId, v);}    
   
    
  //*--------------*
  //* Feature: subcorpusId

  /** getter for subcorpusId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSubcorpusId() {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_subcorpusId == null)
      jcasType.jcas.throwFeatMissing("subcorpusId", "ontonotes5.to_uima.types.Document");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Document_Type)jcasType).casFeatCode_subcorpusId);}
    
  /** setter for subcorpusId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSubcorpusId(String v) {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_subcorpusId == null)
      jcasType.jcas.throwFeatMissing("subcorpusId", "ontonotes5.to_uima.types.Document");
    jcasType.ll_cas.ll_setStringValue(addr, ((Document_Type)jcasType).casFeatCode_subcorpusId, v);}    
   
    
  //*--------------*
  //* Feature: genre

  /** getter for genre - gets 
   * @generated
   * @return value of the feature 
   */
  public String getGenre() {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_genre == null)
      jcasType.jcas.throwFeatMissing("genre", "ontonotes5.to_uima.types.Document");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Document_Type)jcasType).casFeatCode_genre);}
    
  /** setter for genre - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGenre(String v) {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_genre == null)
      jcasType.jcas.throwFeatMissing("genre", "ontonotes5.to_uima.types.Document");
    jcasType.ll_cas.ll_setStringValue(addr, ((Document_Type)jcasType).casFeatCode_genre, v);}    
   
    
  //*--------------*
  //* Feature: source

  /** getter for source - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSource() {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "ontonotes5.to_uima.types.Document");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Document_Type)jcasType).casFeatCode_source);}
    
  /** setter for source - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSource(String v) {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "ontonotes5.to_uima.types.Document");
    jcasType.ll_cas.ll_setStringValue(addr, ((Document_Type)jcasType).casFeatCode_source, v);}    
  }

    