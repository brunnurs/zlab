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
import org.junit.Ignore;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author user
 */
public class CategoryImporterServiceTest {
    
    private CategoryImporterService categoryService;
    public CategoryImporterServiceTest() {
    }
    
   
    
    @Before
    public void setUp() {
        categoryService = new CategoryImporterService();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testReadArticleFileWithTestData(){
        //**** prepare ****
        categoryService.setDataInputStream(getClass().getClassLoader().getResourceAsStream("wikitestdata/test_skos_categories.nt"));
        
        //**** action ****
        categoryService.importCategoryGraph();
        
        //**** verify ****
        assertThat(categoryService.getAllCategories().containsKey("Artificial_mythology"), is(true));
        assertThat(categoryService.getAllCategories().containsKey("Monty_Python"), is(true));
        
        WikiCategory simpsons = categoryService.getAllCategories().get("The_Simpsons");
        assertThat(simpsons.getParents().size(), is(7));
        
        WikiCategory aSimpsonParent = categoryService.getAllCategories().get("Media_franchises");
        
        assertThat(simpsons.getParents(), hasItem(aSimpsonParent));
        assertThat(aSimpsonParent.getChilds(), hasItem(simpsons));
        
      
        System.out.println(simpsons.getParents());
    }
    
    @Test
    @Ignore
    public void testReadArticleFileWithRealData() throws FileNotFoundException{
        //**** prepare ****
        categoryService.setMaxReadLines(1000000);
        categoryService.setDataInputStream(new FileInputStream(new File("/home/user/Downloads/skos_categories_en.nt")));
        
        //**** action ****
        categoryService.importCategoryGraph();
        
        //**** verify ****
        System.out.println("Category-Count: " +categoryService.getAllCategories().size());
        
        int allSubCats = 0;
        int allParentCats = 0;
        for(WikiCategory cat: categoryService.getAllCategories().values()){
            allSubCats += cat.getChilds().size();
            allParentCats += cat.getParents().size();
        }
        
        System.out.println("A test category (parents): " + categoryService.getAllCategories().get("Algorithms").getParents());
        System.out.println("A test category (childs): " + categoryService.getAllCategories().get("Algorithms").getChilds());
        
        System.out.println("Average Child Count: " +(double)allSubCats/(double)categoryService.getAllCategories().size());
        System.out.println("Average Parent Count: " +(double)allParentCats/(double)categoryService.getAllCategories().size());        
    }
}
