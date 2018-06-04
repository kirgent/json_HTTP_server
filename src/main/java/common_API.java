import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

class common_API extends json_server {

    final String expected200 = "200 OK";
    final String expected403 = "403 Forbidden";
    final String expected404 = "404 Not Found";
    final String expected405 = "405 Method Not Allowed";
    final String expected500 = "500 Internal Server Error";

    final String DEBUG_LEVEL = "DBG";
    final String INFO_LEVEL = "INF";

    final String SERVERLOG = "ServerLog.txt";
    final String CLIENTLOG = "ClientLog.txt";

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";
    static final String METHOD_OPTIONS = "OPTIONS";
    static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_POST + "," + METHOD_OPTIONS;

    static final int STATUS_OK = 200;
    static final int STATUS_METHOD_NOT_ALLOWED = 405;
    static final int STATUS_NOT_FOUND = 404;
    static final int STATUS_SERVER_INTERNAL_ERROR = 500;
    static final int NO_RESPONSE_LENGTH = -1;

    String html = "json_server 0.1:";
    private String supported_context = "/ /reminders /stop";
    private boolean write_file = true;
    private boolean show_debug_level = true;
    private boolean show_info_level = true;


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

    void logger(String log, String level, String s) throws IOException {
        if(level.equals("INF") && show_info_level) {
            System.out.println(s);
            if (write_file) {
                write_to_file(log, s + "\n");
            }
        }

        if(level.equals("DBG") && show_debug_level) {
            System.out.println(s);
            if (write_file) {
                write_to_file(log, s + "\n");
            }
        }
    }

    private void write_to_file(String log, String string) throws IOException {
        String fileName = "ServerLog.txt";
        if (log.equals(CLIENTLOG)) {
            fileName = "ClientLog.txt";
        }
        FileWriter writer = new FileWriter(fileName, true);
        writer.write(string);
        //writer.append('\n');
        writer.flush();
        writer.close();
    }

}
