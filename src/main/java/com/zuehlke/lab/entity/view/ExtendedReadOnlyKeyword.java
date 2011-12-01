/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity.view;

import java.io.Serializable;


public class ExtendedReadOnlyKeyword extends ReadOnlyKeyword implements Serializable{

    public Object key;
    
    public ExtendedReadOnlyKeyword(Object key,String word, int count) {
        super(word, count);
        this.key = key;
    }

    public Object getKey() {
        return key;
    }
}
