/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;


/**
 *
 * @author mhaspra
 */
@Stateless
@LocalBean
public class LanguageDetectionService {
    
    private List<String> englishWords;
    private List<String> germanWords; 

    public LanguageDetectionService() {
        englishWords = readWords("textresources/top1000en.txt");
        germanWords = readWords("textresources/top1000de.txt");
    }

    public Language getLanguage(String text) {
        int germanWordOccurrences = countOccurrences(text, germanWords);
        int englishWordOccurences = countOccurrences(text, englishWords);
   
        if(germanWordOccurrences > englishWordOccurences){
            return Language.GERMAN;
        } else if(englishWordOccurences > germanWordOccurrences){
            return Language.ENGLISH;
        }else{
            return Language.UNKNOWN;
        }
   
    }
    
    private int countOccurrences(String text, List<String> words){
        int i = 0;
        for(String word :  words){
            if(text.contains(word)){
                i++;
            }
        }
        return i;
    }

    private List<String> readWords(String fileName) {
        List<String> words = new LinkedList<String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileName)));
            String word = reader.readLine();
            while (word != null) {
                words.add(word);
                word = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LanguageDetectionService.class.getName()).log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LanguageDetectionService.class.getName()).log(Level.WARNING, null, ex);
        }
        return words;
    }
}