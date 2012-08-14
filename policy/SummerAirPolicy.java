/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package policy;

import policy.core.PolicyComponent;
import appliance.core.Appliance;
import appliance.core.ApplianceType;
import household.HouseHold;
import java.util.ArrayList;
import java.util.Stack;
import time.TimedEvent;

/**This policy models the activities of a household
 * during summer time
 *
 * @author Tokoni Kemenanabo
 */
public class SummerAirPolicy extends PolicyComponent {

    /**WarmWeekPolicy integrated into the SummerAirPolicy*/
    private WarmWeekPolicy warmWeek;
    /**WarmWeekendPolicy integrated into the SummerAirPolicy*/
    private WarmWeekendPolicy warmWeekend;

   /**Constructor, creates a new SummerAirPolicy
    * 
    * @param house the household which uses policyComponent
    */
    public SummerAirPolicy(HouseHold house){
        super(house);

        warmWeek = new WarmWeekPolicy(house);
        warmWeekend = new WarmWeekendPolicy(house);

    }

    /**Returns the general rules associated with this
     * SummerAirPolicy
     *
     * @return rules a Stack of timed events representing
     * the behaviour of the household using this policy
     */
    public Stack<TimedEvent> generalRules() {

        Stack<TimedEvent> rules = new Stack<TimedEvent>();

        //Rules from the WarmWeekPolicy
        Stack<TimedEvent> week = warmWeek.generalRules();
        //Rules from the WarmWeekendPolicy
        Stack<TimedEvent> weekend = warmWeekend.generalRules();

        //Add all rules from the WarmWeekPolicy to the rules of the SummerAirPolicy
        rules.addAll(week);
        //Add all rules from the WarmWeekendPolicy to the rules of the SummerAirPolicy
        rules.addAll(weekend);
        
        return rules;
    }

}