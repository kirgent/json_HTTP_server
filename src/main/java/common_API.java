import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.http.HttpHeaders.ALLOW;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

class common_API extends JsonHTTPServer {

    final String expected200 = "200 OK";
    final String expected201 = "201 Created";
    final String expected400 = "400 Bad Request";
    final String expected403 = "403 Forbidden";
    final String expected404 = "404 Not Found";
    final String expected500 = "500 Internal Server Error";
    final String expected504 = "504 Server data timeout";

    boolean show_response_body = true;
    boolean show_response_json = false;
    private boolean show_debug_level = true;
    private boolean show_info_level = true;
    private boolean show_generated_json = true;
    private int count_pairs = 5;
    String host = "localhost";
    int port = 8080;

    private List<String> testname;
    private List<String> macaddress;
    private List<String> count_reminders;
    private List<String> count_iterations;

    private String html = "json_HTTP_server:";
    /*private String list_params = "testname=" + testname.get(0) + ", " +
            "macaddress=" + macaddress.get(0) + ", " +
            "count_reminders="+ count_reminders.get(0) + ", " +
            "count_iterations=" + count_iterations.get(0);*/
    private ArrayList list_params;



    private String prepare_url(String host, int port, String context){
        return "http://" + host + ":" + port + context;
    }

    StringBuilder read_buffer(StringBuilder buffer, HttpResponse response) throws IOException {
        /*//todo 1st: from www http get:
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        //StringBuffer buffer = new StringBuffer();
        String line;
        StringBuilder s = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            s.append(buffer.append(line));
            System.out.print("response body: " + s);
        }
        return buffer;*/


        //todo 2nd my variant:
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
        //StringBuilder buffer = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            buffer.append(line);
        }
        if (reader.readLine() == null) {
            System.out.println();
        }
        if(show_response_body){
            System.out.println("response body: " + buffer);
        }


        //todo from www http post:
        /*BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }*/

        return buffer;
    }


    /** Check response buffer for expected_patterns and fill new ArrayList_result for returning
     * @param body
     * @param patterns
     * @return
     */
    ArrayList check_buffer(String body, ArrayList patterns) {
        ArrayList result = new ArrayList();
        for (int i=0; i<patterns.size(); i++){
            if(body.contains(patterns.get(i).toString())) {
                result.add(i,patterns.get(i));
            } else {
                result.add(i,"<>");
            }
        }
        return result;
    }


    ArrayList http_request(String method, String host, int port, String context, String json) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        //HttpResponse httpresponse = null;
        //try {
            //if (Objects.equals(method, METHOD_GET)) {
                print_out("vetka if GET");
                HttpGet request = new HttpGet(prepare_url(host, port, context));
                //request.addHeader("User-Agent", USER_AGENT);
                request.setHeader("content-type", "application/json");
                //request.setHeader("Connection", "close");
                //request.setEntity(new StringEntity(generate_json()));
                HttpResponse server_response = client.execute(request);

            /*} else if (Objects.equals(method, METHOD_POST)) {
                System.out.println("vetka if POST");
                HttpPost request = new HttpPost(prepare_url(host, port, context));
                request.setHeader("content-type", "application/x-www-form-urlencoded");
                //request.setHeader("content-type", "application/json");
                //request.setHeader("Connection", "close");
                request.setEntity(new StringEntity(generate_json()));
                httpresponse = client.execute(request);
            }*/
            ArrayList arrayList = new ArrayList();
            arrayList.add(0, server_response.getStatusLine().getStatusCode() + " " + server_response.getStatusLine().getReasonPhrase());
            //arrayList.add(1, check_body_response(read_response(new StringBuilder(),server_response).toString()));
            print_out("[INF] response code: " + arrayList.get(0));
            print_out("[INF] response body: " + arrayList.get(1));
        //}
        /*catch (Exception e) {
            //todo: handle exception
            System.out.println("catch exception! client.execute(request): " + e);
            e.printStackTrace();
        }*/

        String responsejson = "";
        try {
            responsejson = EntityUtils.toString(server_response.getEntity(), "UTF-8");
            if(show_response_json){
                print_out("[INF] response_json: " + responsejson);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(responsejson);
            if (resultObject instanceof JSONArray) {
                JSONArray array=(JSONArray)resultObject;
                for (Object object : array) {
                    JSONObject obj =(JSONObject)object;
                    System.out.println(obj.get("measurements"));
                }
            } else if (resultObject instanceof JSONObject) {
                JSONObject obj =(JSONObject)resultObject;
                System.out.println(obj.get("temperature"));
            }
        } catch (ParseException e) {
            //todo: handle exception
            System.out.println("!!! catch exception: JSONParser: " + e);
            arrayList.add(2, e);
            //e.printStackTrace();
        }
        if(show_info_level) {
            System.out.println("[INF] return data: " + arrayList + "\n");
        }
        return arrayList;
    }

    ArrayList request(String host, int port, String context) throws IOException {
        HttpPost request = new HttpPost(prepare_url(host, port, context));
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(generate_json()));
        if(show_debug_level) {
            System.out.println("[DBG] request string: " + request);
        }

        long start = System.currentTimeMillis();
        HttpResponse response = HttpClients.createDefault().execute(request);
        long finish = System.currentTimeMillis();
        int diff = (int)(finish-start);
        System.out.print("[INF] " + diff + "ms request");

        ArrayList arrayList = new ArrayList<>();
        arrayList.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        //arrayList.add(1, check_buffer(read_buffer(new StringBuilder(),response).toString(), ""));

        if(show_info_level) {
            System.out.println("[INF] return data: " + arrayList + "\n");
        }
        return arrayList;
    }

    private String generate_json() {
        JSONObject json = new JSONObject();
        JSONArray array_measurements = new JSONArray();
        json.put("measurements", array_measurements);
        for (int i = 0; i < count_pairs; i++) {
            JSONObject object_in_measurements = new JSONObject();
            object_in_measurements.put("date", new Date());
            object_in_measurements.put("temperature", generate_t_value());
            object_in_measurements.put("unit", "C");
            //object_in_measurements.put("unit", "K");
            //object_in_measurements.put("unit", "F");
            array_measurements.add(object_in_measurements);
        }

        String result = json.toString();
        if(show_generated_json) {
            System.out.println("[JSON] generated json: " + result);
        }
        return result;
    }

    private int generate_t_value() {
        Random random = new Random();
        return Math.abs(random.nextInt(1000));
    }

    void process_context_main(String context, HttpServer server){
        System.out.println("context:" + context);
        server.createContext(context, httpExchange -> {
            try {
                Headers headers = httpExchange.getResponseHeaders();
                String requestMethod = httpExchange.getRequestMethod().toUpperCase();
                String responseBody = html + " " + requestMethod + " " + context + " " + list_params;
                print_out(responseBody);
                write_file(responseBody);
                switch (requestMethod) {
                    case METHOD_GET:
                        print_out("case METHOD_GET");
                        Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                        testname = requestParameters.get("testname");
                        macaddress = requestParameters.get("macaddress");
                        count_reminders = requestParameters.get("count_reminders");
                        count_iterations = requestParameters.get("count_iterations");
                        http_response(httpExchange, headers, responseBody);
                        break;
                    case METHOD_POST:
                        print_out("case METHOD_POST");
                        http_response(httpExchange, headers, responseBody);
                        break;
                    case METHOD_OPTIONS:
                        print_out("case METHOD_OPTIONS");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        print_out("case default");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                httpExchange.close();
            }
        });
    }

    void process_context_reminders(String context, HttpServer server) {
        System.out.println("context:" + context);
        //ArrayList arrayList = new ArrayList();
        server.createContext(context, httpExchange -> {
            try {
                Headers headers = httpExchange.getResponseHeaders();
                //System.out.println(headers.get(HEADER_CONTENT_TYPE).toString());
                String requestMethod = httpExchange.getRequestMethod().toUpperCase();
                Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                testname = requestParameters.get("testname");
                macaddress = requestParameters.get("macaddress");
                count_reminders = requestParameters.get("count_reminders");
                count_iterations = requestParameters.get("count_iterations");
                String responseBody = html + " " + requestMethod + " " + context + " " + list_params;
                print_out(responseBody);
                write_file(responseBody);
                switch (requestMethod) {
                    case METHOD_GET:
                        print_out("case METHOD_GET");
                        //Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                        // do something with the request parameters
                        http_response(httpExchange, headers, responseBody);
                        break;
                    case METHOD_POST:
                        print_out("case METHOD_POST");
                        break;
                    case METHOD_OPTIONS:
                        print_out("case METHOD_OPTIONS");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        print_out("case default");
                        headers.set(ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                httpExchange.close();
            }
        });
    }

    void process_context_stop(String context, HttpServer server) {
        System.out.println("context:" + context);
        server.createContext(context, httpExchange -> {
            try {
                Headers headers = httpExchange.getResponseHeaders();
                String requestMethod = httpExchange.getRequestMethod().toUpperCase();
                Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                testname = requestParameters.get("testname");
                macaddress = requestParameters.get("macaddress");
                count_reminders = requestParameters.get("count_reminders");
                count_iterations = requestParameters.get("count_iterations");
                String responseBody = html + " " + requestMethod + " " + context + " " + list_params;
                print_out(responseBody);
                write_file(responseBody);
                switch (requestMethod) {
                    case METHOD_GET:
                        print_out("case METHOD_GET");
                        http_response(httpExchange, headers, responseBody);
                        server.stop(0);
                        break;
                    default:
                        print_out("case default");
                        break;
                }
            } finally {
                httpExchange.close();
            }
        });
    }

    ArrayList<String> read_config() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        Properties p = new Properties();
        String fileName= "src/main/resources/config.properties";
        p.load(new FileInputStream(fileName));

        list.add(0, p.getProperty("app.name", "default_name"));
        list.add(1, p.getProperty("app.version", "default_version"));
        list.add(2, p.getProperty("app.vendor","Java"));

        //System.out.println("app.name=" + list.get(0));
        //System.out.println("app.version=" + list.get(1));
        //System.out.println("app.vendor=" + list.get(2));
        return list;
        }

    void write_config(String key, String value) throws IOException {
            try {
                Properties p = new Properties();
                p.setProperty(key, value);

                FileOutputStream os = new FileOutputStream(new File("src/main/resources/config.properties"));
                p.store(os, "Favorite Things");
                os.close();
            } catch (Exception e) {
                System.out.println("catch exception! write_config: " + e);
                e.printStackTrace();
            }


        }

    private void print_out(String string) {
        System.out.println(string);
    }

    private void write_file(String string) throws IOException {
        FileWriter writer = new FileWriter("ServerLog.txt", true);
        writer.write(string);
        writer.append('\n');
        writer.flush();
    }

    private void http_response(HttpExchange httpExchange, Headers headers, String responseBody) throws IOException {
        System.out.println(responseBody);
        headers.set(CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        headers.set("HTTP/1.1", "200 OK");
        headers.set("Server", "JsonHTTPServer 0.1");
        //headers.set("Content-Length", responseBody);
        //headers.set("Connection", "close");
        byte[] rawResponseBody = responseBody.getBytes(CHARSET);
        httpExchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
        httpExchange.getResponseBody().write(rawResponseBody);
    }

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

    private static String decodeUrlComponent(String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e);
        }
    }

}
