package policy;

import policy.core.PolicyComponent;
import java.util.Stack;
import household.HouseHold;
import time.Time;
import time.TimedEvent;
import java.util.ArrayList;
import appliance.core.Appliance;
import appliance.core.ApplianceType;
import appliance.InteriorLight;
import appliance.Toaster;
import appliance.Computer;
import appliance.TV;
import appliance.DvdPlayer;
import appliance.Microwave;
import appliance.Kettle;
import appliance.PowerShower;
import appliance.Laptop;
import appliance.Heating;

/**
 *This Class models the activities of a household during a Chilled(Relaxed in house) Weekend 
 * @author Alexander Finn <k1183655>
 */
public class ChilledWeekend extends PolicyComponent {

    private OnlineGaming onlinegaming1; //create object of type OnlineGaming
    private GreedyDinner studentchillweekenddinner1; //create object of type StudentChillWeekendDinner
    
   /**
    * Constructor, creates a new ChilledWeekend adopted by the specified household
    * @param h the household which uses policyComponent
    */
    public ChilledWeekend(HouseHold h) {
        super(h);
        
        onlinegaming1 = new OnlineGaming(h); //create instance of online gaming
        studentchillweekenddinner1 = new GreedyDinner(h);
        
}



    /**Rules that are applied to this house as a part of this policy.
     * These rules can be widely varied.
     *
     * @return The TimedEvents that represent state-independent policy rules
     */
    public Stack<TimedEvent> generalRules(){

        ArrayList<Appliance> interiorlight1, toaster1, computer1,
                                    tv1, dvd1, microwave1, kettle1, powershower1, laptop1, heating1;

        interiorlight1 = house.getAppliancesByClass(InteriorLight.class);
        toaster1 = house.getAppliancesByClass(Toaster.class);
        computer1 = house.getAppliancesByClass(Computer.class);
        tv1 = house.getAppliancesByClass(TV.class);
        dvd1 = house.getAppliancesByClass(DvdPlayer.class);
        microwave1 = house.getAppliancesByClass(Microwave.class);
        kettle1 = house.getAppliancesByClass(Kettle.class);
        powershower1 = house.getAppliancesByClass(PowerShower.class);
        laptop1 = house.getAppliancesByClass(Laptop.class);
        heating1 = house.getAppliancesByClass(Heating.class);

            Stack<TimedEvent> stack = new Stack<TimedEvent>();
            Stack<TimedEvent> Gaming = onlinegaming1.generalRules(); //retrieves online gaming general rules
            Stack<TimedEvent> StudentDinner = studentchillweekenddinner1.generalRules(); //retrieves student dinner rules
            
        for (Appliance interiorlight1on : interiorlight1) { 
            stack.addAll(interiorlight1on.generateRepeatedUsagePeriod(18,5,0,6, new Time(0, 0, 1, 0, 1))); ////simulates the use of light for general use
            stack.addAll(interiorlight1on.generateRepeatedUsagePeriod(20,6,0,2, new Time(0, 0, 1, 0, 1))); ////simulates the use of light for study
        }
        for (Appliance toaster1on : toaster1) {
            stack.addAll(toaster1on.generateRepeatedUsagePeriod(13,5,0,1, new Time(0, 0, 1, 0, 1)));  //simulates the use of toaster
        }   
        for (Appliance kettle1on : kettle1) {
            stack.addAll(kettle1on.generateRepeatedUsagePeriod(13,5,0,1, new Time(0, 0, 1, 0, 1))); //simulates the use of a kettle 
        }
        for (Appliance microwave1on : microwave1) {
            stack.addAll(microwave1on.generateRepeatedUsagePeriod(13,5,0,1, new Time(0, 0, 1, 0, 1))); //simulates the use of microwave
        }
        for (Appliance powershower1on : powershower1) {
            stack.addAll(powershower1on.generateRepeatedUsagePeriod(12,5,0,1, new Time(0, 0, 1, 0, 1))); //simulates the use of powershower
            stack.addAll(powershower1on.generateRepeatedUsagePeriod(15,6,0,1, new Time(0, 0, 1, 0, 1)));
        }
        for (Appliance laptop1on : laptop1) {
            stack.addAll(laptop1on.generateRepeatedUsagePeriod(14,5,0,3, new Time(0, 0, 1, 0, 1))); //laptop for internet
            stack.addAll(laptop1on.generateRepeatedUsagePeriod(21,5,0,3, new Time(0, 0, 1, 0, 1))); //laptop for Skype
            stack.addAll(laptop1on.generateRepeatedUsagePeriod(20,6,0,2, new Time(0, 0, 1, 0, 1))); //laptop for study
            stack.addAll(laptop1on.generateRepeatedUsagePeriod(00,6,0,2, new Time(0, 0, 1, 0, 1))); //laptop for Skype & study
        }
        for (Appliance heating1on : heating1) {
            stack.addAll(heating1on.generateRepeatedUsagePeriod(15,5,0,1, new Time(0, 0, 1, 0, 0))); //simulates the use of heating
            stack.addAll(heating1on.generateRepeatedUsagePeriod(19,5,0,1, new Time(0, 0, 1, 0, 0)));
        }
        for (Appliance dvd1on : dvd1) {
            stack.addAll(dvd1on.generateRepeatedUsagePeriod(13,5,0,1, new Time(0, 0, 1, 0, 1))); //simulates the use of DVD player
        }
        for (Appliance tv1on : tv1) {
            stack.addAll(tv1on.generateRepeatedUsagePeriod(17,6,0,2, new Time(0, 0, 1, 0, 1))); //simulates the use of TV
        }
        for (Appliance computer1on : computer1) {
            stack.addAll(computer1on.generateRepeatedUsagePeriod(16,5,0,2, new Time(0, 0, 1, 0, 1))); //simulates the use of computer
        }
        
        stack.addAll(Gaming); //push all gaming rules into stack
        stack.addAll(StudentDinner); //push all student dinner rules onto stack
        return stack;
       
      
    }
}