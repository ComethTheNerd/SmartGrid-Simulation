package time.commands;

/**Allows us to attach anonymous functionality to a TimedEvent that is
 * executed at the start of, during, and at the end of an event.
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public interface ITimedCommand {
    /**Executed at the start of a TimedEvent to which this ITimedCommand 
     * is attached
     */
    void atStart();

    /**Executed during the TimedEvent to which this ITimedCommand is attached
     */
    void atDuring();

    /**Executed at the end of a TimedEvent to which this ITimedCommand is
     * attached
     */
    void atEnd();
}
