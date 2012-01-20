    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

import com.zuehlke.lab.entity.Keyword;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.zuehlke.lab.entity.RelevanceStatus;
import com.zuehlke.lab.entity.RelevanceWord;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author user
 */
@Singleton
@Startup
@LocalBean
public class RelevanceService {

    @PersistenceContext(unitName="cloudCompPU")
    EntityManager em;
    
    private Map<String, RelevanceStatus> relevanceMap;
    
    @PostConstruct
    private void loadRelevanceList(){
       Query q = em.createNamedQuery("RelevanceWord.findAll",RelevanceWord.class);
       relevanceMap = new HashMap<String, RelevanceStatus>();
       for(RelevanceWord word : (List<RelevanceWord>)q.getResultList()){
           relevanceMap.put(word.getWord(), word.getStatus());
       } 
    }
    
    public void removeBlackListedWords(LinkedList<Keyword> keywords){
        LinkedList<Keyword> retVal = new LinkedList<Keyword>();
        Iterator<Keyword> iterator = keywords.iterator();
        while(iterator.hasNext()){
            Keyword keyword = iterator.next();
            RelevanceStatus status = relevanceMap.get(keyword.getWord());
            if(status != null){
                if(status == RelevanceStatus.AUTO_BLACKLISTED || status == RelevanceStatus.BLACKLISTED ){
                    iterator.remove();
                }
            }
        }
    }
    
    public LinkedList<Keyword>  removeWithListedWords(LinkedList<Keyword> keywords){
        LinkedList<Keyword> retVal = new LinkedList<Keyword>();
        Iterator<Keyword> iterator = keywords.iterator();
        while(iterator.hasNext()){
            Keyword keyword = iterator.next();
            RelevanceStatus status = relevanceMap.get(keyword.getWord());
            if(status != null){
                if(status == RelevanceStatus.AUTO_WITHLISTED || status == RelevanceStatus.WITHLISTED){
                    iterator.remove();
                    retVal.add(keyword);
                }
            }
        }
        return retVal;
    }
    
    public void setUserBlacklisted(String keyword){
        setUserStatus(keyword, RelevanceStatus.BLACKLISTED);
    }
    
    public void setUserWithlisted(String keyword){
        setUserStatus(keyword, RelevanceStatus.WITHLISTED);
    }
    
    private void setUserStatus(String keyword, RelevanceStatus status){
        RelevanceStatus oldStatus = relevanceMap.get(keyword);
        RelevanceWord word;
        if(oldStatus == null){
            word = new RelevanceWord(keyword, status);
        }else{
            word = findByWord(keyword);
        }
        word.setStatus(status);
        if(status == RelevanceStatus.BLACKLISTED){
            removeKeywordFromDocs(keyword);
        }
        
        relevanceMap.put(keyword, status);
    }
    
    private void removeKeywordFromDocs(String word){
        Query q = em.createNativeQuery("DELETE FROM keyword as k WHERE k.word = ?");
        q.setParameter(1, word);
        q.executeUpdate();
        em.clear();
    }
    
    public void setAutoBlacklisted(String keyword){
        setAutoStatus(keyword, RelevanceStatus.AUTO_BLACKLISTED);
    }
    
    public void setAutoWithlisted(String keyword){
       setAutoStatus(keyword, RelevanceStatus.AUTO_WITHLISTED);
    }
    
    public void setAutoWithlisted(LinkedList<Keyword> keywords) {
        for(Keyword k :keywords){
            setAutoWithlisted(k.getWord());
        }
    }
    
    private void setAutoStatus(String keyword, RelevanceStatus status){
        if(relevanceMap.containsKey(keyword)){
            throw new RuntimeException("Bad operation!!");
        }
        em.persist(new RelevanceWord(keyword,status));
        relevanceMap.put(keyword, status);
    }
    
    private RelevanceWord findByWord(String word){
        Query q = em.createNamedQuery("RelevanceWord.findByWord", RelevanceWord.class);
        q.setParameter("word", word);
        return (RelevanceWord) q.getSingleResult();
    }
    
    public List<RelevanceWord> getRelevanceWords(){
        Query q = em.createNamedQuery("RelevanceWord.findAllOrderByCount");
        return q.getResultList();
    }

    public void updateCount(){
        Query q = em.createNativeQuery("UPDATE RelevanceWord as rw SET rw.count= (SELECT SUM(k.COUNT) FROM KEYWORD k WHERE rw.WORD = k.WORD )");
        q.executeUpdate();
        em.clear();
        loadRelevanceList();
    }    
}
