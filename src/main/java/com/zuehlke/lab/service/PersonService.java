/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.Person;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveDocumentToUser(Document doc, String firstname, String lastname){
        String queryString = "select p from Person p WHERE p.firstname = :firstname AND p.lastname = :lastname";
        TypedQuery<Person> query = em.createQuery(queryString, Person.class);
        query.setParameter("firstname", firstname );
        query.setParameter("lastname", lastname);
        
        List<Person> result = query.getResultList();
        Person p = null;
        if(result.isEmpty()){
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
          System.out.println("Person ["+p.getFirstname()+","+p.getLastname()+"] seems to be ambiguous"); 
        }
    }
}
