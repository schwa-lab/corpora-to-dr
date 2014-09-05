
/* First created by JCasGen Fri Mar 14 09:40:47 EST 2014 */
package ontonotes5.to_uima.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.DocumentAnnotation_Type;

/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * @generated */
public class Document_Type extends DocumentAnnotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Document_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Document_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Document(addr, Document_Type.this);
  			   Document_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Document(addr, Document_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Document.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ontonotes5.to_uima.types.Document");
 
  /** @generated */
  final Feature casFeat_docId;
  /** @generated */
  final int     casFeatCode_docId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getDocId(int addr) {
        if (featOkTst && casFeat_docId == null)
      jcas.throwFeatMissing("docId", "ontonotes5.to_uima.types.Document");
    return ll_cas.ll_getStringValue(addr, casFeatCode_docId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setDocId(int addr, String v) {
        if (featOkTst && casFeat_docId == null)
      jcas.throwFeatMissing("docId", "ontonotes5.to_uima.types.Document");
    ll_cas.ll_setStringValue(addr, casFeatCode_docId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_subcorpusId;
  /** @generated */
  final int     casFeatCode_subcorpusId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getSubcorpusId(int addr) {
        if (featOkTst && casFeat_subcorpusId == null)
      jcas.throwFeatMissing("subcorpusId", "ontonotes5.to_uima.types.Document");
    return ll_cas.ll_getStringValue(addr, casFeatCode_subcorpusId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSubcorpusId(int addr, String v) {
        if (featOkTst && casFeat_subcorpusId == null)
      jcas.throwFeatMissing("subcorpusId", "ontonotes5.to_uima.types.Document");
    ll_cas.ll_setStringValue(addr, casFeatCode_subcorpusId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_genre;
  /** @generated */
  final int     casFeatCode_genre;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getGenre(int addr) {
        if (featOkTst && casFeat_genre == null)
      jcas.throwFeatMissing("genre", "ontonotes5.to_uima.types.Document");
    return ll_cas.ll_getStringValue(addr, casFeatCode_genre);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setGenre(int addr, String v) {
        if (featOkTst && casFeat_genre == null)
      jcas.throwFeatMissing("genre", "ontonotes5.to_uima.types.Document");
    ll_cas.ll_setStringValue(addr, casFeatCode_genre, v);}
    
  
 
  /** @generated */
  final Feature casFeat_source;
  /** @generated */
  final int     casFeatCode_source;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getSource(int addr) {
        if (featOkTst && casFeat_source == null)
      jcas.throwFeatMissing("source", "ontonotes5.to_uima.types.Document");
    return ll_cas.ll_getStringValue(addr, casFeatCode_source);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSource(int addr, String v) {
        if (featOkTst && casFeat_source == null)
      jcas.throwFeatMissing("source", "ontonotes5.to_uima.types.Document");
    ll_cas.ll_setStringValue(addr, casFeatCode_source, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Document_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_docId = jcas.getRequiredFeatureDE(casType, "docId", "uima.cas.String", featOkTst);
    casFeatCode_docId  = (null == casFeat_docId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_docId).getCode();

 
    casFeat_subcorpusId = jcas.getRequiredFeatureDE(casType, "subcorpusId", "uima.cas.String", featOkTst);
    casFeatCode_subcorpusId  = (null == casFeat_subcorpusId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_subcorpusId).getCode();

 
    casFeat_genre = jcas.getRequiredFeatureDE(casType, "genre", "uima.cas.String", featOkTst);
    casFeatCode_genre  = (null == casFeat_genre) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_genre).getCode();

 
    casFeat_source = jcas.getRequiredFeatureDE(casType, "source", "uima.cas.String", featOkTst);
    casFeatCode_source  = (null == casFeat_source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_source).getCode();

  }
}



    