/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiArticle;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author user
 */
@Stateless
public class WikiArticleFacade extends AbstractFacade<WikiArticle> {

    @PersistenceContext(unitName = "cloudCompPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Take care! this operation is really expensive atm... TODO: should we replace the LIKE?
     * @param titlePart
     * @return 
     */
    public List<WikiArticle> findArticlesByTitle(String titlePart) {
        String queryString = "select art from WikiArticle art WHERE art.title LIKE :titlePart";
        TypedQuery<WikiArticle> query = em.createQuery(queryString, WikiArticle.class);
        query.setParameter("titlePart", titlePart);

        return query.getResultList();
    }

    public WikiArticleFacade() {
        super(WikiArticle.class);
    }
}
