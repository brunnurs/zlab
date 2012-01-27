/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiCategory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author user
 */
@Stateless
public class WikiCategoryFacade extends AbstractFacade<WikiCategory> {
    @PersistenceContext(unitName = "cloudCompPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public WikiCategoryFacade() {
        super(WikiCategory.class);
    }
    
}
