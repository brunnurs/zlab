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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
@Singleton
@Startup
@LocalBean
public class SynonymsServices {

    @PersistenceContext(unitName="cloudCompPU")
    EntityManager em;
    
    private Map<String,String> synonyms;
    
    private String addWord;
    
    public SynonymsServices(){
        synonyms = new HashMap<String, String>();
    }
    
    @PostConstruct
    private void loadSynonym(){
        
       Query q = em.createNativeQuery("SELECT SYNONYM , KEYWORD FROM SYNONYM");
       
       for(String[] res : (List<String[]>)q.getResultList()){
           synonyms.put(res[0], res[1]);
       }
       //BeanResult.setQueryResultClass(q,String.class);
        
       //synonyms = new HashSet<String>(q.getResultList());
       System.out.print("dsf");
       
    }
    
    public void addSynonym(String word, String synonym){
        Query q = em.createQuery("SELECT doc FROM Document doc, IN(doc.keywords) k WHERE k.word = :word", Document.class);
        q.setParameter("word", word);
        List<Document> docs = q.getResultList();
        for(Document doc : docs){
            Keyword k = doc.getKeyword(word);
            Keyword sy = doc.getKeyword(synonym);
            
            if(sy == null){
                sy = new Keyword(synonym, k.getCount());
                em.persist(sy);
                doc.addKeyword(sy);
            }else{
                sy.setCount(sy.getCount() + k.getCount());
            }
            doc.removeKeyword(word);
            em.remove(k);
        }
        em.flush();
        
        String sql = "INSERT INTO Synonym values ('"+word+"','"+synonym+"')";
        System.out.println(sql);
        
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        
        synonyms.put(word,synonym);
    }
}