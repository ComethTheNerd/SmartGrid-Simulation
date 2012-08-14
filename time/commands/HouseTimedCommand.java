package time.commands;
import infopackage.HouseParameters;
/**A TimedCommand that executes and modifies HouseParameters
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class HouseTimedCommand implements ITimedCommand{
    /**the house parameters that are the subject of the HouseTimedCommand*/
    protected HouseParameters houseParams;

    /**Creates a new HouseTimedCommand attached
     * to house specified by HouseParameters.
     *
     * @param houseParams Indicates house for which this HouseTimedCommand is used.
     */

    public HouseTimedCommand(HouseParameters houseParams){
        this.houseParams = houseParams;
    }

    public abstract void atStart();
    public abstract void atDuring();
    public abstract void atEnd();
}
