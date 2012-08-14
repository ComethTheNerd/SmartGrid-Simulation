package time;
import time.commands.ITimedCommand;
import household.HouseHold;
import time.commands.HouseTimedCommand;
import infopackage.HouseParameters;
/**
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class HouseTimedEvent extends TimedEvent {
    /**the House Parameters associated with this HouseTimedEvent*/
    private HouseParameters houseParams;

    /**Creates a new HouseTimedEvent with supplied command,
     * startTime, endTime, interval between runs and attached
     * to given house specified by HouseParameters.
     *
     * @param houseParams Indicates house which adopts this timed event
     * @param startTime start time of timed event
     * @param endTime end time of timed event
     * @param command command performed during timed event
     * @param intervalBetweenRuns determined whether this timed event is a recurring one.
     */
    public HouseTimedEvent(HouseParameters houseParams, Time startTime, Time endTime, HouseTimedCommand command, Time intervalBetweenRuns){

        super(startTime, endTime, command, intervalBetweenRuns);
        
        this.houseParams = houseParams;
    }

    /**Returns the House Parameters to which this HouseTimedEvent is
     * attached
     *
     * @return The House Parameters to which this HouseTimedEvent is attached
     */
    public HouseParameters getRegisteredHouseParams(){
        return houseParams;
    }
}
