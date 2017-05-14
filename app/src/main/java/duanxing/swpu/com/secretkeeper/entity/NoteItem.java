package duanxing.swpu.com.secretkeeper.entity;

/**
 * Created by duanxing on 14/05/2017.
 */

public class NoteItem {
    private String title;
    private String content;

    public NoteItem() {
    }

    public NoteItem(String t, String c) {
        this.title = t;
        this.content = c;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public void setContent(String c) {
        this.content = c;
    }
}
