package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ShortestPath {


    public static void main(String[] args)  {

        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Car car = objectMapper.readValue(json, Car.class);
            String color = car.getColor();
            System.out.println(color);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}