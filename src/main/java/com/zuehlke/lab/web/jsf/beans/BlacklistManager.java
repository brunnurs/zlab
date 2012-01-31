package com.zuehlke.lab.web.jsf.beans;


import com.zuehlke.lab.entity.RelevanceStatus;
import com.zuehlke.lab.entity.RelevanceWord;
import com.zuehlke.lab.service.RelevanceService;
import com.zuehlke.lab.service.CloudService;
import com.zuehlke.lab.service.importer.ImporterService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
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
        private Integer count;

        public KeywordSelection(String word,RelevanceStatus status, Integer count) {
           this.word = word;
           this.status = status;
           this.count = count;
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
         
        public String getJuged(){
           return (status == RelevanceStatus.AUTO_BLACKLISTED || status == RelevanceStatus.AUTO_WITHLISTED)? "SYSTEM" : "USER";
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public RelevanceStatus getStatus() {
            return status;
        }

        public void setStatus(RelevanceStatus status) {
            this.status = status;
        }
    }
 
    private static final long serialVersionUID = 1L;
    private List<KeywordSelection> keywords;
    private int lastViewedIndex;
    private DataTable htmlDataTable;
    
    @EJB
    RelevanceService relevanceService;
    
    @EJB
    ImporterService importerService;
	
    
    @EJB
    CloudService cloudService;
    
    @PostConstruct
    protected void init(){
        keywords = new ArrayList<KeywordSelection>();
        int i = 0;
        for(RelevanceWord k : relevanceService.getRelevanceWords()){
            keywords.add(new KeywordSelection(k.getWord(),k.getStatus(),k.getCount()));
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
        boolean  value = (Boolean)(((SelectBooleanCheckbox) event.getSource()).getValue());
        KeywordSelection word = (KeywordSelection) htmlDataTable.getRowData();
        List data = htmlDataTable.getFilteredData();
        if(value == true){
            relevanceService.setUserBlacklisted(word.getWord());
        }else{
            relevanceService.setUserWithlisted(word.getWord());
        }
    }

    public DataTable getHtmlDataTable() {
        return htmlDataTable;
    }

    public void setHtmlDataTable(DataTable htmlDataTable) {
        this.htmlDataTable = htmlDataTable;
    }
    
    public SelectItem[] getJugedOption(){
        SelectItem[] options = new SelectItem[3];
        options[0] = new SelectItem("", "SELECT");
        options[1] = new SelectItem("SYSTEM", "SYSTEM");
        options[2] = new SelectItem("USER", "USER");
        return options; 
    }

    
    
}