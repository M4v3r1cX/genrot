package com.bsodsoftware.genrot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class main {

    public static String randomWordApi = "https://random-word-api.herokuapp.com/word?number=1";
    public static String suffixFile = "path-to-file";
    public static String blacklistFile = "path-to-file";

    public static void main(String[] args) {
        List<String> suffixes = readFile(suffixFile);
        List<String> blacklist = readFile(blacklistFile);
        String prefix = "";
        boolean isValidWord = false;

        while (!isValidWord) {
            prefix = getWord();
            if (blacklist.isEmpty()) {
                isValidWord = true;
            } else {
                if (!blacklist.contains(prefix)) {
                    isValidWord = true;
                }
            }
        }
        Random rand = new Random();
        String suffix = suffixes.get(rand.nextInt(suffixes.size()));
        System.out.println(prefix + suffix);
    }

    public static String getWord() {
        String ret = "";

        System.out.println("Trying to get a random word...");
        System.out.println("");

        try {
            URL url = new URL(randomWordApi);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            String preword = content.toString();
            preword = preword.replaceAll("[^a-zA-Z0-9]", "");
            if (preword.endsWith("s")) {
                preword = preword.substring(0, preword.length() - 1);
            }
            ret = preword;
        } catch (Exception ex) {
            System.out.println("Error trying to get random word");
            ex.printStackTrace();
        }

        return ret;
    }

    public static List<String> readFile(String filePath) {
        List<String> ret = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                ret.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + filePath + "not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static boolean contains(List<String> list, String str) {
        boolean ret = false;

        for (String s : list) {
            if (s.toLowerCase().equals(str.toLowerCase())) {
                ret = true;
                break;
            }
        }

        return ret;
    }
}
