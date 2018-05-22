import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonHTTPServer {
    static String HOSTNAME = "localhost";
    static int PORT = 8080;
    static int BACKLOG = 1;

    static final Charset CHARSET = StandardCharsets.UTF_8;

    static int STATUS_OK = 200;
    static int STATUS_METHOD_NOT_ALLOWED = 405;
    static int STATUS_NOT_FOUND = 404;
    static final int NO_RESPONSE_LENGTH = -1;

    static final String HEADER_ALLOW = "Allow";
    static String HEADER_CONTENT_TYPE = "Content-Type";

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";
    static final String METHOD_OPTIONS = "OPTIONS";
    static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_POST + "," + METHOD_OPTIONS;

    private static ArrayList list_params;

    public static void main(final String... args) throws IOException {
        common_api api = new common_api();
        final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);

        list_params = api.read_config();
        System.out.println(list_params);
        //common.write_config("app.name", "1111111111111");
        //System.out.println(common.read_config());

        api.process_context_main("/", server);
        api.process_context_reminders("/reminders", server);
        api.process_context_stop("/stop", server);
        server.start();
    }

}