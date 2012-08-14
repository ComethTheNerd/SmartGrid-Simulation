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
import appliance.Iron;
import time.ApplianceTimedEvent;
import time.commands.ApplianceUseCommand;

/**Policy for ironing between 12 and 2 repeated every 3 days
 * w/ iron.
 *
 * @author Jake Baker
 */

public class IronClothes extends PolicyComponent {

    /**
    * Constructor, creates a new IronClothes policy adopted by the specified household
    * @param h the household which uses policyComponent
    */
    public IronClothes(HouseHold h) {
        super(h);
    }

    public Stack<TimedEvent> generalRules() {
        ArrayList<Appliance> ironing;
        ironing = house.getAppliancesByClass(Iron.class);

       Stack<TimedEvent> stack = new Stack<TimedEvent>();
        for (Appliance startIroning : ironing) {
            stack.push(startIroning.generateUsageHour(12, 2, new Time(0, 3, 0, 0, 0)));
        }

        return stack;

    }
}
