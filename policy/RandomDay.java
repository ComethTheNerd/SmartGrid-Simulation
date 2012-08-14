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
import appliance.TV;
import appliance.DvdPlayer;
import appliance.Grill;
import appliance.HairDryer;
import appliance.PowerShower;
import appliance.Laptop;
import time.ApplianceTimedEvent;
import time.commands.ApplianceUseCommand;

/**Policy for RandomDay.
 * Repeats different events differently
 * w/ tv, dvd, hairdryer, grill, powershower and laptop
 * @author Jake Baker
 */

public class RandomDay extends PolicyComponent {

    /**
    * Constructor, creates a new RandomDay adopted by the specified household
    * @param h the household which uses policyComponent
    */
    public RandomDay(HouseHold h) {
	super(h);
    }

/**
 *
 * Description
 * tv and dvd used together, but tv on for longer.
 * shower and hairdryer used at 6am and 7am, and repeated everyday.
 * grill used at 1 oclock and then repeated every 2 days
 */
    public Stack<TimedEvent> generalRules() {

        ArrayList<Appliance> tv, dvd, hairdryer, grill, powerShower, laptop;
	tv = house.getAppliancesByClass(TV.class);
	dvd = house.getAppliancesByClass(DvdPlayer.class);
	hairdryer = house.getAppliancesByClass(HairDryer.class);
	grill = house.getAppliancesByClass(Grill.class);
	powerShower = house.getAppliancesByClass(Laptop.class);

	Stack<TimedEvent> stack = new Stack<TimedEvent>();
	for (Appliance tvon : tv) {
	    stack.push(tvon.generateUsageHour(19, 6, new Time(0, 5, 0, 0, 0)));
	}
	for (Appliance dvdon : dvd) {
	    stack.push(dvdon.generateUsageHour(19, 3, new Time(0, 5, 0, 0, 0)));
	}
	for (Appliance hairDryOn : hairdryer) {
	    stack.push(hairDryOn.generateUsageHour(7, 1, new Time(0, 1, 0, 0, 0)));
	}
	for (Appliance grillOn : grill) {
	    stack.push(grillOn.generateUsageHour(13, 2, new Time(0, 2, 0, 0, 0)));
	}
	for (Appliance powershowOn : powerShower) {
	    stack.push(powershowOn.generateUsageHour(6, 1, new Time(0, 1, 0, 0, 0)));
	}

	return stack;
    }
}


//TV;
//import appliance.DvdPlayer;
//import appliance.Grill;
//import appliance.HairDryer;
//import appliance.Powershower;
//import appliance.Laptop;