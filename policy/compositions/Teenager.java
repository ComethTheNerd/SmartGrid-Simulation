package policy.compositions;
import java.util.Stack;
import time.TimedEvent;
import household.HouseHold;
import java.util.ArrayList;
import policy.*;
/**
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class Teenager extends PolicyComposition{

     /**Creates a new Teenager policy composition
     *
     * @param h Household which adopts this policy
     */
    public Teenager(HouseHold h){
       super(h);
       pols.add(new OnlineGaming(house));
       pols.add(new RandomDay(house));
       pols.add(new ChilledWeekend(house));
       pols.add(new WatchMovie(house));
    }


}
