package org.example;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    public void load() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            String file = "src/main/java/org/example/schleswig-holstein.json";

            //ObjectMapper maps a json to a pojo. For our purposes the pojo is an instance of our Geojson class
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Geojson mapJson = mapper.readValue(new FileInputStream(file), Geojson.class);
            //Create Graph object from the Map-json, to do path-searches on.
            Graph graph = new Graph(mapJson);


            while (true) {

                Socket clientSocket = serverSocket.accept();
                InputStream is = clientSocket.getInputStream();
                OutputStream os = clientSocket.getOutputStream();

                //Receive double array of coordinates for the path-search
                ObjectInputStream ois = new ObjectInputStream(is);

                //Send Object Outputstream to send Geojson path through.
                ObjectOutputStream oos = new ObjectOutputStream(os);

                double[] coords = (double[])ois.readObject();
                double originLat = coords[0];
                double originLong = coords[1];
                double destinationLat = coords[2];
                double destinationLong = coords[3];
                double algorithm = coords[4];
                ArrayList<Node> route;

                if (algorithm == 0) {
                    route = graph.anyLocationDijkstra(originLat, originLong, destinationLat, destinationLong);

                } else {
                    route = graph.anyLocationAStar(originLat, originLong, destinationLat, destinationLong);
                                    }
                String routeGeoJson = Geojson.transformToString(route);
                oos.writeObject(routeGeoJson);
                is.close();
                os.close();
                clientSocket.close();

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
