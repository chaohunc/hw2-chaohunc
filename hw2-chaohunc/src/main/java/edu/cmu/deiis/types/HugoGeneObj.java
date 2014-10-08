

/* First created by JCasGen Sat Oct 04 23:02:59 EDT 2014 */
package edu.cmu.deiis.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Sun Oct 05 10:26:08 EDT 2014
 * XML source: /Users/chaohunc/Documents/workspace/hw2-chaohunc/src/main/resources/HugoAEDescriptor.xml
 * @generated */
public class HugoGeneObj extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(HugoGeneObj.class);
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
  protected HugoGeneObj() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public HugoGeneObj(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public HugoGeneObj(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public HugoGeneObj(JCas jcas, int begin, int end) {
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
  //* Feature: geneName

  /** getter for geneName - gets 
   * @generated
   * @return value of the feature 
   */
  public String getGeneName() {
    if (HugoGeneObj_Type.featOkTst && ((HugoGeneObj_Type)jcasType).casFeat_geneName == null)
      jcasType.jcas.throwFeatMissing("geneName", "edu.cmu.deiis.types.HugoGeneObj");
    return jcasType.ll_cas.ll_getStringValue(addr, ((HugoGeneObj_Type)jcasType).casFeatCode_geneName);}
    
  /** setter for geneName - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGeneName(String v) {
    if (HugoGeneObj_Type.featOkTst && ((HugoGeneObj_Type)jcasType).casFeat_geneName == null)
      jcasType.jcas.throwFeatMissing("geneName", "edu.cmu.deiis.types.HugoGeneObj");
    jcasType.ll_cas.ll_setStringValue(addr, ((HugoGeneObj_Type)jcasType).casFeatCode_geneName, v);}    
  }

    