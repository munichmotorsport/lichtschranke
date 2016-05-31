package de.nilsgruenewald.mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

public class LapService {
	
	static boolean raceRunning = false;
	static ArrayList<Lap> lapsDriven = new ArrayList<Lap>();
	static ArrayList<Lap> totalLapsDriven = new ArrayList<Lap>();
	private Random random;
	private int totalRounds;
	private int cars;
	private int carsFinishedRace;
	
	static File file = new File("/home/pi/timestamps");
	static FileReader fileReader;
	static BufferedReader bufferedReader;
	static Boolean readerInitialized = false;
	
	/**
	 * starts the race if no one is already running
	 */
	public void startRace(int amountOfCars, int totalRounds) {
		this.totalRounds = totalRounds;
		this.cars = amountOfCars;
		long startTime = new Date().getTime();
		carsFinishedRace = 0;
		
		if (!raceRunning && lapsDriven.size() == 0 && totalLapsDriven.size() == 0) {
			random = new Random();
			raceRunning = true;
			
			for (int i=0; i<cars; i++) {
//				int barCode = random.nextInt(1000000);
				int barCode = 1000+1+i;
				Lap lapZero = new Lap(barCode, startTime);
				lapsDriven.add(lapZero);
				totalLapsDriven.add(lapZero);
				System.out.println(lapZero.getBarCode() + ": " + lapZero.getTime());
				//driveLap(barCode, startTime, 1);
			}
		}
	}
	
	/**
	 * checks if a race is already running
	 * 
	 * @return boolean raceRunning, true = race is running
	 * 								false = race is NOT running
	 */
	public Boolean checkRaceRunning() {
		return raceRunning;
	}
	
	/**
	 * checks, if there are new laps in the cache
	 * 
	 * @return boolean, true = new laps in cache
	 * 					false = no laps in cache
	 */
	public Boolean checkNewLaps() {
		if (lapsDriven.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * returns the quantity of laps in cache
     * 
     * @return integer, quantity of laps
     */
	public int quantityOfLaps() {
		return lapsDriven.size();
	}
	
	/**
     * returns last driven lap
     * 
     * @return Lap lap, last lap
     */
	public Lap getLastLap() {
		/*
		Lap lap = null;
		if (lapsDriven.size() > 0) {
			lap = lapsDriven.get(0);
			lapsDriven.remove(0);
		}
		*/
		
		Lap lap = null;
		try {
			System.out.println(readerInitialized);
			if(!readerInitialized) {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				readerInitialized = true;
			}
			System.out.println(readerInitialized);
			String line;
			line = bufferedReader.readLine();
			if (line != null && !line.equals("")) {
				System.out.println(line);
				lap = new Lap(1000, Long.parseLong(line));
				lapsDriven.add(lap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (lapsDriven.size() > 0) {
			lap = lapsDriven.get(0);
			lapsDriven.remove(0);
		}
		System.out.println(readerInitialized);
		return lap;
	}
	
	/**
     * returns all driven laps
     * 
     * @return all driven laps as JSON
     */
	public String getTotalLaps() {
		String totalRounds = "{ ";
		boolean commaAdded = false;
		System.out.println(totalLapsDriven.size() );
		if (totalLapsDriven.size() > 0) {
			for (int i = 0; i < totalLapsDriven.size(); i++) {
				if (commaAdded) totalRounds = totalRounds + ", ";
				commaAdded = true;
				totalRounds = totalRounds + "\"" + totalLapsDriven.get(i).getBarCode() + "\": ";
				totalRounds = totalRounds + "\"" + totalLapsDriven.get(i).getTime() + "\"";
			}
		}
		totalRounds = totalRounds + " }";
		System.out.println(totalRounds);
		return totalRounds;
	}
	
	/**
	 * stops current race
	 */
	public void stopRace() {
		raceRunning = false;
	}
	
	/**
	 * simulates a car
	 * 
	 * @param int barCode
	 */
	public void driveLap(int barCode, long time, int drivenRoundNumber) {
		int addedTime = (random.nextInt(5000)+10000);
		Timer timer = new Timer();
		if (drivenRoundNumber <= totalRounds) {
			if (LapService.raceRunning == true) {
				timer.schedule(new CarTimer(this, barCode, time, addedTime, drivenRoundNumber), addedTime);
			}
		} else {
			carsFinishedRace++;
			if (carsFinishedRace == cars) {
				raceRunning = false;
				System.out.println("Race finished");
			}
		}
	}

	/**
	 * deletes laps from cache
	 */
	public void clearCache() {
		lapsDriven.clear();
		totalLapsDriven.clear();
	}
}
