package universe;
import gui.View;
import input.Console;
import input.IInput;
import utils.Config;
/**Manages the different output channels that are available to the user,
 * and allows for switching between them dynamically
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class UserInput implements IObserver{
    /**Characteristic of the Singleton Design Pattern*/
    private static UserInput instance;
    /**Identifiers for the different output channels
     */
    public enum OUTPUT_MODE{Console, Gui;}
    /**A handle to the View object*/
    private View view;
    /**the currently selected output channel
     */
    private OUTPUT_MODE output;
    /**the current output channel object*/
    private IInput currentOut;
    /**a handle to the Smart Grid Universe Singleton instance*/
    private SmartGridUniverse universe;

    /**Allows for only one single instance of this class to be created
     * during the simulation lifetime
     *
     * @return the same single instance of this class every time
     */
    public static UserInput getInstance(){
        // instantiate if necessary
        if(instance == null) instance = new UserInput();
        return instance;
    }

    private UserInput(){
        universe = SmartGridUniverse.getInstance();

        // set up the output channel as the default
        output = Config.OUTPUT;
        if(output == OUTPUT_MODE.Gui){
            currentOut = new View();
        }
         else if(output == OUTPUT_MODE.Console){
             currentOut = Console.getInstance();
         }
        // open the output channel
        currentOut.open();

    }

    /**Returns the current output mode identifier
     *
     * @return a constant identifier associated with the current output channel
     */
    public OUTPUT_MODE getCurrentOutputMode(){
        return output;
    }


    public void update(){
        currentOut.update();
    }

    /**Change the current output channel to the output channel
     * type associated with the given constant identifier
     *
     * @param om the identifier to change the output channel to
     */
    public void changeOutput(OUTPUT_MODE om){
        // we remember the state because we restore it to this after changing the channel
        boolean state = universe.isPaused();
        // pause the simulation before the change
        universe.setPause(true);
        // call close on the current output channel
        currentOut.close();
        // assign the new output channel value
        output = om;

        if(om == OUTPUT_MODE.Gui){
            currentOut = new View();
        }
        if(om == OUTPUT_MODE.Console){
            currentOut = Console.getInstance();
        }
        // open the new output channel
        currentOut.open();
        // restore the state of the simulation
        universe.setPause(state);
    }
}


