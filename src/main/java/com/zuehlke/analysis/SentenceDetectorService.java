/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class SentenceDetectorService {
    
    
    private SentenceDetectorME enSentenceDetector,deSentenceDetector;
    
    @PostConstruct
    protected void init() throws IOException{
        enSentenceDetector = new SentenceDetectorME(new SentenceModel(this.getClass().getClassLoader().getResourceAsStream("nlpmodels/en-sent.bin")));
        deSentenceDetector = new SentenceDetectorME(new SentenceModel(this.getClass().getClassLoader().getResourceAsStream("nlpmodels/de-sent.bin")));
    }
    

    public String[] splitEnglishTextIntoSentences(String text) {
        return split(enSentenceDetector, text);
    }

    public String[] splitGermantextIntoSentences(String text) {
        return split(deSentenceDetector, text);
    }

    private String[] split(SentenceDetectorME sentenceDetector, String text) {
        String[] sentences = new String[0];
        sentences = sentenceDetector.sentDetect(text);
        return sentences;
    }
}
