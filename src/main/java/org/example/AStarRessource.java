package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("/astar")
public class AStarRessource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@QueryParam("originLat") double originLat, @QueryParam("originLon") double originLong,
                      @QueryParam("destinationLat") double destinationLat, @QueryParam("destinationLon") double
                              destinationLong) throws IOException {
        String route = RequestDirection.getDijkstra(originLat, originLong, destinationLat, destinationLong);

        return route;
    }
}
