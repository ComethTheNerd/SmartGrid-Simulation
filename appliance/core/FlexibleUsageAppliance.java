package appliance.core;
import time.Time;
/** Granularity only goes down to an hour in our simulation so this allows us
 * to add a more specialised middle-layer to the Appliance hierarchy, in order
 * to divide that hourly usage by a more realistic usage period of time for the
 * Appliance
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class FlexibleUsageAppliance extends Appliance{
    /**the duration in minutes that this appliance is typically used for*/
    protected int usageMinutes = 3; // default
    /**the number of minutes in an hour is used in the energy calculation*/
    protected final int minutesInHour = 60;
    
    /**Uses the energy required for a standard usage hour for this appliance, and
     * adjusts it to reflect the fact this appliance is used for a shorter period
     *
     * @param rawEnergy the standard energy required by this appliance for a usage hour
     * @return the adjusted energy required for a usage of this appliance,
     * based on its typically shorter usage period
     */
    protected final int burstEnergy(int rawEnergy){
        // note this allows for times >1 hour too, eg, 90 minutes
        float multiplier = ((float)usageMinutes / minutesInHour);
        // return the adjusted consumption for this appliance
        return (int)((float)rawEnergy * multiplier);
    }

    @Override
    public int energyRequired(Time time){
        /* We override the energyRequired() method so that we can adjust
           the calculated usage accordingly */
        return burstEnergy(super.energyRequired(time));
    }
}
