import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class test_JSON_HTTP_server extends common_API {
    private common_API api = new common_API();

    @Test
    void test_context_reminders() throws IOException {
        //final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        String context = "/reminders";
        String json = "{\"temperature\":\"500\"}";
        //api.process_context_main("/", server);
        //api.process_context_reminders(context, server);
        //server.start();
        //ArrayList actual = api.request(host, port, context);
        //assertEquals(expected200, actual.get(0));
        ArrayList actual = api.http_request(METHOD_GET, host, port, context, json);
        assertEquals(expected200, actual.get(0));
        assertEquals("", "Unexpected token END OF FILE at position 0.");
        //server.stop(-1);
    }

    @Test
    void test_context_reminders__empty_json() throws IOException {
        //final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        String context = "/reminders";
        String json = "{}";
        //api.process_context_main("/", server);
        //api.process_context_reminders(context, server);
        //server.start();
        //ArrayList actual = api.request(host, port, context);
        //assertEquals(expected200, actual.get(0));
        ArrayList actual = api.http_request(METHOD_GET, "google.com", 80, context, json);
        assertEquals(expected200, actual.get(0));
        assertEquals("Unexpected token END OF FILE at position 0.", actual.get(2));
        //server.stop(-1);
    }

    @Test
    void test_context_X__404_NOT_FOUND() throws IOException {
        //final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        String context = "/reminders";
        String json = "{}";
        //api.process_context_main("/", server);
        //api.process_context_reminders(context, server);
        //server.start();
        //ArrayList actual = api.request(host, port, context);
        //assertEquals(expected200, actual.get(0));
        ArrayList actual = api.http_request(METHOD_GET, host, port, "/X", json);
        assertEquals(expected404, actual.get(0));
        //server.stop(-1);
    }

    @Test
    void test_server_MyHandler() throws IOException, InterruptedException {
        server_MyHandler handler = new server_MyHandler();
        String actual = handler.start();
        assertEquals(expected200, actual);
    }


}