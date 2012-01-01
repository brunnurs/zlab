/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class NLPService {
    
    @EJB
    private POSTaggingService posTaggingService;
    
    @EJB
    private LanguageDetectionService languageDetectionService;
    
    public Map<String, MutableInt> extractNouns(String text) {
        List<Pair<String, String>> taggedWords = Collections.EMPTY_LIST;
        
        Language language = languageDetectionService.getLanguage(text);
        
        if(language == Language.GERMAN){
            taggedWords = posTaggingService.tagGermanText(text);
        }
        if(language == Language.ENGLISH){
            taggedWords = posTaggingService.tagEnglishText(text);
        }
        
        Map<String, MutableInt> nouns = new HashMap<String, MutableInt>();
        for(Pair<String, String> taggedWord : taggedWords){
            if(taggedWord.getRight().equals("NN")){
                if(nouns.containsKey(taggedWord.getLeft())){
                    nouns.get(taggedWord.getLeft()).increment();
                }else{
                    nouns.put(taggedWord.getLeft(), new MutableInt(1));
                }
            }
        }
        return nouns;
    }
    
    public void setPosTaggingService(POSTaggingService posTaggingService){
        this.posTaggingService = posTaggingService;
    }
    
    public void setLanguageDetectionService(LanguageDetectionService languageDetectionService){
        this.languageDetectionService = languageDetectionService;
    }
}
