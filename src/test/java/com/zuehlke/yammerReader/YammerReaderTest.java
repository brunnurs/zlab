/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.yammerReader;

import com.zuehlke.lab.service.importer.YammerImporterService;
import static junit.framework.Assert.assertEquals;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
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
public class YammerReaderTest {

    @Test
    @Ignore
    public void printAllMsg() throws IOException, JAXBException {
        OAuthService service = new ServiceBuilder().provider(YammerApi.class).apiKey("kvl0i1Cue7AvlBhE5VjEQ").apiSecret("X3ugswFmUVhFr38Usy5A4FtOWZg9vkQqEa8dPRhpHQ").build();
        Token accessToken = new Token("MNu9aicIIbT0g8pZ9VChkQ", "OmSz5PJpl8Qh7x7fEB6zc3bBd4hBS63ce41iRGThFxU");

        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.yammer.com/api/v1/users.json");

        service.signRequest(accessToken, request); // the access token from step 4
        Response response = request.send();
        
        System.out.println(response.getBody());
    }

    @Test
    public void simpleUnmarshalling() throws IOException, JSONException {
        JSONObject jSONObject = new JSONObject(IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("jsonUnmarshalling/json_user")));
        long expectedId = 5999174;
        assertEquals(expectedId, jSONObject.getLong("id"));
    }
    
    @Test
    public void simpleAarrayUnmarshalling() throws IOException, JSONException{
        JSONArray jSONArray = new JSONArray(IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("jsonUnmarshalling/json_users")));
        long expectedId1 = 5999174;
        assertEquals(expectedId1, jSONArray.getJSONObject(0).getLong("id"));
        assertEquals(50, jSONArray.length());
        long expectedId2 = 8785479;
        assertEquals(expectedId2, jSONArray.getJSONObject(49).getLong("id"));
    }
    
    @Test
    @Ignore
    public void testService(){
        YammerImporterService yammerImporterService = new YammerImporterService();
        List<Pair<String, Long>> ids = yammerImporterService.retrieveYammerIds();
        System.out.println(ids.get(0).getRight());
    }
}
