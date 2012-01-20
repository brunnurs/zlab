/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.zuehlke.lab.entity.YammerUser;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author user
 */
@Stateless
@Path("json")
public class YammerUserFacadeREST extends AbstractFacade<YammerUser> {
    @PersistenceContext(unitName = "cloudCompPU")
    private EntityManager em;

    public YammerUserFacadeREST() {
        super(YammerUser.class);
    }


    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public YammerUser find(@PathParam("id") Long id) {
        YammerUser yammerUser = new YammerUser();
        yammerUser.setName("Achim gerhard");
        yammerUser.setDepartment("JAP");
        yammerUser.setJobTitle("Software Engineer");
        yammerUser.setYammerId("123");
        return yammerUser;
    }

    @Override
    protected EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
