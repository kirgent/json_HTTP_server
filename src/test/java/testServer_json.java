import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class testServer_json extends client_API {
    private String host = "localhost";
    private int port = 8080;
    private String context = "/temperature";
    private String params = "test=Add_Modify_Delete_Purge&macaddress=123123123&count_reminders=10";
    private String url = "http://" + host + ":" + port + context + "?" + params;

    @Test
    void testServer_for_correct_json() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0, expected200);
        expected.add(1, "GOOD json");
        String json = generate_json();

        ArrayList actual = post(url, expected, json);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        //assertEquals("Unexpected token END OF FILE at position 0.", actual.get(2));
    }

    @Test
    void testServer_for_empty_json() throws IOException {
        ArrayList expected = new ArrayList();
        expected.add(0, expected200);
        expected.add(1, "incorrect json: empty json!");
        String json = "{}";

        ArrayList actual = post(url, expected, json);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        //assertEquals("Unexpected token END OF FILE at position 0.", actual.get(2));
    }

    @Test
    void testServer_for_incorrect_json_format() throws IOException {
        String json = "{\"temp\":\"123123\"}";
        //ArrayList actual = request(METHOD_POST, host, port, context, json);
        //assertEquals(expected500, actual.get(0));
        //assertEquals("incorrect json: incorrect format!", actual.get(1));
        //assertEquals("Unexpected token END OF FILE at position 0.", actual.get(2));
    }

    @Test
    void testServer_for_incorrect_method() throws IOException {
        //ArrayList actual = request("PUT", host, port, context, "{}");
        //assertEquals(expected200, actual.get(0));
        //assertEquals("incorrect http method!", actual.get(1));
    }




}