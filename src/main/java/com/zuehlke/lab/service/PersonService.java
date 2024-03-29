/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.Person;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class PersonService {

    @PersistenceContext(unitName = "cloudCompPU")
    EntityManager em;

    public void saveDocumentToUser(Document doc, String firstname, String lastname){
        
        TypedQuery<Person> query = em.createNamedQuery("Person.findByName", Person.class);
        query.setParameter("firstname", firstname );
        query.setParameter("lastname", lastname);
        
        List<Person> result = query.getResultList();
        Person p = null;
        if(result.isEmpty()){
            System.err.println("Can't find the person!"); 
            p = new Person();
            p.setFirstname(firstname);
            p.setLastname(lastname);
            p.addDocument(doc);
            em.persist(p);    
        }else if(result.size() == 1){
            p = result.get(0);
            p.getDocuments().clear();
            p.addDocument(doc);
        }else{
          System.err.println("Person ["+firstname+","+lastname+"] seems to be ambiguous"); 
        }
    }
}
