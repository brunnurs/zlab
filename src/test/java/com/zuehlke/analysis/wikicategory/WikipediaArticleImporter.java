/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiArticle;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;

/**
 * this class is used to import the big wikipedia-data without running in the jee container (if we start the application-server first, we would have to add the big 
 * resource-files there, which is a lot of work). 
 * @author user
 */
public class WikipediaArticleImporter {

    public WikipediaArticleImporter() {
    }

    
    @Test
    public void importArticles() throws ClassNotFoundException, SQLException {

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        DriverManager.getConnection("jdbc:derby://localhost:1527/sample").close();


 
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("wikiImporterPU");
        EntityManager em = emFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            WikiArticle testArticle = new WikiArticle("Test");
            testArticle.appendCategory("Esel");
            testArticle.appendCategory("Hund");
            em.persist(testArticle);
            em.getTransaction().commit();
            
        } finally {
            em.clear();
            emFactory.close();
        }

    }
}
