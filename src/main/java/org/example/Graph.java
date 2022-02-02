package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.*;

import java.util.*;

public class Graph {
    ArrayList<Node> nodelist;
    HashMap<String, Node> coordMap;

    Graph(Geojson map) {

        coordMap = new HashMap<String, Node>(); //to quickly map coordinates to node
        nodelist = new ArrayList<Node>();


        double[][] coordinates = map.features[0].geometry.coordinates;
        for (double[] coord : coordinates) {
            double lat = coord[0];
            double lang = coord[1];

            ArrayList<Node> outnodes = new ArrayList<Node>();
            Node node = new Node(lat, lang, outnodes);
            nodelist.add(node);
            String latlang = lat + "," + lang;
            coordMap.put(latlang, node);

        }

        for (int i = 1; i < map.features.length; i++) {
            Geojson featuresNew = map.features[i];
            Geojson geo = featuresNew.geometry;
            String type = geo.type;

            if (type.contentEquals("LineString")) {//add edges to outnodes
                double[][] vertices = geo.coordinates;
                //Iterate through LineString
                for (int j = 0; j < vertices.length - 1; j++) {
                    double[] vertex = vertices[j];
                    double lat = vertex[0];
                    double lang = vertex[1];

                    //check if startnode is already in Graph. If not, add it.
                    String key = lat + "," + lang;
                    if (!coordMap.containsKey(key)) {

                        ArrayList<Node> emptyOutnodes = new ArrayList<Node>();
                        Node nodeAdd = new Node(lat, lang, emptyOutnodes);
                        //add to nodelist
                        nodelist.add(nodeAdd);
                        //add coordinate-string - node  -pair to hashmap
                        coordMap.put(key, nodeAdd);

                    }
                    //check if outnode is already in the Graph. If not, add it.
                    double[] vertexNext = vertices[j + 1];
                    double latNext = vertexNext[0];
                    double langNext = vertexNext[1];

                    String keyNext = latNext + "," + langNext;
                    if (!coordMap.containsKey(keyNext)) {

                        ArrayList<Node> emptyOutnodesNext = new ArrayList<Node>();
                        Node nodeAddNext = new Node(latNext, langNext, emptyOutnodesNext);
                        //add to nodelist
                        nodelist.add(nodeAddNext);
                        //add coordinate-string - node  -pair to hashmap
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
    public Node nextNode(double lat, double lon){
        Node nearestNode = nodelist.get(0);
        for (int i =1; i< nodelist.size(); i++){
            Node currentNode = nodelist.get(i);
            double currentDistance = haversineDist(lat,lon,currentNode.lat, currentNode.lang);
            if (currentDistance < haversineDist(lat, lon, nearestNode.lat,nearestNode.lang)){
                nearestNode = currentNode;
            }
        }

        return nearestNode;
    }

    public static double euclDist(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
    }

    public static double haversineDist(double lat1, double lon1, double lat2, double lon2){
        final int radius = 6371; //earth radius
        double distLat = (lat1-lat2)*Math.PI/180;
        double distLon = (lon1-lon2)*Math.PI/180;
        double a = Math.sin(distLat/2)*Math.sin(distLat/2)+Math.cos(lat1*Math.PI/180)*Math.cos(lat2*Math.PI/180)*
                Math.sin(distLon/2)*Math.sin(distLon/2);
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = radius*c;
        return dist;
    }
    public ArrayList getPath(double originLat, double originLon, double destinationLat,
                             double destinationLon, String algorithm){

        double infinity = 9999999999.9;
        Node startNode = coordMap.get(Double.toString(originLat) + "," + Double.toString(originLon));
        Node endNode = coordMap.get(Double.toString(destinationLat) + "," + Double.toString(destinationLon));

        //create Hashmaps
        HashMap<Node, Double> distances = new HashMap<Node, Double>();
        HashMap<Node, Node> previous = new HashMap<>();
        HashMap<Node, Boolean> finished = new HashMap();

        //initialize Hashmaps
        for (Node node : nodelist) {
            if (node == startNode) {
                distances.put(node, 0.0000000);
            } else {
                distances.put(node, infinity);
            }
            previous.put(node, null);
            finished.put(node, false);
        }

        //priority queue for discovered nodes

        Comparator<Node> nodeComparator = new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                double addDistance1;
                double addDistance2;
                if (algorithm == "Dijkstra"){
                    addDistance1 = 0;
                    addDistance2 = 0;
                }else {
                    addDistance1 = haversineDist(n1.lat, n1.lang, destinationLat,destinationLon);
                    addDistance2 = haversineDist(n2.lat, n2.lang, destinationLat,destinationLon);}
                if ((distances.get(n1) +addDistance1) - (distances.get(n2)+addDistance2) > 0) {
                    return 1;
                } else if ((distances.get(n1) +addDistance1) - (distances.get(n2)+addDistance2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };

        PriorityQueue<Node> discoveredQue = new PriorityQueue<>(nodeComparator);
        ArrayList<Node> discovered = new ArrayList<>();
        discovered.add(startNode);

        while (finished.get(endNode) == false) {
            discovered.sort(nodeComparator);
            for (Node vertix : discovered){
                System.out.println("distance "+ distances.get(vertix));
            }
            System.out.println();
            discovered.sort(nodeComparator);
            Node currentNode = discovered.get(0);
            discovered.remove(0);
            System.out.println("currentNode " + currentNode);
            System.out.println("number of neighbor: "+currentNode.outnodes.size());
            for (Node neighbor : currentNode.outnodes) {
                System.out.println("Picking new neighbor " + neighbor);
                if (finished.get(neighbor) == false) {
                    double distance = haversineDist(currentNode.lat, currentNode.lang, neighbor.lat, neighbor.lang);
                    System.out.println("distance between current node and neighbor " + distance);
                    if (distances.get(currentNode) + distance < distances.get(neighbor)) {
                        //update distance, if new distance is shorter
                        System.out.println("Updating distance of neighbor");
                        System.out.println("Old distance " + distances.get(neighbor));
                        distances.put(neighbor, distances.get(currentNode) + distance);
                        //update previous
                        System.out.println("New distance " + distances.get(neighbor));
                        System.out.println("Updating route. Current previous node " + previous.get(neighbor));
                        previous.put(neighbor, currentNode);
                        System.out.println("Updating route. New previous node " + previous.get(neighbor));
                    }
                    if (!discovered.contains(neighbor)) {
                        discovered.add(neighbor);
                        System.out.println("adding neighbor to the queue");
                    }
                } else {
                    System.out.println("neighbor already has shortest path");
                }


            }
            System.out.println("Updating finished nodes from " + finished.get(currentNode));
            finished.put(currentNode, true);
            System.out.println("Updating finished nodes to " + finished.get(currentNode));
            }
            //create route list to return path.
            ArrayList<Node> routeList = new ArrayList<>();
            Node nodePath = endNode;
            routeList.add(nodePath);
            while (previous.get(nodePath) != null) {
                routeList.add(previous.get(nodePath));
                nodePath = routeList.get(routeList.size() - 1);

            }
            Collections.reverse(routeList);

            return routeList;
        }

        public ArrayList anyLocationDijkstra(double originLat, double originLon, double destinationLat,
                                             double destinationLon){
            Node start = nextNode(originLat,originLon);
            Node end = nextNode(destinationLat,destinationLon);
            ArrayList route = getPath(start.lat, start.lang, end.lat, end.lang, "Dijkstra");
            return route;
        }

        public ArrayList anyLocationAStar(double originLat, double originLon, double destinationLat,
                                             double destinationLon){
            Node start = nextNode(originLat,originLon);
            Node end = nextNode(destinationLat,destinationLon);
            ArrayList route = getPath(start.lat, start.lang, end.lat, end.lang, "AStar");
            return route;
        }



        public String toString() {
            String printString = "";
            for (Node entry : nodelist) {
                printString = printString + entry.toString();
            }
            return printString;
        }


    }






