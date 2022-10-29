package infoqoch.telegrambot;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesUtil {

    public static String findProperty(String key) {
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

    @Test
    @Disabled("프로퍼티가 없을 수 있으므로 생략한다.")
    void test(){
        String testTelegramToken = findProperty("test.telegram.integration");
        assertThat(testTelegramToken).containsAnyOf("true", "false");
    }
}
