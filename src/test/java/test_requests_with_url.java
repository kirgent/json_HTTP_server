import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ArrayList actual = request.get("http://google.ru/321");
        assertEquals(expected404, actual.get(0));
    }

    @Test
    void test_post_request() throws IOException {
        ArrayList actual = request.post("https://selfsolve.apple.com/wcResults.do");
        assertEquals(expected200, actual.get(0));
    }

}
