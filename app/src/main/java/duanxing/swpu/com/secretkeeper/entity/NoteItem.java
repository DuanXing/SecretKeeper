package duanxing.swpu.com.secretkeeper.entity;

/**
 * Created by duanxing on 14/05/2017.
 */

public class NoteItem {
    private int id;
    private String title;
    private String content;

    private NoteItem() {
    }

    public NoteItem(int i, String t, String c) {
        this.id = i;
        this.title = t;
        this.content = c;
    }

    public int getId () {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public void setContent(String c) {
        this.content = c;
    }
}
