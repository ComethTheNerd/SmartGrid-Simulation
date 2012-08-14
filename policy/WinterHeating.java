package policy;

import policy.core.PolicyComponent;
import time.TimedEvent;
import java.util.Stack;
import household.HouseHold;
import time.commands.ApplianceTimedCommand;
import appliance.core.Appliance;
import appliance.Heating;
import universe.SmartGridUniverse;
import time.Time;
import java.util.ArrayList;
import time.ApplianceTimedEvent;
import appliance.StorageHeater;

/**Typically people have their heating on throughout most of the cold
 * harsh Winter period! This PolicyComponent models that behaviour by
 * turning on heating related appliances for Winter.
 * 
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class WinterHeating extends PolicyComponent {

	/**Registers the HouseHold to which this PolicyComponent is attached
	*/
    public WinterHeating(HouseHold h) {
        super(h);
    }

    public Stack<TimedEvent> generalRules() {
        Stack<TimedEvent> rules = new Stack<TimedEvent>();

		// get all of the heaters and schedule them to use the WinterHeatingCommand
		// that will take care of turning them on when it gets to Winter
        for(Appliance heat : house.getAppliancesByClass(Heating.class)){
            rules.add(new ApplianceTimedEvent(heat,
                    new Time(0),
                    new Time(0),
                    new WinterHeatingCommand(heat),
                    new Time(0)));
        }
		// Also do the same for storage heaters as they are likely to be required too
         for(Appliance heat : house.getAppliancesByClass(StorageHeater.class)){
            rules.add(new ApplianceTimedEvent(heat,
                    new Time(0,0),
                    new Time(0,1),
                    new WinterHeatingCommand(heat),
                    new Time(0,1)));
        }
        return rules;
    }

}
