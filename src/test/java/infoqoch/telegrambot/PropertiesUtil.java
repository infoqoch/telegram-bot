package infoqoch.telegrambot;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesUtil {

    @Test
    @Disabled
    void test(){
        String testTelegramToken = getToken("test-telegram-token");
        assertThat(testTelegramToken).isEqualTo("your-telegram-bot-token-for-test");
    }

    public static String getToken(String key) {
        try{
            final URL resource = PropertiesUtil.class.getClassLoader().getResource("application.properties");
            File path = new File(resource.toURI());
            try(FileReader file = new FileReader(path)) {
                Properties p = new Properties();
                p.load(file);
                return p.getProperty(key);
            }
        }catch (Exception e){
            throw new IllegalArgumentException("check properties (test/resources/application.properties)", e);
        }
    }

    public static boolean isIntegrationTest(String key) {
        return Boolean.parseBoolean(getToken(key));
    }
}
