package infoqoch.telegrambot.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpGetParamMapTest {
    @Test
    void test(){
        final HttpGetParamMap httpGetParamMap = new HttpGetParamMap("abc.com");
        httpGetParamMap.addParam("name", "kim");
        httpGetParamMap.addParam("age", "10");
        assertThat(httpGetParamMap.createUrl()).isEqualTo("abc.com?name=kim&age=10");
    }

}