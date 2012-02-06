/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import com.zuehlke.analysis.LexiconImpl;
import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.Person;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.YammerApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author user
 */
@Stateful
@LocalBean
public class YammerRESTClient {
    private final long WEBSERVICE_TIMEOUT = 30000;
    OAuthService service;
    Token accessToken;

    @PostConstruct
    protected void init(){
        service = new ServiceBuilder().provider(YammerApi.class).apiKey("kvl0i1Cue7AvlBhE5VjEQ").apiSecret("X3ugswFmUVhFr38Usy5A4FtOWZg9vkQqEa8dPRhpHQ").build();
        accessToken = new Token("MNu9aicIIbT0g8pZ9VChkQ", "OmSz5PJpl8Qh7x7fEB6zc3bBd4hBS63ce41iRGThFxU");
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void insertYammerIds() {
        List<Pair<String, Long>> yammerIds = retrieveYammerIds();
        List<Person> persons = entityManager.createNamedQuery("Person.findAll", Person.class).getResultList();
        Logger.getLogger(YammerRESTClient.class.getName()).log(Level.INFO, "Retrieved " + yammerIds + " yammer ids.");
        for (Pair<String, Long> yammerId : yammerIds) {
            addYammerId(persons, yammerId);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void importYammerPosts() {
        List<Person> persons = entityManager.createNamedQuery("Person.findAll", Person.class).getResultList();
        for (Person person : persons) {
            if (person.getYammerId() != null && person.getYammerId() > 0) {
                try {
                    Thread.sleep(WEBSERVICE_TIMEOUT);
                } catch (InterruptedException ex) {
                    Logger.getLogger(YammerRESTClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                importPosts(person);
            }
        }
    }

    private void importPosts(Person person) {
        try {
            JSONArray messages = retrievePosts(person.getYammerId());
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                String text = message.getJSONObject("body").getString("plain");
                Document document = new Document();
                document.setRawData(text);
                document.setOwner(person);
                person.addDocument(document);
                documentAnalysisService.analyseDocument(document);
            }
        } catch (JSONException ex) {
            Logger.getLogger(YammerRESTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addYammerId(List<Person> persons, Pair<String, Long> yammerId) {
        String fullname = deAccent(yammerId.getLeft().toLowerCase());
        Long id = yammerId.getRight();

        boolean found = false;
        for (Person person : persons) {
            if (fullname.contains(deAccent(person.getFirstname().toLowerCase())) && fullname.contains(deAccent(person.getLastname().toLowerCase()))) {
                person.setYammerId(id);
                Logger.getLogger(YammerRESTClient.class.getName()).log(Level.INFO, "Found match for: " + yammerId.getLeft());
                found = true;
                break;
            }
        }
        if (!found) {
            Logger.getLogger(YammerRESTClient.class.getName()).log(Level.INFO, "Found NO match for: " + yammerId.getLeft());
        }

    }

    private List<Pair<String, Long>> retrieveYammerIds() {
        List<Pair<String, Long>> ids = new LinkedList<Pair<String, Long>>();
        try {
            boolean all = false;
            int page = 1;
            while (!all) {
                JSONArray jSONArray = retrieveUsers(page);

                for (int i = 0; i < jSONArray.length(); i++) {
                    ids.add(new ImmutablePair<String, Long>(jSONArray.getJSONObject(i).getString("full_name"), jSONArray.getJSONObject(i).getLong("id")));
                }
                if (jSONArray.length() < 50) {
                    all = true;
                } else {
                    System.out.println(jSONArray.length() + " retrieved for page=" + page);
                    page++;
                }
                if (!all) {
                    try {
                        Thread.sleep(WEBSERVICE_TIMEOUT);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(YammerRESTClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(YammerRESTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

    private JSONArray retrievePosts(long userId) throws JSONException {
        String requestString = "https://www.yammer.com/api/v1/messages/from_user/" + userId + ".json";
        OAuthRequest request = new OAuthRequest(Verb.GET, requestString);
        service.signRequest(accessToken, request);
        Response response = request.send();
        JSONObject jSONObject = new JSONObject(response.getBody());
        return jSONObject.getJSONArray("messages");
    }

    private JSONArray retrieveUsers(int page) throws JSONException {
        String pageParam = "?page=" + String.valueOf(page);
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.yammer.com/api/v1/users.json" + pageParam);
        service.signRequest(accessToken, request);
        Response response = request.send();
        return new JSONArray(response.getBody());
    }

    public final static String deAccent(String input) {
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
