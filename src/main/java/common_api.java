import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class common_api {

    void read_config() throws IOException {
        Properties prop=new Properties();
        String fileName= "/config.properties";
        InputStream is=new FileInputStream(fileName);

        prop.load(is);

        System.out.println(prop.getProperty("app.name"));
        System.out.println(prop.getProperty("app.version"));

        System.out.println(prop.getProperty("app.vendor","Java"));
        }
}
