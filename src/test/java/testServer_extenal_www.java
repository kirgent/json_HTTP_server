import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class testServer_extenal_www extends client_API {
    private String host = "localhost";
    private int port = 8080;

    private ArrayList expected = new ArrayList();

    @Test
    void test_1_get_request__200_OK() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "<title>Google</title>");

        ArrayList actual = get("http://google.ru", expected);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void test_2_get_request__200_OK__search() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "<title>httpClient - ");

        ArrayList actual = get("http://www.google.com/search?q=httpClient", expected);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void test_3_get_request__404_Not_Found() throws IOException {
        expected.add(0, expected404);
        expected.add(1, "<title>Error 404 (Not Found)!!!</title>");
        expected.add(2, "The requested URL <code>/somepage</code> was not found on this server.");

        ArrayList actual = get("http://google.ru/somepage", expected);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void test_4_post_request__403_Forbidden() throws IOException {
        expected.add(0, expected403);
        expected.add(1, "<title>403 Forbidden</title>");

        ArrayList actual = post("https://selfsolve.apple.com/wcResults.do", expected, "", false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    @Disabled
    void test_get_request__TO_TEST() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "json_server 0.1: GET /reminders");
        expected.add(2, "somebody");

        String url = prepare_url(host, port, "/temperature", "test=test1");
        String json = generate_json(1)
                ;
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

}
