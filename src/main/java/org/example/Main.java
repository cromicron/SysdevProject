
package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
        String file = "src/main/java/org/example/schleswig-holstein.json";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Geojson sh = mapper.readValue(new FileInputStream(file), Geojson.class);
        Graph shGraph = new Graph(sh);
        for (Node node: shGraph.nodelist){
            System.out.println(node.outnodes);
        }
        ArrayList<Node>pathDijkstra = shGraph.anyLocationDijkstra(54.4635957,9.8403052, 54.486864,9.8599366);
        //ArrayList<Node>pathAStar = shGraph.anyLocationAStar(9.8403052,54.4635957, 9.8599366,54.486864);
        //create String from array
        String pathDijk = pathDijkstra.stream().map(Node::toString).collect(Collectors.joining(","));
        //String pathA = pathAStar.stream().map(Node::toString).collect(Collectors.joining(","));
        String jsonString = Geojson.transformToString(pathDijkstra);
        System.out.println(pathDijk);
        System.out.println("json"+jsonString);




        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
        System.in.read();
        server.stop();


            }
}

