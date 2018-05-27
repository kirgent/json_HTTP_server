import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class test_requests_with_url extends common_API {
    private http_requests request = new http_requests();

    @Test
    void test_get_request__200_OK() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0,"");
        expected.add(1, "<title>Google</title>");

        ArrayList actual = request.get("http://google.ru", expected);
        assertEquals(expected200, actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    void test_get_request__200_OK__search() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0,"");
        expected.add(1, "<title>httpClient - ");

        ArrayList actual = request.get("http://www.google.com/search?q=httpClient", expected);
        assertEquals(expected200, actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    void test_get_request__404_Not_Found() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0,"");
        expected.add(1, "<title>Error 404 (Not Found)!!!</title>");
        expected.add(2, "The requested URL <code>/somepage</code> was not found on this server.");

        ArrayList actual = request.get("http://google.ru/somepage", expected);
        assertEquals(expected404, actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    void test_post_request__403_Forbidden() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0,"");
        expected.add(1, "<title>403 Forbidden</title>");

        ArrayList actual = request.post("https://selfsolve.apple.com/wcResults.do", expected);
        assertEquals(expected403, actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    void test_get_request__localhost________() throws IOException, InterruptedException {
        String host = "localhost";
        common_API api = new common_API();
        HttpServer server = HttpServer.create(new InetSocketAddress(host, 8080), BACKLOG);
        api.process_context_main("/", server);
        api.process_context_reminders("/reminders", server);
        api.process_context_stop("/stop", server);
        server.start();
        Thread.sleep(Long.MAX_VALUE);
/*
        ArrayList expected = new ArrayList();
        expected.add(0,"");
        expected.add(1, "json_HTTP_server: GET /");
        expected.add(2, "json_HTTP_server: GET /reminders");
        expected.add(3, "json_HTTP_server: GET /stop");

        ArrayList actual = request.get("http://localhost:8080/", expected);
        assertEquals(expected200, actual.get(0));
        assertEquals("json_HTTP_server: GET /", actual.get(1));


        actual = request.get("http://localhost:8080/reminders", expected);
        assertEquals(expected200, actual.get(0));
        assertEquals("json_HTTP_server: GET /reminders", actual.get(2));


        actual = request.get("http://localhost:8080/stop", expected);
        assertEquals(expected200, actual.get(0));
        assertEquals("json_HTTP_server: GET /stop", actual.get(3));*/
    }

    @Test
    void test_get_request__TO_TEST() throws IOException {
        String host = "192.168.0.103";
        String post = "8080";
        ArrayList expected = new ArrayList();
        expected.add(0,"");
        expected.add(1, "json_server 0.1: GET /reminders");

        ArrayList actual = request.get("http://" + host + ":" + port + "/reminders?test=1", expected);
        assertEquals(expected200, actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

}
