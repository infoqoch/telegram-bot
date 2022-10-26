package infoqoch.telegrambot.util;

import infoqoch.telegrambot.util.HttpHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;

class HttpHandlerTest {

    HttpHandler httpHandler;

    @BeforeEach
    void setUp() throws IOException {
        httpHandler = mock(HttpHandler.class);
    }
    @Test
    @DisplayName("200 정상")
    void status_200_get()  {

    }

    @Test
    @DisplayName("200 정상")
    void status_200_post()  {

    }

    @Test
    @DisplayName("400에러 대응")
    void status_400()  {

    }

    @Test
    @DisplayName("500에러 대응")
    void status_500()  {

    }
}