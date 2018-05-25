import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
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
        System.out.print("request time: " + (int)(finish-start) + "ms");

        ArrayList what_found = check_buffer(read_buffer(new StringBuilder(),response).toString(),patterns);

        ArrayList result = new ArrayList();
        result.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        for (int i=1; i<what_found.size(); i++) {
            result.add(i, what_found.get(i));
        }
        System.out.println("response data: " + result);
        return result;
    }

    ArrayList post(String url, ArrayList patterns) throws IOException {
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
        System.out.print("request time: " + (int)(finish-start));

        ArrayList what_found = check_buffer(read_buffer(new StringBuilder(),response).toString(),patterns);

        ArrayList result = new ArrayList();
        result.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        for (int i=1; i<what_found.size(); i++) {
            result.add(i, what_found.get(i));
        }


        /*BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }*/
        System.out.println("response data: " + result);
        return result;
    }

}
