/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author user
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "RelevanceWord.findAll", query = "SELECT r FROM RelevanceWord r"),
    @NamedQuery(name = "RelevanceWord.findAllOrderByCount", query = "SELECT r FROM RelevanceWord r ORDER BY r.count DESC"),
    @NamedQuery(name = "RelevanceWord.findById", query = "SELECT r FROM RelevanceWord r WHERE r.id = :id"),
    @NamedQuery(name = "RelevanceWord.findByStatus", query = "SELECT r FROM RelevanceWord r WHERE r.status = :status"),
    @NamedQuery(name = "RelevanceWord.findByWord", query = "SELECT r FROM RelevanceWord r WHERE r.word = :word")})
public class RelevanceWord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length=500,unique=true)
    private String word;
    @Column(nullable=false)
    private Integer count;
    @Column(nullable=false)
    private RelevanceStatus status; 

    public RelevanceWord() {
    }
    
    public RelevanceWord(String word, RelevanceStatus status) {
        this.word = word;
        this.status = status;
        this.count = 0;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RelevanceStatus getStatus() {
        return status;
    }

    public void setStatus(RelevanceStatus status) {
        this.status = status;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
