/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class NLPService {
    
    @Inject
    private POSTaggingService posTaggingService;
    
    @Inject
    private LanguageDetectionService languageDetectionService;
    
    public Map<String, MutableInt> extractNouns(String text) {
        List<Pair<String, String>> taggedWords = Collections.EMPTY_LIST;
        
        switch(languageDetectionService.getLanguage(text)){
            case GERMAN:
                taggedWords = posTaggingService.tagGermanText(text);
                break;
            case ENGLISH:
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
