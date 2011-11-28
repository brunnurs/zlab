/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.persistance.facade;

import com.zuehlke.persistance.entities.RawData;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author user
 */
@Stateless
public class RawDataFacade extends AbstractFacade<RawData> {
    @PersistenceContext(unitName = "zlabPersistanceUnit")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public RawDataFacade() {
        super(RawData.class);
    }
    
}
