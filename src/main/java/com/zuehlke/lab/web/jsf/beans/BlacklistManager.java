package com.zuehlke.lab.web.jsf.beans;


import com.zuehlke.lab.entity.RelevanceStatus;
import com.zuehlke.lab.entity.RelevanceWord;
import com.zuehlke.lab.service.RelevanceService;
import com.zuehlke.lab.service.CloudService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
@ManagedBean
@RequestScoped
public class BlacklistManager implements Serializable {
    
    public class KeywordSelection{
        
        private RelevanceStatus status;
        private String word;
        private int index;

        public KeywordSelection(String word,RelevanceStatus status, int index) {
           this.word = word;
           this.index = index;
           this.status = status;
        }

        public String getWord() {
            return word;
        }

        public boolean isAuto() {
            return (status == RelevanceStatus.AUTO_BLACKLISTED || status == RelevanceStatus.AUTO_WITHLISTED);
        }
        
        public boolean isUser() {
            return (status == RelevanceStatus.WITHLISTED || status == RelevanceStatus.BLACKLISTED);
        }

        public boolean isBlacklisted() {
            return (status == RelevanceStatus.BLACKLISTED || status == RelevanceStatus.AUTO_BLACKLISTED);
        }

         public void setBlacklisted(boolean value) {
            if(value == true){
                status = RelevanceStatus.BLACKLISTED;
            }else{
                status = RelevanceStatus.WITHLISTED;
            }
         }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
 
    private static final long serialVersionUID = 1L;
    private List<KeywordSelection> keywords;
    private DataTable htmlDataTable;
    
    @EJB
    RelevanceService relevanceService;
	
    
    @EJB
    CloudService cloudService;
    
    @PostConstruct
    protected void init(){
        keywords = new ArrayList<KeywordSelection>();
        int i = 0;
        for(RelevanceWord k : relevanceService.getRelevanceWords()){
            keywords.add(new KeywordSelection(k.getWord(),k.getStatus(),i));
            i++;
        }
    }

    public List<KeywordSelection> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordSelection> keywords) {
        this.keywords = keywords;
    }
    
    public void handleChange(AjaxBehaviorEvent event){
        Object value = ((SelectBooleanCheckbox) event.getSource()).getValue();
        KeywordSelection word = (KeywordSelection) htmlDataTable.getRowData();
    }

    public DataTable getHtmlDataTable() {
        return htmlDataTable;
    }

    public void setHtmlDataTable(DataTable htmlDataTable) {
        this.htmlDataTable = htmlDataTable;
    }
    
    public void onRowSelect(){
         KeywordSelection word = (KeywordSelection) htmlDataTable.getRowData();
    }
}