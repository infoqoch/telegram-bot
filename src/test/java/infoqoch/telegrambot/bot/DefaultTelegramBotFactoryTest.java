package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.PropertiesUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EnabledIf("infoqoch.telegrambot.IntegrationTest#isIntegrationTest")
class DefaultTelegramBotFactoryTest {

    String validToken = PropertiesUtil.findProperty("test.telegram.token");
    String invalidToken = "invalid_token_12345";

    @Test
    @DisplayName("잘못된 토큰으로 init 호출 시 IllegalStateException 발생")
    void init_shouldThrowException_whenInvalidToken() {

        assertThatThrownBy(() -> DefaultTelegramBotFactory.init(invalidToken))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("failed to access telegram bot");
    }

    @Test
    @DisplayName("정상 토큰으로 init 호출 시 TelegramBot 생성 성공")
    void init_shouldSucceed_whenValidToken() {

        TelegramBot bot = DefaultTelegramBotFactory.init(validToken);
        assertThat(bot).isNotNull();
    }
}
