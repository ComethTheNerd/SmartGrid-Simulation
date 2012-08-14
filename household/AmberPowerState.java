package household;
import policy.core.PolicyComponent;
import java.awt.Color;

/**Represents when a house is an amber state of power usage
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class AmberPowerState extends PowerState{

    /**
     * Creates a new AmberPowerState instance with the supplied Household
     *
     * @param house HouseHold attached to this AmberPowerState
     */
    public AmberPowerState(HouseHold house){
        super(house);
        this.stateColor = Color.ORANGE;
    }

    public void action(){
        for(PolicyComponent component : house.getPolicy()) component.amberStateAction();
    }

        public int getStateInt(){return PowerState.AMBER;}
}
