/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.analysis.wikicategory;

import com.zuehlke.lab.entity.WikiCategory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class WikiReader {

    public final static String preArticle = "<http://dbpedia.org/resource/";
    public final static String preCategory = "<http://dbpedia.org/resource/Category:";
    public final static char tokenizer = '\n';
    private Map<String, WikiCategory> allCategories = new HashMap<String, WikiCategory>();
    private Map<String, List<WikiCategory>> articleCategories = new HashMap<String, List<WikiCategory>>();
    private InputStream dataInputStream;
    private long maxReadLines = 1000;

    public void readArticleFileSequential() {
        try {

            //BufferedReader input = new BufferedReader(new InputStreamReader(dataInputStream));
            InputStreamReader input = new InputStreamReader(dataInputStream);
            try {
                int lineCount = 0;
                String currentLine = "";

                while ((currentLine = readLine(input)) != null && lineCount <= maxReadLines) {
                    String[] articleCategory = getArticleCategoryByLine(currentLine);
                    insertArticleCategory(articleCategory);
                    lineCount++;
                }

            } finally {
                input.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void readArticleFileBufferd() {
        String currentLine = "";
        int lineCount = 0;
        try {
            long allReadChars = 0;

            BufferedReader input = new BufferedReader(new InputStreamReader(dataInputStream));
            try {

                while ((currentLine = input.readLine()) != null && lineCount <= maxReadLines) {
                    String[] articleCategory = getArticleCategoryByLine(currentLine);
                    if (articleCategory != null) {
                        insertArticleCategory(articleCategory);
                    }
                    allReadChars += currentLine.length();

                    lineCount++;

                    if (lineCount % 100000 == 0 && lineCount > 0) {
                        allCategories = new HashMap<String, WikiCategory>();
                        articleCategories = new HashMap<String, List<WikiCategory>>();
                        System.out.println("Current Line Counter:" + lineCount);
//                        input = new BufferedReader(new InputStreamReader(dataInputStream));
//                        long acctualySkipped = input.skip(allReadChars);
//                        System.out.println("try to skip: " + allReadChars + ". Actually skipped: " + acctualySkipped);
                    }
                }
            } finally {
                input.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String readLine(InputStreamReader inputReader) throws IOException, Exception {
        int currentChar = 0;
        StringBuilder lineBuilder = new StringBuilder();

        while ((currentChar = inputReader.read()) != -1 && (char) currentChar != tokenizer) {
            lineBuilder.append((char) currentChar);
        }

        if (currentChar == tokenizer) {
            return lineBuilder.toString();
        } else if (currentChar == -1) {
            return null;
        } else {
            throw new Exception("wtf?");
        }
    }

    private void insertArticleCategory(String[] articleCategory) {
        if (!articleCategories.containsKey(articleCategory[0])) {
            articleCategories.put(articleCategory[0], new LinkedList<WikiCategory>());
        }
        List<WikiCategory> categoriesToThisArticle = articleCategories.get(articleCategory[0]);
        categoriesToThisArticle.add(getCategoryIfExistOrNot(articleCategory[1]));

    }

    private WikiCategory getCategoryIfExistOrNot(String category) {
        if (allCategories.containsKey(category)) {
            return allCategories.get(category);
        } else {
            WikiCategory newCat = new WikiCategory();
            newCat.setDescription(category);
            allCategories.put(category, newCat);
            return newCat;
        }
    }

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

    public Map<String, WikiCategory> getAllCategories() {
        return allCategories;
    }

    public Map<String, List<WikiCategory>> getArticleCategories() {
        return articleCategories;
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
}
