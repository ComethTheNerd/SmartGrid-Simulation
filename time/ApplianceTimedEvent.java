package time;
import appliance.core.Appliance;
import time.commands.ApplianceTimedCommand;

/**Allows us to tailor TimedEvents to specific Appliances,
 * therefore enabling closer control of the appliance
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class ApplianceTimedEvent extends TimedEvent{
    /**The Appliance to which this ApplianceTimedEvent is bound
     */
    private Appliance appliance;


    /**Creates a new ApplianceTimedEvent with supplied command,
     * startTime, endTime, interval between runs and attached to given appliance
     *
     * @param appliance appliance which adopts this timed event
     * @param startTime start time of timed event
     * @param endTime end time of timed event
     * @param command command performed during timed event
     * @param intervalBetweenRuns determined whether this timed event is a recurring one.
     */
    public ApplianceTimedEvent(Appliance appliance, Time startTime, Time endTime, ApplianceTimedCommand command, Time intervalBetweenRuns){

        super(startTime, endTime, command, intervalBetweenRuns);

        this.appliance = appliance;
    }

    /**Returns the appliance to which this ApplianceTimedEvent is
     * attached
     *
     * @return The appliance to which this ApplianceTimedEvent is attached
     */
    public Appliance getRegisteredAppliance(){
        return appliance;
    }
}
