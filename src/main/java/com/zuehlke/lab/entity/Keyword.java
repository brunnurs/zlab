/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@XmlRootElement
public class Keyword implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String word;
    private int count;
    @ManyToOne
    private Document document;
    
    public Keyword() {
    }
    
    public Keyword(String word, Long count) {
        this.word = word;
        this.count = count.intValue();
        
    }
    
    public Keyword(String word, int count) {
        this.word = word;
        this.count = count;
        
    }

    public Keyword(Document document, String word, int count) {
        this.word = word;
        this.count = count;
        this.document = document;
        document.addKeyword(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;   
    }
}
