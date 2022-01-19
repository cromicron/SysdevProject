package org.example;
//class to parse reponse of post request into.

import java.util.ArrayList;
import java.util.HashMap;

public class POJOPost {
    public ArrayList<POJOPost> routes;
    public POJOPost summary;
    public double distance;
    public double duration;
    public ArrayList segments;
    public ArrayList steps;
    public String instruction;
    public String name;
    public int type;
    public int[] way_points;

}
