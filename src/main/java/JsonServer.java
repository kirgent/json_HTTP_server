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

public class JsonServer {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;
    private static final int BACKLOG = 1;

    static final Charset CHARSET = StandardCharsets.UTF_8;

    static int STATUS_OK = 200;
    private static int STATUS_METHOD_NOT_ALLOWED = 405;
    private static int STATUS_NOT_FOUND = 404;
    private static final int NO_RESPONSE_LENGTH = -1;

    private static final String HEADER_ALLOW = "Allow";
    static String HEADER_CONTENT_TYPE = "Content-Type";

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_POST + "," + METHOD_OPTIONS;

    private static List<String> test;
    private static List macaddress;
    private static List count_reminders;
    private static List<String> count_iterations;


    public static void main(final String... args) throws IOException {
        common_api common = new common_api();
        final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);

        System.out.println(common.read_config());
        common.write_config("app.name", "1111111111111");
        System.out.println(common.read_config());

        server.createContext("/", httpExchange -> {
            try {
                final Headers headers = httpExchange.getResponseHeaders();
                switch (httpExchange.getRequestMethod().toUpperCase()) {
                    case METHOD_GET:

                        //<a href="URL">текст_ссылки</a>

                        String example = "Example of reminders request: http://localhost:8080/reminders?" +
                            "test=test1&" +
                            "macaddress=123456789012&" +
                            "count_reminders=X&" +
                            "count_iterations=Y";
                        String s1 = "List of tests for NewAPI:\n" +
                                "test1_Add_Purge, " +
                                "test2_Add_Delete_Purge, " +
                                "test3_Add_Modify_Delete_Purge\n\n" +
                                "List of tests for OldAPI:\n" +
                                "test1_Add_Purge, " +
                                "test2_Add_Delete_Purge";


                        String responseBody = example + "\n" + s1;

                        final Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                        // do something with the request parameters

                        common.write_to_file(responseBody);
                        common.write_response(httpExchange, headers, responseBody);
                        break;
                    case METHOD_OPTIONS:
                        System.out.println("case METHOD_OPTIONS !!!");
                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        System.out.println("case default !!!");
                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                httpExchange.close();
            }
        });
        server.createContext("/reminders", httpExchange -> {
            try {
                final Headers headers = httpExchange.getResponseHeaders();
                //System.out.println(headers.get(HEADER_CONTENT_TYPE).toString());
                final String requestMethod = httpExchange.getRequestMethod().toUpperCase();
                switch (requestMethod) {
                    case METHOD_GET:
                        final Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                        // do something with the request parameters
                        test = requestParameters.get("test");
                        macaddress = requestParameters.get("macaddress");
                        count_reminders = requestParameters.get("count_reminders");
                        count_iterations = requestParameters.get("count_iterations");

                        String list_params = "test=" + test +
                                ", macaddress=" + macaddress +
                                ", count_reminders="+ count_reminders +
                                ", count_iterations=" + count_iterations;

                        /*if(macaddress.length()>12){
                            System.out.println("Wrong macaddress !!!" + macaddress.length());
                        }
                        if(!count_reminders.getClass().equals("Integer")){
                            System.out.println("Wrong count_reminders type !!!" + count_reminders.getClass());
                        }*/
                        String html = "<html><body><h1>Testing of reminders</h1></body></html>";

                        final String responseBody = html + list_params;

                        common.write_response(httpExchange, headers, responseBody);
                        break;
                    case METHOD_OPTIONS:
                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                httpExchange.close();
            }
        });
        server.start();
    }

    private static Map<String, List<String>> getRequestParameters(final URI requestUri) {
        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(final String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (final UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }
}

/*
import java.io.IOException;
        import java.io.OutputStream;
        import java.net.InetSocketAddress;

        import com.sun.net.httpserver.HttpExchange;
        import com.sun.net.httpserver.HttpHandler;
        import com.sun.net.httpserver.HttpServer;

public class Test {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
*/