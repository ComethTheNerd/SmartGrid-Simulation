package policy;

import policy.core.PolicyComponent;
import appliance.core.ApplianceType;
import appliance.SwimmingPoolHeating;
import java.util.Stack;
import household.HouseHold;
import time.HouseTimedEvent;
import time.Time;
import time.TimedEvent;
import java.util.ArrayList;
import appliance.core.Appliance;
import time.ApplianceTimedEvent;
import time.commands.ApplianceUseCommand;

/**Models a swimming activity during the late hours of the day
 *
 * @author Sonke Tietgens <K1186281>
 */
public class LateSwimmer extends PolicyComponent {

    /**creates a new LateSwimmer object
     * 
     * @param h the household which uses policyComponent
     */
    public LateSwimmer(HouseHold h) {
        super(h);
    }

    public Stack<TimedEvent> generalRules() {
        ArrayList<Appliance> heaters; // This creating a list of arrays which will be used to store a single Appliance
        heaters = house.getAppliancesByClass(SwimmingPoolHeating.class);  // This sets the variable heaters to the SwimmingPoolHeating class that is a sub class of the appliances

        Stack<TimedEvent> stack = new Stack<TimedEvent>();
        for (Appliance heat : heaters) {
            stack.push(heat.generateUsageHour(18, 4, new Time(0, 2, 0, 0, 0)));
            // This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        }

        return stack;

    }
}
