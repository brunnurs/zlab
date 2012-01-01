/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author mhaspra
 */
@Stateless
@LocalBean
public class POSTaggingService {
    
   @Inject
   TokenizerService tokenizerService;
   
   @Inject
   SentenceDetectorService sentenceDetectorService;

   public List<Pair<String, String>> tagEnglishText(String text){
       List<Pair<String, String>> postokens = new LinkedList<Pair<String, String>>();
       String[] sentences = sentenceDetectorService.splitEnglishTextIntoSentences(text);
       for(String sentence : sentences){
           String[] tokens = tokenizerService.tokenizeEnglishText(sentence);
           String[] postags =  tag(ClassLoader.getSystemResourceAsStream("nlpmodels/en-pos-maxent.bin"), tokens);
           for(int i = 0; i < tokens.length; i++){
               postokens.add(new ImmutablePair<String, String>(tokens[i], postags[i]));
           }
       }
       return postokens;
   }
   
   public List<Pair<String, String>> tagGermanText(String text){
       List<Pair<String, String>> postokens = new LinkedList<Pair<String, String>>();
       String[] sentences = sentenceDetectorService.splitGermantextIntoSentences(text);
       for(String sentence : sentences){
           String[] tokens = tokenizerService.tokenizeGermanText(sentence);
           String[] postags = tag(this.getClass().getClassLoader().getResourceAsStream("nlpmodels/de-pos-maxent.bin"), tokens);
           for(int i = 0; i < tokens.length; i++){
               postokens.add(new ImmutablePair<String, String>(tokens[i], postags[i]));
           }
       }
       return postokens;
   }
   
   private String[] tag(InputStream modelinputStream, String[] sentence){
       String[] postokens = new String[0]; 
       try {
            POSModel model =  new POSModel(modelinputStream);
            POSTaggerME tagger = new POSTaggerME(model);
            postokens = tagger.tag(sentence);
        } catch (IOException ex) {
            Logger.getLogger(POSTaggingService.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
           return postokens;
       }
   }
   
    public void setSentenceDetectorService(SentenceDetectorService sentenceDetectorService) {
        this.sentenceDetectorService = sentenceDetectorService;
    }

    public void setTokenizerService(TokenizerService tokenizerService) {
        this.tokenizerService = tokenizerService;
    }
    
}
