import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Date;

@Deprecated
class server_API_via_MyHandler implements HttpHandler {

    @Deprecated
    public void handle(HttpExchange he) throws IOException {
        System.out.println(new Date() + " : handle() method");
        InputStream is = he.getRequestBody();
        int request_body = is.read(); // .. read the request body
        System.out.println(new Date() + " : " + request_body);

        String response = "This is the response";
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @Deprecated
    void start() throws IOException, InterruptedException {
        //String host = "192.168.0.103";
        String host = "localhost";
        int port = 8081;
        int BACKLOG = -1;
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port),BACKLOG);
        server.createContext("/xxx", new server_API_via_MyHandler());
        server.setExecutor(null); // creates a default executor!?!?
        server.start();
        Thread.sleep(Long.MAX_VALUE);
    }

}