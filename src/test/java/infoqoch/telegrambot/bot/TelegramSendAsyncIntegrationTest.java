package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.PropertiesUtil;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.config.TelegramUrls;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpClientHttpHandler;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@EnabledIf("infoqoch.telegrambot.IntegrationTest#isIntegrationTest")
class TelegramSendAsyncIntegrationTest {
    // properties
    String token = PropertiesUtil.findProperty("test.telegram.token");
    int chatId = Integer.parseInt(PropertiesUtil.findProperty("test.telegram.chat-id"));

    // target
    TelegramSend send;

    ExecutorService executor = Executors.newFixedThreadPool(10);

    @BeforeEach
    private void setUp() {
        send = new DefaultTelegramSend(
                HttpClientHttpHandler.createAsyncDefault(executor)
                , TelegramBotProperties.defaultProperties(token)
                , DefaultJsonBind.getInstance());
    }

    @Test
    @DisplayName("기본적인 메시지 보내기")
    void send_message() throws InterruptedException {
        // given
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
        long initialTaskCount = threadPoolExecutor.getTaskCount();
        long initialCompletedTaskCount = threadPoolExecutor.getCompletedTaskCount();

        log.info("initialTaskCount={}, initialCompletedTaskCount={}", initialTaskCount, initialCompletedTaskCount);

        // when
        final Response<SendMessageResponse> response = send.message(
                new SendMessageRequest(chatId, "executor 테스트"));

        // 비동기 작업 완료 대기
        Thread.sleep(1000);

        // then
        assertThat(response.isOk()).isTrue();
        long currentTaskCount = threadPoolExecutor.getTaskCount();
        long currentCompletedTasks = threadPoolExecutor.getCompletedTaskCount();
        log.info("currentTaskCount={}, currentCompletedTasks={}", currentTaskCount, currentCompletedTasks);
        assertThat(currentTaskCount).isGreaterThan(initialTaskCount);
        assertThat(currentCompletedTasks).isGreaterThan(initialCompletedTaskCount);
    }
}