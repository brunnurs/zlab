/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiCategory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class CategoryImporterService {

    public final static String preCategory = "<http://dbpedia.org/resource/Category:";
    public final static String keywordBeforeParent = "#broader";
    private Map<String, WikiCategory> allCategories = new HashMap<String, WikiCategory>();
    private InputStream dataInputStream;
    private long maxReadLines = 1000;

    public void importCategoryGraph() {

        String currentLine = "";
        int lineCount = 0;
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(dataInputStream));
            try {
                while ((currentLine = input.readLine()) != null && lineCount <= maxReadLines) {
                    String childCatString = getChildCategory(currentLine);
                    String parentCatString = getParentCategory(currentLine);
                    
                    //if parentCatString is null, it's not child/parent line
                    if (parentCatString != null) {
                        WikiCategory childCat = getCategoryIfExistOrNot(childCatString);
                        WikiCategory parentCategory = getCategoryIfExistOrNot(parentCatString);

                        parentCategory.getChilds().add(childCat);
                        childCat.getParents().add(parentCategory);
                    }

                    lineCount++;
                }
                System.out.println("added " + allCategories.size() + " categories");
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void initialSaveAllCategoriesToDatabase(){        
        int current = 0;
        for(WikiCategory cat : allCategories.values()){
            categoryFacade.getEntityManager().merge(cat);
            System.out.println(current+" from "+allCategories.values().size());
            current++;
        }
    }

    public void removeAllExistingWikiCategoriesFromDatabase(){
        for(WikiCategory cat: categoryFacade.findAll()){
            categoryFacade.remove(cat);
        }
    }
    
    private WikiCategory getCategoryIfExistOrNot(String category) {
        if (allCategories.containsKey(category)) {
            return allCategories.get(category);
        } else {
            WikiCategory newCat = new WikiCategory();
            newCat.setDescription(category);
            allCategories.put(category, newCat);
            return newCat;
        }
    }

    private String getChildCategory(String line) {
        int startCategory = line.indexOf(preCategory) + preCategory.length();
        int endCategory = line.indexOf(">", startCategory);
        return line.substring(startCategory, endCategory);
    }

    /**
     * Will return the parent category, if the given line is a child/parent-line. Otherwise, it returns null
     * @param line
     * @return 
     */
    private String getParentCategory(String line) {
        int indexOfSpecialKeyword = line.indexOf(keywordBeforeParent);
        if (indexOfSpecialKeyword != -1) {
            int startCategory = line.indexOf(preCategory, line.indexOf(keywordBeforeParent)) + preCategory.length();
            int endCategory = line.indexOf(">", startCategory);

            return line.substring(startCategory, endCategory);
        } else {
            return null;
        }
    }

    public InputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setDataInputStream(InputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public long getMaxReadLines() {
        return maxReadLines;
    }

    public void setMaxReadLines(long maxReadLines) {
        this.maxReadLines = maxReadLines;
    }

    public Map<String, WikiCategory> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(Map<String, WikiCategory> allCategories) {
        this.allCategories = allCategories;
    }

    public void setCategoryFacade(WikiCategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }
    
    
}
