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
        assertThat(new MarkdownStringBuilder().plain("나는 무엇(을)를 하였다.").toString()).isEqualTo("나는 무엇\\(을\\)를 하였다\\.");
        assertThat(new MarkdownStringBuilder().italic("이탈릭가자!").lineSeparator().url("가자", "https://naver.com").toString()).isEqualTo("_이탈릭가자\\!_"+System.lineSeparator()+"[가자](https://naver.com)");
    }

    @Test
    void append_msbs(){
        final MarkdownStringBuilder msb1 = new MarkdownStringBuilder().plain("반갑습니다.");
        final MarkdownStringBuilder msb2 = new MarkdownStringBuilder().append(msb1).plain("!!");
        assertThat(msb2.toString()).isEqualTo("반갑습니다\\.\\!\\!");
    }

    @Test
    void constructor(){
        final MarkdownStringBuilder msb = new MarkdownStringBuilder("반갑습니다.");
        assertThat(msb.toString()).isEqualTo("반갑습니다\\.");
    }

    @Test
    void notEscapedTest(){
        try{
            final MarkdownStringBuilder msb = new MarkdownStringBuilder().notEscapedTest("<h2>hi!</h2>");
            assertThat(msb.toString()).isEqualTo("<h2>hi!</h2>");
        }catch (NotEscapedMSBException e){
            throw new IllegalArgumentException(e);
        }
    }

    @Test
    void command_split_space(){
        assertThat(new MarkdownStringBuilder().command("share mine", "Y").text()).isEqualTo("/share\\_mine\\_Y");
    }

}
