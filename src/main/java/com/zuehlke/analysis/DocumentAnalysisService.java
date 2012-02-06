/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.Keyword;
import com.zuehlke.lab.service.RelevanceService;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class DocumentAnalysisService {
    @PersistenceContext(name="cloudCompPU")
    EntityManager entityManager;
    @Inject
    NLPService nlpService;
    @EJB
    RelevanceService relevanceService;

    public void analyseDocument(Document doc) {
        List<Keyword> keywords = getKeywords(nlpService.extractTerms(doc.getRawData()));
        for (Keyword keyword : relevanceService.removeWithListedWords(keywords)) {
            keyword.setDocument(doc);
            doc.addKeyword(keyword);
            entityManager.persist(keyword);
        }
        relevanceService.removeBlackListedWords(keywords);
        relevanceService.setAutoWithlisted(keywords);
        for (Keyword keyword : keywords) {
            keyword.setDocument(doc);
            doc.addKeyword(keyword);
            entityManager.persist(keyword);
        }
    }
    
     public List<Keyword> getKeywords(Map<String,MutableInt> nouns){
       LinkedList<Keyword> retVal =  new LinkedList<Keyword>();
       for(Map.Entry<String,MutableInt> entry : nouns.entrySet()){
          retVal.add(new Keyword(entry.getKey(),entry.getValue().intValue()));
       }
       return retVal;
    }
}
