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
    
    PERSON("PERSON_ID","PERSON_NAME"), TECHNOLOGY("WORD","WORD"), UNIT("UNIT_ID","UNIT_NAME");
    
    private String searchColumn;
    private String selectColumn;

    private SearchAttribute(String searchColumn, String selectColumn) {
        this.searchColumn = searchColumn;
        this.selectColumn = selectColumn;
    }
    
    public String getSearchColumn(){
        return searchColumn;
    }

    public String getSelectColumn() {
        return selectColumn;
    }
}
