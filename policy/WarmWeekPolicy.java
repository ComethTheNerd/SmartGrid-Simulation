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
 * on a warm week
 *
 * @author Tokoni Kemenanabo
 */
public class WarmWeekPolicy extends PolicyComponent {

    /**Constructor, creates a new WarmWeekPolicy
     *
     * @param house the household which uses policyComponent
     */
    public WarmWeekPolicy(HouseHold house){
        super(house);
    }


    /**Returns the general rules associated with this
     * WarmWeekPolicy
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
            //AirConditioning comes at 6 or 7 on each day from monday to friday
            //and lasts for two hours repeating each week for each day.
            rules.addAll(ac.generateRepeatedUsagePeriod(6+r.nextInt(2), 0, 4, 2, new Time(0,0,1)));
            //<....work....>
            //More rules after work/school
            rules.addAll(ac.generateRepeatedUsagePeriod(17+r.nextInt(3), 0, 4, 3, new Time(0,0,1)));
            rules.addAll(ac.generateRepeatedUsagePeriod(22+r.nextInt(2),0, 4, 1, new Time(0,0,1)));
            rules.addAll(ac.generateRepeatedUsagePeriod(0,0, 4, 5, new Time(0,0,1)));

        }

        return rules;
    }

}
