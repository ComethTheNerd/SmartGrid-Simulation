package API;
import policy.core.PolicyComponent;
import policy.*;
import java.util.Stack;
import time.*;
import household.HouseHold;
import appliance.*;

/**A sample PolicyComponent skeleton for a programmer to use as a reference
 * when programming with the API framework
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class TemplatePolicyComponent extends PolicyComponent{
    /* NOTE: WHEN WRITING CUSTOM POLICY COMPONENTS YOU MUST REGISTER
       THEM WITH THE SYSTEM.

       THIS SHOULD BE DONE IN RegisterPolicies.java - WE'LL TAKE IT FROM THERE!

       CHECK OUT package:policy FOR SOME IDEAS TO GET STARTED!
    */

    /* NOTE: this.house is an inherited member holding the HouseHold
     instance that an instance of this PolicyComponent  is attached to */

    public TemplatePolicyComponent(HouseHold h){
        super(h);
    }

    /**Store a reference to the household this policy will be attached to
     *
     * @param house The house this policy is attached to
     */
    //abstract void registerHouseHold(HouseHold house);

    /**This will be called by the registered house whenever it is in a
     * Red state of power consumption (via RedPowerState).
     */
    @Override
    public void redStateAction(){
        /* TODO: WHAT YOU WANT TO HAPPEN WHEN THE HOUSE IS IN A RED POWER STATE */
    }

    /**This will be called by the registered house whenever it is in a
     * Amber state of power consumption (via AmberPowerState).
     */
    @Override
    public void amberStateAction(){

       /* TODO: WHAT YOU WANT TO HAPPEN WHEN THE HOUSE IS IN AN AMBER POWER STATE */
    }

    /**This will be called by the registered house whenever it is in a
     * Green state of power consumption (via GreenPowerState).
     */
    @Override
    public void greenStateAction(){

        /* TODO: WHAT YOU WANT TO HAPPEN WHEN THE HOUSE IS IN A GREEN POWER STATE */
    }

    /**Rules that are applied to this house as a part of this policy.
     * These rules can be widely varied.
     *
     * @return The TimedEvents that represent state-independent policy rules
     */
    public Stack<TimedEvent> generalRules(){

        Stack<TimedEvent> rules = new Stack<TimedEvent>();

        /*
         TODO: ADD YOUR CUSTOM HOUSE/APPLIANCE TIMED EVENTS TO THE STACK,
         THAT WILL BE USED TO CONTROL THE WAY this.house ACTS WHEN AN
         INSTANCE OF THIS POLICY IS ATTACHED - REGARDLESS OF POWER STATE
         */

        return rules;
    }
}
