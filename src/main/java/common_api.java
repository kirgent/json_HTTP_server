import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

class common_api {

    ArrayList read_config() throws IOException {
        ArrayList list = new ArrayList();
        Properties p = new Properties();
        String fileName= "src/main/resources/config.properties";
        p.load(new FileInputStream(fileName));

        list.add(0, p.getProperty("app.name", "default_name"));
        list.add(1, p.getProperty("app.version", "default_version"));
        list.add(2, p.getProperty("app.vendor","Java"));

        System.out.println("app.name=" + list.get(0));
        System.out.println("app.version=" + list.get(1));
        System.out.println("app.vendor=" + list.get(2));

        return list;
        }

        void write_config(String key, String value) throws IOException {
            Properties p = new Properties();
            String fileName= "src/main/resources/config.properties";
            p.load(new FileInputStream(fileName));
            p.setProperty(key, value);

        }
}
