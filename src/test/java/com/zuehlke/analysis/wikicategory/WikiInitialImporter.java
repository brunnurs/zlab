/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import org.junit.Ignore;
import org.junit.Test;

/**
 * this class is used to import the big wikipedia-data without running in the jee container (if we start the application-server first, we would have to add the big 
 * resource-files there, which is a lot of work). 
 * @author user
 */
public class WikiInitialImporter {

    public WikiInitialImporter() {
    }

    @Test
    @Ignore
    public void importCategoryGraph() throws FileNotFoundException {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("wikiImporterPU");
        EntityManager em = emFactory.createEntityManager();

        WikiCategoryImporterService categoryImportService = new WikiCategoryImporterService();
        categoryImportService.setCategoryFacade(new WikiCategoryFacade(em));

        try {
            em.getTransaction().begin();
            categoryImportService.removeAllExistingWikiCategoriesFromDatabase();
            em.getTransaction().commit();
            System.out.println("Tabellen geleert");
        } finally {
            em.close();
        }

        categoryImportService.setMaxReadLines(1000000);
        //categoryImportService.setDataInputStream(getClass().getClassLoader().getResourceAsStream("wikitestdata/test_skos_categories.nt"));
        categoryImportService.setDataInputStream(new FileInputStream(new File("/home/user/Downloads/skos_categories_en.nt")));

        categoryImportService.importCategoryGraph();
        System.out.println("Graph eingelesen");

        try {
            em.getTransaction().begin();
            categoryImportService.initialSaveAllCategoriesToDatabase();
            em.getTransaction().commit();
            System.out.println("Graph in Datenbank gespeichert");
        } finally {
            em.close();
        }
    }

    @Test
    @Ignore
    public void importArticles() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {


        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("wikiImporterPU");
        EntityManager em = emFactory.createEntityManager();

        WikiArticleImporterService articleImportService = new WikiArticleImporterServiceManualTransaction();
        articleImportService.setArticleFacade(new WikiArticleFacade(em));

        
        articleImportService.setMaxReadLines(20000000);
        //articleImportService.setDataInputStream(getClass().getClassLoader().getResourceAsStream("wikitestdata/test_article_categories.nt"));
        articleImportService.setDataInputStream(new FileInputStream(new File("/home/user/Downloads/article_categories_en.nt")));
        
        articleImportService.importAllArticles();
    }
    
    class  WikiArticleImporterServiceManualTransaction extends WikiArticleImporterService{
        
        @Override
        protected long importATranche(long trancheSize, long charsToSkip) throws IOException{
        
            EntityManager em = this.articleFacade.getEntityManager();
            em.getTransaction().begin();
            long l = super.importATranche(trancheSize, charsToSkip);
            em.getTransaction().commit();
            em.clear();
            return l;         
        }   
    }
    
}
