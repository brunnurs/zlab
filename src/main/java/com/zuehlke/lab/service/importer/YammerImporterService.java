/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import com.zuehlke.analysis.DocumentAnalysisService;
import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.Person;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author user
 */
@Stateful
@LocalBean
public class YammerImporterService {

    @PersistenceContext(name = "cloudCompPU")
    EntityManager entityManager;
    @EJB
    DocumentAnalysisService documentAnalysisService;
    @Inject
    YammerRESTClient yammerRESTClient;
   

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void insertYammerIds() {
        List<Pair<String, Long>> yammerIds = retrieveYammerIds();
        List<Person> persons = entityManager.createNamedQuery("Person.findAll", Person.class).getResultList();
        Logger.getLogger(YammerImporterService.class.getName()).log(Level.INFO, "Retrieved " + yammerIds + " yammer ids.");
        for (Pair<String, Long> yammerId : yammerIds) {
            addYammerId(persons, yammerId);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void importYammerPosts() {
        List<Person> persons = entityManager.createNamedQuery("Person.findAll", Person.class).getResultList();
        for (Person person : persons) {
            if (person.getYammerId() != null && person.getYammerId() > 0) {
                importPosts(person);
            }
        }
    }

    private void importPosts(Person person) {
        try {
            JSONArray messages = yammerRESTClient.retrievePosts(person.getYammerId());
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                String text = message.getJSONObject("body").getString("plain");
                Document document = new Document();
                document.setRawData(text);
                document.setOwner(person);
                person.addDocument(document);
                documentAnalysisService.analyzeDocument(document);
            }
        } catch (JSONException ex) {
            Logger.getLogger(YammerImporterService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addYammerId(List<Person> persons, Pair<String, Long> yammerId) {
        String fullname = deAccent(yammerId.getLeft().toLowerCase());
        Long id = yammerId.getRight();

        boolean found = false;
        for (Person person : persons) {
            if (fullname.contains(deAccent(person.getFirstname().toLowerCase())) && fullname.contains(deAccent(person.getLastname().toLowerCase()))) {
                person.setYammerId(id);
                Logger.getLogger(YammerImporterService.class.getName()).log(Level.INFO, "Found match for: " + yammerId.getLeft());
                found = true;
                break;
            }
        }
        if (!found) {
            Logger.getLogger(YammerImporterService.class.getName()).log(Level.INFO, "Found NO match for: " + yammerId.getLeft());
        }

    }

    private List<Pair<String, Long>> retrieveYammerIds() {
        List<Pair<String, Long>> ids = new LinkedList<Pair<String, Long>>();
        try {
            boolean all = false;
            int page = 1;
            while (!all) {
                JSONArray jSONArray = yammerRESTClient.retrieveUsers(page);

                for (int i = 0; i < jSONArray.length(); i++) {
                    ids.add(new ImmutablePair<String, Long>(jSONArray.getJSONObject(i).getString("full_name"), jSONArray.getJSONObject(i).getLong("id")));
                }
                if (jSONArray.length() < 50) {
                    all = true;
                } else {
                    System.out.println(jSONArray.length() + " retrieved for page=" + page);
                    page++;
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(YammerImporterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

    private String deAccent(String input) {
        final StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '\u00E4': // ä
                    output.append("ae");
                    break;
                case '\u00E7': // ç
                    output.append("c");
                    break;
                case '\u00E8': // è
                case '\u00E9': // é
                case '\u00EA': // ê
                case '\u00EB': // ë
                    output.append("e");
                    break;
                case '\u00EC': // ì
                case '\u00ED': // í
                case '\u00EE': // î
                case '\u00EF': // ï
                    output.append("i");
                    break;
                case '\u00F1': // ñ
                    output.append("n");
                    break;
                case '\u00F2': // ò
                case '\u00F3': // ó
                case '\u00F4': // ô
                case '\u00F5': // õ
                    output.append("o");
                    break;
                case '\u00F6': // ö
                    output.append("oe");
                    break;
                case '\u00DF': // ß
                    output.append("ss");
                    break;
                case '\u00F9': // ù
                case '\u00FA': // ú
                case '\u00FB': // û
                    output.append("u");
                    break;
                case '\u00FC': // ü
                    output.append("ue");
                    break;
                case '\u00FD': // ý
                case '\u00FF': // ÿ
                    output.append("y");
                    break;
                default:
                    output.append(input.charAt(i));
                    break;
            }
        }
        return output.toString();
    }
}
