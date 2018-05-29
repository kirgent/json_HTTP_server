import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class testServer_requests_with_url extends client_API {
    private String host = "localhost";
    private int port = 8080;

    private ArrayList expected = new ArrayList();

    @Test
    void test_1_get_request__200_OK() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0, expected200);
        expected.add(1, "<title>Google</title>");

        ArrayList actual = get("http://google.ru", expected);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    void test_2_get_request__200_OK__search() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0, expected200);
        expected.add(1, "<title>httpClient - ");

        ArrayList actual = get("http://www.google.com/search?q=httpClient", expected);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    void test_3_get_request__404_Not_Found() throws IOException {
        expected.add(0, expected404);
        expected.add(1, "<title>Error 404 (Not Found)!!!</title>");
        expected.add(2, "The requested URL <code>/somepage</code> was not found on this server.");

        ArrayList actual = get("http://google.ru/somepage", expected);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    void test_4_post_request__403_Forbidden() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0, expected403);
        expected.add(1, "<title>403 Forbidden</title>");

        ArrayList actual = post("https://selfsolve.apple.com/wcResults.do", expected, "");
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    @Disabled
    void test_get_request__TO_TEST() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0, expected200);
        expected.add(1, "json_server 0.1: GET /reminders");
        expected.add(2, "somebody");

        String host = "localhost";
        String port = "8080";
        String context = "/temperature";
        String params = "test=test";
        String url = "http://" + host + ":" + port + context + "?" + params;
        String json = generate_json();

        ArrayList actual = post(url, expected, json);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    @Disabled
    void test_server_MyHandler() throws IOException, InterruptedException {
        server_API_via_MyHandler handler = new server_API_via_MyHandler();
        handler.start();
        //assertEquals(expected200, actual);
    }
}
