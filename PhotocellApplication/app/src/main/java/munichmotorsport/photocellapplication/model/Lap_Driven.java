package munichmotorsport.photocellapplication.model;

// Ist diese Klasse generiert? Bzw. wird sie überhaupt noch genutzt?
public class Lap_Driven {

    private int barCode;
    private long time;

    public Lap_Driven() {
    }

    Lap_Driven(int barCode, long time) {
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
