import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class json_server {
    private static String host = "localhost";
    private static int port = 8080;

    public static void main(final String... args) throws IOException {
        server_API api = new server_API();
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), -1);
        //HttpServer server = HttpServer.create();
        //server.bind(new InetSocketAddress(8080),0);

        ArrayList list_params = api.read_config();
        System.out.println("[SERVER] params from config.properties: "
                + "\nlist_of_contexts: " + list_params.get(0)
                + "\nlist_of_params: " + list_params.get(1)
                + "\nenable_context_temperature: " + list_params.get(2)
                + "\nenable_context_stop: " + list_params.get(3));

        //api.process_context_main(server, "/");
        api.process_context_temperature(server, "/temperature");
        api.process_context_stop(server, "/stop");
        server.start();
        if(server.getExecutor()!=null){
            api.logger(api.INFO_LEVEL, "[SERVER] server started: on " + server.getAddress());
            api.logger(api.INFO_LEVEL, "[SERVER] in console mode type CTRL+C to quit");
        }
    }

}