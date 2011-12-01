/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class SentenceDetectorService {

    public String[] splitEnglishTextIntoSentences(String text) {
        return split(ClassLoader.getSystemResourceAsStream("nlpmodels/en-sent.bin"), text);
    }

    public String[] splitGermantextIntoSentences(String text) {
        return split(ClassLoader.getSystemResourceAsStream("nlpmodels/de-sent.bin"), text);
    }

    private String[] split(InputStream modelInputStream, String text) {
        String[] sentences = new String[0];
        try {
            SentenceModel sentenceModel = new SentenceModel(modelInputStream);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
            sentences = sentenceDetector.sentDetect(text);
        } catch (IOException ex) {
            Logger.getLogger(SentenceDetectorService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return sentences;
        }

    }
}
