/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiArticle;
import com.zuehlke.lab.entity.WikiCategory;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class WikiCategoryService {

    @Inject
    WikiCategoryImporterService categoryImporterService;
    
    @Inject
    WikiArticleFacade articleFacade;
    //TODO: load this Categories in a proper way! (for example a property file, on startup)
    //Take care that the Categories are loaded WITH the correct db-id! otherwise it will not work (equals-method)
    List<WikiCategory> definedAllowedCategories = new LinkedList<WikiCategory>();

    /**
     * Check, if the word, existing in a title of a wikipedia-article, has a category (recursivly searched through the category-graph) which is
     * defined in the category-properties.
     * @param word
     * @return 
     */
    public boolean isWordInAllowedCategories(String word) {

        CategoryTraverser graphTraverser = new CategoryTraverser();
        graphTraverser.setAllowedCategories(definedAllowedCategories);
        
        List<WikiArticle> articles = articleFacade.findArticlesByTitle(word);
        for (WikiArticle article : articles) {
            if (isaCategoryOfThisArticleAllowed(article, graphTraverser)) {
                return true;
            }
            System.out.println("we travled "+graphTraverser.getTraveledCategories().size()+ " categories till now" );
        }
        return false;
    }

    private boolean isaCategoryOfThisArticleAllowed(WikiArticle article,CategoryTraverser graphTraverser) {
        
        String[] directCategories = article.getDirectCategories().split("\\|");
        for (String directCategory : directCategories) {            
            WikiCategory extendedCategory = categoryImporterService.getAllCategories().get(directCategory);
            
            if(graphTraverser.doWeFoundAnAllowedCategory(extendedCategory))
                return true;
        }
        return false;

    }

//    private boolean doWeHaveAtLeastOneSimilarCategory(List<WikiCategory> categories, List<WikiCategory> definedAllowedCategories) {
//        for (WikiCategory cat : categories) {
//            if (definedAllowedCategories.contains(cat)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public List<WikiCategory> getDefinedAllowedCategories() {
        return definedAllowedCategories;
    }

    public void setDefinedAllowedCategories(List<WikiCategory> definedAllowedCategories) {
        this.definedAllowedCategories = definedAllowedCategories;
    }

    public WikiArticleFacade getArticleFacade() {
        return articleFacade;
    }

    public void setArticleFacade(WikiArticleFacade articleFacade) {
        this.articleFacade = articleFacade;
    }

    public WikiCategoryImporterService getCategoryImporterService() {
        return categoryImporterService;
    }

    public void setCategoryImporterService(WikiCategoryImporterService categoryImporterService) {
        this.categoryImporterService = categoryImporterService;
    }
    
}
