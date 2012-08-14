package time.commands;
import appliance.core.Appliance;
/**Models a Balance Load command, that is, ensuring an appliance
 * remains off for the duration of the TimedEvent to which it is attached
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class BalanceLoadCommand extends ApplianceTimedCommand{
    /**whether the appliance was on initially before this command*/
    boolean originalState = false;

    /**Creates new BalanceLoadCommand to be used on attached appliance.
     *
     * @param appliance appliance for which BalanceLoadCommand is used.
     */
    public BalanceLoadCommand (Appliance appliance){

        super(appliance);
    }

    public void atStart(){

        originalState = appliance.isApplianceOn();
        // Turn on appliance at start of command
        appliance.setOn(false);
    }


    public void atDuring(){

        appliance.setOn(false); // ensure it is off
    }

    public void atEnd(){
        // System.out.println("Balance Load! Turning " + appliance.getClass() + " on!");
        // Turn on appliance at start of command
        appliance.setOn(originalState);
    }
}
