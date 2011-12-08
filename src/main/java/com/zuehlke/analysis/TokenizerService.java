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
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class TokenizerService {

    public String[] tokenizeEnglishText(String text) {
        return tokenize(ClassLoader.getSystemResourceAsStream("nlpmodels/en-token.bin"), text);
    }

    public String[] tokenizeGermanText(String text) {
        return tokenize(ClassLoader.getSystemResourceAsStream("nlpmodels/de-token.bin"), text);
    }

    private String[] tokenize(InputStream modelInputStream, String text) {
        String[] tokens = new String[0];
        try {
            TokenizerModel tokenizerModel = new TokenizerModel(modelInputStream);
            Tokenizer tokenizer = new TokenizerME(tokenizerModel);
            tokens = tokenizer.tokenize(text);
        } catch (IOException ex) {
            Logger.getLogger(TokenizerService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return tokens;
        }
    }
}
