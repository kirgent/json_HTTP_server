import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class testServer_internal extends client_API {
    private String host = "localhost";
    private int port = 8080;
    private String context = "/temperature";
    private String params = "test=Add_Modify_Delete_Purge&macaddress=123123123&count_reminders=10";
    private String url = "http://" + host + ":" + port + context + "?" + params;

    private ArrayList expected = new ArrayList();

    @Test
    void testServer__correct_json() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "\"success\":true");

        String json = generate_json(1);
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void testServer__correct_json2() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "\"success\":true");

        String json = "{\"measurements\":[{\"date\":\"Wed May 30 16:31:04 MSK 2018\"}]}";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void testServer__correct_json3() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "\"success\":true");

        String json = "{\"measurements\":[{\"date\":1233123123}, \"unitttttt\":\"L\"]}";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void testServer__empty_body() throws IOException {
        expected.add(0, expected500);
        expected.add(1, "\"success\":false");
        expected.add(2, "incorrect json: empty body");

        String json = "";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void testServer__empty_json_in_body() throws IOException {
        expected.add(0, expected500);
        expected.add(1, "\"success\":false");
        expected.add(2, "incorrect json: empty json in body");

        String json = "{}";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void testServer__incorrect_json_format() throws IOException {
        expected.add(0, expected500);
        expected.add(1, "\"success\":false");
        expected.add(2, "incorrect json: incorrect json format");

        String json = "{\"temp\":\"123123123\"}";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void testServer__incorrect_request() throws IOException {
        expected.add(0, expected500);
        expected.add(1, "incorrect json: incorrect json format, incorrect one or more mandatory field: measurements, date");

        String json = "123123123";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void testServer_for_incorrect_method() throws IOException {
        //ArrayList actual = request("PUT", host, port, context, "{}");
        //assertEquals(expected200, actual.get(0));
        //assertEquals("incorrect http method!", actual.get(1));
    }




}