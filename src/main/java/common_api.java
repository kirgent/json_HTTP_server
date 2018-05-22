import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class common_api extends JsonHTTPServer {

    List<String> testname;
    List<String> macaddress;
    List count_reminders;
    List count_iterations;

    String html = "Testing of reminders";
    /*private String list_params = "testname=" + testname.get(0) + ", " +
            "macaddress=" + macaddress.get(0) + ", " +
            "count_reminders="+ count_reminders.get(0) + ", " +
            "count_iterations=" + count_iterations.get(0);*/
    ArrayList list_params;
    final String expected200 = "200 OK";
    final String expected404 = "404 Not Found";
    final String expected500 = "500 Internal Server Error";
    final String expected504 = "504 Server data timeout";
    private boolean show_debug_level = false;
    private boolean show_info_level = false;
    private boolean show_generated_json = true;
    private int count_pairs = 10;
    String host = "localhost";
    int port = 8080;
    private boolean show_response_body = true;

    private String prepare_url(String host, int port, String context){
        return "http://" + host + ":" + port + context;
    }

    private StringBuilder read_response(StringBuilder body, HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        //StringBuilder body = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            if(show_response_body) {
                System.out.print(", response body: " + body.append(line));
            }else{
                body.append(line);
            }
            if (reader.readLine() == null) {
                System.out.println();
            }
        }
        return body;
    }


    private String check_body_response(String body) {
        String result = "";
               if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"Request not accomplished\"")){
            result += "Request not accomplished";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-ST-001 Box is not registered\"")){
            result += "REM-ST-001 Box is not registered";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"unknown MAC\"")){
            result += "unknown MAC";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"STB not available\"")){
            result += "STB not available";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"Timeout detected by BoxResponseTracker")){
            result += "Timeout detected by BoxResponseTracker";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: missing program start\"")){
            result += "REM-008 Reminders parsing error: missing program start";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: invalid program start\"")){
            result += "REM-008 Reminders parsing error: invalid program start";
        }
        //if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: missing channel number\"")){
        //result += "REM-008 Reminders parsing error: missing channel number";
        //}
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: invalid channel number\"")){
            result += "REM-008 Reminders parsing error: invalid channel number";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: missing offset\"")){
            result += "REM-008 Reminders parsing error: missing offset";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: invalid offset\"")){
            result += "REM-008 Reminders parsing error: invalid offset";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: incorrect reminderScheduleId\"")){
            result += "REM-008 Reminders parsing error: incorrect reminderScheduleId";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: wrong number of reminders\"")){
            result += "REM-008 Reminders parsing error: wrong number of reminders";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"Incorrect request: ChangeReminders\"")){
            result += "Incorrect request: ChangeReminders";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"Incorrect request: blablabla\"")){
            result += "Incorrect request: blablabla";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"name cannot be null\"")){
            result += "name cannot be null";
        }
        if(body.contains("REM-008 Reminders parsing error: wrong deviceId")){
            result += "REM-008 Reminders parsing error: wrong deviceId";
        }
        if(body.contains("REM-008 Reminders parsing error: wrong operation")){
            result += "REM-008 Reminders parsing error: wrong operation";
        }
        if(body.contains("REM-008 Reminders parsing error: incorrect message format")){
            result += "REM-008 Reminders parsing error: incorrect message format";
        }
        if(body.contains("REM-008 Reminders parsing error: incorrect reminderId")){
            result += "REM-008 Reminders parsing error: incorrect reminderId";
        }
        if(body.contains("incorrect value")){
            result += "incorrect value";
        }
        if(body.contains("SET-025 Unsupported data type: Not a JSON Object:")){
            result += "SET-025 Unsupported data type: Not a JSON Object";
        }
        if(body.contains("responseCode\":\"ERROR_SCHEDULING_REMINDER")){
            result += "ERROR_SCHEDULING_REMINDER";
        }
        //if(Objects.equals(result, "")){
        //result = " ";
        //}
        //System.out.println("[DBG] check_body_for_statuscode: result: " + result);
        return result;
    }


    ArrayList request(String host, int port, String context) throws IOException {
        //if(show_debug_info) {
        /*System.out.println("[INF] " + new Date() + ": " + operation + " for macaddress=" + mac + " to ams_ip=" + ams_ip + ", "
                + "count_reminders=" + count_reminders + ", "
                + "reminderProgramStart=multi, "
                + "reminderChannelNumber=" + reminderChannelNumber + ", "
                + "reminderProgramId=" + reminderProgramId + ", "
                + "reminderOffset=" + reminderOffset + ", "
                + "reminderScheduleId=multi, "
                + "reminderId=multi");*/
        //}

        HttpPost request = new HttpPost(prepare_url(host, port, context));
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(generate_json_reminder()));
        if(show_debug_level) {
            System.out.println("[DBG] request string: " + request);
        }

        long start = System.currentTimeMillis();
        HttpResponse response = HttpClients.createDefault().execute(request);
        long finish = System.currentTimeMillis();
        int diff = (int)(finish-start);
        System.out.print("[INF] " + diff + "ms request");

        ArrayList arrayList = new ArrayList();
        arrayList.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        arrayList.add(1, check_body_response(read_response(new StringBuilder(),response).toString()));


        if(show_info_level) {
            System.out.println("[INF] return data: " + arrayList + "\n");
        }
        return arrayList;
    }

    private String generate_json_reminder() {
        JSONObject json = new JSONObject();

        JSONArray array_measurements = new JSONArray();
        json.put("measurements", array_measurements);
        for (int i = 0; i < count_pairs; i++) {
            JSONObject object_in_measurements = new JSONObject();
            object_in_measurements.put("date", new Date());
            object_in_measurements.put("temperature", t_value());
            object_in_measurements.put("unit", "C");

            array_measurements.add(object_in_measurements);
        }

        String result = json.toString();
        if(show_generated_json) {
            System.out.println("[JSON] generated json: " + result);
        }
        return result;
    }

    private int t_value() {
        Random random = new Random();
        return Math.abs(random.nextInt(1000));
    }

    void process_context_main(String context, HttpServer server){
        server.createContext(context, httpExchange -> {
            try {
                Headers headers = httpExchange.getResponseHeaders();
                String requestMethod = httpExchange.getRequestMethod().toUpperCase();
                switch (requestMethod) {
                    case METHOD_GET:
                        //<a href="URL">текст_ссылки</a>
                        /*String example = "Example of reminders request: http://localhost:8080/reminders?" +
                                "test=test1&" +
                                "macaddress=123456789012&" +
                                "count_reminders=X&" +
                                "count_iterations=Y";*/
                        /*String s1 = "List of tests for NewAPI:\n test1_Add_Purge, test2_Add_Delete_Purge, test3_Add_Modify_Delete_Purge\n\n" +
                                "List of tests for OldAPI:\n" +
                                "test1_Add_Purge, " +
                                "test2_Add_Delete_Purge";*/
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
    }

    ArrayList process_context_reminders(String context, HttpServer server) {
        ArrayList arrayList = new ArrayList();
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
                switch (requestMethod) {
                    case METHOD_GET:
                        //Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                        // do something with the request parameters

                        /*if(macaddress.length()>12){
                            System.out.println("Wrong macaddress !!!" + macaddress.length());
                        }
                        if(!count_reminders.getClass().equals("Integer")){
                            System.out.println("Wrong count_reminders type !!!" + count_reminders.getClass());
                        }*/

                        String responseBody = html + "\n" +
                                requestMethod + "\n" +
                                "/reminders" + "\n" +
                                //list_params;
                                testname + "\n" +
                                testname.get(0);
                        print_out(responseBody);
                        write_file(responseBody);
                        write_response(httpExchange, headers, responseBody);
                        break;
                    case METHOD_POST:
                        responseBody = html + "\n" +
                                requestMethod + "\n" +
                                context + "\n" +
                                list_params;

                        print_out(responseBody);
                        write_file(responseBody);
                        break;
                    case METHOD_OPTIONS:
                        responseBody = html + "\n" +
                                requestMethod + "\n" +
                                context + "\n" +
                                list_params;
                        print_out(responseBody);
                        write_file(responseBody);

                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        httpExchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        responseBody = html + "\n" +
                                requestMethod + "\n" +
                                "/reminders" + "\n" +
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
        return arrayList;
    }

    void process_context_stop(String context, HttpServer server) {
        server.createContext(context, httpExchange -> {
            try {
                Headers headers = httpExchange.getResponseHeaders();
                String requestMethod = httpExchange.getRequestMethod().toUpperCase();
                Map<String, List<String>> requestParameters = getRequestParameters(httpExchange.getRequestURI());
                testname = requestParameters.get("testname");
                macaddress = requestParameters.get("macaddress");
                count_reminders = requestParameters.get("count_reminders");
                count_iterations = requestParameters.get("count_iterations");
                switch (requestMethod) {
                    case METHOD_GET:
                        String responseBody = html + "\n" +
                                requestMethod + "\n" +
                                context + "\n" +
                                list_params;
                        print_out(responseBody);
                        write_file(responseBody);
                        write_response(httpExchange, headers, responseBody);
                        server.stop(0);
                        break;
                }
            } finally {
                httpExchange.close();
            }
        });

    }

    ArrayList read_config() throws IOException {
        ArrayList list = new ArrayList();
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
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    void print_out(String string) {
        System.out.println(string);
    }

    void write_file(String string) throws IOException {
        FileWriter writer = new FileWriter("ServerLog.txt", true);
        writer.write(string);
        writer.append('\n');
        writer.flush();
    }

    void write_response(HttpExchange httpExchange, Headers headers, String responseBody) throws IOException {
        System.out.println(responseBody);
        //headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        //headers.set("HTTP/1.1", "200 OK");
        headers.set("Server", "JsonHTTPServer 0.1");
        headers.set("Content-Length", responseBody);
        //headers.set("Connection", "close");
        byte[] rawResponseBody = responseBody.getBytes(CHARSET);
        httpExchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
        httpExchange.getResponseBody().write(rawResponseBody);
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
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }

}
