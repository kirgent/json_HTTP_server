import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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


    /** SERVER side
     * @param server
     * @param context
     */
    void process_context_main(HttpServer server, String context){
        System.out.println("[SERVER] process_context_main: " + context);
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
                        logger(INFO_LEVEL,"[SERVER] case METHOD_OPTIONS");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        logger(INFO_LEVEL,"[SERVER] case default");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                he.close();
            }
        });
    }

    /** SERVER side
     * @param server
     * @param context
     * @throws IOException
     */
    void process_context_temperature(HttpServer server, String context) throws IOException {
        logger(INFO_LEVEL,"[SERVER] process_context_temperature: " + context);

        server.createContext(context, he -> {
            logger(INFO_LEVEL, "[SERVER] new client connected: " +
                    he.getRemoteAddress() + " " +
                    he.getProtocol() + " " +
                    he.getRequestMethod() + " " +
                    he.getRequestURI());
            try {
                Headers headers = he.getResponseHeaders();
                String requestMethod = he.getRequestMethod().toUpperCase();
                Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());

                //String server_response = html + " " + requestMethod + " " + context;
                String server_response= "{\"measurements\":[{\"date\":\"Mon May 28 21:35:00 MSK 2018\",\"unit\":\"C\",\"temperature\":907}]}";
                /*server_response += add_params_if_not_null(requestParameters.get("test"),
                        requestParameters.get("macaddress"),
                        requestParameters.get("count_reminders"),
                        requestParameters.get("count_iterations"));*/

                switch (requestMethod) {
                    case METHOD_GET:
                        send_response(he, headers, server_response);
                        break;
                    case METHOD_POST:
                        send_response(he, headers, server_response);
                        break;
                    case METHOD_OPTIONS:
                        logger(INFO_LEVEL,"[SERVER] case METHOD_OPTIONS");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        logger(INFO_LEVEL,"[SERVER] case default");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                he.close();
            }
        });
    }

    /** SERVER side
     * @param server
     * @param context
     * @throws IOException
     */
    void process_context_stop(HttpServer server, String context) throws IOException {
        logger(INFO_LEVEL,"[SERVER] process_context_stop(): " + context);
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
                        logger(INFO_LEVEL,"[SERVER] case default");
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
     * @param header
     * @param responseBody
     * @throws IOException
     */
    private void send_response(HttpExchange he, Headers header, String responseBody) throws IOException {
        System.out.println("[SERVER] send_response: " + responseBody);
        header.set(CONTENT_TYPE, String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        header.set("HTTP/1.1", "200 OK");
        header.set("Server", "json_server 0.1");
        //headers.set("Content-Length", responseBody);
        //headers.set("Connection", "close");
        byte[] rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
        he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
        he.getResponseBody().write(rawResponseBody);
    }

    /** this code from WWW
     * @param requestUri
     * @return
     */
    private Map<String, List<String>> getRequestParameters(URI requestUri) {
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

    /** this code from WWW
     * @param urlComponent
     * @return
     */
    private static String decodeUrlComponent(String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e);
        }
    }

    final String read_response(StringBuilder body, HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
        //StringBuilder body = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            if(show_response_body) {
                System.out.print("response body: " + body.append(line));
            }else{
                body.append(line);
            }
            if (reader.readLine() == null) {
                System.out.println();
            }
        }
        return body.toString();
    }

}
