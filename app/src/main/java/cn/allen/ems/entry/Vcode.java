package cn.allen.ems.entry;

public class Vcode {
    private String Message;
    private boolean status;

    public Vcode() {
    }

    @Override
    public String toString() {
        return "Vcode{" +
                "Message='" + Message + '\'' +
                ", status=" + status +
                '}';
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
