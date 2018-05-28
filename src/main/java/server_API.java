import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpHeaders.ALLOW;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class server_API extends common_API{

    private List<String> test;
    private List<String> macaddress;
    private List<String> count_reminders;
    private List<String> count_iterations;


    /** There are SERVER implementations
     * @param server
     * @param context
     */
    void process_context_main(HttpServer server, String context){
        System.out.println("process_context_main: " + context);
        server.createContext(context, he -> {
            try {
                Headers headers = he.getResponseHeaders();
                String requestMethod = he.getRequestMethod().toUpperCase();
                Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());

                String response = html + " " + requestMethod + " " + context;
                response += add_params_if_not_null(requestParameters.get("test"),
                        requestParameters.get("macaddress"),
                        requestParameters.get("count_reminders"),
                        requestParameters.get("count_iterations"));

                switch (requestMethod) {
                    case METHOD_GET:
                        send_response(he, headers, response);
                        break;
                    case METHOD_POST:
                        send_response(he, headers, response);
                        break;
                    case METHOD_OPTIONS:
                        print_out("case METHOD_OPTIONS");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        print_out("case default");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                he.close();
            }
        });
    }

    /** There are SERVER implementations
     * @param server
     * @param context
     */
    void process_context_reminders(HttpServer server, String context) {
        System.out.println("process_context_reminders(): " + context);
        server.createContext(context, he -> {
            try {
                Headers headers = he.getResponseHeaders();
                //System.out.println(headers.get(HEADER_CONTENT_TYPE).toString());
                String requestMethod = he.getRequestMethod().toUpperCase();
                Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());
                test = requestParameters.get("test");
                macaddress = requestParameters.get("macaddress");
                count_reminders = requestParameters.get("count_reminders");
                count_iterations = requestParameters.get("count_iterations");
                String response = html + " " + requestMethod + " " + context;
                response += add_params_if_not_null(test, macaddress, count_reminders, count_iterations);
                switch (requestMethod) {
                    case METHOD_GET:
                        send_response(he, headers, response);
                        break;
                    case METHOD_POST:
                        send_response(he, headers, response);
                        break;
                    case METHOD_OPTIONS:
                        print_out("case METHOD_OPTIONS");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        print_out("case default");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                he.close();
            }
        });
    }

    /** There are SERVER implementations
     * @param server
     * @param context
     */
    void process_context_stop(HttpServer server, String context) {
        System.out.println("process_context_stop(): " + context);
        server.createContext(context, he -> {
            try {
                Headers headers = he.getResponseHeaders();
                String requestMethod = he.getRequestMethod().toUpperCase();
                Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());
                test = requestParameters.get("test");
                macaddress = requestParameters.get("macaddress");
                count_reminders = requestParameters.get("count_reminders");
                count_iterations = requestParameters.get("count_iterations");
                String response = html + " " + requestMethod + " " + context;
                response += add_params_if_not_null(test, macaddress, count_reminders, count_iterations);
                switch (requestMethod) {
                    case METHOD_GET:
                        send_response(he, headers, response);
                        server.stop(0);
                        break;
                    default:
                        print_out("case default");
                        break;
                }
            } finally {
                he.close();
            }
        });
    }

    private String add_params_if_not_null(List test, List macaddress, List count_reminders, List count_iterations) {
        String result = "";
        if(test!=null){
            result += " " + test;
        }
        if(macaddress!=null){
            result += " " + macaddress;
        }
        if(count_reminders!=null){
            result += " " + count_reminders;
        }
        if(count_iterations!=null){
            result += " " + count_iterations;
        }
        return result;
    }

    /** There are SERVER implementations
     * @param he
     * @param headers
     * @param responseBody
     * @throws IOException
     */
    private void send_response(HttpExchange he, Headers headers, String responseBody) throws IOException {
        System.out.println("new client connected: " +
                he.getRemoteAddress() + " " +
                he.getProtocol() + " " +
                he.getRequestMethod() + " " +
                he.getRequestURI());

        System.out.println("send_response() to client: " + responseBody);
        headers.set(CONTENT_TYPE, String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        headers.set("HTTP/1.1", "200 OK");
        headers.set("Server", "json_server 0.1");
        //headers.set("Content-Length", responseBody);
        //headers.set("Connection", "close");
        byte[] rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
        he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
        he.getResponseBody().write(rawResponseBody);
    }

    Map<String, List<String>> getRequestParameters(URI requestUri) {
        Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (String rawRequestParameter : rawRequestParameters) {
                String[] requestParameter = rawRequestParameter.split("=", 2);
                String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e);
        }
    }

}
