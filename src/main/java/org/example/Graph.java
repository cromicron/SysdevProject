package org.example;

import jakarta.json.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph extends HashMap {

    public Graph () {}

    public void connectNodes(ArrayList route){
        for (int j = 0; j < route.size() -1; j++ ){
            ArrayList startNode = (ArrayList) route.get(j);
            ArrayList endNode = (ArrayList) route.get(j + 1);
            System.out.println(startNode);
            this.put(startNode, endNode);
        }


        }
    }

