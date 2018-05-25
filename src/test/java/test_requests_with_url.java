import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class test_requests_with_url extends common_API {
    private http_requests request = new http_requests();

    @Test
    void test_get_request__200() throws IOException {
        //ArrayList actual = request.get("http://www.google.com/search?q=httpClient");
        ArrayList actual = request.get("http://google.ru/");
        assertEquals(expected200, actual.get(0));
        assertEquals("<!doctype html>", actual.get(1));
    }

    @Test
    void test_get_request__404() throws IOException {
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
    void test_post_request() throws IOException {
        ArrayList actual = request.post("https://selfsolve.apple.com/wcResults.do");
        assertEquals(expected200, actual.get(0));
    }

}
