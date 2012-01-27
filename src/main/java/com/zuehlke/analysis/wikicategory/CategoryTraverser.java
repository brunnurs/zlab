/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiCategory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author user
 */

public class CategoryTraverser {

    Map<String, WikiCategory> traveldCategories = new HashMap<String, WikiCategory>();
    
    List<WikiCategory> allowedCategories;
    
    public boolean doWeFoundAnAllowedCategory(WikiCategory currentCategory){
        traveldCategories.put(currentCategory.getDescription(), currentCategory);
        if(allowedCategories.contains(currentCategory))
            return true;
        System.out.println("Put on the list: "+currentCategory.getDescription());
        for(WikiCategory parentCategory : currentCategory.getParents()){
            if(!traveldCategories.containsKey(parentCategory.getDescription())){
               if(doWeFoundAnAllowedCategory(parentCategory))
                   return true;
            }
        }
         
        return false;
    }

    public List<WikiCategory> getAllowedCategories() {
        return allowedCategories;
    }

    public void setAllowedCategories(List<WikiCategory> allowedCategories) {
        this.allowedCategories = allowedCategories;
    }

    public Map<String, WikiCategory> getTraveledCategories() {
        return traveldCategories;
    }
    
    
    
    
} 
    
