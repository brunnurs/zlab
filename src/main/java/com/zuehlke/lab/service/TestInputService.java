/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.zuehlke.lab.entity.Event;
import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.Keyword;
import com.zuehlke.lab.entity.Person;
import com.zuehlke.lab.entity.Unit;
import javax.ejb.EJB;

/**
 *
 * @author user
 */
@Singleton
@Startup
@LocalBean
public class TestInputService {
    
    @PersistenceContext(unitName="cloudCompPU")
    EntityManager em;
    
    @EJB
    RelevanceService bls;

    public void initData() {
        
        
        Person p = new Person("1.","P");
        em.persist(p);
        Person p1 = new Person("2.","P");
        em.persist(p1);
        Person p2 = new Person("3.","P");
        em.persist(p2);
        Person p3 = new Person("4.","P");
        em.persist(p3);
        Person p4 = new Person("5.","P");
        em.persist(p4);
        Person p5 = new Person("6.","P");
        em.persist(p5);
        Person p6 = new Person("7.","P");
        em.persist(p6);
        Person p7 = new Person("8.","P");
        em.persist(p7);
        Person p8 = new Person("9.","P");
        em.persist(p8);
        Person p9 = new Person("10.","P");
        em.persist(p9);
        Person p10 = new Person("11.","P");
        em.persist(p10);
        Person p11 = new Person("12.","P");
        em.persist(p11);
        
        
        Event c = new Event("1. Course");
        List<Person> ps = new ArrayList<Person>();
        ps.add(p2);
        ps.add(p3);
        ps.add(p4);
        ps.add(p5);
        ps.add(p6);
        ps.add(p7);
        c.setPersons(ps);
        em.persist(c);
        
        Event c1 = new Event("2. Course");
        ps = new ArrayList<Person>();
        ps.add(p11);
        ps.add(p10);
        ps.add(p9);
        ps.add(p5);
        ps.add(p2);
        ps.add(p1);
        c1.setPersons(ps);
        em.persist(c1);
        
        Event c2 = new Event("3. Course");
        ps = new ArrayList<Person>();
        ps.add(p);
        ps.add(p10);
        ps.add(p9);
        ps.add(p7);
        ps.add(p2);
        ps.add(p8);
        c2.setPersons(ps);
        em.persist(c2);
        
        
        Event c3 = new Event("4. Course");
        ps = new ArrayList<Person>();
        ps.add(p1);
        ps.add(p2);
        ps.add(p3);
        ps.add(p4);
        ps.add(p5);
        ps.add(p6);
        ps.add(p7);
        ps.add(p8);
        ps.add(p9);
        ps.add(p10);
        ps.add(p11);
        c3.setPersons(ps);
        em.persist(c3);
        
        Document doc = new Document();
        doc.setOwner(p);
        getKeywords1(doc);
        em.persist(doc);
        
        doc = new Document();
        doc.setOwner(p1);
        getKeywords2(doc);
        em.persist(doc);
        
        doc = new Document();
        doc.setOwner(p1);
        getKeywords2(doc);
        em.persist(doc);
        
        doc = new Document();
        doc.setOwner(c);
        getKeywords3(doc);
        em.persist(doc);
        
        doc = new Document();
        doc.setOwner(c1);
        getKeywords4(doc);
        em.persist(doc);
        
        doc = new Document();
        doc.setOwner(c2);
        getKeywords5(doc);
        em.persist(doc);
        
        doc = new Document();
        doc.setOwner(c3);
        getKeywords6(doc);
        em.persist(doc);
        
        
        Unit unit = new Unit("1. Unit");
        em.persist(unit);
        unit.addPerson(p);
        unit.addPerson(p2);
        unit.addPerson(p9);
        unit.addPerson(p7);
        
        unit = new Unit("2. Unit");
        em.persist(unit);
        unit.addPerson(p1);
        unit.addPerson(p4);
        unit.addPerson(p11);
        unit.addPerson(p6);
        unit = new Unit("3. Unit");
        em.persist(unit);
        unit.addPerson(p8);
        unit.addPerson(p9);
        unit.addPerson(p3);
        unit.addPerson(p5);
        
        

    }

    
    private void getKeywords1(Document d){
        new Keyword(d,"Java", 5);
        new Keyword(d,"JNDI", 1);
        new Keyword(d,"EJB", 1);
        new Keyword(d,"Tomcat", 1);
        new Keyword(d,"Galssfish", 5);
    }
    
    private void getKeywords2(Document d){
        List<Keyword> k = new ArrayList<Keyword>();
        new Keyword(d,"Java", 5);
        new Keyword(d,"Scrum", 5);
        new Keyword(d,"JDBC", 1);
        new Keyword(d,".Net", 1);
    }
    
    private void getKeywords3(Document d){
        new Keyword(d,"JPA", 5);
        new Keyword(d,"Hibernate", 3);
        new Keyword(d,"JDBC", 1);
        new Keyword(d,"TopLink", 2);
        new Keyword(d,"Glassfish", 1);
        new Keyword(d,"JPQL", 3);
        new Keyword(d,"REST", 3);
        new Keyword(d,"SOAP", 3);
    }
    
    private void getKeywords4(Document d){
        new Keyword(d,"C#", 5);
        new Keyword(d,"IIS", 3);
        new Keyword(d,"VB", 1);
        new Keyword(d,"ASP.NET", 2);
        new Keyword(d,"Webservices", 5);
        new Keyword(d,"REST", 3);
        new Keyword(d,"SOAP", 3);
    }
    
    private void getKeywords5(Document d){
        new Keyword(d,"Web 2.0", 5);
        new Keyword(d,"CSS", 3);
        new Keyword(d,"HTML 5", 5);
        new Keyword(d,"Flash", 2);
        new Keyword(d,"Javascript", 5);
        new Keyword(d,"PHP", 5);
        new Keyword(d,"Ajax", 2);
        new Keyword(d,"HTML", 3);
        new Keyword(d,"MobileAps", 4);
        new Keyword(d,"IOs", 3);
        new Keyword(d,"Android", 5);
    }
    
    private void getKeywords6(Document d){
        new Keyword(d,"Java", 5);
    }

    public void addToBlacklist() {
//        bls.addToBlackList("Java");
//        bls.addToBlackList("Herr");
//        bls.addToBlackList("Technologie");
    }
    
}
