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
    
    
    
    private Tokenizer enTokenizer,deTokenizer;
    
    @PostConstruct
    protected void init() throws IOException{
        enTokenizer = new TokenizerME(new TokenizerModel(this.getClass().getClassLoader().getResourceAsStream("nlpmodels/en-token.bin")));
        deTokenizer = new TokenizerME(new TokenizerModel(this.getClass().getClassLoader().getResourceAsStream("nlpmodels/de-token.bin")));
    }

    public String[] tokenizeEnglishText(String text) {
        return tokenize(enTokenizer, text);
    }

    public String[] tokenizeGermanText(String text) {
        return tokenize(deTokenizer, text);
    }

    private String[] tokenize(Tokenizer tokenizer, String text) {
        String[] tokens = new String[0];
        tokens = tokenizer.tokenize(text);
        return tokens;
    }
}
