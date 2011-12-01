/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity.view;

import com.zuehlke.lab.entity.Keyword;
import java.io.Serializable;


public class ReadOnlyKeyword extends Keyword implements Serializable{

    public ReadOnlyKeyword(String word, int count) {
        super(word, count);
        
    }   
}
