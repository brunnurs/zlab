/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
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
@Stateless
@LocalBean
public class YammerImporterService {

    OAuthService service = new ServiceBuilder().provider(YammerApi.class).apiKey("kvl0i1Cue7AvlBhE5VjEQ").apiSecret("X3ugswFmUVhFr38Usy5A4FtOWZg9vkQqEa8dPRhpHQ").build();
    Token accessToken = new Token("MNu9aicIIbT0g8pZ9VChkQ", "OmSz5PJpl8Qh7x7fEB6zc3bBd4hBS63ce41iRGThFxU");

    public void importYammerIds() {
    }

    public List<Pair<String, Long>> retrieveYammerIds() {
        List<Pair<String, Long>> ids = new LinkedList<Pair<String, Long>>();
        try {
            JSONArray jSONArray = retrieveUsers(1);
            for(int i = 0; i < jSONArray.length(); i++){
                ids.add(new ImmutablePair<String, Long>(jSONArray.getJSONObject(i).getString("full_name"),jSONArray.getJSONObject(i).getLong("id")));
            }
        } catch (JSONException ex) {
            Logger.getLogger(YammerImporterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

    private JSONArray retrieveUsers(int page) throws JSONException {
        String pageParam = "?page=" + String.valueOf(page);
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.yammer.com/api/v1/users.json" + pageParam);
        service.signRequest(accessToken, request);
        Response response = request.send();
        return new JSONArray(response.getBody());
    }
}
