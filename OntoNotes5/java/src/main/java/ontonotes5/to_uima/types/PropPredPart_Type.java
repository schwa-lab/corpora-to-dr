
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
import org.apache.uima.jcas.cas.AnnotationBase_Type;

/** 
 * Updated by JCasGen Fri Mar 14 18:08:43 EST 2014
 * @generated */
public class PropPredPart_Type extends AnnotationBase_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (PropPredPart_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = PropPredPart_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new PropPredPart(addr, PropPredPart_Type.this);
  			   PropPredPart_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new PropPredPart(addr, PropPredPart_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = PropPredPart.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ontonotes5.to_uima.types.PropPredPart");
 
  /** @generated */
  final Feature casFeat_encoded;
  /** @generated */
  final int     casFeatCode_encoded;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getEncoded(int addr) {
        if (featOkTst && casFeat_encoded == null)
      jcas.throwFeatMissing("encoded", "ontonotes5.to_uima.types.PropPredPart");
    return ll_cas.ll_getStringValue(addr, casFeatCode_encoded);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEncoded(int addr, String v) {
        if (featOkTst && casFeat_encoded == null)
      jcas.throwFeatMissing("encoded", "ontonotes5.to_uima.types.PropPredPart");
    ll_cas.ll_setStringValue(addr, casFeatCode_encoded, v);}
    
  
 
  /** @generated */
  final Feature casFeat_node;
  /** @generated */
  final int     casFeatCode_node;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getNode(int addr) {
        if (featOkTst && casFeat_node == null)
      jcas.throwFeatMissing("node", "ontonotes5.to_uima.types.PropPredPart");
    return ll_cas.ll_getRefValue(addr, casFeatCode_node);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setNode(int addr, int v) {
        if (featOkTst && casFeat_node == null)
      jcas.throwFeatMissing("node", "ontonotes5.to_uima.types.PropPredPart");
    ll_cas.ll_setRefValue(addr, casFeatCode_node, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public PropPredPart_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_encoded = jcas.getRequiredFeatureDE(casType, "encoded", "uima.cas.String", featOkTst);
    casFeatCode_encoded  = (null == casFeat_encoded) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_encoded).getCode();

 
    casFeat_node = jcas.getRequiredFeatureDE(casType, "node", "ontonotes5.to_uima.types.ParseNode", featOkTst);
    casFeatCode_node  = (null == casFeat_node) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_node).getCode();

  }
}



    