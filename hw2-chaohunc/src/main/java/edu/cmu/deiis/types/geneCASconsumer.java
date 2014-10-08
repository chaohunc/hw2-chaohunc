package edu.cmu.deiis.types;

/* 
 *******************************************************************************************
 * N O T E :     The XML format (XCAS) that this Cas Consumer outputs, is eventually 
 *               being superceeded by the more standardized and compact XMI format.  However
 *               it is used currently as the expected form for remote services, and there is
 *               existing tooling for doing stand-alone component development and debugging 
 *               that uses this format to populate an initial CAS.  So it is not 
 *               deprecated yet;  it is also being kept for compatibility with older versions.
 *               
 *               New code should consider using the XmiWriterCasConsumer where possible,
 *               which uses the current XMI format for XML externalizations of the CAS
 *******************************************************************************************               
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.XMLSerializer;
import org.xml.sax.SAXException;

public class geneCASconsumer extends CasConsumer_ImplBase {
  /**
   * Name of configuration parameter that must be set to the path of a directory into which the
   * output files will be written.
   */
  
  static final double threshold = 0.7;

  double[] hit = new double[10];
  double[] miss = new double [10];
  public void initialize() throws ResourceInitializationException {
    for (int i=0;i<10;i++)
    {
      hit[i]=0;
      miss[i]=0;
    }
  }

  /**
   * Processes the CasContainer which was populated by the AnalysisEngines. <br>
   * In this case, the CAS is converted to txt file.
   * 
   * @param aCAS
   *          CasContainer which has been populated by the TAEs
   * 
   * @throws ResourceProcessException
   *           if there is an error in processing the Resource
   * 
   */
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    HashMap<String, HashMap<String, Double>> hmap = new HashMap<String, HashMap<String, Double>>();
    FSIterator<Annotation> iter = jcas.getAnnotationIndex(GeneObj.type).iterator();
    FSIterator<Annotation> iter2 = jcas.getAnnotationIndex(HugoGeneObj.type).iterator();
    while (iter2.hasNext()) {
      HugoGeneObj gene = (HugoGeneObj) iter2.next();
      if (hmap.containsKey(gene.getCasProcessorId())) {
        hmap.get(gene.getCasProcessorId()).put(gene.getGeneName(), gene.getConfidence());
      } else {
        HashMap<String, Double> temp = new HashMap<String, Double>();
        temp.put(gene.getGeneName(), gene.getConfidence());
        hmap.put(gene.getCasProcessorId(), temp);
      }

    }

    // File outFile = null;
    BufferedWriter oFile2 = null;
    try {
      oFile2 = new BufferedWriter(new FileWriter(new File(
              (String) getConfigParameterValue("OutputFile"))));
    } catch (IOException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }
    /*
    Measurement m = null;
    try {
      m = new Measurement();
    } catch (IOException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }*/
    while (iter.hasNext()) {
      GeneObj gene = (GeneObj) iter.next();

      double score = gene.getConfidence();

      if (hmap.containsKey(gene.getID())) {
        HashMap<String, Double> temp = hmap.get(gene.getID());
        String geneName = gene.getGeneName();
        for (Entry<String, Double> c : temp.entrySet()) {
          if (geneName.contains(c.getKey())) {
            System.out.print(geneName + " " + score);
            score += c.getValue();
            System.out.print(" " + score);
            System.out.println(" ");

          }
          
          else if (gene.getPosStart()==0)
          {
              if (geneName.toLowerCase().contains(c.getKey().toLowerCase()))
              {
                score += c.getValue();
                
              }
          }
        }
      }
      /*
      if (m.map.containsKey(gene.getID() + " " + gene.getGeneName()))
      {
        if (score>=1)
            score=1;
        for (int k=0;k<(int)(score*10);k++)
          hit[k]++;
      }
      else
      {
        if (score>=1)
          score=1;
      for (int k=0;k<(int)(score*10);k++)
          miss[k]++;
        
      }
      */
      if (score < threshold || gene.getGeneName().length() < 2)
        continue;
      /*
      if (m.map.containsKey(gene.getID() + " " + gene.getGeneName()))
      {
        m.matchBool.put(gene.getID() + " " + gene.getGeneName(),true);
        m.hit++;
      }
      else {

        System.out.println("Miss " + gene.getID() + " " + gene.getGeneName() + " "
                + gene.getConfidence());

        if (m.mapbackup.containsKey(gene.getID())) {
          ArrayList<String> c = m.mapbackup.get(gene.getID());
          for (String d : c) {
            System.out.println(d);
          }
        }
        m.miss++;

      }
       */
      String output = gene.getID() + "|" + gene.getPosStart() + " " + gene.getPosEnd() + "|"
              + gene.getGeneName();

      try {
        oFile2.write(output);
        oFile2.newLine();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    /*
    for (Entry<String, Boolean> c: m.matchBool.entrySet())
    {
      if (c.getValue()==false)  
        System.out.println("mos" + c.getKey());
    }
    
    
    Double d = m.hit / (m.map.size());
    Double d2 = m.hit / (m.hit + m.miss);
    Double f1 = 2 * d * d2 / (d + d2);
    System.out.println("hit" + m.hit);
    System.out.println("miss" + m.miss);
    System.out.println("total" + m.map.size());

    System.out.println("F1" + f1);
    System.out.println("Recall" + d);
    System.out.println("Precision" + d2);

    for (int k=0;k<10;k++)
    {
      System.out.println("now Threshold " + k);
     d = hit[k] / (m.map.size());
     d2 = hit[k] / (hit[k] + miss[k]);
     f1 = 2 * d * d2 / (d + d2);
    System.out.println("hit" + hit[k]);
    System.out.println("miss" + miss[k]);
    System.out.println("total" + m.map.size());

    System.out.println(f1);
    System.out.println(d2);
    System.out.println(d);
    }
    */
    try {
      oFile2.close();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }

}
