/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

/**
 *
 * @author user
 */
public enum SearchAttribute {
    
    PERSON("PERSON_ID","PERSON_NAME", -1), TECHNOLOGY("WORD","WORD",100), UNIT("UNIT_ID","UNIT_NAME",-1);
    
    private String searchColumn;
    private String selectColumn;
    private int maxCount;

    private SearchAttribute(String searchColumn, String selectColumn, int maxCount) {
        this.searchColumn = searchColumn;
        this.selectColumn = selectColumn;
        this.maxCount = maxCount;
    }
    
    public String getSearchColumn(){
        return searchColumn;
    }

    public String getSelectColumn() {
        return selectColumn;
    }
    
    public int getMaxCount(){
        return maxCount;
    }
}
