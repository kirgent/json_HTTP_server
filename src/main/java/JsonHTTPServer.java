import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class JsonHTTPServer {
    static String host = "localhost";
    static int port = 8080;

    static int STATUS_OK = 200;
    static int STATUS_METHOD_NOT_ALLOWED = 405;
    static int STATUS_NOT_FOUND = 404;
    static final int NO_RESPONSE_LENGTH = -1;

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";
    static final String METHOD_OPTIONS = "OPTIONS";
    static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_POST + "," + METHOD_OPTIONS;

    private static ArrayList<String> list_params;

    public static void main(final String... args) throws IOException, InterruptedException {
        server_API api = new server_API();
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), -1);

        list_params = api.read_config();
        //System.out.println(list_params);
        //common.write_config("app.name", "1111111111111");
        //System.out.println(common.read_config());

        api.process_context_main(server, "/");
        api.process_context_reminders(server, "/reminders");
        api.process_context_stop(server, "/stop");
        server.start();
        if(server.getExecutor()!=null){
            System.out.println("server started: on " + server.getAddress());
        }
        Thread.sleep(Long.MAX_VALUE);
    }

}