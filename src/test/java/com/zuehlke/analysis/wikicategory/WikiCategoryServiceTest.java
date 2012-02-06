/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiArticle;
import com.zuehlke.lab.entity.WikiCategory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryImmediateEditor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author user
 */
public class WikiCategoryServiceTest {
    
    WikiCategoryService categoryService;
    
    public WikiCategoryServiceTest() {
    }

    
    @Before
    public void setUp() {
        categoryService = new WikiCategoryService();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testIsWordAllowedInCategories() throws FileNotFoundException{
        //****** Prepare ****
        //Load the real category-graph, to test if the performance of the graph-traverser is sufficient
        WikiCategoryImporterService categoryImporter =  new WikiCategoryImporterService();
        loadRealCategoryGraph(categoryImporter);
        
        WikiArticleFacade articleFacadeMock = mock(WikiArticleFacade.class);
        WikiArticle articleMock = createArticleMock();
        List<WikiArticle> articleList = new LinkedList<WikiArticle>();
        articleList.add(articleMock); 
        when(articleFacadeMock.findArticlesByTitle(articleMock.getTitle())).thenReturn(articleList);
        
        List<WikiCategory> allowedCategories = new LinkedList<WikiCategory>();
        allowedCategories.add(new WikiCategory("Forms_of_government"));
        
        //Inject the Mockups
        categoryService.setArticleFacade(articleFacadeMock);
        categoryService.setCategoryImporterService(categoryImporter);
        categoryService.setDefinedAllowedCategories(allowedCategories);
        
        //***** Action *****
        boolean isIt = categoryService.isWordInAllowedCategories("Algeria");
        
        //***** Verify *****
        assertThat(isIt, is(true));
    }
    
    private void loadRealCategoryGraph(WikiCategoryImporterService categoryImporter) throws FileNotFoundException{
        categoryImporter.setMaxReadLines(1000000);
        categoryImporter.setDataInputStream(new FileInputStream(new File("/home/user/Downloads/skos_categories_en.nt")));

        categoryImporter.importCategoryGraph();        
    }
    
    private WikiArticle createArticleMock(){
        WikiArticle articleMock = new WikiArticle("Algeria");
        articleMock.appendCategory("Algeria");
        articleMock.appendCategory("African_countries");
        articleMock.appendCategory("Republics");
        return articleMock;
    }
            
            
}
