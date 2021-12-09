package org.example;

import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/orsdirections")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET

    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getIt(@QueryParam("originLat") double originLat, @QueryParam("originLon") double originLong,
                            @QueryParam("destinationLat") double destinationLat, @QueryParam("destinationLon") double
                        destinationLong) {
        JsonObject route = RequestDirection.poiSearch(originLat, originLong, destinationLat, destinationLong);

        System.out.println("test"+route);
        return route;
    }





}
