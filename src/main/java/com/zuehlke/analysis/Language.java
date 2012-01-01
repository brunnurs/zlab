/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

/**
 *
 * @author user
 */
public enum Language {
    GERMAN("DE"), ENGLISH("EN"), UNKNOWN(null);
    
    private String code;

    private Language(String code) {
        this.code = code;
    } 
}
