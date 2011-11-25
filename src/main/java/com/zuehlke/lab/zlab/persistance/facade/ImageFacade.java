/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.zlab.persistance.facade;

import com.zuehlke.lab.zlab.persistance.entities.Image;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author user
 */
@Stateless
public class ImageFacade extends AbstractFacade<Image> {
    @PersistenceContext(unitName = "com.zuehlke.lab_zlab_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ImageFacade() {
        super(Image.class);
    }
    
}
