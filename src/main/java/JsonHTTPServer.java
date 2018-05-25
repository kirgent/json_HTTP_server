import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JsonHTTPServer {
    static String host = "localhost";
    static int port = 8080;
    static int BACKLOG = 1;

    static final Charset CHARSET = StandardCharsets.UTF_8;

    static int STATUS_OK = 200;
    static int STATUS_METHOD_NOT_ALLOWED = 405;
    static int STATUS_NOT_FOUND = 404;
    static final int NO_RESPONSE_LENGTH = -1;

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";
    static final String METHOD_OPTIONS = "OPTIONS";
    static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_POST + "," + METHOD_OPTIONS;

    private static ArrayList list_params;

    public static void main(final String... args) throws IOException, InterruptedException {
        common_API api = new common_API();
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), BACKLOG);

        //list_params = api.read_config();
        //System.out.println(list_params);
        //common.write_config("app.name", "1111111111111");
        //System.out.println(common.read_config());

        api.process_context_main("/", server);
        api.process_context_reminders("/reminders", server);
        api.process_context_stop("/stop", server);
        server.start();
        System.out.println("before LONG sleep");
        Thread.sleep(Long.MAX_VALUE);
        System.out.println("after LONG sleep");
    }

}