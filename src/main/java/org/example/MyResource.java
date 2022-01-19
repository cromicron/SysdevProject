package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/orsdirections")
public class MyResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String post(JsonObject postRequest) throws JsonProcessingException {
        JsonNumber originLatJson = (JsonNumber) postRequest.get("originLat");
        JsonNumber originLongJson = (JsonNumber) postRequest.get("originLon");
        JsonNumber destinationLatJson = (JsonNumber) postRequest.get("destinationLat");
        JsonNumber destinationLongJson = (JsonNumber) postRequest.get("destinationLon");
        double originLat = originLatJson.doubleValue();
        double originLong = originLongJson.doubleValue();
        double destinationLat = destinationLatJson.doubleValue();
        double destinationLong = destinationLongJson.doubleValue();
        String route = RequestDirection.postRoute(originLat, originLong, destinationLat, destinationLong);


        /*The UI does not know what to do with the result from the post request. We have to transform the geojson to be
        similar to the one from the get request. We need to wrap the segments into properties, and those into features.
         */
        ObjectMapper mapper = new ObjectMapper();
        //Write as file


        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        POJOPost jsonRoute = mapper.readValue(route, POJOPost.class);
        HashMap<String, ArrayList> segments = new HashMap<String, ArrayList>();
        HashMap<String, HashMap> properties = new HashMap<String, HashMap>();
        HashMap<String, ArrayList> features = new HashMap<String, ArrayList>();
        segments.put("segments", jsonRoute.routes.get(0).segments);
        properties.put("properties", segments);
        ArrayList featuresList = new ArrayList<>();
        featuresList.add(properties);
        features.put("features", featuresList);
        String postOutput = mapper.writeValueAsString(features);

        System.out.println(postOutput);


        return postOutput;
    }



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@QueryParam("originLat") double originLat, @QueryParam("originLon") double originLong,
                            @QueryParam("destinationLat") double destinationLat, @QueryParam("destinationLon") double
                        destinationLong) {
        String route = RequestDirection.getRoute(originLat, originLong, destinationLat, destinationLong);
        System.out.println("test"+route);
        return route;
    }




}
