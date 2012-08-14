package policy;
import policy.core.PolicyComponent;
import appliance.core.ApplianceType;
import household.HouseHold;
import appliance.core.Appliance;
import time.TimedEvent;
import java.util.Stack;


/**Models a very energy conscious person with a keen interest in protecting
 * the envinronment!
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class Environmentalist extends PolicyComponent {

	/**Registers the HouseHold to which this PolicyComponent is attached
	*/
    public Environmentalist(HouseHold house){
        super(house);

    }

    /**Store a reference to the household this policy will be attached to
     *
     * @param house The house this policy is attached to
     */
    //abstract void registerHouseHold(HouseHold house);

    /**This will be called by the registered house whenever it is in a
     * Red state of power consumption (via RedPowerState).
     */
    public void redStateAction(){
        // Turns all the appliances off, can't be wasting energy!!
        for(Appliance a : house.getAllAppliances()){
            a.setOn(false);
        }
    }

    /**This will be called by the registered house whenever it is in a
     * Amber state of power consumption (via AmberPowerState).
     */
    public void amberStateAction(){
		// I'll turn off these luxury appliances, just in case we risk going to a red power state!
        for(Appliance a : house.getAppliancesByCategory(ApplianceType.LUXURY_ITEMS)){
            a.setOn(false);
        }

    }

    /**This will be called by the registered house whenever it is in a
     * Green state of power consumption (via GreenPowerState).
     */
    public void greenStateAction(){
		// I'm happy! Think Green.
	}

    /**Rules that are applied to this house as a part of this policy.
     * These rules can be widely varied.
     *
     * @return The TimedEvents that represent state-independent policy rules
     */
    public Stack<TimedEvent> generalRules(){
		// I only react to power states!
        return new Stack<TimedEvent>();

    }


}
