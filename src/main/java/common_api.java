import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import javax.security.auth.login.Configuration;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

class common_api extends JsonServer{

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

    void write_to_file(String result) throws IOException {
        FileWriter writer = new FileWriter("ServerLog.txt", true);
        writer.write(result);
        writer.append('\n');
        writer.flush();
    }

    void write_response(HttpExchange httpExchange, Headers headers, String responseBody) throws IOException {
        /*String basic_response = "HTTP/1.1 200 OK\r\n" +
                "Server: JsonServer 0.1\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + responseBody.length() + "\r\n" +
                "Connection: close\r\n\r\n";*/
        System.out.println(responseBody);
        //headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        //headers.set("HTTP/1.1", "200 OK");
        headers.set("Server", "JsonServer 0.1");
        headers.set("Content-Length", responseBody.toString());
        //headers.set("Connection", "close");
        final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
        httpExchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
        httpExchange.getResponseBody().write(rawResponseBody);
    }
}
