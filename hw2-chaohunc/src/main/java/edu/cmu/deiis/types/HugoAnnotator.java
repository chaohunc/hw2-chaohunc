package edu.cmu.deiis.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

public class HugoAnnotator extends JCasAnnotator_ImplBase {

  @Override
  /**
   * HugoGeneAnnotator used mainly two methods, exact matching and  partial matching. 
   * For exact matching, it needs to match the sentence with the approved name of gene and 
   * previous approved name of gene. For partial matching, we only match each single word 
   *  in the sentence with a gene dictionary. They will all be sent to the CAS consumer. 
   * @param JCAS
   *          aJCAS which has been processed by the CollectionReader
   * 
   * @throws AnalysisEngineProcessException
   */
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String docText = aJCas.getDocumentText();
    // System.out.println(docText);
    // File modelFile = new File("ne-en-bio-genetag.HmmChunker");
    String[] strFile = new String[20];
    strFile[0] = "ApprovedName";
    strFile[1] = "PreviousName";
    strFile[2] = "ApprovedSymbol";
    strFile[3] = "PreviousSymbol";
    strFile[4] = "NameSingleWordMatch";

    // hmap was used for exact matching
    HashMap<String, Float> hmap = new HashMap<String, Float>();

    // hmapSingleToken was used for partial matching
    HashMap<String, Float> hmapSingleToken = new HashMap<String, Float>();

    for (int i = 0; i < strFile.length; i++) {
      InputStream in2 = null;
      if (strFile[i] != null) {
        try {
          in2 = getContext()
                  .getResourceAsStream(strFile[i]);
        } catch (ResourceAccessException e1) {
          e1.printStackTrace();
        }
        ObjectInputStream objIn2;

        try {
          InputStreamReader is = new InputStreamReader(in2);
          StringBuilder sb = new StringBuilder();
          BufferedReader br = new BufferedReader(is);
          String read = br.readLine();

          while (read != null) {
            sb.append(read);
            if (i < 2) {
              // initialized dictionary for exact matching 
              if (!read.trim().equals("")) {
                String[] len = read.split(" ");
                hmap.put(read.trim(), (float) 0.7 * (float) len.length);
              }
              read = br.readLine();
            }

            // initialized dictionary for partial matching 
            else {
              if (!read.trim().equals("") && read.trim().length() >= 3) {
                String[] len = read.split(" ");
                hmapSingleToken.put(read.trim(), (float) 0.4 * (float) len.length);
              }
              read = br.readLine();

            }
          }
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

    System.out.println(hmap.size());

    String[] str = docText.split("\n");
    int nowPos = 0;
    double hit = 0;
    double miss = 0;

      /*
    Measurement m = null;
    
    try {
      m = new Measurement();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }*/
    for (int i = 0; i < str.length; i++) {
      String id = str[i].substring(0, 14);
      String sentence = str[i].substring(15, str[i].length()).toLowerCase();
      // if exact matching
      for (Entry<String, Float> ma : hmap.entrySet()) {
        if (sentence.contains(ma.getKey().toLowerCase())) {
          /*
          if (m.map.containsKey(id + " " + ma.getKey())) {
            hit++;
            System.out.println("hit " + id + " " + ma.getKey());
          } else {
            miss++;
            System.out.println("miss " + id + " " + ma.getKey());

          }*/
          HugoGeneObj ann = new HugoGeneObj(aJCas);
          ann.setCasProcessorId(id);
          ann.setConfidence(ma.getValue());
          ann.setGeneName(ma.getKey());
          ann.addToIndexes();

        }

      }

      
      
      String[] tokens = sentence.split(" ");

      for (int j = 0; j < tokens.length; j++) {        
        if (tokens[j].trim() != "")
          // if partial matching
          if (hmapSingleToken.containsKey(tokens[j])) {
            /*if (m.map.containsKey(id + " " + tokens[j])) {
              hit++;
              System.out.println("hit " + id + " " + tokens[j]);
            } else {
              miss++;
              System.out.println("miss " + id + " " + tokens[j]);

            }*/
            HugoGeneObj ann = new HugoGeneObj(aJCas);
            ann.setCasProcessorId(id);
            ann.setConfidence(hmapSingleToken.get(tokens[j]));
            ann.setGeneName(tokens[j]);
            ann.addToIndexes();

          }

      }

    }

    // used to output the experiment result
    /*
    for (int j = 0; j < 10; j++) {
      System.out.println("0." + j);
      Double d = hit / (m.map.size());
      Double d2 = hit / (hit + miss);
      Double f1 = 2 * d * d2 / (d + d2);
      System.out.println("hit" + hit);
      System.out.println("miss" + miss);
      System.out.println("F1" + f1);
      System.out.println("Recall" + d);
      System.out.println("Precision" + d2);
      System.out.println(f1);
      System.out.println(d2);
      System.out.println(d);
    }*/
    // System.exit(1);
  }

}
