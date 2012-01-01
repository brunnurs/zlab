/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@XmlRootElement
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Owner owner;
    @OneToMany(mappedBy="document",cascade= CascadeType.ALL)
    private List<Keyword> keywords;
    
    private DocumentSource source;
    
    private String rawData;
    
    @Transient
    private Map<String,Keyword> keywordMap;
    
    public Document(){
        keywordMap = new HashMap<String, Keyword>();
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    @PreRemove @PreUpdate @PrePersist
    private void preSave(){
        keywords = new ArrayList<Keyword>(keywordMap.values());
    }
    
    @PostLoad
    private void postLoad(){
        for(Keyword k : keywords){
            keywordMap.put(k.getWord(), k);
        }
    }

    public Collection<Keyword> getKeywords() {
        return keywordMap.values();
    }

    
    public void setKeywords(Collection<Keyword> keywords) {
        this.keywordMap.clear();
        for(Keyword k : keywords){
            this.keywordMap.put(k.getWord(), k);
        }
    }
    
    public Keyword removeKeyword(String word) {
        return this.keywordMap.remove(word);
    }
    
    public Keyword getKeyword(String word) {
        return this.keywordMap.get(word);
    }
    
    public void addKeyword(Keyword keyword) {
        this.keywordMap.put(keyword.getWord(),keyword);
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public DocumentSource getSource() {
        return source;
    }

    public void setSource(DocumentSource source) {
        this.source = source;
    }
}
