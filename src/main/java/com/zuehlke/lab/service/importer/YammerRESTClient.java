/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import org.apache.commons.lang3.StringUtils;
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
    private final String apiKey = "kvl0i1Cue7AvlBhE5VjEQ";
    private final String apiSecret = "X3ugswFmUVhFr38Usy5A4FtOWZg9vkQqEa8dPRhpHQ";
    private final String tokenKey = "MNu9aicIIbT0g8pZ9VChkQ";
    private final String tokenSecret = "OmSz5PJpl8Qh7x7fEB6zc3bBd4hBS63ce41iRGThFxU";
    OAuthService service;
    Token accessToken;

    @PostConstruct
    protected void init() {
        service = new ServiceBuilder().provider(YammerApi.class).apiKey(apiKey).apiSecret(apiSecret).build();
        accessToken = new Token(tokenKey, tokenSecret);
    }

    public JSONArray retrievePostsByUser(long userId) throws JSONException {
        try {
            Thread.sleep(WEBSERVICE_TIMEOUT);
        } catch (InterruptedException ex) {
            Logger.getLogger(YammerRESTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        String requestString = "https://www.yammer.com/api/v1/messages/from_user/" + userId + ".json";
        OAuthRequest request = new OAuthRequest(Verb.GET, requestString);
        service.signRequest(accessToken, request);
        Response response = request.send();
        JSONObject jSONObject = new JSONObject(response.getBody());
        return jSONObject.getJSONArray("messages");
    }

    public JSONArray retrieveUsers(int page) throws JSONException {
        try {
            Thread.sleep(WEBSERVICE_TIMEOUT);
        } catch (InterruptedException ex) {
            Logger.getLogger(YammerRESTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        String pageParam = "?page=" + String.valueOf(page);
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.yammer.com/api/v1/users.json" + pageParam);
        service.signRequest(accessToken, request);
        Response response = request.send();
        return new JSONArray(response.getBody());
    }

    public JSONArray retrievePostsByNetwork(int numOfPosts) throws JSONException {
        JSONArray posts = retrievePosts("");
        while(posts.length() < numOfPosts){
            posts.put(retrievePosts(posts.getJSONObject(posts.length()-1).getString("id")));
        }
        return posts;
    }

    private JSONArray retrievePosts(String olderThan) throws JSONException {
        try {
            Thread.sleep(WEBSERVICE_TIMEOUT);
        } catch (InterruptedException ex) {
            Logger.getLogger(YammerRESTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        String  olderThanParam = StringUtils.isEmpty(olderThan) ? "" : "older_than=" + olderThan;
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.yammer.com/api/v1/messages.json "+ olderThanParam);
        service.signRequest(accessToken, request);
        Response response = request.send();
        JSONObject jSONObject = new JSONObject(response.getBody());
        return jSONObject.getJSONArray("messages");
    }
}
