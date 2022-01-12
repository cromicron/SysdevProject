package org.example;

import jakarta.json.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
    ArrayList<Node> nodelist;
    HashMap<String, Node> coordMap;

    Graph(JsonObject map) {
        //give a Json object and generate a graph. A graph consists of nodes and vertices. The vertices are represented
        //
        coordMap = new HashMap<String, Node>(); //to quickly map coordinates to node
        nodelist = new ArrayList<Node>();
        JsonArray features = map.getJsonArray("features");
        JsonObject ob = features.getJsonObject(0);
        JsonObject geometry = ob.getJsonObject("geometry");
        JsonArray coordinates = geometry.getJsonArray("coordinates");
        for (JsonValue coord : coordinates) {
            JsonNumber latJson = coord.asJsonArray().getJsonNumber(0);
            JsonNumber langJson = coord.asJsonArray().getJsonNumber(1);
            double lat = latJson.doubleValue();
            double lang = langJson.doubleValue();
            ArrayList<Node> outnodes = new ArrayList<Node>();
            Node node = new Node(lat, lang, outnodes);
            nodelist.add(node);
            String latlang = Double.toString(lat) + "," + Double.toString(lang);
            coordMap.put(latlang, node);
        }


        for (int i = 1; i < features.size(); i++) {
            JsonObject featuresNew = features.getJsonObject(i);
            JsonObject geo = featuresNew.getJsonObject("geometry");
            String type = geo.getString("type");

            if (type.contentEquals("LineString")) {//add edges to outnodes
                JsonArray vertices = geo.getJsonArray("coordinates");
                //Iterate through LineString
                for (int j = 0; j < vertices.size()-1; j++) {
                    JsonValue vertex = vertices.get(j);
                    JsonNumber latJson = vertex.asJsonArray().getJsonNumber(0);
                    JsonNumber langJson = vertex.asJsonArray().getJsonNumber(1);
                    double lat = latJson.doubleValue();
                    double lang = langJson.doubleValue();
                    //check if startnode is already in Graph. If not, add it.
                    String key = Double.toString(lat) + "," + Double.toString(lang);
                    if (!coordMap.containsKey(key)) {
                        ArrayList<Node> emptyOutnodes = new ArrayList<Node>();
                        Node nodeAdd = new Node(lat, lang, emptyOutnodes);
                        coordMap.put(key, nodeAdd);
                    }
                    //check if outnode is already in the Graph. If not, add it.
                    JsonValue vertexNext = vertices.get(j+1);
                    JsonNumber latJsonNext = vertexNext.asJsonArray().getJsonNumber(0);
                    JsonNumber langJsonNext = vertexNext.asJsonArray().getJsonNumber(1);
                    double latNext = latJsonNext.doubleValue();
                    double langNext = langJsonNext.doubleValue();
                    String keyNext = Double.toString(latNext) + "," + Double.toString(langNext);
                    if (!coordMap.containsKey(keyNext)) {
                        ArrayList<Node> emptyOutnodesNext = new ArrayList<Node>();
                        Node nodeAddNext = new Node(latNext, langNext, emptyOutnodesNext);
                        coordMap.put(keyNext, nodeAddNext);
                    }
                    //add outnode-pointer into the outnode list
                    Node startNode = coordMap.get(key);
                    Node endNode = coordMap.get(keyNext);
                    startNode.outnodes.add(endNode);
                }
            }
        }
    }
        public String toString() {
            String printString = "";
            for (Node entry : nodelist) {
                printString = printString + entry.toString();
            }
            return printString;
        }


    }






