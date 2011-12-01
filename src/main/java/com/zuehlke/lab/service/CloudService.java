/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

import com.zuehlke.lab.service.util.BeanResult;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.zuehlke.lab.web.jsf.model.Cloud;
import com.zuehlke.lab.entity.Event;
import com.zuehlke.lab.entity.view.ExtendedReadOnlyKeyword;
import com.zuehlke.lab.entity.Keyword;
import com.zuehlke.lab.entity.Person;
import com.zuehlke.lab.entity.view.ReadOnlyKeyword;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class CloudService {
    
    
    private static String ALL_SQL = "Select k.word, k.count FROM AGG_KEYWORD as k";
    private static String PERSON_SQL = "Select k.word, k.count FROM PERSON_AGG_KEYWORD as k WHERE k.person_id = ?1";
    private static String COURSE_SQL = "Select k.word, k.count FROM EVENT_AGG_KEYWORD as k WHERE k.course_id = ?1";
    
    
    @PersistenceContext(unitName="cloudCompPU")
    EntityManager em;
    
    
    public Cloud getRecursiveCloud(List<SearchAttribute> parameters){
        Cloud cloud = new Cloud("Company",0,null);
        addRecursiveCloud(cloud, parameters,new ArrayList<SearchAttribute>(), new ArrayList<Object>() ,0);
        return cloud;
    }
    
    private void addRecursiveCloud(Cloud cloud, List<SearchAttribute> parameters, List<SearchAttribute> actParameters ,List<Object> values, int pos){
        
        List<ExtendedReadOnlyKeyword> keys = getKeywords(actParameters, values, parameters.get(pos));
        List<Cloud> innnerClouds = getClouds((List)keys);
        if(pos < parameters.size()-1){
            int i = 0;
            for(Cloud element : innnerClouds){
                values.add(keys.get(i).getKey());
                actParameters.add(parameters.get(pos));
                    addRecursiveCloud(element, parameters, actParameters, values, pos+1);
                values.remove(pos);
                actParameters.remove(pos);
                i++;
            }
        }
        cloud.setElements(innnerClouds);
        
    }
    
    public Cloud getCloud(List<SearchAttribute> parameters, List<Object> values, SearchAttribute select){
        return new Cloud("company",0,getClouds(parameters, values, select));
    }
    
    private List<ExtendedReadOnlyKeyword> getKeywords(List<SearchAttribute> parameters, List<Object> values, SearchAttribute select){
        String sql = "SELECT k."+select.getSearchColumn()+" as id, k."+select.getSelectColumn()+" as word, SUM(k.count) as count FROM UNIT_SELECT_PERSON_AGG_KEYWORD as k ";
        if(parameters != null && parameters.size() > 0){
            sql += "WHERE ";
            int i = 0;
            for(SearchAttribute as : parameters){
              sql += "k."+as.getSearchColumn()+" = ";
              
              if(values.get(i) instanceof String)sql += "'";
                      sql += String.valueOf(values.get(i));
              if(values.get(i) instanceof String)sql += "'";
              if(i < parameters.size()-1) sql += "AND ";
              i++;
            }
         }
        sql += " GROUP BY k."+select.getSelectColumn()+", k."+select.getSearchColumn();
        
       System.out.print(sql);
       Query q = em.createNativeQuery(sql);
       BeanResult.setQueryResultClass(q, ExtendedReadOnlyKeyword.class);
       return q.getResultList();
    }
    
    
    private List<Cloud> getClouds(List<SearchAttribute> parameters, List<Object> values, SearchAttribute select){
       List result = getKeywords(parameters, values, select); 
       return getClouds(result);
    }
    
    
    public Cloud getCloud(){
       Query q = em.createNativeQuery(ALL_SQL);
       BeanResult.setQueryResultClass(q, ReadOnlyKeyword.class); 
       return getCloud("company",q.getResultList());
    }
    
    
    public Cloud getCloud(Event c){
       Query q = em.createNativeQuery(COURSE_SQL);
       q.setParameter(1, c.getId());
       BeanResult.setQueryResultClass(q, ReadOnlyKeyword.class); 
       return getCloud(c.getName(),q.getResultList());
    }
    
    
    public Cloud getCloud(Person p){
       Query q = em.createNativeQuery(PERSON_SQL);
       q.setParameter(1, p.getId());
       BeanResult.setQueryResultClass(q, ReadOnlyKeyword.class); 
       return getCloud(p.getFirstname()+" "+p.getLastname(),q.getResultList());
    }
    
    private Cloud getCloud(String rootLable, List<Keyword> keywords){
        return new Cloud(rootLable,0,getClouds(keywords));
    }
    
    private List<Cloud> getClouds(List<Keyword> keywords){
        List<Cloud> clouds = new ArrayList<Cloud>();
        double max_count = 0;
        for(Keyword k : keywords){
            clouds.add(new Cloud(k.getWord(),k.getCount(),null));
            if(k.getCount() > max_count) max_count = k.getCount();
        }
        double max_factor = max_count / Cloud.MAX_WEIGHT;
        for(Cloud c : clouds){
            c.setWeight((int)(c.getWeight()/max_factor));
        }
        return clouds;
    }
}
