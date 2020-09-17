package allen.frame.entry;

import java.io.Serializable;

public class Emoj implements Serializable {
    private String flag;
    private int resId;

    public Emoj() {
    }

    public Emoj(String flag, int resId) {
        this.flag = flag;
        this.resId = resId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
