/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.yammerReader;

import com.zuehlke.lab.entity.YammerUser;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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
    String achimId = "5999174";
    @Test
    @Ignore
    public void printAllMsg() throws IOException, JAXBException {
        OAuthService service = new ServiceBuilder().provider(YammerApi.class).apiKey("kvl0i1Cue7AvlBhE5VjEQ").apiSecret("X3ugswFmUVhFr38Usy5A4FtOWZg9vkQqEa8dPRhpHQ").build();
        Token accessToken = new Token("MNu9aicIIbT0g8pZ9VChkQ", "OmSz5PJpl8Qh7x7fEB6zc3bBd4hBS63ce41iRGThFxU");

        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.yammer.com/api/v1/users.json");

        service.signRequest(accessToken, request); // the access token from step 4
        Response response = request.send();
        
        JAXBContext context = JAXBContext.newInstance(YammerUser.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        System.out.println(response.getBody());
        List<YammerUser> yammerusers = (List<YammerUser>)unmarshaller.unmarshal(response.getStream());
        System.out.println(yammerusers.size());
    }
    
    @Test
    @Ignore
    public void simpleUnmarshalling(){
        try {
            JAXBContext context = JAXBContext.newInstance(YammerUser.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            YammerUser yammerUser = (YammerUser)unmarshaller.unmarshal(this.getClass().getClassLoader().getResourceAsStream("jsonUnmarshalling/json_user"));
        } catch (JAXBException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(YammerReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
