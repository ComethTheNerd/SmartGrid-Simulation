/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package policy;

import policy.core.PolicyComponent;
import appliance.AirConditioning;
import appliance.core.Appliance;
import appliance.core.ApplianceType;
import household.HouseHold;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import time.Time;
import time.TimedEvent;

/**This policy models the activities of a household
 * on a warm weekend
 *
 * @author Tokoni Kemenanabo
 */
public class WarmWeekendPolicy extends PolicyComponent{

    /**Constructor, creates a new WarmWeekendPolicy
     *
     * @param house the household which uses policyComponent
     */
    public WarmWeekendPolicy(HouseHold house){
        super(house);

    }



    /**Returns the general rules associated with this
     * WarmWeekendPolicy
     *
     * @return rules a Stack of timed events representing
     * the behaviour of the household using this policy
     */
    @Override
    public Stack<TimedEvent> generalRules() {

        Random r = new Random();
        Stack<TimedEvent> rules = new Stack<TimedEvent>();
        //List of airconditioners in the household
        ArrayList<Appliance> air = house.getAppliancesByClass(AirConditioning.class);

        //apply these rules for each airconditioner in the household
        for(Appliance ac : air){
            //AirConditioning comes at 6,7 or 8 on each day from saturday to sunday
            //and lasts for two hours repeating each week for each day.
            rules.addAll(ac.generateRepeatedUsagePeriod(7+r.nextInt(3), 5, 1, 2, new Time(0,0,1)));
            //More rules similar to the above
            rules.addAll(ac.generateRepeatedUsagePeriod(10+r.nextInt(3), 5, 1, 2, new Time(0,0,1)));
            rules.addAll(ac.generateRepeatedUsagePeriod(13+r.nextInt(3), 5, 1, 2, new Time(0,0,1)));
            rules.addAll(ac.generateRepeatedUsagePeriod(16+r.nextInt(3), 5, 1, 1, new Time(0,0,1)));
            rules.addAll(ac.generateRepeatedUsagePeriod(19+r.nextInt(3), 5, 1, 1, new Time(0,0,1)));
            rules.addAll(ac.generateRepeatedUsagePeriod(22+r.nextInt(2), 5, 1, 1, new Time(0,0,1)));
            rules.addAll(ac.generateRepeatedUsagePeriod(0  , 5, 1, 5, new Time(0,0,1)));

        }
        return rules;
    }

}
