import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Date;

class server_MyHandler implements HttpHandler {

    public void handle(HttpExchange he) throws IOException {
        System.out.println(new Date() + " : handle() method");
        InputStream is = he.getRequestBody();
        int request_body = is.read(); // .. read the request body
        System.out.println(new Date() + " : " + request_body);

        String response = "This is the response";
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        //os.close();
    }

    String start() throws IOException, InterruptedException {
        System.out.println(new Date() + " : 1");
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8000),-1);
        System.out.println(new Date() + " : 2");
        server.createContext("/xxx", new server_MyHandler());
        System.out.println(new Date() + " : 3");
        server.setExecutor(null); // creates a default executor
        System.out.println(new Date() + " : 4 before start");
        server.start();
        System.out.println(new Date() + " : 5 after start");
        //Thread.sleep(10000);
        System.out.println(new Date() + " : 6 after sleep");
        return "200 OK";
    }

}