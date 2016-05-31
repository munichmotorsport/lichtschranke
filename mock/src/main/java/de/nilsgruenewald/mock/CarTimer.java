package de.nilsgruenewald.mock;

import java.util.TimerTask;

public class CarTimer extends TimerTask {
	
	public LapService lapService;
	private Lap lap;
	private int barCode;
	private long newTime;
	private int round;
	
	public CarTimer(LapService lapService, int barCode, long oldTime, long addedTime, int round) {
		this.lapService = lapService;
		this.barCode = barCode;
		this.round = round;
		newTime = oldTime+addedTime;
		lap = new Lap(barCode, newTime);
	}

	@Override
	public void run() {
		LapService.lapsDriven.add(lap);
		LapService.totalLapsDriven.add(lap);
		System.out.println(lap.getBarCode() + ": " + lap.getTime());
		lapService.driveLap(barCode, newTime, round+1);
	}
}
