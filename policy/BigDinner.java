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
import appliance.Barbeque;
import appliance.Blender;
import appliance.Hob;
import appliance.Kettle;
import appliance.Microwave;
import appliance.Oven;
import appliance.TV;
//import policy.PolicyComponent;
import time.ApplianceTimedEvent;
import time.commands.ApplianceUseCommand;

/**A policy modelling the event of cooking a Big Dinner at evening
 *
 * @author Ogor
 */
public class BigDinner extends PolicyComponent{
    
     
   

  /**
    * Constructor, creates a new BigDinner policy adopted by the specified household
    * @param h the household which uses policyComponent
    */
  public BigDinner(HouseHold h){
    super(h);
    
     
    } /**/

    public Stack<TimedEvent> generalRules() {
        ArrayList<Appliance> hob, oven, Mwave, kettle, tv, blender, bbq;
        tv = house.getAppliancesByClass(TV.class);
        hob = house.getAppliancesByClass(Hob.class);
        oven = house.getAppliancesByClass(Oven.class);
        Mwave = house.getAppliancesByClass(Microwave.class);
        kettle = house.getAppliancesByClass(Kettle.class);
        blender = house.getAppliancesByClass(Blender.class);
        bbq = house.getAppliancesByClass(Barbeque.class);

        Stack<TimedEvent> stack = new Stack<TimedEvent>();
        for (Appliance tvon : tv) {
            stack.push(tvon.generateUsageHour(18, 2, new Time(0, 1, 0, 0, 0)));
        }
        
        for (Appliance hobon : hob) {
            stack.push(hobon.generateUsageHour(16, 5, new Time(0, 1, 0, 0, 0)));
        }
        
        for (Appliance ovenon : oven) {
            stack.push(ovenon.generateUsageHour(17, 3,new Time(0, 1, 0, 0, 0)));
        }
        
        for (Appliance mwaveon : Mwave) {
            stack.push(mwaveon.generateUsageHour(18, 1, new Time(0, 1, 0, 0, 0)));
        }
        
        for (Appliance kettleon : kettle) {
            stack.push(kettleon.generateUsageHour(19, 1, new Time(0, 1, 0, 0, 0)));
        }
        
        for (Appliance blenderon : blender) {
            stack.push(blenderon.generateUsageHour(16, 1, new Time(0, 1, 0, 0, 0)));
        }
        
        for (Appliance bbqon : bbq) {
            stack.push(bbqon.generateUsageHour(16, 2,new Time(0, 1, 0, 0, 0)));
        }
        return stack;
    }
}
