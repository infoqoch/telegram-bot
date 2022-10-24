package infoqoch.telegrambot.bot.config;

import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramBotPropertiesTest {

    @Test
    void get_default_properties(){
        final TelegramBotProperties properties = TelegramBotProperties.defaultProperties("@test_token");
        assertThat(properties.getUrl().getSendMessage()).isEqualTo("https://api.telegram.org/bot@test_token/sendMessage");
    }

    @Test
    void get_properties_out_of_bot(){
        TelegramBot bot =  DefaultTelegramBotFactory.init("@test_token");
        final TelegramBotProperties properties = TelegramBotProperties.defaultProperties("@test_token");
        assertThat(bot.url().getSendMessage()).isEqualTo(properties.getUrl().getSendMessage());
    }
}