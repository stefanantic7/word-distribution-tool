package rs.raf.word_distribution;

public class InputDataFrame {
    final private String source;
    final private String content;

    public InputDataFrame(String source, String content) {
        this.source = source;
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }
}
