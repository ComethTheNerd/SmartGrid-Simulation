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
import appliance.DishWasher;
import appliance.DvdPlayer;
import appliance.GamesConsole;
import appliance.Hob;
import appliance.Kettle;
import appliance.Oven;
import appliance.Refrigerator;
import appliance.TV;
import time.ApplianceTimedEvent;
import time.commands.ApplianceUseCommand;

/**A policy modelling the event of cooking lunch at mid-day
 *
 * @author Sonke Tietgens <K1186281>
 */
public class CookedLunch extends PolicyComponent {
   
    /**creates a new CookedLunch object
     *
     * @param h the household which uses policyComponent
     */
    public CookedLunch(HouseHold h) {
        super(h);
        
    }


    public Stack<TimedEvent> generalRules() {
        ArrayList<Appliance> ket, fridge, ho, ov, dish; // This creating a list of arrays which will be used to store a single Appliance
        ket = house.getAppliancesByClass(Kettle.class); // This sets the variable ket to the Kettle class that is a sub class of the appliances
        fridge = house.getAppliancesByClass(Refrigerator.class); //This sets the variable fridge to the Refrigerator class that is a sub class of the appliances
        ho = house.getAppliancesByClass(Hob.class); // This sets the variable ho to the Hob class that is a sub class of the appliances
        ov = house.getAppliancesByClass(Oven.class); // This sets the variable ov to the Oven class that is a sub class of the appliances
        dish = house.getAppliancesByClass(DishWasher.class); // This sets the variable dish to the DishWasher class that is a sub class of the appliances

        Stack<TimedEvent> stack = new Stack<TimedEvent>();
        for (Appliance keton : ket) {
            stack.push(keton.generateUsageHour(13, 1, new Time(0, 2, 0, 0, 0)));
        }// This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        for (Appliance fridgeon : fridge) {
            stack.push(fridgeon.generateUsageHour(13, 2, new Time(0, 2, 0, 0, 0)));
        }// This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        for (Appliance fridgeon : ho) {
            stack.push(fridgeon.generateUsageHour(14, 1, new Time(0, 2, 0, 0, 0)));
        }// This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        for (Appliance ovon : ov) {
            stack.push(ovon.generateUsageHour(14, 1, new Time(0, 2, 0, 0, 0)));
        }// This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        for (Appliance dishon : dish) {
            stack.push(dishon.generateUsageHour(15, 1, new Time(0, 2, 0, 0, 0)));
        }// This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        return stack;
    }
}
