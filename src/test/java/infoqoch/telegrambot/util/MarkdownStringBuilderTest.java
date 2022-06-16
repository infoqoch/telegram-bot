package infoqoch.telegrambot.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MarkdownStringBuilderTest {
    @Test
    void methods_test(){
        assertThat(new MarkdownStringBuilder().italic("흘림글씨야").toString()).isEqualTo("_흘림글씨야_");
        assertThat(new MarkdownStringBuilder().italic("흘림글씨야!").toString()).isEqualTo("_흘림글씨야\\!_");
        assertThat(new MarkdownStringBuilder().underline("언더라인*^^*이얌").toString()).isEqualTo("__언더라인\\*^^\\*이얌__");
        assertThat(new MarkdownStringBuilder().bold("굵게가자!").toString()).isEqualTo("*굵게가자\\!*");
        assertThat(new MarkdownStringBuilder().bold("굵게##!|가자!").toString()).isEqualTo("*굵게\\#\\#\\!\\|가자\\!*");
        assertThat(new MarkdownStringBuilder().strikethrough("취소선이야ㅠ").toString()).isEqualTo("~취소선이야ㅠ~");
        assertThat(new MarkdownStringBuilder().code("<h3>코드블럭!</h3>").toString()).isEqualTo("`\\<h3\\>코드블럭\\!\\<\\/h3\\>`");
        assertThat(new MarkdownStringBuilder().url("링크가자!", "https://naver.com").toString()).isEqualTo("[링크가자\\!](https://naver.com)");
        assertThat(new MarkdownStringBuilder().lineSeparator().toString()).isEqualTo(System.lineSeparator());
        assertThat(new MarkdownStringBuilder().plain("<h3>코드블럭!</h3>").toString()).isEqualTo("\\<h3\\>코드블럭\\!\\<\\/h3\\>");
        assertThat(new MarkdownStringBuilder().command("조회","abc 123").toString()).isEqualTo("/조회\\_abc\\_123");
    }

    @Test
    void return_this_stream(){
        final String actual = new MarkdownStringBuilder().italic("이탈릭가자!").lineSeparator().url("가자", "https://naver.com").toString();
        assertThat(actual).isEqualTo("_이탈릭가자\\!_"+System.lineSeparator()+"[가자](https://naver.com)");
    }
}
