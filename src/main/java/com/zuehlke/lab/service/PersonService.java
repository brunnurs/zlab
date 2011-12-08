/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

import com.zuehlke.lab.entity.Person;
import java.util.List;
import java.util.Scanner;
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

    public void storeNewOrExistingUserWithRawData(List<Person> persons) {
        for (Person p : persons) {
            String queryString = "select p from Person p WHERE p.firstname = :firstname AND p.lastname = :lastname";
            TypedQuery<Person> query = em.createQuery(queryString, Person.class);
            query.setParameter("firstname", p.getFirstname());
            query.setParameter("lastname", p.getLastname());
            
            List<Person> result = query.getResultList();
            if(result.isEmpty()){
                em.persist(p);
            }else if(result.size() == 1){
                result.get(0).setRawdata(p.getRawdata());
            }else{
              System.out.println("Person ["+p.getFirstname()+","+p.getLastname()+"] seems to bi ambiguous"); 
            }
        }
    }
}
