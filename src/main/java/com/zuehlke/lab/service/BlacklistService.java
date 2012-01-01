    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.Keyword;
import com.zuehlke.lab.service.util.BeanResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author user
 */
@Singleton
@Startup
@LocalBean
public class BlacklistService {

    @PersistenceContext(unitName="cloudCompPU")
    EntityManager em;
    
    private Set<String> blacklist;
    
    private String addWord;
    
    @PostConstruct
    private void loadBlacklist(){
       Query q = em.createNativeQuery("SELECT KEYWORD FROM BLACKLIST");
       
       //BeanResult.setQueryResultClass(q,String.class);
        
       blacklist = new HashSet<String>();//q.getResultList());
    }
    
    public Collection<String> removeBlacklist(Collection<String> keywords){
        List<String> retVal = new ArrayList<String>();
        for(String keyword : keywords){
            if(!blacklist.contains(keyword)){
                retVal.add(keyword);
            }
        }
        return retVal;
    }
    
    public void addToBlackList(String word){
        Query q = em.createQuery("SELECT doc FROM Document doc, IN(doc.keywords) k WHERE k.word = :word", Document.class);
        q.setParameter("word", word);
        List<Document> docs = q.getResultList();
        for(Document doc : docs){
            Keyword k = doc.removeKeyword(word);
            em.remove(k);
        }
        em.flush();
        
        String sql = "INSERT INTO BLacklist values ('"+word+"')";
        System.out.println(sql);
        
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        
        blacklist.add(word);
    }
}
