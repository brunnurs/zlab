/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

/**
 *
 * @author user
 */
@Entity
public class WikiCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String description;
    
    private String parentPath;
    
    @OneToMany()
    @JoinTable(name="CATEGORY_TO_CATEGORY_P")
    private List<WikiCategory> parents = new LinkedList<WikiCategory>();
    @OneToMany
    @JoinTable(name="CATEGORY_TO_CATEGORY_CH")
    private List<WikiCategory> childs = new LinkedList<WikiCategory>();
    

    public WikiCategory(){
        
    }

    public WikiCategory(String description){ 
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WikiCategory)) {
            return false;
        }
        WikiCategory other = (WikiCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)) || !this.description.equals(other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zuehlke.lab.entity.Category[ id=" + id + ", description = "+description+" ]";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public List<WikiCategory> getChilds() {
        return childs;
    }

    public void setChilds(List<WikiCategory> childs) {
        this.childs = childs;
    }

    public List<WikiCategory> getParents() {
        return parents;
    }

    public void setParents(List<WikiCategory> parents) {
        this.parents = parents;
    }

}
