package household;
import java.awt.Color;

/** Represents the concept of a house being in different states of usage depending
 * on its current consumption and the set thresholds for each state
 *
 * @author Darius Hodaei
 */
public abstract class PowerState {
    /**a handle to the house to which this power state is attached*/
    protected HouseHold house;
    /**the color representing this power state*/
    protected Color stateColor;

    
    /**constant identifier for green power state*/
    public static final int GREEN = 0;
    /**constant identifier for amber power state*/
    public static final int AMBER = 1;
    /**constant identifier for red power state*/
    public static final int RED = 2;
    
    /**Registers the given HouseHold to this PowerState
     *
     * @param house The HouseHold to which this PowerState will be attached
     */
    public PowerState(HouseHold house){this.house = house; 
        stateColor = Color.BLUE;
    }

    /**Uses the relevant IPolicy method to execute state dependant
     * functionality on the house
     */
    public abstract void action();

    /**Retrieves the color representation of this power state
     *
     * @return the color that represents this power state
     */
    public Color getColor(){
        return stateColor;
    }

    /**Returns the constant that identifies this particular power state
     *
     * @return the int constant that is unique to the particular power state
     */
    public abstract int getStateInt();

    /**Converts the int representation of a power state to its String counterpart
     *
     * @param n the integer constant to convert to a string
     * @return the string name corresponding to the power state represented by the given int
     */
    public static String intToStateString(int n){

       switch(n){
           case 0: return "GREEN";

           case 1: return "AMBER";

           case 2: return "RED";

           default: return "Unrecognized State";
       }
    }
}
