package infoqoch.telegrambot.bot.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramBotPropertiesTest {

    @Test
    void get_default_properties(){
        final TelegramBotProperties properties = TelegramBotProperties.defaultProperties("@test_token");
        assertThat(properties.getUrl().getSendMessage()).isEqualTo("https://api.telegram.org/bot@test_token/sendMessage");
    }

    @Disabled
    @Test
    void get_resource_properties(){
        // TODO yaml 고민
//        final TelegramBotProperties properties = TelegramBotProperties.yamlProperties("test.yaml");
//        assertThat(properties.getUrl().getSendMessage()).isEqualTo("test send message url");
    }

}