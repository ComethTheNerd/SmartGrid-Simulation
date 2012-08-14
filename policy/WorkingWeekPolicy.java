package policy;
import policy.core.PolicyComponent;
import java.util.Stack;
import household.HouseHold;
import infopackage.HouseParameters;
import time.HouseTimedEvent;
import time.Time;
import time.TimedEvent;
/**Models a working week being repeated every week
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class WorkingWeekPolicy extends PolicyComponent {
	/**The house parameters we use to modify number of occupants at home*/
    private HouseParameters houseParams;

	/**Registers the house this PolicyComponent is attached to
	*/
    public WorkingWeekPolicy(HouseHold h){
        super(h);
        this.houseParams = house.getParams();
    }

    public Stack<TimedEvent> generalRules(){

       Stack<TimedEvent> rules = new Stack<TimedEvent>();

        // This will model a weekly work routine of Monday-Friday 9am-5pm daily
       HouseTimedEvent weekWrapper = new HouseTimedEvent(houseParams,
                                        new Time(9,0),
                                        new Time(17,4),
                                        new WeeklyWorkRoutine(houseParams),
                                        new Time(0,0,1));
        rules.push(weekWrapper);
        return rules;

    }
}
