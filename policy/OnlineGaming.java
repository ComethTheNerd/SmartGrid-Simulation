package policy;

import policy.core.PolicyComponent;
import appliance.core.Appliance;
import appliance.core.ApplianceType;
import appliance.Computer;
import appliance.GamesConsole;
import appliance.InteriorLight;
import appliance.TV;
import household.HouseHold;
import java.util.ArrayList;
import java.util.Stack;
import time.Time;
import time.TimedEvent;

/**
 *This Class models the activities of a household during various OnlineGaming sessions
 * @author Alexander Finn<k1183655>
 */
public class OnlineGaming extends PolicyComponent {

   /**
    * Constructor, creates a new OnlineGaming adopted by the specified household
    * @param h the household which uses policyComponent
    */
    public OnlineGaming(HouseHold h) {
        super(h);
    }

    /**Rules that are applied to this house as a part of this policy.
     * These rules can be widely varied.
     *
     * @return The TimedEvents that represent state-independent policy rules
     */
    public Stack<TimedEvent> generalRules(){
        ArrayList<Appliance> interiorlight1, gamesconsole1, tv1;
        interiorlight1 = house.getAppliancesByClass(InteriorLight.class);
        gamesconsole1 = house.getAppliancesByClass(GamesConsole.class);
        tv1 = house.getAppliancesByClass(TV.class);

        Stack<TimedEvent> stack = new Stack<TimedEvent>(); 
            
        for (Appliance interiorlight1on : interiorlight1) { 
            stack.addAll(interiorlight1on.generateRepeatedUsagePeriod(18,5,0,3, new Time(0, 0, 1, 0, 0))); //simulates the use of interior light on Saturday
            stack.addAll(interiorlight1on.generateRepeatedUsagePeriod(22,6,0,2, new Time(0, 0, 1, 0, 0))); //simulates the use of interior light on Sunday
        }
        for (Appliance gamesconsole1on : gamesconsole1) {
            stack.addAll(gamesconsole1on.generateRepeatedUsagePeriod(18,5,0,3, new Time(0, 0, 1, 0, 0))); //simulates the use of Games console on Saturday
            stack.addAll(gamesconsole1on.generateRepeatedUsagePeriod(22,6,0,2, new Time(0, 0, 1, 0, 0))); //simulates the use of Games console on Sunday
        }
        for (Appliance tv1on : tv1) {
            stack.addAll(tv1on.generateRepeatedUsagePeriod(18,5,0,3, new Time(0, 0, 1, 0, 0))); //simulates the use of TV on Saturday
            stack.addAll(tv1on.generateRepeatedUsagePeriod(22,6,0,2, new Time(0, 0, 1, 0, 0))); //simulates the use of TV on Sunday
        }
        
        return stack;
            
    }
}
    
