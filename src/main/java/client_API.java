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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class client_API  extends common_API{

    private boolean show_response_json = true;
    private boolean show_response_body = true;
    private boolean show_generated_json = true;

    private ArrayList client_config = new ArrayList();

    ArrayList read_client_config(String fileName) throws IOException {
        Properties property = new Properties();
        property.load(new FileInputStream(fileName));
        client_config.add(0, property.getProperty("server_host"));
        client_config.add(1, property.getProperty("server_port"));
        return client_config;
    }

    //todo
    @Deprecated
    ArrayList request(String method, String host, int port, String context, String json) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(prepare_url(host, port, context, ""));
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
        logger(CLIENTLOG, INFO_LEVEL,"response code: " + arrayList.get(0));

        String json_response = "";

        json_response = EntityUtils.toString(server_response.getEntity(), "UTF-8");
        if(show_response_json){
            logger(CLIENTLOG, INFO_LEVEL,"response_json: " + json_response);
        }

        try {
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(json_response);
            if (resultObject instanceof JSONArray) {
                JSONArray array=(JSONArray)resultObject;
                for (Object object : array) {
                    JSONObject obj =(JSONObject)object;
                    logger(CLIENTLOG, INFO_LEVEL, obj.get("measurements").toString());
                }
            } else if (resultObject instanceof JSONObject) {
                JSONObject obj =(JSONObject)resultObject;
                logger(CLIENTLOG, INFO_LEVEL, obj.get("temperature").toString());
            }
        } catch (ParseException e) {
            //todo: handle exception
            logger(CLIENTLOG, INFO_LEVEL, "!catch exception: JSONParser: " + e);
            arrayList.add(2, e);
            //e.printStackTrace();
        }
        logger(CLIENTLOG, INFO_LEVEL, "filtered data: " + arrayList + "\n");
        return arrayList;
    }

    //todo
    @Deprecated
    ArrayList request(String host, int port, String context) throws IOException {
        HttpPost request = new HttpPost(prepare_url(host, port, context, ""));
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(generate_json(1)));
        logger(CLIENTLOG, INFO_LEVEL, "request string: " + request);

        long start = System.currentTimeMillis();
        HttpResponse response = HttpClients.createDefault().execute(request);
        long finish = System.currentTimeMillis();
        int diff = (int)(finish-start);
        logger(CLIENTLOG, INFO_LEVEL, diff + "ms request");

        ArrayList arrayList = new ArrayList<>();
        arrayList.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        //arrayList.add(1, check_buffer(read_buffer(new StringBuilder(),response).toString(), ""));
        logger(CLIENTLOG, INFO_LEVEL, "return data: " + arrayList + "\n");
        return arrayList;
    }

    String generate_json(int count_pairs) throws IOException {
        JSONObject json = new JSONObject();
        JSONArray array_measurements = new JSONArray();
        json.put("measurements", array_measurements);
        for (int i = 0; i < count_pairs; i++) {
            JSONObject object_in_measurements = new JSONObject();

            object_in_measurements.put("date", (int) (System.currentTimeMillis() / 1000L));
            object_in_measurements.put("temperature", generate_t_value());
            object_in_measurements.put("unit", "C");
            //object_in_measurements.put("unit", "K");
            //object_in_measurements.put("unit", "F");
            array_measurements.add(object_in_measurements);
        }
        return json.toString();
    }

    private int generate_t_value() {
        return Math.abs(new Random().nextInt(1000));
    }

    private String read_response(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            builder.append(line);
            //todo
            /*if (reader.readLine() == null) {
                logger(INFO_LEVEL, "\n");
            }*/
        }
        return builder.toString();
    }

    private void check_responsebody(String responseBody, ArrayList patterns, ArrayList arrayList) {
        for (int i=1; i<patterns.size(); i++){
            if(responseBody.contains(patterns.get(i).toString())) {
                int a = responseBody.indexOf(patterns.get(i).toString());
                int l = patterns.get(i).toString().length();
                arrayList.add(i,responseBody.substring(a, a+l));
            } else {
                arrayList.add(i,"<>");
            }
        }
    }


    ArrayList get(String url, ArrayList patterns) throws IOException {
        logger(CLIENTLOG, INFO_LEVEL, new Date() + ": get request to: " + url);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader(ACCEPT,"application/json");
        request.addHeader(CONTENT_TYPE, "application/json");
        //request.addHeader(USER_AGENT, "afent");

        long start = System.currentTimeMillis();
        HttpResponse response = client.execute(request);
        long finish = System.currentTimeMillis();
        int diff = (int)(finish-start);
        logger(CLIENTLOG, INFO_LEVEL, diff + "ms request");

        String responseBody = read_response(response);
        if(show_response_body){
            logger(CLIENTLOG, INFO_LEVEL, "responseBody: " + responseBody);
        }

        ArrayList arrayList = new ArrayList();
        arrayList.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        for (int i=1; i<patterns.size(); i++){
            if(responseBody.contains(patterns.get(i).toString())) {
                arrayList.add(i,patterns.get(i));
            } else {
                arrayList.add(i,"<>");
            }
        }

        logger(CLIENTLOG, INFO_LEVEL, "filtered  data: " + arrayList);
        return arrayList;
    }

    ArrayList post(String url, ArrayList patterns, String json, boolean parse_json) throws IOException {
        logger(CLIENTLOG, INFO_LEVEL, "post request to: " + url);
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        //request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        /*List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("locale", ""));
        urlParameters.add(new BasicNameValuePair("caller", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));*/
        //request.setEntity(new UrlEncodedFormEntity(urlParameters));
        if(show_generated_json) {
            logger(CLIENTLOG, INFO_LEVEL, "generated json: " + json);
        }
        request.setEntity(new StringEntity(json));

        long start = System.currentTimeMillis();
        HttpResponse response = client.execute(request);
        long finish = System.currentTimeMillis();
        int diff = (int)(finish-start);
        logger(CLIENTLOG, INFO_LEVEL, diff + "ms request");


        String responseBody = read_response(response);
        if(show_response_body){
            logger(CLIENTLOG, INFO_LEVEL, "responseBody: " + responseBody);
        }


        //todo
        ArrayList arrayList = new ArrayList();
        arrayList.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        check_responsebody(responseBody, patterns, arrayList);
        /*String responsebody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        if(show_response_json){
            logger(INFO_LEVEL,"response body: " + responsebody);
        }*/

        if(parse_json) {
            ArrayList arrayList_parsed = new ArrayList();
            try {
                JSONParser parser = new JSONParser();
                Object resultObject = parser.parse(responseBody);

                if (resultObject instanceof JSONArray) {
                    JSONArray array = (JSONArray) resultObject;
                    for (Object object : array) {
                        JSONObject obj = (JSONObject) object;
                        arrayList_parsed.add(obj.get("measurements"));
                        //parsed.add(obj.get("date"));
                        logger(CLIENTLOG, INFO_LEVEL, "JSONArray JSONParser-ed data: " + arrayList_parsed);
                    }
                } else if (resultObject instanceof JSONObject) {
                    JSONObject obj = (JSONObject) resultObject;
                    arrayList_parsed.add(obj.get("measurements"));
                    //parsed.add(obj.get("date"));
                    logger(CLIENTLOG, INFO_LEVEL, "JSONObject JSONParser-ed data: " + arrayList_parsed);
                }
            } catch (ParseException e) {
                //todo: handle exception
                System.out.println("catch exception: JSONParser: seems not json format");
                e.printStackTrace();
            }

            logger(CLIENTLOG, INFO_LEVEL, "filtered data parsed: " + arrayList_parsed);
        }

        logger(CLIENTLOG, INFO_LEVEL, "filtered data: " + arrayList + "\n");
        return arrayList;
    }

    String prepare_url(String host, int port, String context, String params){
        if(params.isEmpty()) {
            return "http://" + host + ":" + port + context;
        } else {
            return "http://" + host + ":" + port + context + "?" + params;
        }
    }


}
