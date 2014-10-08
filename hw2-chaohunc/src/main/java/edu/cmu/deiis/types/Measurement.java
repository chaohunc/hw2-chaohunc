package edu.cmu.deiis.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Measurement {
    HashMap<String,Integer> map = new HashMap<String, Integer>();
    HashMap<String,ArrayList<String>> mapbackup = new HashMap<String, ArrayList<String>>();
    HashMap<String,Boolean> matchBool = new HashMap<String,Boolean>();
       
    double hit;
    double miss;
    
    /**
     * Initialized the Measurement, which could be used to calculate precision or recall  
     *
     */
    Measurement() throws IOException
    {
      InputStream in = this.getClass().getClassLoader().getResourceAsStream("main/resources/data/sample.out");
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String str;
      this.hit=0;
      this.miss=0;
      while ((str = reader.readLine())!=null)
      {
          String[] token = str.split("\\|");
          String[] cS = token[1].split(" ");
          map.put(new String(token[0]+" "+token[2]), 1);
          matchBool.put(new String(token[0]+" "+token[2]), false);
          ArrayList<String> v = new ArrayList<String>();
          if (mapbackup.containsKey(token[0]))
          {
                v = mapbackup.get(token[0]);
                v.add(token[2]);
                mapbackup.put(new String(token[0]) , v);
          }
          else
          {
            v.add(token[2]);
            mapbackup.put(new String(token[0]) , v);
            
          }
          System.out.println(token[0]+" "+ cS[0] +" "+token[2]);
          
      }
    }
}
