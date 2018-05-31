import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.http.HttpHeaders.ALLOW;

public class server_API extends common_API{

    private boolean show_request_headers = true;
    private boolean show_request_body = true;

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
        logger(SERVERLOG, INFO_LEVEL,"[SERVER] process_context_temperature: " + context);
        //http://www.javenue.info/post/java-http-server
        server.createContext(context, exchange-> {
            count_of_received_requests++;

            if(show_info_level) {
                logger(SERVERLOG, INFO_LEVEL, "[SERVER] " + new Date() + ": new client connected: " +
                        exchange.getRemoteAddress() + " " +
                        exchange.getProtocol() + " " +
                        exchange.getRequestMethod() + " " +
                        exchange.getRequestURI());
            }

            //get request headers
            String requestHeaders = my_getRequestHeaders(exchange);
            if(show_request_headers) {
                logger(SERVERLOG, INFO_LEVEL, "[SERVER] requestHeaders: \n" + requestHeaders);
            }

            //get request body
            String requestbody = my_getRequestBody(exchange);
            if(show_request_body) {
                logger(SERVERLOG, INFO_LEVEL, "[SERVER] requestBody: \n" + requestbody);
            }

            //parsing params in URI
            Headers headers = exchange.getResponseHeaders();
            String requestMethod = exchange.getRequestMethod().toUpperCase();
            Map<String, List<String>> requestParameters = getRequestParameters(exchange.getRequestURI());
            String params = add_params_if_not_null(requestParameters.get("test"),
                       requestParameters.get("macaddress"));

            //check_responsebody();

            String message = "example of correct json: " +
                    "\n{\"measurements\":[{\"date\":<unix timestamp>,\"temperature\":xxx,\"unit\":\"C\"}]}, where:" +
                    "\nmeasurements - array is mandatory." +
                    "\ndate         - field is mandatory, value is unix timestamp" +
                    "\ntemperature  - field is optional, value is type short" +
                    "\nunit         - field is optional, value is string, can be: K, k, C, c, F, f";

            int responseCode;
            JSONObject json = new JSONObject();

            if (requestbody.equals("")) {
                json.put("success", false);
                json.put("error", "incorrect json: empty body");
                responseCode = STATUS_SERVER_INTERNAL_ERROR;

            } else if (requestbody.equals("{}")) {
                json.put("success", false);
                json.put("error", "incorrect json: empty json in body");
                responseCode = STATUS_SERVER_INTERNAL_ERROR;

            } else if (!requestbody.contains("\"measurements\":[") || !requestbody.contains("\"date\":")){
                json.put("success", false);
                json.put("error", "incorrect json: incorrect json format, incorrect one or more mandatory field: measurements, date");
                json.put("message", message);
                responseCode = STATUS_SERVER_INTERNAL_ERROR;

            } else if (requestbody.contains("\"measurements\":[") && requestbody.contains("\"date\":")){
                json.put("success", true);
                responseCode = STATUS_OK;

                generate_xml(global_config.get(4).toString());

            } else {
                json.put("success", false);
                json.put("error", "incorrect json: incorrect json format");
                json.put("message", message);
                responseCode = STATUS_SERVER_INTERNAL_ERROR;
            }
            String responseBody = json.toJSONString();

            /*Headers header = new Headers();
            header.set(CONTENT_TYPE, String.format("application/json; charset=%s", StandardCharsets.UTF_8));
            header.set("HTTP/1.1", responseCode + "");
            header.set("Server", "json_server 0.1");*/
            //headers.set("Content-Length", responseBody);
            //headers.set("Connection", "close");

            try {
                switch (requestMethod) {
                    case METHOD_GET:
                        send_response(exchange, responseCode, responseBody);
                        break;
                    case METHOD_POST:
                        send_response(exchange, responseCode, responseBody);
                        break;
                    case METHOD_OPTIONS:
                        logger(SERVERLOG, INFO_LEVEL,"[SERVER] case METHOD_OPTIONS");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        exchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        logger(SERVERLOG, INFO_LEVEL,"[SERVER] case default");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                exchange.close();
            }
        });
    }

    private void generate_xml(String fileName) throws IOException {

        logger(SERVERLOG, INFO_LEVEL, "used: " + fileName);
        /*try {
            FileOutputStream os = new FileOutputStream(new File(fileName));
            os.close();
        } catch (Exception e) {
            System.out.println("catch exception! write_config: " + e);
            e.printStackTrace();
        }*/
    }

    private String my_getRequestBody(HttpExchange he) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(he.getRequestBody()));
        StringBuilder builder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            builder.append(line);
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
        logger(SERVERLOG, INFO_LEVEL,"[SERVER] process_context_stop(): " + context);
        server.createContext(context, exchange -> {
            count_of_received_requests++;

            logger(SERVERLOG, INFO_LEVEL, "[SERVER] " + new Date() + ": new client connected: " +
                    exchange.getRemoteAddress() + " " +
                    exchange.getProtocol() + " " +
                    exchange.getRequestMethod() + " " +
                    exchange.getRequestURI());

            //get request headers
            String requestHeaders = my_getRequestHeaders(exchange);
            if(show_request_headers) {
                logger(SERVERLOG, INFO_LEVEL, "[SERVER] requestHeaders: \n" + requestHeaders);
            }

            //get request body
            String requestbody = my_getRequestBody(exchange);
            if(show_request_body) {
                logger(SERVERLOG, INFO_LEVEL, "[SERVER] requestBody: \n" + requestbody);
            }

            //Headers headers = exchange.getResponseHeaders();
            String requestMethod = exchange.getRequestMethod().toUpperCase();
            //Map<String, List<String>> requestParameters = getRequestParameters(exchange.getRequestURI());
            //test = requestParameters.get("test");
            //macaddress = requestParameters.get("macaddress");
            //String responseBody = html + " " + requestMethod + " " + context;
            //responseBody += add_params_if_not_null(test, macaddress);
            String responseBody = "server was stopped by request";
            try {
                switch (requestMethod) {
                    case METHOD_GET:
                        send_response(exchange, STATUS_OK, responseBody);
                        server.stop(0);
                        logger(SERVERLOG, INFO_LEVEL, "[SERVER] shutdown, count of received requests: " + count_of_received_requests);
                        break;
                    default:
                        logger(SERVERLOG, INFO_LEVEL,"[SERVER] case default");
                        break;
                }
            } finally {
                exchange.close();
            }
        });
    }

    @Deprecated
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

    /** There are SERVER implementations
     * @param exchange
     * @param responseCode
     * @param responseBody
     * @throws IOException
     */
    private void send_response(HttpExchange exchange, int responseCode, String responseBody) throws IOException {
        logger(SERVERLOG, INFO_LEVEL, "[SERVER] send responseBody: " + responseBody);
        byte[] rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(responseCode, rawResponseBody.length);
        exchange.getResponseBody().write(rawResponseBody);
        exchange.getResponseBody().close();
    }

    private String read_response(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            builder.append(line);
        }
        return builder.toString();
    }

}
