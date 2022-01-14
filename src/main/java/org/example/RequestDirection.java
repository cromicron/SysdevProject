package org.example;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

import java.io.StringReader;

public class RequestDirection {
    private static final String OPENROUTESERVICE_URL = "https://api.openrouteservice.org/v2/directions/driving-car";
    private static final String OPENROUTESERVICE_KEY = "5b3ce3597851110001cf6248655d4d1d3e7e4d90a06f096c4723317f";
    private static final JerseyClient client = new JerseyClientBuilder().build();


    public static String getRoute(double originLat, double originLon,double destinationLat, double destinationLon){
        String getURL = OPENROUTESERVICE_URL+ "?api_key="+OPENROUTESERVICE_KEY+"&start="+originLon+","+
                originLat+"&end="+destinationLon+","+destinationLat;
        System.out.println(getURL);
        final Response response = client.target(getURL)
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                .get();
        String responseString = response.readEntity(String.class);
        return responseString;


    }

    public  static String postRoute(double originLat, double originLon,double destinationLat, double destinationLon) {

        final JsonObject request = Json.createObjectBuilder()
                //.add("format_in", "point")
                .add(
                        "coordinates",
                        Json.createArrayBuilder()
                                .add(Json.createArrayBuilder()
                                        .add(originLon)
                                        .add(originLat)
                                        .build())
                                .add(Json.createArrayBuilder()
                                        .add(destinationLon)
                                        .add(destinationLat)
                                        .build())
                                .build()
                )
                .build();
        System.out.println(request);


        final JerseyWebTarget webTargetPost = client.target(OPENROUTESERVICE_URL);
        final Response response = webTargetPost
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", OPENROUTESERVICE_KEY) // send the API key for authentication
                .post(Entity.json(request));

        // check the result
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());
        }

        // get the JSON response
        final String responseString = response.readEntity(String.class);


        System.out.println("Response: " + responseString);


        return  responseString;

    }

}

