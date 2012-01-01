package com.zuehlke.lab.web.jsf.beans;


import com.zuehlke.lab.entity.Keyword;
import com.zuehlke.lab.service.BlacklistService;
import com.zuehlke.lab.service.CloudService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
@ManagedBean
@RequestScoped
public class BlacklistManager implements Serializable {
    
    
    public class KeywordSelection{
        
        private boolean blacklisted;
        private String Word;

        public KeywordSelection(boolean blacklisted, String Word) {
            this.blacklisted = blacklisted;
            this.Word = Word;
        }

        public String getWord() {
            return Word;
        }

        public void setWord(String Word) {
            this.Word = Word;
        }

        public boolean isBlacklisted() {
            return blacklisted;
        }

        public void setBlacklisted(boolean blacklisted) {
            this.blacklisted = blacklisted;
        }
    }
 
    private static final long serialVersionUID = 1L;
    private List<KeywordSelection> keywords;
    
    @EJB
    BlacklistService blacklistService;
	
    
    @EJB
    CloudService cloudService;
    
    @PostConstruct
    protected void init(){
        keywords = new ArrayList<KeywordSelection>();
        for(Keyword k : cloudService.getTopKeyWords(1000)){
            keywords.add(new KeywordSelection(false, k.getWord()));
        }
    }

    public List<KeywordSelection> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordSelection> keywords) {
        this.keywords = keywords;
    }
    
    public void handle(){
        for(KeywordSelection k : keywords){
            if(k.isBlacklisted()){
                blacklistService.addToBlackList(k.getWord());
            }
        }
    }
}