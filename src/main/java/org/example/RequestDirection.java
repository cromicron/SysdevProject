package org.example;

import jakarta.json.Json;
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

    public  static JsonObject poiSearch(double lat1, double lon1,double lat2, double lon2) {
        // create a json object which we will send in the post request
        // {
        //      format_in: "point",
        //      geometry: [lon, lat]
        // }
        final JsonObject request = Json.createObjectBuilder()
                //.add("format_in", "point")
                .add(
                        "coordinates",
                        Json.createArrayBuilder()
                                .add(Json.createArrayBuilder()
                                        .add(lon1)
                                        .add(lat1)
                                        .build())
                                .add(Json.createArrayBuilder()
                                        .add(lon2)
                                        .add(lat2)
                                        .build())
                                .build()
                )
                .build();
        System.out.println(request);

        final JerseyClient client = new JerseyClientBuilder().build();
        final JerseyWebTarget webTarget = client.target(OPENROUTESERVICE_URL);

        final Response response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", OPENROUTESERVICE_KEY) // send the API key for authentication
                .post(Entity.json(request));
        System.out.println(response);
        // check the result
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());
        }

        // get the JSON response
        final String responseString = response.readEntity(String.class);
        final JsonObject jsonObject = Json.createReader(new StringReader(responseString)).readObject();
        System.out.println("Response: " + jsonObject);

        // Extract the route
       /* final JsonNumber route = jsonObject
                .getJsonObject("geometry")
                .getJsonArray("coordinates")
                .getJsonNumber(2);
        System.out.println("Elevation: " + route.doubleValue() + "m");*/
        return request;
    }

}

