import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;

class http_requests extends common_API {


    ArrayList get(String url, ArrayList patterns) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);
        long start = System.currentTimeMillis();
        HttpResponse response = client.execute(request);
        long finish = System.currentTimeMillis();
        System.out.println("request time: " + (int)(finish-start) + "ms");

        ArrayList result = new ArrayList();
        patterns = check_body_response(read_buffer(new StringBuilder(),response).toString(),patterns);

        result.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        result.add(1, patterns.get(1));
        result.add(2, patterns.get(2));
        System.out.println("response data: " + result);
        return result;
    }

    ArrayList post(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("locale", ""));
        urlParameters.add(new BasicNameValuePair("caller", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        long start = System.currentTimeMillis();
        HttpResponse response = client.execute(post);
        long finish = System.currentTimeMillis();
        System.out.println("request time: " + (int)(finish-start));
        ArrayList result = new ArrayList();
        result.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());


        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        result.add(1, buffer);
        System.out.println("response data: " + result);
        return result;
    }

}
