package universe;
/**Interface that is essential to the Observer Pattern Design Pattern.
 * Subscribers that wish to receive notifications must implement this interface
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public interface IObserver {

    /**Method called by the observed object on all its subscribers. 
     * In context this will be upon every iteration of the simulation 
     * loop in SmartGridUniverse.
     * 
     */
    public abstract void update();
}
