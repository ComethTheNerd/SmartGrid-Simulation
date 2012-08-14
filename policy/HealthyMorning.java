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
import appliance.Blender;
import appliance.SwimmingPoolHeating;
import appliance.GamesConsole;
import appliance.TV;
import time.ApplianceTimedEvent;
import time.commands.ApplianceUseCommand;

/**Policy for Healthy Early Morning
 * w/ Blender & swimming Pool Heater
 *
 * @author Jake Baker
 */

public class HealthyMorning extends PolicyComponent {


     /**
    * Constructor, creates a new HealthyMorning policy adopted by the specified household
    * @param h the household which uses policyComponent
    */
    public HealthyMorning(HouseHold h) {
        super(h);
    }

 
    public Stack<TimedEvent> generalRules() {
        ArrayList<Appliance> blender, swimmingPoolHeating;

        blender = house.getAppliancesByClass(Blender.class);
        swimmingPoolHeating = house.getAppliancesByClass(SwimmingPoolHeating.class);


        Stack<TimedEvent> stack = new Stack<TimedEvent>();
        for (Appliance blenderOn : blender) {
            stack.push(blenderOn.generateUsageHour(6, 1, new Time(0, 1, 1, 0, 0)));
        }
        for (Appliance swimHeatOn : swimmingPoolHeating) {
            stack.push(swimHeatOn.generateUsageHour(7, 2, new Time(0, 1, 1, 0, 0)));
        }
        return stack;
    }
}
