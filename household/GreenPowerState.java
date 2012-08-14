package household;
import policy.core.PolicyComponent;
import java.awt.Color;
/**Represents when a house is a green state of power usage
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class GreenPowerState extends PowerState{

    /**
     * Creates a new GreenPowerState instance with the supplied Household
     *
     * @param house HouseHold attached to this GreenPowerState
     */
    public GreenPowerState(HouseHold house){
        super(house);
        this.stateColor = Color.GREEN;
    }

    public void action(){
        for(PolicyComponent component : house.getPolicy()) component.greenStateAction();
    }

    public int getStateInt(){return PowerState.GREEN;}
}
