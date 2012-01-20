//
// $Id: LexiconImpl.java,v 1.2 2006/02/16 18:15:02 perera Exp $
//
// Durm German Lemmatization System
// http://www.ipd.uka.de/~durm/tm/lemma/
//
//
// Implementation of the lexicon update algorithms. Implements
// the interface Lexicon.
//
// (c) 2005--2006 University of Karlsruhe, IPD,
//                Praharshana Perera
//
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 

package com.zuehlke.analysis;

import java.io.*;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class LexiconImpl implements Lexicon
{
  
  //HashMap dicEntries  = new HashMap();   
  //HashMap stemEntries = new HashMap();
  //HashMap stems       = new HashMap(); 
  
  private HashMap lexiconEntries = new HashMap();
  private HashMap entriesLemma   = new HashMap();
  private HashMap lemmaEntries   = new HashMap();
  
  public LexiconImpl(String filepath) throws java.io.IOException
  {
    
   LexiconUtil.setLexPath(filepath);
   LexiconUtil.loadEntries(lexiconEntries,entriesLemma,lemmaEntries);
   
  }
  
  
  public void addWord(String word, String stemm, String gender, String number, String gcase)
  {
       if(lexiconEntries.containsKey(word))
    {
       makeUpdate(word,getFeatures(word),stemm,gender,number,gcase);
    }
    else
    {
       addNewWord(word,stemm,gender,number,gcase);
    }
       
  }
  
   
   public String getLemma(String word)
   {
      if(getFeatures(word)!=null)
       return getFeatures(word).lemma;
      else
       return null;
   }
   
   public String getNumber(String word)
   {
      return getFeatures(word).number;
   }
   
   public String getGender(String word)
   {
     return getFeatures(word).gender;
   }
   
   public String getCase(String word)
   {
      return getFeatures(word).gcase;
   }
   
   
  

 private void addNewWord(String word, String stemm, String gender, String number, String gcase)
 { 
              
           LexiconEntry lentry = new LexiconEntry();
    lentry.noun         = word;
    lentry.number       = number;
    lentry.gender       = gender;
    lentry.gcase        = gcase;
    lentry.freq         = "1";
    lentry.insertedOn   = makeTime();
    lentry.lastModified = makeTime();
    lentry.pointer      = "-1";
    lentry.locked       = "unlocked";
    //Make the stem update
           stemm               = updateStem(stemm);
    lentry.lemma       = stemm;
    
    //Make the other stem update
    updateWord(stemm);
    
    //Load the stem Hash Map
    loadStems(stemm,word);
    
    entriesLemma.put(word,stemm);    
    lexiconEntries.put(word,lentry);
    
    
    
    
 }

  private String makeCase(String oldcase, String newcase)
  {
      String entryCase="";
      if(oldcase.indexOf("Nom") >= 0 || newcase.indexOf("Nom") >= 0)
         entryCase +="Nom.";
      if(oldcase.indexOf("Akk") >= 0 || newcase.indexOf("Akk") >= 0)
         entryCase +="Akk.";
      if(oldcase.indexOf("Dat") >= 0 || newcase.indexOf("Dat") >= 0)
         entryCase +="Dat.";
      if(oldcase.indexOf("Gen") >= 0 || newcase.indexOf("Gen") >= 0)
        entryCase +="Gen.";
  
     return entryCase.substring(0,entryCase.length()-1);
}

private String makeNumber(String oldnumber, String newnumber)
{
  String entryCase="";
  if(oldnumber.indexOf("Sg") >= 0 || newnumber.indexOf("Sg") >= 0)
     entryCase +="Sg.";
  if(oldnumber.indexOf("Pl") >= 0 || newnumber.indexOf("Pl") >= 0)
     entryCase +="Pl.";
  
  
     return entryCase.substring(0,entryCase.length()-1);
}

private String makeStem(String oldstem, String newstem)
{
   
   StringTokenizer st1 = new StringTokenizer(oldstem, ".");
   StringTokenizer st2 = new StringTokenizer(newstem,".");
   ArrayList  oldlist  = new ArrayList();
   ArrayList  newlist  = new ArrayList();
   ArrayList  retList  = new ArrayList();
   String retstring    ="";
   
   while(st1.hasMoreTokens())
     oldlist.add(st1.nextToken());
   while(st2.hasMoreTokens())
     newlist.add(st2.nextToken());
  
  for(int i=0;i<newlist.size();i++)
    {
   String str = newlist.get(i).toString();
   if(!oldlist.contains(str))
   {
     oldlist.add(str);
   }
   else
   {
     retList.add(str);
   }
    
    }
    
   if(retList.size() == 0)
   { 
      for(int i=0;i<oldlist.size();i++)
      {
         retstring +=oldlist.get(i).toString()+".";
      }
      return retstring.substring(0,retstring.length()-1).trim();
    }
    else
    {
      for(int i=0;i<retList.size();i++)
      {
         retstring +=retList.get(i).toString()+".";
      }
      return retstring.substring(0,retstring.length()-1).trim();
    }

}



private LexiconEntry getFeatures(String word)
{
    return (LexiconEntry)lexiconEntries.get(word);
 
}

private void makeUpdate(String word, LexiconEntry lentry, String stemm, String gender, String number, String gcase)
{  
        
        if(lentry.locked.equals("unlocked"))   
        {
 
         if(!lentry.number.trim().equals(number))
                {
           lentry.number  = makeNumber(lentry.number,number);
                }
   
         if(!lentry.gender.trim().equals(gender))
         {
           int distance = returnDistanace(lentry.gender,gender);
           if(distance == 2)
             lentry.gender = gender;
           else if( distance == -1 )
             lentry.gender = lentry.gender+"."+gender;
         }
 
        if(!lentry.gcase.trim().equals(gcase))
        {
           lentry.gcase = makeCase(lentry.gcase,gcase);
        }
  
  lentry.freq = makeNumber(lentry.freq);
 
         lentry.lastModified = makeTime();
  
 
         if(!lentry.lemma.trim().equals(stemm))
         {
       
           lentry.lemma = makeStem(lentry.lemma,stemm);
         }
 
 
        //Update the stem entries
        entriesLemma.put(word,lentry.lemma);
 
       //Make the stem update
       lentry.lemma =updateStem(lentry.lemma);
 
       //make the other stem update
       updateWord(lentry.lemma);
 
 
       //Load the stem hash map
       loadStems(lentry.lemma,word);
 
       // Make the update
       lexiconEntries.put(word, lentry);
       entriesLemma.put(word,lentry.lemma);
 
    } 
}

private int returnDistanace(String feature, String newword)
{
    if(feature.length() > newword.length())
    {
       return 2;
    }
    else if(feature.length() < newword.length())
    {
       return -2;
    }
    else
    {
      if(checkSub(feature,newword))
    return 1;
   else
    return -1;
    }
}

private boolean checkSub(String word1,String word2)
{
   StringTokenizer st1 = new StringTokenizer(word2, ".");
   boolean flag = false;
   
   while(st1.hasMoreTokens())
   {
      if( word1.indexOf(st1.nextToken()) >= 0 )
   {
    flag = true;
   }
   else
   {
    flag = false;
    break;
   }
   }

   
   return flag;
   
   
}

public void updateLexicon() throws java.io.IOException
{
    
    LexiconUtil.writeEntries(lexiconEntries);
}

private void updateWord(String word, String stem)
{
     LexiconEntry lentry      = getFeatures(word);
     lentry.lemma             = stem;
     lexiconEntries.put(word, lentry);
     entriesLemma.put(word, stem);
}

private void updateWord(String stem)
{
   if(lemmaEntries.containsKey(stem))
   {
     String words = lemmaEntries.get(stem).toString();
     StringTokenizer st = new StringTokenizer(words, ".");
     while(st.hasMoreTokens())
     {
       String word = st.nextToken().trim();
       updateWord(word, stem);
     }
     
   }
}

private String updateStem(String stem)
{
    StringTokenizer st = new StringTokenizer(stem, ".");
 String current="";
    while(st.hasMoreTokens())
    {
      String retstem = st.nextToken().trim();
      if(entriesLemma.containsValue(retstem))
   {
        current += retstem+".";
        stem     = current.substring(0,current.length()-1).trim();  
   }
    }
    return stem;  
}


private void loadStems(String stem, String word)
  {
     
     StringTokenizer st = new StringTokenizer(stem, ".");
     if(st.countTokens()>1)
     {
         while(st.hasMoreTokens())
        {
           String tempstem = st.nextToken().trim();
           if(lemmaEntries.containsKey(tempstem))
           {
       String current = lemmaEntries.get(tempstem).toString();
       current +="."+word;
       lemmaEntries.put(tempstem,current);
           }
           else
              lemmaEntries.put(tempstem,word);
      
        }
     }
  }


private String makeNumber(String num)
  {
     int val = Integer.parseInt(num);
     val++;
     return (new Integer(val)).toString();
  }
  
private String makeTime()
{
   GregorianCalendar gc = new GregorianCalendar();
   int date = gc.get(Calendar.DATE);
   int month = gc.get(Calendar.MONTH)+1;
   int year  = gc.get(Calendar.YEAR);
   
   int hour   = gc.get(Calendar.HOUR_OF_DAY);
   int minute = gc.get(Calendar.MINUTE);
   int second = gc.get(Calendar.SECOND);
   
   String time = date+"/"+month+"/"+year+" "+hour+":"+minute+":"+second;
   
   return time;
}  

}