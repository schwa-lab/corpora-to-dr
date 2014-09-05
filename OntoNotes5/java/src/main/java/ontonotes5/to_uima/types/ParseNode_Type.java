
/* First created by JCasGen Fri Mar 14 09:40:31 EST 2014 */
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
public class ParseNode_Type extends AnnotationBase_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (ParseNode_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = ParseNode_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new ParseNode(addr, ParseNode_Type.this);
  			   ParseNode_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new ParseNode(addr, ParseNode_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = ParseNode.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ontonotes5.to_uima.types.ParseNode");
 
  /** @generated */
  final Feature casFeat_tag;
  /** @generated */
  final int     casFeatCode_tag;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTag(int addr) {
        if (featOkTst && casFeat_tag == null)
      jcas.throwFeatMissing("tag", "ontonotes5.to_uima.types.ParseNode");
    return ll_cas.ll_getStringValue(addr, casFeatCode_tag);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTag(int addr, String v) {
        if (featOkTst && casFeat_tag == null)
      jcas.throwFeatMissing("tag", "ontonotes5.to_uima.types.ParseNode");
    ll_cas.ll_setStringValue(addr, casFeatCode_tag, v);}
    
  
 
  /** @generated */
  final Feature casFeat_pos;
  /** @generated */
  final int     casFeatCode_pos;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPos(int addr) {
        if (featOkTst && casFeat_pos == null)
      jcas.throwFeatMissing("pos", "ontonotes5.to_uima.types.ParseNode");
    return ll_cas.ll_getStringValue(addr, casFeatCode_pos);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPos(int addr, String v) {
        if (featOkTst && casFeat_pos == null)
      jcas.throwFeatMissing("pos", "ontonotes5.to_uima.types.ParseNode");
    ll_cas.ll_setStringValue(addr, casFeatCode_pos, v);}
    
  
 
  /** @generated */
  final Feature casFeat_token;
  /** @generated */
  final int     casFeatCode_token;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getToken(int addr) {
        if (featOkTst && casFeat_token == null)
      jcas.throwFeatMissing("token", "ontonotes5.to_uima.types.ParseNode");
    return ll_cas.ll_getRefValue(addr, casFeatCode_token);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setToken(int addr, int v) {
        if (featOkTst && casFeat_token == null)
      jcas.throwFeatMissing("token", "ontonotes5.to_uima.types.ParseNode");
    ll_cas.ll_setRefValue(addr, casFeatCode_token, v);}
    
  
 
  /** @generated */
  final Feature casFeat_phraseType;
  /** @generated */
  final int     casFeatCode_phraseType;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPhraseType(int addr) {
        if (featOkTst && casFeat_phraseType == null)
      jcas.throwFeatMissing("phraseType", "ontonotes5.to_uima.types.ParseNode");
    return ll_cas.ll_getStringValue(addr, casFeatCode_phraseType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPhraseType(int addr, String v) {
        if (featOkTst && casFeat_phraseType == null)
      jcas.throwFeatMissing("phraseType", "ontonotes5.to_uima.types.ParseNode");
    ll_cas.ll_setStringValue(addr, casFeatCode_phraseType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_functionTags;
  /** @generated */
  final int     casFeatCode_functionTags;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getFunctionTags(int addr) {
        if (featOkTst && casFeat_functionTags == null)
      jcas.throwFeatMissing("functionTags", "ontonotes5.to_uima.types.ParseNode");
    return ll_cas.ll_getStringValue(addr, casFeatCode_functionTags);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setFunctionTags(int addr, String v) {
        if (featOkTst && casFeat_functionTags == null)
      jcas.throwFeatMissing("functionTags", "ontonotes5.to_uima.types.ParseNode");
    ll_cas.ll_setStringValue(addr, casFeatCode_functionTags, v);}
    
  
 
  /** @generated */
  final Feature casFeat_corefSection;
  /** @generated */
  final int     casFeatCode_corefSection;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getCorefSection(int addr) {
        if (featOkTst && casFeat_corefSection == null)
      jcas.throwFeatMissing("corefSection", "ontonotes5.to_uima.types.ParseNode");
    return ll_cas.ll_getIntValue(addr, casFeatCode_corefSection);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCorefSection(int addr, int v) {
        if (featOkTst && casFeat_corefSection == null)
      jcas.throwFeatMissing("corefSection", "ontonotes5.to_uima.types.ParseNode");
    ll_cas.ll_setIntValue(addr, casFeatCode_corefSection, v);}
    
  
 
  /** @generated */
  final Feature casFeat_syntacticLink;
  /** @generated */
  final int     casFeatCode_syntacticLink;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getSyntacticLink(int addr) {
        if (featOkTst && casFeat_syntacticLink == null)
      jcas.throwFeatMissing("syntacticLink", "ontonotes5.to_uima.types.ParseNode");
    return ll_cas.ll_getRefValue(addr, casFeatCode_syntacticLink);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSyntacticLink(int addr, int v) {
        if (featOkTst && casFeat_syntacticLink == null)
      jcas.throwFeatMissing("syntacticLink", "ontonotes5.to_uima.types.ParseNode");
    ll_cas.ll_setRefValue(addr, casFeatCode_syntacticLink, v);}
    
  
 
  /** @generated */
  final Feature casFeat_children;
  /** @generated */
  final int     casFeatCode_children;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getChildren(int addr) {
        if (featOkTst && casFeat_children == null)
      jcas.throwFeatMissing("children", "ontonotes5.to_uima.types.ParseNode");
    return ll_cas.ll_getRefValue(addr, casFeatCode_children);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setChildren(int addr, int v) {
        if (featOkTst && casFeat_children == null)
      jcas.throwFeatMissing("children", "ontonotes5.to_uima.types.ParseNode");
    ll_cas.ll_setRefValue(addr, casFeatCode_children, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getChildren(int addr, int i) {
        if (featOkTst && casFeat_children == null)
      jcas.throwFeatMissing("children", "ontonotes5.to_uima.types.ParseNode");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_children), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_children), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_children), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setChildren(int addr, int i, int v) {
        if (featOkTst && casFeat_children == null)
      jcas.throwFeatMissing("children", "ontonotes5.to_uima.types.ParseNode");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_children), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_children), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_children), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public ParseNode_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_tag = jcas.getRequiredFeatureDE(casType, "tag", "uima.cas.String", featOkTst);
    casFeatCode_tag  = (null == casFeat_tag) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tag).getCode();

 
    casFeat_pos = jcas.getRequiredFeatureDE(casType, "pos", "uima.cas.String", featOkTst);
    casFeatCode_pos  = (null == casFeat_pos) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_pos).getCode();

 
    casFeat_token = jcas.getRequiredFeatureDE(casType, "token", "ontonotes5.to_uima.types.Token", featOkTst);
    casFeatCode_token  = (null == casFeat_token) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_token).getCode();

 
    casFeat_phraseType = jcas.getRequiredFeatureDE(casType, "phraseType", "uima.cas.String", featOkTst);
    casFeatCode_phraseType  = (null == casFeat_phraseType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_phraseType).getCode();

 
    casFeat_functionTags = jcas.getRequiredFeatureDE(casType, "functionTags", "uima.cas.String", featOkTst);
    casFeatCode_functionTags  = (null == casFeat_functionTags) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_functionTags).getCode();

 
    casFeat_corefSection = jcas.getRequiredFeatureDE(casType, "corefSection", "uima.cas.Integer", featOkTst);
    casFeatCode_corefSection  = (null == casFeat_corefSection) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_corefSection).getCode();

 
    casFeat_syntacticLink = jcas.getRequiredFeatureDE(casType, "syntacticLink", "ontonotes5.to_uima.types.ParseNode", featOkTst);
    casFeatCode_syntacticLink  = (null == casFeat_syntacticLink) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_syntacticLink).getCode();

 
    casFeat_children = jcas.getRequiredFeatureDE(casType, "children", "uima.cas.FSArray", featOkTst);
    casFeatCode_children  = (null == casFeat_children) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_children).getCode();

  }
}



    