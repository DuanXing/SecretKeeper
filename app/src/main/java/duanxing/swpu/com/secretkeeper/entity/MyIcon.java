package duanxing.swpu.com.secretkeeper.entity;

/**
 * Created by duanxing on 2017/5/3.
 */

public class MyIcon {
    private int iId;
    private String iName;

    public MyIcon() {
    }

    public MyIcon(int iId, String iName) {
        this.iId = iId;
        this.iName = iName;
    }

    public int getiId() {
        return iId;
    }

    public String getiName() {
        return iName;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}