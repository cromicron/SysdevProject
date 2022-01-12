package org.example;

import java.util.ArrayList;

public class Node {
    double lat;
    double lang;
    ArrayList<Node> outnodes;
    Node (double latitude, double longitude, ArrayList<Node> outnodesList){
        lat = latitude;
        lang = longitude;
        outnodes = outnodesList;
    }

    public String toString(){
        return "("+Double.toString(lat)+", "+Double.toString(lang)+")";
    }
}
