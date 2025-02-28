package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private static final String API_URL = "https://storage.googleapis.com/maoz-event/rawdata.txt";

    public static void main(String[] args) {
        try {
            String rawData = fetchRawData();
            processAndDisplayData(new JSONObject(rawData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchRawData() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().reduce("", (acc, line) -> acc + line);
        }
    }

    private static void processAndDisplayData(JSONObject jsonObject) {
        JSONArray nodesArray = jsonObject.getJSONArray("nodes");
        JSONArray edgesArray = jsonObject.getJSONArray("edges");

        List<String> nodes = new ArrayList<>();
        List<String> addressIn = new ArrayList<>();
        List<String> addressOut = new ArrayList<>();

        for (int i = 0; i < nodesArray.length(); i++) {
            nodes.add(nodesArray.getJSONObject(i).getString("type"));
        }

        for (int i = 0; i < edgesArray.length(); i++) {
            JSONObject edge = edgesArray.getJSONObject(i);
            addressIn.add(edge.getString("source"));
            addressOut.add(edge.getString("target"));
        }

        System.out.println("[Nodes = '" + String.join("', '", nodes) + "']\n");
        System.out.println("[addressIn = '" + String.join("', '", addressIn) + "']\n");
        System.out.println("[addressOut = '" + String.join("', '", addressOut) + "']\n");
    }
}