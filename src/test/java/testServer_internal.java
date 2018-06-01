import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class testServer_internal extends client_API {
    private String host = "localhost";
    private int port = 8080;
    private String context = "/temperature";
    private String params = "test=Add_Modify_Delete_Purge&macaddress=123123123&count_reminders=10";
    private String url = "http://" + host + ":" + port + context + "?" + params;

    String client_fileName_config = "src/main/resources/config.properties.client";

    private ArrayList expected = new ArrayList();
    common_API api = new common_API();

    @BeforeEach
    void setup() throws IOException {
        ArrayList propertiesList = api.read_config(client_fileName_config);
        api.logger(api.SERVERLOG, api.INFO_LEVEL, new Date() + ": read " + client_fileName_config + ":\n"
                + "server_host=" + propertiesList.get(0) + "\n"
                + "server_port=" + propertiesList.get(1));
    }

    @Test
    void test1_Server_correct_json() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "\"success\":true");

        String json = generate_json(1);
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void test2_Server_correct_json2() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "\"success\":true");

        String json = "{\"measurements\":[{\"date\":\"Wed May 30 16:31:04 MSK 2018\"}]}";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void test3_Server_correct_json3() throws IOException {
        expected.add(0, expected200);
        expected.add(1, "\"success\":true");

        String json = "{\"measurements\":[{\"date\":1233123123}, \"unitttttt\":\"L\"]}";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void test4_Server_empty_body() throws IOException {
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
    void test5_Server_empty_json_in_body() throws IOException {
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
    void test6_Server_incorrect_json_format() throws IOException {
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
    void test7_Server_incorrect_json_format2() throws IOException {
        expected.add(0, expected500);
        expected.add(1, "incorrect json: incorrect json format, incorrect one or more mandatory field: measurements, date");

        String json = "123123123";
        ArrayList actual = post(url, expected, json, false);
        for(int i = 0; i< expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }

}