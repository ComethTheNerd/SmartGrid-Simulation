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
import appliance.AudioEquipment;
import appliance.Computer;
import appliance.DishWasher;
import appliance.DvdPlayer;
import appliance.GamesConsole;
import appliance.Hob;
import appliance.Kettle;
import appliance.Oven;
import appliance.Refrigerator;
import appliance.TV;
import appliance.VacuumCleaner;
import appliance.WashingMachine;
import time.ApplianceTimedEvent;
import time.commands.ApplianceUseCommand;

/**A policy modelling the activities of a household during
 * household cleaning
 *
 * @author Sonke Tietgens <K1186281>
 */
public class HouseCleaning extends PolicyComponent {

    /**creates a new HouseCleaning object
     * 
     * @param h the household which uses policyComponent
     */
    public HouseCleaning(HouseHold h) {
        super(h);

    }

    public Stack<TimedEvent> generalRules() {
        ArrayList<Appliance> vacuum, dish, wash, music; // This creating a list of arrays which will be used to store a single Appliance
        vacuum = house.getAppliancesByClass(VacuumCleaner.class);  // This sets the variable vacuum to the VacuumCleaner class that is a sub class of the appliances
        dish = house.getAppliancesByClass(Refrigerator.class);  // This sets the variable dish to the Refrigerator class that is a sub class of the appliances
        wash = house.getAppliancesByClass(WashingMachine.class); // This sets the variable wash to the WashingMachinge class that is a sub class of the appliances
        music = house.getAppliancesByClass(AudioEquipment.class); // This sets the variable music to the AudioEquipment class that is a sub class of the appliances

        Stack<TimedEvent> stack = new Stack<TimedEvent>();
        for (Appliance vac : vacuum) {
            stack.push(vac.generateUsageHour(13, 1, new Time(0, 0, 1, 0, 0)));
            // This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        }
        for (Appliance di : dish) {
            stack.push(di.generateUsageHour(13, 2, new Time(0, 0, 1, 0, 0)));
            // This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        }
        for (Appliance was : wash) {
            stack.push(was.generateUsageHour(14, 1, new Time(0, 0, 1, 0, 0)));
            // This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        }
        for (Appliance audio : music) {
            stack.push(audio.generateUsageHour(14, 1, new Time(0, 0, 1, 0, 0)));
            // This is the function that tells the appliance to triger at a specific time and the duration. It also show the interval
        }
        return stack;
    }
}