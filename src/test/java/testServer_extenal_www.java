import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
@Deprecated
class testServer_extenal_www extends client_API {
    private String host = "localhost";
    private int port = 8080;

    private ArrayList expected = new ArrayList();

    @Test
    @Disabled
    @Deprecated
    void test_1_get_request__200_OK() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "<title>Google</title>");

        ArrayList actual = get("http://google.ru", expected);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    @Disabled
    @Deprecated
    void test_2_get_request__200_OK__search() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "<title>httpClient - ");

        ArrayList actual = get("http://www.google.com/search?q=httpClient", expected);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    @Disabled
    @Deprecated
    void test_3_get_request__404_Not_Found() throws IOException {
        expected.add(0, expected404);
        expected.add(1, "<title>Error 404 (Not Found)!!1</title>");
        expected.add(2, "The requested URL <code>/somepage</code> was not found on this server.");

        ArrayList actual = get("http://google.ru/somepage", expected);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    @Disabled
    @Deprecated
    void test_4_post_request__405_Method_Not_Allowed() throws IOException {
        expected.add(0, expected405);
        expected.add(1, " <title>Error 405 (Method Not Allowed)!!1</title>");
        expected.add(2, "<p>The request method <code>POST</code> is inappropriate for the URL");

        ArrayList actual = post("https://google.com", expected, "", false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }


}
