package policy;
import policy.core.PolicyComponent;
import java.util.ArrayList;
import java.util.Stack;
import time.TimedEvent;
import time.Time;
import household.HouseHold;
import appliance.core.Appliance;
import appliance.core.ApplianceType;
import appliance.Kettle;
import appliance.Hob;
import appliance.InteriorLight;
import appliance.Blender;
import appliance.Microwave;

/**
 *This Class models the activities of a household during the GreedyDinner Policy
 * @author Alexander Finn <k1183655>
 */
public class GreedyDinner extends PolicyComponent {

   /**
    * Constructor, creates a new GreedyDinner adopted by the specified household
    * @param h the household which uses policyComponent
    */
    public GreedyDinner(HouseHold h) {
        super(h);     
    }

    /**Rules that are applied to this house as a part of this policy.
     * These rules can be widely varied.
     *
     * @return The TimedEvents that represent state-independent policy rules
     */
    public Stack<TimedEvent> generalRules(){

        ArrayList<Appliance> interiorlight2,kettle2,hob2,blender2,microwave2;

        interiorlight2 = house.getAppliancesByClass(InteriorLight.class);
        kettle2 = house.getAppliancesByClass(Kettle.class);
        hob2 = house.getAppliancesByClass(Hob.class);
        microwave2 = house.getAppliancesByClass(Microwave.class);
        blender2 = house.getAppliancesByClass(Blender.class);

            Stack<TimedEvent> stack = new Stack<TimedEvent>();
            
        for (Appliance interiorlight2on : interiorlight2) { 
            stack.addAll(interiorlight2on.generateRepeatedUsagePeriod(1,5,1,2, new Time(0, 0, 1, 0, 0))); //simulates the use of interior light
        }
        for (Appliance kettle2on : kettle2) {
            stack.addAll(kettle2on .generateRepeatedUsagePeriod(1,5,1,2, new Time(0, 0, 1, 0, 0))); //simulates the use of kettle
        }
        for (Appliance hob2on : hob2) {
            stack.addAll(hob2on.generateRepeatedUsagePeriod(1,5,1,2, new Time(0, 0, 1, 0, 0))); //simulates the use of electric hob
        }
        for (Appliance blender2on : blender2) {
            stack.addAll(blender2on.generateRepeatedUsagePeriod(1,5,1,2, new Time(0, 0, 1, 0, 0))); //simulates the use of blender
        } 
            for (Appliance microwave2on : microwave2) {
            stack.addAll(microwave2on.generateRepeatedUsagePeriod(1,5,1,2, new Time(0, 0, 1, 0, 0))); //simulates the use of microwave
        }
        return stack;
       
    }
}
