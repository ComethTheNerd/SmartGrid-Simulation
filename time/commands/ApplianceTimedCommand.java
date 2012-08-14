package time.commands;
import appliance.core.Appliance;
/**A TimedCommand that acts upon an Appliance
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class ApplianceTimedCommand implements ITimedCommand{
    /**The Appliance that is the subject of this ApplianceTimedCommand*/
    protected Appliance appliance;

    /**Creates new ApplianceTimedCommand which is attached to supplied appliance
     *
     * @param appliance appliance for which ApplianceTimedCommand is used
     */
    public ApplianceTimedCommand(Appliance appliance){
        this.appliance = appliance;
    }

    public abstract void atStart();
    public abstract void atDuring();
    public abstract void atEnd();
}

