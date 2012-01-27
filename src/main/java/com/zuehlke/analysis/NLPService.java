/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
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
    
    private Lexicon lexicon;
    
    @PostConstruct
    protected void init() throws IOException{
      lexicon = new LexiconImpl("textresources/delexicon.txt");
    }

    public Map<String, MutableInt> extractTerms(String text) {
        List<Pair<String, String>> taggedWords = Collections.EMPTY_LIST;

        Language language = languageDetectionService.getLanguage(text);

        if (language == Language.GERMAN) {
            taggedWords = posTaggingService.tagGermanText(text);
        }
        if (language == Language.ENGLISH) {
            taggedWords = posTaggingService.tagEnglishText(text);
        }

        Map<String, MutableInt> terms = new HashMap<String, MutableInt>();

        while (!taggedWords.isEmpty()) {
            String term = getNextTerm(taggedWords);
            if (terms.containsKey(term)) {
                terms.get(term).increment();
            } else {
                terms.put(term, new MutableInt(1));
            }
        }

        return terms;
    }

    private String getNextTerm(List<Pair<String, String>> taggedWords) {
        while (!taggedWords.isEmpty()) {
            Pair<String, String> taggedWord = taggedWords.remove(0);
            if (taggedWord.getRight().equals("NN") && !ignore(taggedWord.getLeft())) {
                return getLemma(taggedWord.getLeft());
            } else if (taggedWord.getRight().equals("NE") && !ignore(taggedWord.getLeft())) {
                return extractNamedEntity(taggedWord.getLeft(), taggedWords);
            }
        }
        return "";
    }
    
    private String getLemma(String noun){
        String lemma = lexicon.getLemma(noun);
        if(lemma == null){
            return noun;
        }else{
            if(lemma.contains(".")){
                return lemma.substring(0, lemma.indexOf("."));
            }else{
                return lemma;
            }
        }
    }
    
   private boolean ignore(String candidate){
       if(candidate.length() <= 3){
           return !hasCharacter(candidate);
       }else{
           return false;
       }
   }
   
   private boolean hasCharacter(String candidate){
       return candidate.matches(".*[a-zA-Z].*");
   }

    private String extractNamedEntity(String firstWord, List<Pair<String, String>> taggedWords) {
        StringBuilder namedEntity = new StringBuilder(firstWord);
        while (!taggedWords.isEmpty()) {
            Pair<String, String> taggedWord = taggedWords.remove(0);
            if (taggedWord.getRight().equals("NE")) {
                namedEntity.append(" ");
                namedEntity.append(taggedWord.getLeft());
            } else {
                break;
            }
        }

        return namedEntity.toString();
    }

    public void setPosTaggingService(POSTaggingService posTaggingService) {
        this.posTaggingService = posTaggingService;
    }

    public void setLanguageDetectionService(LanguageDetectionService languageDetectionService) {
        this.languageDetectionService = languageDetectionService;
    }
}
