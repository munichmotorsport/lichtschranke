package de.nilsgruenewald.mock;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class WebAPI {
	
	private LapService lapService = new LapService();
	
	/**
	 * starts the race with adjustable cars and adjustable totalRounds
	 */
	@GET
    @Path("/startRace/{password}/{cars}/{rounds}")
    public void startRace(@PathParam("password") String password, @PathParam("cars") int cars, @PathParam("rounds") int rounds) {
		if(password.equals("mms")) {
			lapService.startRace(cars, rounds);
		}
    }
	
	/**
	 * checks if a race is already running
	 * 
	 * @return boolean raceRunning, true = race is running
	 * 								false = race is NOT running
	 */
	@GET
	@Path("/checkRaceRunning")
	public Boolean checkRaceRunning() {
		return lapService.checkRaceRunning();
	}
	
	/**
	 * checks, if there are new laps in the cache
	 * 
	 * @return boolean, true = new laps in cache
	 * 					false = no laps in cache
	 */
    @GET
    @Path("/checkNewLaps")
    public Boolean checkNewLaps() {
        return lapService.checkNewLaps();
    }
    
    /**
     * returns the quantity of laps in cache
     * 
     * @return integer, quantity of laps
     */
    @GET
    @Path("/quantityOfLaps")
    public int quantityOfLaps() {
		return lapService.quantityOfLaps();
    }
    
    /**
     * returns last driven lap
     * 
     * @return lap, last lap
     */
    @GET
    @Path("/getLastLap")
    @Produces(MediaType.APPLICATION_JSON)
    public Lap getLastLap() {
    	return lapService.getLastLap();
    }
    
    /**
     * returns all driven laps
     * 
     * @return all driven laps as JSON
     */
    @GET
    @Path("/getTotalLaps")
    public String getTotalLaps() {
		return lapService.getTotalLaps();
    }
    
    /**
     * stops current race
     */
    @GET
    @Path("/stopRace")
    public void stopRace() {
    	lapService.stopRace();
    }
    
    /**
     * deletes laps from cache
     */
    @GET
    @Path("/clearCache")
    public void clearCache() {
    	lapService.clearCache();
    }
}