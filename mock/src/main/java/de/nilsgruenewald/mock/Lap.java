package de.nilsgruenewald.mock;

public class Lap {
	
	private int barCode;
	private long time;
	
	public Lap() {
	}
	
	Lap(int barCode, long time) {
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
