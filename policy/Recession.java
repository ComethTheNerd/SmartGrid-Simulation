package policy;
import policy.core.PolicyComponent;
import appliance.core.ApplianceType;
import java.util.Stack;
import time.TimedEvent;
import household.HouseHold;
import infopackage.HouseParameters;
import time.commands.HouseTimedCommand;
import time.Time;
import time.HouseTimedEvent;
import appliance.core.Appliance;
import household.PowerState;

/**When money is tight you will want to really keep a tight grip on your energy costs,
 * so to achieve this we make sure we do not use a lot of energy. Moreover, we'll
 * love the acceptable energy thresholds so our strict appliance usage has less tolerance
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class Recession extends PolicyComponent{
    
	/**Used to adjust the acceptable power state thresholds*/
	HouseParameters hp;
    
	/**Registers the HouseHold to which this PolicyComponent is attached
	*/
    public Recession(HouseHold h){
        super(h);
		// retrieve the house params we will modify
        hp = house.getParams();
    }
    
    @Override
    public void redStateAction(){

        for(ApplianceType at : ApplianceType.values()){
			// if we ever get in to a red state, lets turn everything off! Think of the money!
            for(Appliance a :house.getAppliancesByCategory(at)){
                a.setOn(false);
            }
        }
    }
    
    @Override
    public void amberStateAction(){
		// these are the appliances we'll turn off if we're in an amber state - the Luxury Items, and the White Goods
        ApplianceType[] types = {ApplianceType.LUXURY_ITEMS, ApplianceType.WHITE_GOODS};

        for(ApplianceType at : types){
			// turn off the selected appliances
            for(Appliance a :house.getAppliancesByCategory(at)){
                a.setOn(false);
            }
        }
       
    
    }

    public Stack<TimedEvent> generalRules(){
        Stack<TimedEvent> rules = new Stack<TimedEvent>();
        // Constant rule that is applied throughout the life of this PolicyComponent
		// NOTE how it just continually repeats...
        rules.add(new HouseTimedEvent(hp, new Time(0,0), new Time(0,1), new RecessionCommand(hp), new Time(0,1)));
        
        return rules;
    }
    
	/**A counterpart to the Recession PolicyComponent. It adjusts acceptable power state thresholds
	* for a house to enforce more strict control over consumption
	*
	*/
    private class RecessionCommand extends HouseTimedCommand{
		/**A shortcut way to access the qualified Headers of a HouseParameters object*/
        private HouseParameters.Header greenLim = HouseParameters.Header.GreenStateLimit,
                amberLim = HouseParameters.Header.AmberStateLimit;
				
        private int 
					/**the amount of times we can go above the limit before taking action*/
					tolerance = 5,
					/**the number of times the acceptable limit is violated*/
                    count,
					/**the original limit set for the Green power state threshold*/
                    originalGreen,
					/**the original limit set for the Amber power state threshold*/
                    originalAmber;

		/**Stores the HouseParameters that we will modify
		*/
        public RecessionCommand(HouseParameters houseParams){
            super(houseParams);
        }
		
        public void atStart(){
			// record the original limits so we can restore these
			// at the end of the command - no long lasting modifications
            originalGreen = hp.getValue(greenLim);
            originalAmber = hp.getValue(amberLim);
			// see if we are violating the limits and need to curtail consumption
            checkState();
        }
        public void atDuring(){
			// see if we are violating the limits and need to curtail consumption
            checkState();
        }
        public void atEnd(){
			// we restore the original values so the state of the house
			// is as it was before this command executed
            hp.setValue(greenLim, originalGreen);
            hp.setValue(amberLim, originalAmber);
        }

		/**Checks to see whether a house's consumption is violating the current
		* tight consumption limits imposed by this command
		*/
        private void checkState(){
			// get the current state as an integer representation
			int current = house.getCurrentState().getStateInt();
			// if the house is in an Amber power state, or a Red power state
            if(current == PowerState.AMBER || current == PowerState.RED){
                ++count; // increment counter
            }
            else count = 0;	 // oth
			
			// if we have violated the limited too many times
            if(count == tolerance){
				// get the current power state limits
                int g = hp.getValue(greenLim),
                    a = hp.getValue(amberLim);
				// set the new limits to 80% of their current value
                hp.setValue(greenLim, (int)(g * 0.8));
                hp.setValue(amberLim, (int)(a * 0.8));
				// reset the counter
                count = 0;
            }
        }
    }

}
