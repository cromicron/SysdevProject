
package org.example;
import com.fasterxml.jackson.databind.JsonNode;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:9090/sysdev";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.example package
        final ResourceConfig rc = new ResourceConfig().packages("org.example");
        rc.register(new CORSFilter());
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     * @return
     */



    public static void main(String[] args) throws IOException {
        Graph network = new Graph();

        //testing using Json class

        JsonPoJo sh = Json.fromFile("src/test/java/org/example/schleswig-holstein.json",JsonPoJo.class);
        JsonNode[] features =sh.getFeatures();

        ArrayList edgeList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            JsonNode sh1 =  features[i];
            JsonPoJo sh2 = Json.fromJson(sh1, JsonPoJo.class);
            JsonPoJo geo = sh2.getGeometry();
            String type = geo.getType();
            if (type.contentEquals("LineString")){
                ArrayList route = geo.getCoordinates();
                edgeList.add(route);
                network.connectNodes(route);

            }


        }



        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
        System.in.read();
        server.stop();


            }
}

