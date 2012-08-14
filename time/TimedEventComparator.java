package time;
import java.util.Comparator;

/**This Class is used to compare two TimedEvent objects
 * It implements the Comparator Class
 *
 * @author Tokoni Kemenanabo
 */
public class TimedEventComparator implements Comparator{

    /**An Implementation of the compare method inherited
     * from the Comparator class.
     * Compares its two arguments for order. The arguments must be of the TimedEvent class.
     * Returns a negative integer, zero, or a positive integer as start time of the
     * first TimedEvent argument is less than, equal to, or greater than the start time of the second.
     * It throws a ClassCastException if the arguments are not fit to be cast as a TimedEvent.
     *
     * @param o1 First object (TimedEvent) in comparison
     * @param o2 Second object (TimedEvent) in comparison
     * @return Integer values: a negative integer, zero, or a positive integer as the
     * 	       start time of the first TimedEvent argument is less than, equal to, or greater than
     * 	       the start time of the second TimedEvent argument.
     *
     */
    public int compare(Object o1, Object o2) {

       try{
            TimedEvent evnt1 = (TimedEvent)o1;
            TimedEvent evnt2 = (TimedEvent)o2;

            return evnt1.getStartTime().compare(evnt2.getStartTime());

        }
        catch(ClassCastException e){

            System.err.println("Objects compared were not of correct type. Expected TimedEvent");
            throw e;
        }

    }

}
