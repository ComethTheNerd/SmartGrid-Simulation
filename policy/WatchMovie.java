package policy;

import policy.core.PolicyComponent;
import appliance.core.ApplianceType;
import java.util.Stack;
import household.HouseHold;
import time.HouseTimedEvent;
import time.Time;
import time.TimedEvent;
import java.util.ArrayList;
import appliance.core.Appliance;
import appliance.Computer;
import appliance.DvdPlayer;
import appliance.GamesConsole;
import appliance.TV;
import time.ApplianceTimedEvent;
import time.commands.ApplianceUseCommand;

/**A policy modelling the behaviour of a household when watching a movie
 *
 * @author Sonke Tietgens <K1186281>
 */
public class WatchMovie extends PolicyComponent {

    /**creates a new WatchMovie object
     * 
     * @param h the household which uses policyComponent
     */
    public WatchMovie(HouseHold h) {
        super(h);
    }


    public Stack<TimedEvent> generalRules() {

        ArrayList<Appliance> tv, dvd;// This creating a list of arrays which will be used to store a single Appliance
        
        tv = house.getAppliancesByClass(TV.class);  // This sets the variable tv to the TV class that is a sub class of the appliances
        dvd = house.getAppliancesByClass(DvdPlayer.class);  // This sets the dvd ket to the DvdPlayer class that is a sub class of the appliances

        Stack<TimedEvent> stack = new Stack<TimedEvent>();
        for (Appliance tvon : tv) {
            stack.push(tvon.generateUsageHour(19, 2, new Time(0, 3, 0, 0, 0)));
            // This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        }
        for (Appliance dvdon : dvd) {
            stack.push(dvdon.generateUsageHour(19, 2, new Time(0, 3, 0, 0, 0)));
            // This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        }
        return stack;
    }
}
