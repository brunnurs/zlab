/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 */
public class LanguageDetectionServiceTest {
    private LanguageDetectionService languageDetectionService;
    
    @Before
    public void setUp(){
        languageDetectionService = new LanguageDetectionService();
    }
    
    @Test
    public void testGermanDetection(){
        final String shortGermanText = "Hallo wie geht es dir?";
        assertEquals(Language.GERMAN, languageDetectionService.getLanguage(shortGermanText));
    }
    
    @Test
    public void testEnglishDetection(){
        final String shortEnglishText = "Firefox is designed by Mozilla.";
        assertEquals(Language.ENGLISH, languageDetectionService.getLanguage(shortEnglishText));
    }
    
    public void testUnknownDetection(){
        final String shortFrenchText = "Bonjour je m'appelle michi.";
        assertEquals(Language.UNKNOWN, languageDetectionService.getLanguage(shortFrenchText));
    }
}
