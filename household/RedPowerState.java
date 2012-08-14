package household;
import policy.core.PolicyComponent;
import java.awt.Color;
/**Represents when a house is a red state of power usage
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class RedPowerState extends PowerState{

    /**Creates a new RedPowerState instance with the supplied Household
     *
     * @param house HouseHold attached to this RedPowerState
     */

    public RedPowerState(HouseHold house){
        super(house);
        this.stateColor = Color.RED;
    }

    public void action(){
        for(PolicyComponent component : house.getPolicy()) component.redStateAction();
    }
    public int getStateInt(){
        return PowerState.RED;}
}
