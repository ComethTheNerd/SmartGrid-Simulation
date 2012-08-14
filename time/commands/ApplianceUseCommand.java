package time.commands;
import appliance.core.Appliance;
import appliance.*;

/**Allows the modelling of using an Appliance, that is, it is turned on at the start
 * of the command, remains on during, and is turned off at the end
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class ApplianceUseCommand extends ApplianceTimedCommand{

    /**Creates new ApplianceUseCommand which is attached to supplied appliance
     *
     * @param appliance appliance for which ApplianceUseCommand is used
     */
    public ApplianceUseCommand(Appliance appliance){

        super(appliance);
    }

    public void atStart(){
        appliance.setOn(true); // turn on
    }

    public void atDuring(){

        appliance.setOn(true); // keep on
    }

    public void atEnd(){
 
        appliance.setOn(false); // turn off
    }
}
