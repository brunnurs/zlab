/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author mhaspra
 */
public class SentenceDetectorServiceTest {
    @Test
    @Ignore
    public void testEnglishSentenceDetection() {
        final String oneSentence = "An input sample sentence.";
        final String[] oneSentenceTokensExpected = {"An input sample sentence."};
        final String biggerText = "OpenNLP has a command line tool which is used to train the models available from the model download page on various corpora. The data must be converted to the OpenNLP Tokenizer training format. Which is one sentence per line.";
        final String[] biggerTextTokensExpected = {"OpenNLP has a command line tool which is used to train the models available from the model download page on various corpora.", "The data must be converted to the OpenNLP Tokenizer training format.", "Which is one sentence per line."};
        SentenceDetectorService sentenceDetectorService =  new SentenceDetectorService();

        Assert.assertArrayEquals(oneSentenceTokensExpected, sentenceDetectorService.splitEnglishTextIntoSentences(oneSentence));
        Assert.assertArrayEquals(biggerTextTokensExpected, sentenceDetectorService.splitEnglishTextIntoSentences(biggerText));
    }
    
    @Test
    @Ignore
    public void testGermanSentenceDetection() {
        final String oneSentence = "Als unabhängiger Partner führt Zühlke Ideen zur Marktreife und Unternehmen zum Erfolg.";
        final String[] oneSentenceTokensExpected = {"Als unabhängiger Partner führt Zühlke Ideen zur Marktreife und Unternehmen zum Erfolg."};
        final String biggerText = "Innovation ist die gelungene Verbindung von Technik, Mensch und Geschäft. Zühlke Engineering setzt Ihre Ideen mit erprobten Technologie- und Business-Konzepten in nachhaltige Markterfolge um. Unsere Kompetenz umfasst wegweisende Produkte, maßgefertigte Softwarelösungen sowie die intelligente Verbindung von Produkten und Software.";
        final String[] biggerTextTokensExpected = {"Innovation ist die gelungene Verbindung von Technik, Mensch und Geschäft.", "Zühlke Engineering setzt Ihre Ideen mit erprobten Technologie- und Business-Konzepten in nachhaltige Markterfolge um.", "Unsere Kompetenz umfasst wegweisende Produkte, maßgefertigte Softwarelösungen sowie die intelligente Verbindung von Produkten und Software."};
        SentenceDetectorService sentenceDetectorService =  new SentenceDetectorService();

        Assert.assertArrayEquals(oneSentenceTokensExpected, sentenceDetectorService.splitEnglishTextIntoSentences(oneSentence));
        Assert.assertArrayEquals(biggerTextTokensExpected, sentenceDetectorService.splitEnglishTextIntoSentences(biggerText));
    }
}
