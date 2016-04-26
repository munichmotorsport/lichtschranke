package munichmotorsport.photocellapplication.model;

public class LapDto {

    private int barCode;
    private long time;

    public LapDto() {
    }

    LapDto(int barCode, long time) {
        this.barCode = barCode;
        this.time = time;
    }

    public int getBarCode() {
        return barCode;
    }

    public void setBarCode(int barCode) {
        this.barCode = barCode;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
