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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class client_API  extends common_API{

    private int count_pairs = 5;

    //todo
    @Deprecated
    ArrayList request(String method, String host, int port, String context, String json) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
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
        logger(INFO_LEVEL,"[INF] response code: " + arrayList.get(0));
        //logger(INFO_LEVEL,"[INF] response body: " + arrayList.get(1));
        //}
        /*catch (Exception e) {
            //todo: handle exception
            System.out.println("catch exception! client.execute(request): " + e);
            e.printStackTrace();
        }*/

        String json_response = "";
        try {
            json_response = EntityUtils.toString(server_response.getEntity(), "UTF-8");
            if(show_response_json){
                logger(INFO_LEVEL,"[INF] response_json: " + json_response);
            }
        }
        catch (IOException e) {
            System.out.println("! catch exception 2");
            e.printStackTrace();
        }

        try {
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(json_response);
            if (resultObject instanceof JSONArray) {
                JSONArray array=(JSONArray)resultObject;
                for (Object object : array) {
                    JSONObject obj =(JSONObject)object;
                    logger(INFO_LEVEL, obj.get("measurements").toString());
                }
            } else if (resultObject instanceof JSONObject) {
                JSONObject obj =(JSONObject)resultObject;
                logger(INFO_LEVEL, obj.get("temperature").toString());
            }
        } catch (ParseException e) {
            //todo: handle exception
            logger(DEBUG_LEVEL, "!!! catch exception: JSONParser: " + e);
            arrayList.add(2, e);
            //e.printStackTrace();
        }
        logger(INFO_LEVEL, "[INF] return data: " + arrayList + "\n");
        return arrayList;
    }

    //todo
    @Deprecated
    ArrayList request(String host, int port, String context) throws IOException {
        HttpPost request = new HttpPost(prepare_url(host, port, context));
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(generate_json()));
        logger(INFO_LEVEL, "request string: " + request);

        long start = System.currentTimeMillis();
        HttpResponse response = HttpClients.createDefault().execute(request);
        long finish = System.currentTimeMillis();
        int diff = (int)(finish-start);
        logger(INFO_LEVEL, diff + "ms request");

        ArrayList arrayList = new ArrayList<>();
        arrayList.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        //arrayList.add(1, check_buffer(read_buffer(new StringBuilder(),response).toString(), ""));
        logger(INFO_LEVEL, "return data: " + arrayList + "\n");
        return arrayList;
    }

    String generate_json() throws IOException {
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
            logger(INFO_LEVEL, "generated json: " + result);
        }
        return result;
    }

    private int generate_t_value() {
        Random random = new Random();
        return Math.abs(random.nextInt(1000));
    }

    private String read_buffer(StringBuilder buffer, HttpResponse response) throws IOException {
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

        InputStream in = response.getEntity().getContent(); //Get the data in the entity

        //todo 2nd my variant:
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
        //StringBuilder buffer = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            buffer.append(line);
        }
        if (reader.readLine() == null) {
            logger(INFO_LEVEL, "\n");
        }
        if(show_response_body){
            logger(INFO_LEVEL, "server response body: " + buffer);
        }


        //todo from www http post:
        /*BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }*/

        return buffer.toString();
    }

    /** Check response buffer for expected_patterns and fill new ArrayList_result for returning
     * @param body
     * @param patterns
     * @return
     */
    private ArrayList check_buffer(String body, ArrayList patterns) {
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


    ArrayList get(String url, ArrayList patterns) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);
        long start = System.currentTimeMillis();
        HttpResponse response = client.execute(request);
        long finish = System.currentTimeMillis();
        int diff = (int)(finish-start);
        logger(INFO_LEVEL, diff + "ms request");

        /*Checking response */
        ArrayList what_found = new ArrayList();
        if (response != null) {
            what_found = check_buffer(read_buffer(new StringBuilder(),response),patterns);
        }

        ArrayList result = new ArrayList();
        result.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        for (int i=1; i<what_found.size(); i++) {
            result.add(i, what_found.get(i));
        }
        logger(INFO_LEVEL, "response data: " + result);
        return result;
    }

    ArrayList post(String url, ArrayList patterns, String json) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        /*List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("locale", ""));
        urlParameters.add(new BasicNameValuePair("caller", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));*/
        //request.setEntity(new UrlEncodedFormEntity(urlParameters));
        request.setEntity(new StringEntity(json));

        long start = System.currentTimeMillis();
        HttpResponse response = client.execute(request);
        long finish = System.currentTimeMillis();
        int diff = (int)(finish-start);
        logger(INFO_LEVEL, diff + "ms request");


        String json_response = "";
        try {
            json_response = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if(show_response_json){
                logger(INFO_LEVEL,"full json_response from server: " + json_response);
            }
        }
        catch (IOException e) {
            System.out.println("! catch exception 1");
            e.printStackTrace();
        }

        ArrayList arrayList = new ArrayList();
        try {
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(json_response);
            if (resultObject instanceof JSONArray) {
                JSONArray array=(JSONArray)resultObject;
                for (Object object : array) {
                    JSONObject obj = (JSONObject)object;
                    arrayList.add(0, obj.get("date"));
                    logger(INFO_LEVEL, "JSONParser: " + arrayList.get(0));
                }
            } else if (resultObject instanceof JSONObject) {
                JSONObject obj =(JSONObject)resultObject;
                arrayList.add(0, obj.get("measurements"));
                logger(INFO_LEVEL, "JSONParser: " + arrayList.get(0));
            }


            //todo VARIANT 2
          /*  JSONArray jsonArray = new JSONArray(json_response);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj=jsonArray.getJSONObject(i);
                String str1=obj.getString("name");...}*/

        } catch (ParseException e) {
            //todo: handle exception
            System.out.println( "! catch exception: JSONParser:");
            e.printStackTrace();
        }



/*        ArrayList what_found = check_buffer(read_buffer(new StringBuilder(),response),patterns);
        arrayList.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        for (int i=1; i<what_found.size(); i++) {
            arrayList.add(i, what_found.get(i));
        }*/

        logger(INFO_LEVEL, "return data from server: " + arrayList + "\n");
        return arrayList;
    }

    private String prepare_url(String host, int port, String context){
        return "http://" + host + ":" + port + context;
    }


}
