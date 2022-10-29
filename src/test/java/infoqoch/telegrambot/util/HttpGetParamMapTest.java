package infoqoch.telegrambot.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpGetParamMapTest {
    // defaultMap 이 있을 때
    // defaultMap 이 있을 때
    // addMap 이 없을 때
    // addMap 이 있을 때
    // createUrl 의 동작 유무
    // createUrl 의 동작 후 defaultMap 만 나오는지

    @Test
    @DisplayName("기본 쿼리와 추가되는 쿼리가 있다.")
    void defaultMap_addMap(){
        // given
        final HttpGetParamMap httpGetParamMap = new HttpGetParamMap("abc.com", Map.of("defaultkey", "defaultvalue"));
        httpGetParamMap.addParam("name", "kim");
        httpGetParamMap.addParam("age", "10");

        // then
        assertThat(httpGetParamMap.createUrl()).isEqualTo("abc.com?defaultkey=defaultvalue&name=kim&age=10");
    }

    @Test
    @DisplayName("기본 쿼리가 없고 추가되는 쿼리가 있다.")
    void empty_defaultMap_addMap(){
        // given
        final HttpGetParamMap httpGetParamMap = new HttpGetParamMap("abc.com");
        httpGetParamMap.addParam("name", "kim");
        httpGetParamMap.addParam("age", "10");

        // then
        assertThat(httpGetParamMap.createUrl()).isEqualTo("abc.com?name=kim&age=10");
    }

    @Test
    @DisplayName("기본 쿼리가 있고 추가되는 쿼리가 없다.")
    void defaultMap_empty_addMap(){
        // given
        final HttpGetParamMap httpGetParamMap = new HttpGetParamMap("abc.com", Map.of("defaultkey", "defaultvalue"));

        // then
        assertThat(httpGetParamMap.createUrl()).isEqualTo("abc.com?defaultkey=defaultvalue&");
    }

    @Test
    @DisplayName("기본 쿼리가 없고 추가되는 쿼리도 없다.")
    void empty_defaultMap_empty_addMap(){
        // given
        final HttpGetParamMap httpGetParamMap = new HttpGetParamMap("abc.com");
        // then
        assertThat(httpGetParamMap.createUrl()).isEqualTo("abc.com?");
    }

    @Test
    @DisplayName("쿼리스트링이 포함된 url을 만든 후, 사용한 값이 삭제된다.")
    void after_create_url_and_clear_automatically(){
        // given
        final HttpGetParamMap httpGetParamMap = new HttpGetParamMap("abc.com", Map.of("defaultkey", "defaultvalue"));
        httpGetParamMap.addParam("name", "kim");
        httpGetParamMap.addParam("age", "10");

        // then
        assertThat(httpGetParamMap.createUrl()).isEqualTo("abc.com?defaultkey=defaultvalue&name=kim&age=10");

        // then
        assertThat(httpGetParamMap.createUrl()).isEqualTo("abc.com?defaultkey=defaultvalue&");
    }
}