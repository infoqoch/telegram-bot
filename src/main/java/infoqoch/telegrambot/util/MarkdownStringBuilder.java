package infoqoch.telegrambot.util;

import java.util.regex.Pattern;

public class MarkdownStringBuilder {
    private final StringBuilder sb = new StringBuilder();

    public String parseMode() {
        return "MarkdownV2";
    }

    public String text() {
        valid();
        return toString();
    }

    private void valid() {
        if(sb.length()==0)
            throw new IllegalArgumentException("append message greater than 0");
    }

    public MarkdownStringBuilder plain(String str) {
        sb.append(escape(str));
        return this;
    }
    public MarkdownStringBuilder italic(String str) {
        sb.append("_").append(escape(str)).append("_");
        return this;
    }
    public MarkdownStringBuilder underline(String str) {
        sb.append("__").append(escape(str)).append("__");
        return this;
    }

    public MarkdownStringBuilder bold(String str) {
        sb.append("*").append(escape(str)).append("*");
        return this;
    }

    public MarkdownStringBuilder strikethrough(String str) {
        sb.append("~").append(escape(str)).append("~");
        return this;
    }

    public MarkdownStringBuilder code(String str) {
        sb.append("`").append(escape(str)).append("`");
        return this;
    }

    public MarkdownStringBuilder url(String text, String url) {
        sb.append("[").append(escape(text)).append("](").append(url).append(")");
        return this;
    }

    public MarkdownStringBuilder lineSeparator() {
        sb.append(System.lineSeparator());
        return this;
    }

    public MarkdownStringBuilder command(String command, String value) {
        sb.append("/").append(escape(command)).append("\\_").append(escape(value.replace(" ", "_")));
        return this;
    }

    private String escape(String str) {
        str = str.replaceAll("[_]", "\\\\_");
        str = str.replaceAll("[\\*]", "\\\\*");
        str = str.replaceAll("[\\]]", "\\\\]");
        str = str.replaceAll("[\\[]", "\\\\[");
        str = str.replaceAll("[(]", "\\(");
        str = str.replaceAll("[\\)]", "\\\\)");
         str = str.replaceAll("[\\/]", "\\\\/");
        str = str.replaceAll("[~]", "\\\\~");
        str = str.replaceAll("[`]", "\\\\`");
        str = str.replaceAll("[>]", "\\\\>");
        str = str.replaceAll("[<]", "\\\\<");
        str = str.replaceAll("[#]", "\\\\#");
        str = str.replaceAll("[\\+]", "\\\\+");
        str = str.replaceAll("[\\-]", "\\\\-");
        str = str.replaceAll("[=]", "\\\\=");
        str = str.replaceAll("[\\|]", "\\\\|");
        str = str.replaceAll("[{]", "\\\\{");
        str = str.replaceAll("[}]", "\\\\}");
        str = str.replaceAll("[\\.]", "\\\\.");
        str = str.replaceAll("[!]", "\\\\!");
        return str;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
