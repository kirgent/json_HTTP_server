import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;

public class json_server {

    int count_of_received_requests = 0;
    static String server_host;
    static int server_port;

    public static void main(final String... args) throws IOException {
        server_API api = new server_API();

        String server_fileName_config = "src/main/resources/config.properties";
        ArrayList propertiesList = api.read_server_config(server_fileName_config);
        api.logger(api.SERVERLOG, api.INFO_LEVEL, "[SERVER] " + new Date() + ": read " + server_fileName_config + ":\n"
                + "server_interface=" + propertiesList.get(0) + "\n"
                + "server_port=" + propertiesList.get(1)
                + "list_of_contexts=" + propertiesList.get(2) + "\n"
                + "list_of_params=" + propertiesList.get(3) + "\n"
                + "enable_context_temperature=" + propertiesList.get(4) + "\n"
                + "enable_context_stop=" + propertiesList.get(5) + "\n"
                + "fileName_xml=" + propertiesList.get(6));


        HttpServer server = HttpServer.create(new InetSocketAddress(server_host, server_port), -1);
        //String fileName_xml = "json.xml";

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