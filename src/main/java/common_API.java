import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

class common_API extends json_server {

    final String expected200 = "200 OK";
    final String expected403 = "403 Forbidden";
    final String expected404 = "404 Not Found";
    final String expected500 = "500 Internal Server Error";

    boolean show_response_body = true;
    boolean show_response_json = true;
    boolean show_debug_level = true;
    boolean show_info_level = true;
    boolean show_generated_json = true;

    final String DEBUG_LEVEL = "DBG";
    final String INFO_LEVEL = "INF";

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";
    static final String METHOD_OPTIONS = "OPTIONS";
    static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_POST + "," + METHOD_OPTIONS;

    static final int STATUS_OK = 200;
    static final int STATUS_METHOD_NOT_ALLOWED = 405;
    static final int STATUS_NOT_FOUND = 404;
    static final int NO_RESPONSE_LENGTH = -1;

    String html = "json_server 0.1:";
    private String supported_context = "/ /reminders /stop";
    private boolean write_file = true;
    /*private String list_params = "test=" + test.get(0) + ", " +
            "macaddress=" + macaddress.get(0) + ", " +
            "count_reminders="+ count_reminders.get(0) + ", " +
            "count_iterations=" + count_iterations.get(0);*/


    ArrayList<String> read_config() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        Properties p = new Properties();
        String fileName= "src/main/resources/config.properties";
        p.load(new FileInputStream(fileName));

        list.add(0, p.getProperty("app.name", "default_name"));
        list.add(1, p.getProperty("app.version", "default_version"));
        list.add(2, p.getProperty("app.vendor","Java"));

        //System.out.println("app.name=" + list.get(0));
        //System.out.println("app.version=" + list.get(1));
        //System.out.println("app.vendor=" + list.get(2));
        return list;
        }

    void write_config(String key, String value) throws IOException {
            try {
                Properties p = new Properties();
                p.setProperty(key, value);

                FileOutputStream os = new FileOutputStream(new File("src/main/resources/config.properties"));
                p.store(os, "Favorite Things");
                os.close();
            } catch (Exception e) {
                System.out.println("catch exception! write_config: " + e);
                e.printStackTrace();
            }


        }

    void logger(String level, String s) throws IOException {
        if(level.equals("INF") && show_info_level) {
            System.out.println(s);
            if (write_file) {
                write_to_file(s + "\n");
            }
        }

        if(level.equals("DBG") && show_debug_level) {
            System.out.println(s);
            if (write_file) {
                write_to_file(s + "\n");
            }
        }
    }

    private void write_to_file(String string) throws IOException {
        FileWriter writer = new FileWriter("ServerLog.txt", true);
        writer.write(string);
        //writer.append('\n');
        writer.flush();
        writer.close();
    }

}
