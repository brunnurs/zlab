//
// $Id: LexiconUtil.java,v 1.4 2006/03/07 17:49:00 perera Exp $
//
// Durm German Lemmatization System
// http://www.ipd.uka.de/~durm/tm/lemma/
//
//
// The Utility class to load and update the lexicon.
//
//
// (c) 2005--2006 University of Karlsruhe, IPD,
//                Praharshana Perera
//
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
package com.zuehlke.analysis;

import java.io.*;
import java.util.*;
import java.util.GregorianCalendar;

public class LexiconUtil {

    public static String lexPath;

    public static void setLexPath(String p) {

        lexPath = p;

        System.out.println("lexicon is loaded from:  " + lexPath);
    }

    public static void loadEntries(HashMap entries, HashMap stemEntries, HashMap stemms) throws java.io.IOException {
        System.out.println(lexPath);
        BufferedReader dicBufReader = new BufferedReader(new InputStreamReader(LexiconUtil.class.getClassLoader().getResourceAsStream(lexPath)));

        String entry;

        while (!((entry = dicBufReader.readLine()) == null)) {
            if ((new StringTokenizer(entry)).countTokens() == 12) {
                LexiconEntry lentry = getValues(entry);
                entries.put(lentry.noun, lentry);
                stemEntries.put(lentry.noun, lentry.lemma);
                loadWithStem(stemms, lentry.lemma, lentry.noun);
            }


        }

        dicBufReader.close();
    }

    private static void writeFile(int number) throws java.io.IOException {
        GregorianCalendar gc = new GregorianCalendar();
        String currentdate = gc.getTime().toString();
        RandomAccessFile datefile = new RandomAccessFile("/home/perera/Dictionary/Test/diclogtime.txt", "rw");
        long filepointer = datefile.length();
        datefile.seek(filepointer);
        datefile.writeBytes(currentdate);
        datefile.writeBytes("\t");
        datefile.writeBytes((new Integer(number)).toString());
        datefile.writeBytes("\r\n");
        datefile.close();


    }

    private static void loadWithStem(HashMap stemms, String stem, String word) {
        StringTokenizer st = new StringTokenizer(stem, ".");
        if (st.countTokens() > 1) {
            while (st.hasMoreTokens()) {
                String tempstem = st.nextToken().trim();
                if (stemms.containsKey(tempstem)) {
                    String current = stemms.get(tempstem).toString();
                    current += "." + word;
                    stemms.put(tempstem, current);
                } else {
                    stemms.put(tempstem, word);
                }

            }
        }
    }

    public static LexiconEntry getValues(String entry) {
        StringTokenizer stk = new StringTokenizer(entry);
        LexiconEntry lentry = new LexiconEntry();
        lentry.noun = stk.nextToken();
        lentry.number = stk.nextToken();
        lentry.gender = stk.nextToken();
        lentry.gcase = stk.nextToken();
        lentry.lemma = stk.nextToken();
        lentry.freq = stk.nextToken();
        String addedDate = stk.nextToken();
        String addedHour = stk.nextToken();
        String currDate = stk.nextToken();
        String currHour = stk.nextToken();
        lentry.insertedOn = addedDate + " " + addedHour;
        lentry.lastModified = currDate + " " + currHour;
        lentry.pointer = stk.nextToken();
        lentry.locked = stk.nextToken();

        return lentry;
    }

    public static void writeEntries(HashMap entries) throws java.io.IOException {
        FileWriter dicWriter = new FileWriter(lexPath);
        BufferedWriter dicBufWriter = new BufferedWriter(dicWriter);

        Set stemms = entries.keySet();
        ArrayList stemmList = sort(stemms);
        for (int i = 0; i < stemmList.size(); i++) {
            String head = stemmList.get(i).toString().trim();
            LexiconEntry lentry = (LexiconEntry) entries.get(head);
            lentry.pointer = (new Integer(i)).toString();
            String writeEntry = lentry.toString();
            dicBufWriter.write(writeEntry);
            dicBufWriter.newLine();

        }

        dicBufWriter.flush();

        dicBufWriter.close();
        dicWriter.close();

        //writeToFiles(stemmList, filepointers, filename);
        writeFile(entries.size());
    }

    private static void writeToFiles(ArrayList wordlist, HashMap pointers, String filename) throws java.io.IOException {
        FileReader freader = new FileReader("/home/perera/Dictionary/Test/lexfiles.txt");
        BufferedReader bufreader = new BufferedReader(freader);

        String str;
        HashMap hm = new HashMap();
        boolean flag = true;
        String word = "";
        String files = "";

        while (!((str = bufreader.readLine()) == null)) {
            if (flag) {
                word = str.trim();
                flag = false;
            } else {
                if (str.equals(".")) {
                    flag = true;
                    hm.put(word, files);
                    files = " ";
                } else {
                    files += str + " ";
                }
            }

        }

        bufreader.close();
        freader.close();

        FileWriter fwriter = new FileWriter("/home/perera/Dictionary/Test/lexfiles.txt");
        BufferedWriter bufwriter = new BufferedWriter(fwriter);


        for (int i = 0; i < wordlist.size(); i++) {
            String check = pointers.get(wordlist.get(i).toString()).toString();
            //System.out.println(check);
            if (hm.containsKey(check)) {
                String filenames = hm.get(check).toString();
                filenames += " " + filename;
                StringTokenizer stk = new StringTokenizer(filenames);
                bufwriter.write((new Integer(i)).toString());
                bufwriter.newLine();
                while (stk.hasMoreTokens()) {
                    bufwriter.write(stk.nextToken());
                    bufwriter.newLine();
                }
                bufwriter.write(".");
                bufwriter.newLine();

            } else {
                bufwriter.write((new Integer(i)).toString());
                bufwriter.newLine();
                bufwriter.write(filename);
                bufwriter.newLine();
                bufwriter.write(".");
                bufwriter.newLine();
            }

        }

        bufwriter.flush();
        bufwriter.close();
        fwriter.close();



    }

    private static ArrayList sort(Set setvals) {
        Object[] elements = setvals.toArray();
        Arrays.sort(elements);
        return new ArrayList(Arrays.asList(elements));

    }
}
