package com.zuehlke.lab.web.jsf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class Cloud implements Serializable {
    
    
    private int id;
    
    
    public static int MAX_WEIGHT = 20;
    public static int MIN_WEIGHT = 3;
    
    private String label;
    private int weight;
    private List<Cloud> elements;
    
    public Cloud(){
        elements = new ArrayList<Cloud>();
    }

    public Cloud(String label, int weight, List<Cloud> elements) {
        this.label = label;
        this.weight = weight;
        this.elements = elements;

        generateId();
        
        if(elements == null){
            this.elements = new ArrayList<Cloud>();
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Cloud> getElements() {
        return elements;
    }

    public void setElements(List<Cloud> subclouds) {
        this.elements = subclouds;
    }

    public boolean addElement(Cloud cloud) {
        return elements.add(cloud);
    }
    
    public boolean addElement(String label, int weight) {
            return elements.add(new Cloud(label,weight,null));
    }
    
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public boolean hasSubCloud(){
       return !elements.isEmpty();
    }

    public int getId() {
        return id;
    }
    
    private void generateId(){
        id = (int)(7*Math.random()*100);
        id = 17 * id + (this.label != null ? this.label.hashCode() : 0);
        
    }
    
}
