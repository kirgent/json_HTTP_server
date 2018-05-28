import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class json_server {
    private static String host = "localhost";
    private static int port = 8080;

    private static ArrayList<String> list_params;

    public static void main(final String... args) throws IOException, InterruptedException {
        server_API api = new server_API();
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), -1);

        //list_params = api.read_config();
        //System.out.println(list_params);
        //common.write_config("app.name", "1111111111111");
        //System.out.println(common.read_config());

        api.process_context_main(server, "/");
        api.process_context_temperature(server, "/temperature");
        api.process_context_stop(server, "/stop");
        server.start();
        if(server.getExecutor()!=null){
            api.logger(api.INFO_LEVEL, "[SERVER] server started: on " + server.getAddress());
        }
        Thread.sleep(Long.MAX_VALUE);
    }

}