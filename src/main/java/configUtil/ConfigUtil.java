package configUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new FileNotFoundException("config.properties не найден в classpath");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Ошибка при загрузке config.properties", ex);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}

