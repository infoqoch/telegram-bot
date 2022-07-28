package infoqoch.telegrambot.util;

public class MarkdownStringBuilder {
    private final StringBuilder sb = new StringBuilder();

    public MarkdownStringBuilder() {}

    public MarkdownStringBuilder(String plain) {
        plain(plain);
    }

    public String parseMode() {
        return "MarkdownV2";
    }

    public MarkdownStringBuilder append(MarkdownStringBuilder msb) {
        if(msb==null) return this;

        sb.append(msb);
        return this;
    }

    public MarkdownStringBuilder plain(String str) {
        sb.append(escape(str));
        return this;
    }

    public MarkdownStringBuilder italic(String str) {
        if(isEmpty(str)) return this;
        sb.append("_").append(escape(str)).append("_");
        return this;
    }

    public MarkdownStringBuilder underline(String str) {
        if(isEmpty(str)) return this;
        sb.append("__").append(escape(str)).append("__");
        return this;
    }

    public MarkdownStringBuilder bold(String str) {
        if(isEmpty(str)) return this;
        sb.append("*").append(escape(str)).append("*");
        return this;
    }

    public MarkdownStringBuilder strikethrough(String str) {
        if(isEmpty(str)) return this;
        sb.append("~").append(escape(str)).append("~");
        return this;
    }

    public MarkdownStringBuilder code(String str) {
        if(isEmpty(str)) return this;
        sb.append("`").append(escape(str)).append("`");
        return this;
    }

    public MarkdownStringBuilder url(String text, String url) {
        if(isEmpty(text)||isEmpty(url)) return this;
        sb.append("[").append(escape(text)).append("](").append(url).append(")");
        return this;
    }

    public MarkdownStringBuilder lineSeparator() {
        sb.append(System.lineSeparator());
        return this;
    }

    public MarkdownStringBuilder command(String command, String value) {
        if(isEmpty(command)){
            return this;
        }else if(isEmpty(value)){
            sb.append("/").append(escape(spaceToUnderscore(command))).append(" ");
            return this;
        }else{
            sb.append("/").append(escape(spaceToUnderscore(command))).append("\\_").append(escape(value.replace(" ", "_")));
            return this;
        }
    }

    public MarkdownStringBuilder notEscapedText(String text) throws NotEscapedMSBException{
        sb.append(text);
        return this;
    }

    public int size() {
        return sb.length();
    }

    // null을 허용하지 않음.
    @Override
    public String toString() {
        valid();
        return sb.toString();
    }

    private String escape(String str) {
        str = str.replaceAll("[_]", "\\\\_");
        str = str.replaceAll("[\\*]", "\\\\*");
        str = str.replaceAll("[\\]]", "\\\\]");
        str = str.replaceAll("[\\[]", "\\\\[");
        str = str.replaceAll("[\\(]", "\\\\(");
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

    private void valid() {
        if(sb.length() == 0)
            throw new IllegalArgumentException("string length should be greater than 0");
    }

    private String spaceToUnderscore(String text) {
        return text.replaceAll(" ", "_");
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
