package com.zuehlke.lab.zlab.web;

import com.zuehlke.lab.zlab.persistance.entities.Image;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
@RequestScoped
@Named
public class AchievmentDetailController {

    @Inject
    Image testImage;
    
    String name = "hubert";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PostConstruct
    public void readGETParamAndLoadAchievment() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
}
