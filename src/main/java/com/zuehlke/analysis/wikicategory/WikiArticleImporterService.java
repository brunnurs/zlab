/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiArticle;
import com.zuehlke.lab.entity.WikiCategory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

/**
 *
 * @author user
 */
@Stateless
public class WikiArticleImporterService {

    @Inject
    protected WikiArticleFacade articleFacade;
    public final static String preArticle = "<http://dbpedia.org/resource/";
    public final static String preCategory = "<http://dbpedia.org/resource/Category:";
    public final static char tokenizer = '\n';
    private Map<String, WikiArticle> articles = new HashMap<String, WikiArticle>();
    private InputStream dataInputStream;
    private long maxReadLines = 1000;
    private WikiArticle currentArticle = null;

    public void importAllArticles() throws IOException {


        long allReadChars = 0;
        long lastTranche = 0;
        try {

            while ((lastTranche = importATranche(100000, allReadChars)) != -1) {
                allReadChars += lastTranche;
                articles = new HashMap<String, WikiArticle>(); 
            }
        } finally {
            dataInputStream.close();
        }
    }

    /**
     * Import a tranche (specified by the tranchSize) and store all WikiArticles with their direct categories in the database.
     * Return the read chars, so we can skip them for the next tranche.
     * @param trancheSize
     * @param charsToSkip
     * @return
     * @throws IOException 
     */
    protected long importATranche(long trancheSize, long charsToSkip) throws IOException {

        BufferedReader input = new BufferedReader(new InputStreamReader(dataInputStream));
        input.skip(charsToSkip);

        String currentLine = "";
        long allReadChars = 0;
        int lineCount = 0;
        boolean breakOnNextPossibility = false;

        while ((currentLine = input.readLine()) != null && lineCount <= maxReadLines) {
            String[] articleCategory = getArticleCategoryByLine(currentLine);

            boolean handleArticleCategoryPairResult = false;

            if (articleCategory != null) {
                handleArticleCategoryPairResult = handleArticleCategoryPair(articleCategory);
            }


            if (lineCount % trancheSize == 0 && lineCount > 0) {
                breakOnNextPossibility = true;
            }
            //we wait until the handleArticleCategoryPair creates the next WikiArticle and finish then for the next tranche
            if (breakOnNextPossibility && handleArticleCategoryPairResult) {
                System.out.println("We finally stopped this tranche at line " + lineCount);
                return allReadChars;
            }

            allReadChars += currentLine.length();
            lineCount++;
        }

        //should only reach this code if we are on the end of the file. 
        return -1;

    }

//    private String readLine(InputStreamReader inputReader) throws IOException, Exception {
//        int currentChar = 0;
//        StringBuilder lineBuilder = new StringBuilder();
//
//        while ((currentChar = inputReader.read()) != -1 && (char) currentChar != tokenizer) {
//            lineBuilder.append((char) currentChar);
//        }
//
//        if (currentChar == tokenizer) {
//            return lineBuilder.toString();
//        } else if (currentChar == -1) {
//            return null;
//        } else {
//            throw new Exception("wtf?");
//        }
//    }
    /**
     * Handle a Article/Category-combination. If its necessary, created a new WikiArticle-Object. In this case, true will be returned.
     * @param articleCategory
     * @return was it necessary to create a new WikiArticle-Object?
     */
    private boolean handleArticleCategoryPair(String[] articleCategory) {

        boolean createdNew = false;
        if (currentArticle == null || !currentArticle.getTitle().equals(articleCategory[0])) {
            createdNew = true;
            currentArticle = new WikiArticle(articleCategory[0]);
            articleFacade.create(currentArticle);
            this.articles.put(currentArticle.getTitle(), currentArticle);
        }


        currentArticle.appendCategory(articleCategory[1]);

        return createdNew;
    }

//    private WikiCategory getCategoryIfExistOrNot(String category) {
//        if (allCategories.containsKey(category)) {
//            return allCategories.get(category);
//        } else {
//            WikiCategory newCat = new WikiCategory();
//            newCat.setDescription(category);
//            allCategories.put(category, newCat);
//            return newCat;
//        }
//    }
    private String[] getArticleCategoryByLine(String line) {
        try {
            String toReturn[] = new String[2];
            int startArticle = line.indexOf(preArticle) + preArticle.length();
            int endArticle = line.indexOf(">", startArticle);

            int startCategory = line.indexOf(preCategory) + preCategory.length();
            int endCategory = line.indexOf(">", startCategory);

            toReturn[0] = line.substring(startArticle, endArticle);
            toReturn[1] = line.substring(startCategory, endCategory);

            return toReturn;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public InputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setDataInputStream(InputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public long getMaxReadLines() {
        return maxReadLines;
    }

    public void setMaxReadLines(long maxReadLines) {
        this.maxReadLines = maxReadLines;
    }

    public void setArticleFacade(WikiArticleFacade articleFacade) {
        this.articleFacade = articleFacade;
    }
}
