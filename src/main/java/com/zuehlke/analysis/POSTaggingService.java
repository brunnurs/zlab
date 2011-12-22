/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
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
   
   POSTaggerME englishTagger;
   POSTaggerME germanTagger;
   
   @PostConstruct
    protected void init() throws IOException{
       POSModel model =  new POSModel(this.getClass().getClassLoader().getResourceAsStream("nlpmodels/en-pos-maxent.bin"));
       englishTagger = new POSTaggerME(model);
       model =  new POSModel(this.getClass().getClassLoader().getResourceAsStream("nlpmodels/de-pos-maxent.bin"));
       germanTagger = new POSTaggerME(model);
        
    }

   public List<Pair<String, String>> tagEnglishText(String text){
       List<Pair<String, String>> postokens = new LinkedList<Pair<String, String>>();
       String[] sentences = sentenceDetectorService.splitEnglishTextIntoSentences(text);
       for(String sentence : sentences){
           String[] tokens = tokenizerService.tokenizeEnglishText(sentence);
           String[] postags =  tag(englishTagger, tokens);
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
           String[] postags = tag(germanTagger, tokens);
           for(int i = 0; i < tokens.length; i++){
               postokens.add(new ImmutablePair<String, String>(tokens[i], postags[i]));
           }
       }
       return postokens;
   }
   
   private String[] tag(POSTaggerME tagger, String[] sentence){
       String[] postokens = new String[0]; 
       postokens = tagger.tag(sentence);
       return postokens;
   }
   
    public void setSentenceDetectorService(SentenceDetectorService sentenceDetectorService) {
        this.sentenceDetectorService = sentenceDetectorService;
    }

    public void setTokenizerService(TokenizerService tokenizerService) {
        this.tokenizerService = tokenizerService;
    }
    
}
