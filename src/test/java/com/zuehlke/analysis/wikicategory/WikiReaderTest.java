/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import org.junit.Ignore;
import com.zuehlke.lab.entity.WikiCategory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 *
 * @author user
 */
public class WikiReaderTest {
    WikiReader reader;
    
    @Before
    public void setUp(){
        reader = new WikiReader();
    }
    
    public WikiReaderTest() {
    }
    
    @Test
    public void testReadArticleFileWithTestData(){
        //**** prepare ****
        reader.setDataInputStream(getClass().getClassLoader().getResourceAsStream("wikitestdata/test_article_categories.nt"));
        
        //**** action ****
        reader.readArticleFileBufferd();
        
        //**** verify ****
        assertThat(reader.getAllCategories().containsKey("Autism"), is(true));
        assertThat(reader.getAllCategories().containsKey("Defenders_of_slavery"), is(true));
        
        assertThat(reader.getArticleCategories().containsKey("Aristotle"), is(true));
        
        List<WikiCategory> aristotelesCategories = reader.getArticleCategories().get("Aristotle");
        assertThat(aristotelesCategories.size(), is(20));
        System.out.println(aristotelesCategories);
    }
    
    @Test
    @Ignore
    public void testReadArticleFileWithRealData() throws FileNotFoundException, Throwable{
        //**** prepare ****
        reader.setMaxReadLines(20000000);
        reader.setDataInputStream(new FileInputStream(new File("/home/user/Downloads/article_categories_en.nt")));
        
        //**** action ****
        reader.readArticleFileBufferd();
        
        //**** verify ****
        System.out.println("Article-Count: " +reader.getArticleCategories().size());
        System.out.println("Category-Count: " +reader.getAllCategories().size());
    }
}
