import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class test_json_HTTP_server extends common_api {
    private common_api api = new common_api();

    @Test
    void test_context_main() throws IOException {
        /*final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        server.createContext("/", httpExchange -> {
            try {
                Headers headers = httpExchange.getResponseHeaders();
                String requestMethod = httpExchange.getRequestMethod().toUpperCase();
                switch (requestMethod) {
                    case METHOD_GET:
                        Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                        testname = requestParameters.get("testname");
                        macaddress = requestParameters.get("macaddress");
                        count_reminders = requestParameters.get("count_reminders");
                        count_iterations = requestParameters.get("count_iterations");

                        String responseBody = html + "\n" +
                                requestMethod + "\n" +
                                context + "\n" +
                                list_params;
                        print_out(responseBody);
                        print_out(String.valueOf(testname));
                        write_file(responseBody);
                        write_response(httpExchange, headers, responseBody);
                        break;
                    case METHOD_OPTIONS:
                        responseBody = html + "\n" +
                                requestMethod + "\n" +
                                "/" + "\n" +
                                list_params;
                        print_out(responseBody);
                        write_file(responseBody);
                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        responseBody = html + "\n" +
                                requestMethod + "\n" +
                                "/" + "\n" +
                                list_params;
                        print_out(responseBody);
                        write_file(responseBody);
                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                httpExchange.close();
            }
        });
        server.start();*/

    }

    @Test
    void test_context_reminders () throws IOException {
        common_api api = new common_api();
        //final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        String context = "/reminders";
        //api.process_context_reminders(context, server);
        //server.start();

        ArrayList actual = api.request(host, port, context);
        assertEquals(expected200, actual.get(0));
        //server.stop(-1);
    }

    @Test
    void test_context_stop () throws IOException {
        assertEquals(0, 0);
    }

}