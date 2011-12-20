/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import org.junit.Test;

/**
 *
 * @author user
 */
public class POSTaggingServiceTest {
    
    @Test
    public void testEnglishPOSTagging(){
        final String testTextPlain = "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. Mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group.";
        POSTaggingService posTaggingService = new POSTaggingService();
        posTaggingService.setSentenceDetectorService(new SentenceDetectorService());
        posTaggingService.setTokenizerService(new TokenizerService());
        System.out.println(posTaggingService.tagEnglishText(testTextPlain));
                
    }
    
    @Test
    public void testGermanPOSTagging(){
        final String testTextPlain = "";
        POSTaggingService pOSTaggingService = new POSTaggingService();
        pOSTaggingService.setSentenceDetectorService(new SentenceDetectorService());
        pOSTaggingService.setTokenizerService(new TokenizerService());
        System.out.println(pOSTaggingService.tagGermanText(testTextPlain));
    }
}
