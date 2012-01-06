/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis;

import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author user
 */
public class POSTaggingServiceTest {
    
    @Test
    public void testEnglishPOSTagging() throws IOException{
        final String testTextPlain = "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. Mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group.";
        POSTaggingService posTaggingService = createPOSTaggingService();
        System.out.println(posTaggingService.tagEnglishText(testTextPlain));
    }
    
    @Test
    public void testGermanPOSTagging() throws IOException {
        final String testTextPlain = "Ein Ingenieurbüro für Wasserbau benötigt für eine schwimmende Autobrücke von europaweit einzigartigen Dimensionen ein Wellenalarmsystem. Das System erkennt mit einem eigens dafür entwickelten Algorithmus aufgrund von Druckunterschieden kritische Wellenhöhen und löst automatisch die Sperrung der Brücke aus. Zudem lässt sich über ein Webportal die aktuelle Wellenentwicklung als Grafik in Echtzeit verfolgen. Die Wellendaten werden persistiert, so dass kumulierte Auswertungen möglich sind. Die Alarmierung besteht aus einem verteilten System, wobei eine Messstation auf der Brücke die Wasserdruckdaten an einen entfernten Server weiterleitet, der die Daten schliesslich auswertet und speichert. Ebenfalls Aufgabe des Servers ist das Monitoring des Clients und die Alarmierung bei Ausfall des Systems. Aufgaben: Erstellen eines Konzeptes für das beschriebene Alarmsystem. Design und Implementierung des verteilten Systems. Mehrstufige Tests unter realitätsnahen Bedingungen. Teilprojektleitung und Koordination aller externen Partner. Technologien: C#.NET (ASP.NET, Windows Service, Windows-Form), Microsoft SQL Server 2008, .NET Entity Framework, Web Services (SOAP).";
         POSTaggingService posTaggingService = createPOSTaggingService();
        System.out.println(posTaggingService.tagGermanText(testTextPlain));
    }
    
    private POSTaggingService createPOSTaggingService() throws IOException{
        POSTaggingService posTaggingService = new POSTaggingService();
        posTaggingService.init();
        SentenceDetectorService sentenceDetectorService = new SentenceDetectorService();
        sentenceDetectorService.init();
        posTaggingService.setSentenceDetectorService(sentenceDetectorService);
        TokenizerService tokenizerService = new TokenizerService();
        tokenizerService.init();
        posTaggingService.setTokenizerService(tokenizerService);
        
        return posTaggingService;
    }
}
