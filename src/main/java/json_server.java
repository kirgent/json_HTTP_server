import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;

public class json_server {

    int count_of_received_requests = 0;

    public static void main(final String... args) throws IOException {
        server_API api = new server_API();
        String host = "localhost";
        int port = 8080;
        String server_fileName_config = "src/main/resources/config.properties";
        //String fileName_xml = "json.xml";
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), -1);
        //HttpServer server = HttpServer.create();
        //server.bind(new InetSocketAddress(8080),0);

        ArrayList propertiesList = api.read_config(server_fileName_config);
        api.logger(api.SERVERLOG, api.INFO_LEVEL, "[SERVER] " + new Date() + ": read " + server_fileName_config + ":\n"
                + "list_of_contexts=" + propertiesList.get(0) + "\n"
                + "list_of_params=" + propertiesList.get(1) + "\n"
                + "enable_context_temperature=" + propertiesList.get(2) + "\n"
                + "enable_context_stop=" + propertiesList.get(3) + "\n"
                + "fileName_xml=" + propertiesList.get(4));

        //api.process_context_main(server, "/");
        api.process_context_temperature(server, "/temperature");
        api.process_context_stop(server, "/stop");
        server.start();
        if(server.getExecutor()!=null){
            api.logger(api.SERVERLOG, api.INFO_LEVEL, "[SERVER] server started: on " + server.getAddress());
            api.logger(api.SERVERLOG, api.INFO_LEVEL, "[SERVER] in console mode type CTRL+C to quit");
        }
    }

}