package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.json.JsonArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonPoJo {

    private String title;
    private String type;
    private JsonNode[] features;
    private JsonPoJo geometry;
    private ArrayList coordinates;
    private HashMap properties;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public JsonPoJo getGeometry() {
        return geometry;
    }

    public ArrayList getCoordinates() {
        return coordinates;
    }

    public HashMap getProperties() {
        return properties;
    }

    public JsonNode[] getFeatures() {
        return features;
    }
}
