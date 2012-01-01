/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

/**
 *
 * @author user
 */
public enum DocumentSource {
    COURSE_DESCRIPTION(1), PERSONAL_PROFILE(2);
    
    private int factor;

    private DocumentSource(int factor) {
        this.factor = factor;
    } 
}
