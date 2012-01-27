/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import java.io.File;
import java.io.FileInputStream;
import com.zuehlke.lab.entity.WikiCategory;
import java.io.FileNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author user
*/
public class WikiCategoryImporterServiceTest {
    
    private WikiCategoryImporterService categoryImportService;
    public WikiCategoryImporterServiceTest() {
    }
    
   
    
    @Before
    public void setUp() {
        categoryImportService = new WikiCategoryImporterService();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testReadCategoryFileWithTestDataAndBuildGraph(){
        //**** prepare ****
        categoryImportService.setDataInputStream(getClass().getClassLoader().getResourceAsStream("wikitestdata/test_skos_categories.nt"));
        
        //**** action ****
        categoryImportService.importCategoryGraph();
        
        //**** verify ****
        assertThat(categoryImportService.getAllCategories().containsKey("Artificial_mythology"), is(true));
        assertThat(categoryImportService.getAllCategories().containsKey("Monty_Python"), is(true));
        
        WikiCategory simpsons = categoryImportService.getAllCategories().get("The_Simpsons");
        assertThat(simpsons.getParents().size(), is(7));
        
        WikiCategory aSimpsonParent = categoryImportService.getAllCategories().get("Media_franchises");
        
        assertThat(simpsons.getParents(), hasItem(aSimpsonParent));
        assertThat(aSimpsonParent.getChilds(), hasItem(simpsons));
        
      
        System.out.println(simpsons.getParents());
    }
    
    @Test
    public void testReadCategoryFileWithRealDataAndBuildGraph() throws FileNotFoundException{
        //**** prepare ****
        categoryImportService.setMaxReadLines(1000000);
        categoryImportService.setDataInputStream(new FileInputStream(new File("/home/user/Downloads/skos_categories_en.nt")));
        
        //**** action ****
        categoryImportService.importCategoryGraph();
        
        //**** verify ****
        System.out.println("Category-Count: " +categoryImportService.getAllCategories().size());
        
        int allSubCats = 0;
        int allParentCats = 0;
        for(WikiCategory cat: categoryImportService.getAllCategories().values()){
            allSubCats += cat.getChilds().size();
            allParentCats += cat.getParents().size();
        }
        
        System.out.println("A test category (parents): " + categoryImportService.getAllCategories().get("Algorithms").getParents());
        System.out.println("A test category (childs): " + categoryImportService.getAllCategories().get("Algorithms").getChilds());
        
        System.out.println("Average Child Count: " +(double)allSubCats/(double)categoryImportService.getAllCategories().size());
        System.out.println("Average Parent Count: " +(double)allParentCats/(double)categoryImportService.getAllCategories().size());        
    }
}
