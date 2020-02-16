package src.datetime;

/**
 * TImezone
 */
public class TimeZone {

    private boolean isLocal;

    public TimeZone() {
        setLocal(false);
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

}