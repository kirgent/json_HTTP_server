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
import java.util.*;

import static org.apache.http.HttpHeaders.ALLOW;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class server_API extends common_API{

    private List<String> test;
    private List<String> macaddress;


    /** There are SERVER implementations
     * @param exchange
     * @param header
     * @param responseCode
     * @param responseBody
     * @throws IOException
     */
    private void send_response_to_client(HttpExchange exchange, Headers header, int responseCode, String responseBody) throws IOException {
        System.out.println("send response:\n" + responseBody);
        header.set(CONTENT_TYPE, String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        header.set("HTTP/1.1", responseCode + "OK");
        header.set("Server", "json_server 0.1");
        //headers.set("Content-Length", responseBody);
        //headers.set("Connection", "close");
        byte[] rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(responseCode, rawResponseBody.length);
        exchange.getResponseBody().write(rawResponseBody);
        exchange.getResponseBody().close();
    }

    /** SERVER side
     * @param server
     * @param context
     */
/*    void process_context_main(HttpServer server, String context){
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
*/

    /** SERVER side
     * @param server
     * @param context
     * @throws IOException
     */
    void process_context_temperature(HttpServer server, String context) throws IOException {
        logger(INFO_LEVEL,"[SERVER] process_context_temperature: " + context);
        //http://www.javenue.info/post/java-http-server
        server.createContext(context, exchange-> {
            logger(INFO_LEVEL, "[SERVER] new client connected: " +
                    exchange.getRemoteAddress() + " " +
                    exchange.getProtocol() + " " +
                    exchange.getRequestMethod() + " " +
                    exchange.getRequestURI());

            //get request headers
            //ArrayList requestHeaders = new ArrayList();
            String requestHeaders;
            requestHeaders = my_getRequestHeaders(exchange);
            logger(INFO_LEVEL, "requestHeaders: \n" + requestHeaders);

            //get request body
            String requestbody = my_getRequestBody(exchange);
            if(!requestbody.isEmpty()){
                logger(INFO_LEVEL, "requestBody: \n" + requestbody);
            }

            //parsing params in URI
            Headers headers = exchange.getResponseHeaders();
            String requestMethod = exchange.getRequestMethod().toUpperCase();
            Map<String, List<String>> requestParameters = getRequestParameters(exchange.getRequestURI());
            String params = add_params_if_not_null(requestParameters.get("test"),
                       requestParameters.get("macaddress"));

            //check_responsebody();

            //String responsebody = html + " " + requestMethod + " " + context;
            //String responsebody= "{\"measurements\":[{\"date\":\"2018-29-05\",\"unit\":\"C\",\"temperature\":907}]}";
            /*responsebody = "{\"success\":false,\"error:" +
                    "{" +
                    "\"code\":1," +
                    "\"message\" : \"incorrect json format\"" +
                    "}" +
                    "}";*/

            int responsecode = STATUS_OK;
            String responsebody = "";
            String correct_json = "{\"Measurements\":[{\"Date\":\"Wed May 30 16:31:04 MSK 2018\",\"Unit\":\"C\",\"Temperature\":123}]}";

            System.out.println("1: " + correct_json);
            System.out.println("2: " + requestbody);

            if (requestbody.equals(correct_json + "\n")) {
                responsebody = "{\"success\":true}";

            } else if (requestbody.equals("")) {
                responsebody = "{\"success\":false,\"error\":\"incorrect json: empty body\"}";

            } else if (requestbody.equals("{}\n")) {
                responsebody = "{\"success\":false,\"error\":\"incorrect json: empty json in body\"}";

            } else  if (requestbody.equals("{\"test\":123123123}\n")) {
                responsecode = STATUS_SERVER_INTERNAL_ERROR;
                responsebody = "{\"success\":false,\"error\":\"incorrect json: incorrect json format\"}";

            } else {
                responsecode = STATUS_SERVER_INTERNAL_ERROR;
                responsebody = "incorrect request\n";
            }

            try {
                switch (requestMethod) {
                    case METHOD_GET:
                        logger(INFO_LEVEL,"[SERVER] case METHOD_GET");
                        send_response_to_client(exchange, headers, responsecode, responsebody);
                        break;
                    case METHOD_POST:
                        logger(INFO_LEVEL,"[SERVER] case METHOD_POST");
                        send_response_to_client(exchange, headers, responsecode, responsebody);
                        break;
                    case METHOD_OPTIONS:
                        logger(INFO_LEVEL,"[SERVER] case METHOD_OPTIONS");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        exchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        logger(INFO_LEVEL,"[SERVER] case default");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                exchange.close();
            }
        });
    }

    private boolean check_responsebody() {
        if(true){
            return true;
        } else {
            return false;
        }
    }

    private String my_getRequestBody(HttpExchange he) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(he.getRequestBody()));
        StringBuilder builder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            builder.append(line);
            if (reader.readLine() == null) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String my_getRequestHeaders(HttpExchange he) throws IOException {
        //getRequestHeaders - получаем неизменяемую Map хедеров данного HTTP запроса:
        Headers requestHeaders = he.getRequestHeaders();
        StringBuilder builder = new StringBuilder();
        for (String header : requestHeaders.keySet()) {
            builder.append(header)
                    .append("=")
                    .append(requestHeaders.getFirst(header))
                    .append("\n");
        }
        return builder.toString();
    }

    private ArrayList my_getRequestHeaders2(HttpExchange he) throws IOException {
        ArrayList arrayList = new ArrayList();
        //getRequestHeaders - получаем неизменяемую Map хедеров данного HTTP запроса:
        Headers requestHeaders = he.getRequestHeaders();

        int i = 0;
        for (Iterator<String> iterator = requestHeaders.keySet().iterator(); iterator.hasNext(); ) {
            String header = iterator.next();
            StringBuilder builder = new StringBuilder()
                    .append(header)
                    .append("=")
                    .append(requestHeaders.getFirst(header))
                    .append("\n");
            arrayList.set(i, builder);
            i += 1;
        }
        return arrayList;
    }

    /** SERVER side
     * @param server
     * @param context
     * @throws IOException
     */
    void process_context_stop(HttpServer server, String context) throws IOException {
        logger(INFO_LEVEL,"[SERVER] process_context_stop(): " + context);
        server.createContext(context, he -> {
            logger(INFO_LEVEL, "[SERVER] new client connected: " +
                    he.getRemoteAddress() + " " +
                    he.getProtocol() + " " +
                    he.getRequestMethod() + " " +
                    he.getRequestURI());

            String requestHeaders = my_getRequestHeaders(he);
            logger(INFO_LEVEL, "[SERVER] requestHeaders: \n" + requestHeaders);

            try {
                Headers headers = he.getResponseHeaders();
                String requestMethod = he.getRequestMethod().toUpperCase();
                Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());
                test = requestParameters.get("test");
                macaddress = requestParameters.get("macaddress");
                String responseBody = html + " " + requestMethod + " " + context;
                int responseCode = STATUS_OK;
                responseBody += add_params_if_not_null(test, macaddress);

                switch (requestMethod) {
                    case METHOD_GET:
                        send_response_to_client(he, headers, responseCode, responseBody);
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

    private String add_params_if_not_null(List test, List macaddress) {
        String result = "";
        if(test!=null){
            result += " " + test;
        }
        if(macaddress!=null){
            result += " " + macaddress;
        }
        return result;
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

    private String read_response(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            builder.append(line);
            if (reader.readLine() == null) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

}
