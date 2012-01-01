 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.zuehlke.lab.web.jsf.model.Cloud;
import javax.ejb.Stateless;

/**
 *
 * @author user
 */

@Path("cloudy")
@Stateless
public class CloudFascade{
    
    @EJB
    private BlacklistService blacklistService;
    @EJB
    private SynonymsServices synonymsService;
    
    @DELETE
    @Path("keyword/remove/")
    public void addToBlacklist(@QueryParam("word") String keyword) {
        blacklistService.addToBlackList(keyword);
    }
    
    @PUT
    @Path("keyword/synonym/")
    public void addSynonym(@QueryParam("word") String keyword, @QueryParam("synonym") String synonym) {
        synonymsService.addSynonym(keyword, synonym);
    }
    
    @PUT
    @Path("keyword/category")
    public void newCategory(@QueryParam("word") String keyword, @QueryParam("category") String synonym) {
        blacklistService.addToBlackList(keyword);
    }
    
    @GET
    @Path("cloud")
    public Cloud getCloud(){
        return null;
    }

//    @GET
//    @Path("{id}")
//    @Produces({"application/xml", "application/json"})
//    public Keyword find(@PathParam("id") Long id) {
//        return super.find(id);
//    }
//
//    @GETat
//    @Override
//    @Produces({"application/xml", "application/json"})
//    public List<Keyword> findAll() {
//        return super.findAll();
//    }
//
//    @GET
//    @Path("{from}/{to}")
//    @Produces({"application/xml", "application/json"})
//    public List<Keyword> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
//        return super.findRange(new int[]{from, to});
//    }
//
//    @GET
//    @Path("count")
//    @Produces("text/plain")
//    public String countREST() {
//        return String.valueOf(super.count());
//    }
//
//    @java.lang.Override
//    protected EntityManager getEntityManager() {
//        return em;
//    }
    
}
