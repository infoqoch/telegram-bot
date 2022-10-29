package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TelegramUpdateTest {
    // target
    TelegramUpdate update;

    // mock httpClient
    HttpHandler httpHandler;

    @BeforeEach
    void setUp() throws IOException {
        httpHandler = mock(HttpHandler.class);
        update = new DefaultTelegramUpdate(httpHandler, TelegramBotProperties.defaultProperties("@test_token"), DefaultJsonBind.getInstance());
    }

    @Test
    @DisplayName("1개의 updates 대응")
    void update_list() throws IOException {
        // given
        String json = "{\"ok\":true,\"result\":[{\"update_id\":567841804,\n" +
                "\"message\":{\"message_id\":2102,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652025791,\"text\":\"hi\"}}]}";
        when(httpHandler.get(any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getUpdateId()).isEqualTo(567841804);
    }

    @Test
    @DisplayName("forward 전달 대응")
    void update_forward() throws IOException {
        // given
        String json = "{\"ok\":true,\"result\":[{\"update_id\":567841840,\n" +
                "\"message\":{\"message_id\":2215,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1655610194,\"forward_from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"forward_date\":1655609857,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkBAAIIp2Kum1LQM_36uaRAICjl6W2YiO32AAJfBwACp4hwVfwoRa98qZu7JAQ\",\"file_unique_id\":\"AgADXwcAAqeIcFU\",\"file_size\":18768},\"caption\":\"excel push\"}}]}";

        when(httpHandler.get(any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getMessage().getForwardDate()).isBefore(Instant.now());
        assertThat(updates.get(0).getMessage().getForwardFrom()).isNotNull();
    }

    @Test
    @DisplayName("reply 전달 대응")
    void update_reply() throws IOException {
        // given
        String json = "{\"ok\":true,\"result\":[{\"update_id\":567841901,\n" +
                "    \"message\":{\"message_id\":2375,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1657031357,\"reply_to_message\":{\"message_id\":2372,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1657031087,\"forward_from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"forward_date\":1656700053,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkBAAIJRGLESa_ZSHj1tJ2AyAwvFVYj-hDKAAL_BAACg56JVdF3guuN7A6tKQQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440},\"caption\":\"\\uc774\\ud0c8\\ub9ad\\uba54\\uc2dc\\uc9c0!\",\"caption_entities\":[{\"offset\":0,\"length\":7,\"type\":\"italic\"}]},\"text\":\"excel push\"}}]}";

        when(httpHandler.get(any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getMessage().getText()).isEqualTo("excel push");
        assertThat(updates.get(0).getMessage().getReplyToMessage().getForwardFrom()).isNotNull();
        assertThat(updates.get(0).getMessage().getReplyToMessage().getCaption()).isEqualTo("이탈릭메시지!");
    }


    @Test
    @DisplayName("기본 message 대응")
    void update_plain_chat_message() throws IOException {
        String json = "{\"ok\": true,\"result\": [{\"update_id\": 567841806,\"message\": {\"message_id\": 2148,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482374,\"text\": \"\\ud14c\\uc2a4\\ud2b8 \\uba54\\uc2dc\\uc9c0\"}}]}";
        when(httpHandler.get(any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getUpdateId()).isEqualTo(567841806);
    }

    @Test
    @DisplayName("기본 document 대응")
    void update_document() throws IOException {
        String json = "{\"ok\": true,\"result\": [{\"update_id\": 567841807,\"message\": {\"message_id\": 2149,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482399,\"document\": {\"file_name\": \"test.txt\",\"mime_type\": \"text/plain\",\"file_id\": \"BQACAgUAAxkBAAIIZWKOI5-9F9n8O1nh5Bz2m505K7qfAAI5BgACNKpwVL13NImKS4jvJAQ\",\"file_unique_id\": \"AgADOQYAAjSqcFQ\",\"file_size\": 8},\"caption\": \"send test document\"}}]}";

        when(httpHandler.get(any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getUpdateId()).isEqualTo(567841807);
        assertThat(updates.get(0).getMessage().getDocument().getFileName()).isEqualTo("test.txt");
        assertThat(updates.get(0).getMessage().getCaption()).isEqualTo("send test document");
    }

    @Test
    @DisplayName("document + caption_entities 대응")
    void update_document_entities() throws IOException {
        String json = "{\"ok\":true,\"result\":[{\"update_id\":567841833,\n" +
                "\"message\":{\"message_id\":2201,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1655388392,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkBAAIImWKrOOjwoQFB-7HvkMXy4RP96xaEAAK8BQACVctYVSkAAdvHe7WJRCQE\",\"file_unique_id\":\"AgADvAUAAlXLWFU\",\"file_size\":18768},\"caption\":\"/excel_push\",\"caption_entities\":[{\"offset\":0,\"length\":11,\"type\":\"bot_command\"}]}}]}";

        when(httpHandler.get(any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getUpdateId()).isEqualTo(567841833);
        assertThat(updates.get(0).getMessage().getDocument().getFileName()).isEqualTo("sample.xlsx");
        assertThat(updates.get(0).getMessage().getCaptionEntities().get(0).getType()).isEqualTo("bot_command");
    }

    @Test
    @DisplayName("기본 photo 대응")
    void update_photo() throws IOException {
        String json = "{\"ok\": true,\"result\": [{\"update_id\": 567841808,\"message\": {\"message_id\": 2150,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482413,\"photo\": [{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADcwADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR4\",\"file_size\": 1196,\"width\": 90,\"height\": 90},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADbQADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFRy\",\"file_size\": 15474,\"width\": 320,\"height\": 320},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADeAADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR9\",\"file_size\": 52057,\"width\": 800,\"height\": 800},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADeQADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR-\",\"file_size\": 70180,\"width\": 1280,\"height\": 1280}],\"caption\": \"photo test\"}}]}";

        when(httpHandler.get(any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getUpdateId()).isEqualTo(567841808);
        assertThat(updates.get(0).getMessage().getPhoto().get(0).getHeight()).isEqualTo(90);
        assertThat(updates.get(0).getMessage().getPhoto().get(0).getFileSize()).isEqualTo(1196);
    }

    @Test
    @DisplayName("빈 updates")
    void update_empty() throws IOException {
        // given
        String json = "{\"ok\":true,\"result\":[]}";
        when(httpHandler.get(any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(0);
    }

}