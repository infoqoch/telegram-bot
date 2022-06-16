package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.JsonBind;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TelegramUpdateTest {
    // target
    TelegramUpdate update;

    // mock httpClient
    HttpClient httpClient;
    HttpResponse httpResponse;
    StatusLine statusLine;
    HttpEntity httpEntity;

    @BeforeEach
    void setUp() throws IOException {
        String token = "@test_token";
        JsonBind jsonBind = new DefaultJsonBind();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);

        httpClient = mock(HttpClient.class);
        httpResponse = mock(HttpResponse.class);
        statusLine = mock(StatusLine.class);
        httpEntity = mock(HttpEntity.class);

        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(any())).thenReturn(httpResponse);

        update = new DefaultTelegramUpdate(httpClient, properties, jsonBind);
    }

    @Test
    @DisplayName("1개의 updates 대응")
    void update_list() throws IOException {
        // given
        String mockResponseBody = "{\"ok\":true,\"result\":[{\"update_id\":567841804,\n" +
                "\"message\":{\"message_id\":2102,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652025791,\"text\":\"hi\"}}]}";

        mockStatusCode(200);
        mockEntityBody(mockResponseBody);

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getUpdateId()).isEqualTo(567841804);
    }


    @Test
    @DisplayName("기본 message 대응")
    void update_plain_chat_message() throws IOException {
        String mockResponseBody = "{\"ok\": true,\"result\": [{\"update_id\": 567841806,\"message\": {\"message_id\": 2148,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482374,\"text\": \"\\ud14c\\uc2a4\\ud2b8 \\uba54\\uc2dc\\uc9c0\"}}]}";

        mockStatusCode(200);
        mockEntityBody(mockResponseBody);
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
        String mockResponseBody = "{\"ok\": true,\"result\": [{\"update_id\": 567841807,\"message\": {\"message_id\": 2149,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482399,\"document\": {\"file_name\": \"test.txt\",\"mime_type\": \"text/plain\",\"file_id\": \"BQACAgUAAxkBAAIIZWKOI5-9F9n8O1nh5Bz2m505K7qfAAI5BgACNKpwVL13NImKS4jvJAQ\",\"file_unique_id\": \"AgADOQYAAjSqcFQ\",\"file_size\": 8},\"caption\": \"send test document\"}}]}";

        mockStatusCode(200);
        mockEntityBody(mockResponseBody);
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
        String mockResponseBody = "{\"ok\":true,\"result\":[{\"update_id\":567841833,\n" +
                "\"message\":{\"message_id\":2201,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1655388392,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkBAAIImWKrOOjwoQFB-7HvkMXy4RP96xaEAAK8BQACVctYVSkAAdvHe7WJRCQE\",\"file_unique_id\":\"AgADvAUAAlXLWFU\",\"file_size\":18768},\"caption\":\"/excel_push\",\"caption_entities\":[{\"offset\":0,\"length\":11,\"type\":\"bot_command\"}]}}]}";

        mockStatusCode(200);
        mockEntityBody(mockResponseBody);
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
        String mockResponseBody = "{\"ok\": true,\"result\": [{\"update_id\": 567841808,\"message\": {\"message_id\": 2150,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482413,\"photo\": [{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADcwADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR4\",\"file_size\": 1196,\"width\": 90,\"height\": 90},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADbQADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFRy\",\"file_size\": 15474,\"width\": 320,\"height\": 320},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADeAADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR9\",\"file_size\": 52057,\"width\": 800,\"height\": 800},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADeQADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR-\",\"file_size\": 70180,\"width\": 1280,\"height\": 1280}],\"caption\": \"photo test\"}}]}";

        mockStatusCode(200);
        mockEntityBody(mockResponseBody);
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
        String mockResponseBody = "{\"ok\":true,\"result\":[]}";
        mockStatusCode(200);
        mockEntityBody(mockResponseBody);

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(0);
    }

    private OngoingStubbing<Integer> mockStatusCode(int statusCode) {return when(statusLine.getStatusCode()).thenReturn(statusCode);
    }

    private OngoingStubbing<InputStream> mockEntityBody(String responseEntitContent) throws IOException {
        return when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(responseEntitContent.getBytes()));
    }
}