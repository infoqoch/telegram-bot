package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.PropertiesUtil;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIf("infoqoch.telegrambot.IntegrationTest#isIntegrationTest")
public class TelegramFileAndBotIntegrationTest {
    // properties
    String token = PropertiesUtil.findProperty("test.telegram.token");
    int chatId = Integer.parseInt(PropertiesUtil.findProperty("test.telegram.chat-id"));
    String fileId = PropertiesUtil.findProperty("test.telegram.document.file-id");
    String filePath = PropertiesUtil.findProperty("test.telegram.document.file-path");

    // target
    TelegramBot bot = DefaultTelegramBotFactory.init(token);

    @Test
    @DisplayName("TelegramBot.file() 테스트")
    void get_file_path(){
        // when
        final Response<FilePath> response = bot.file().path(new FilePathRequest(chatId, fileId));
        System.out.println("response = " + response);

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getFilePath()).isEqualTo(filePath);
    }

    @Test
    @DisplayName("앞서 존재하는 파일을 실제로 접근할 수 있는지 확인한다.")
    void get_real_file(){
        // given
        final Response<FilePath> response = bot.file().path(new FilePathRequest(chatId, fileId));
        final String target = bot.url().file() + "/" + response.getResult().getFilePath();

        // then1
        assertThat(target).isEqualTo("https://api.telegram.org/file/bot" + token + "/" + filePath);

        // then2
        try(ReadableByteChannel rbc = Channels.newChannel(new URL(target).openStream())){
            assertThat(rbc.isOpen()).isTrue();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}