package policy.compositions;
import household.HouseHold;
import policy.*;
/**Example of a composition of policy components in to a bigger more complex
 * policy
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class AverageFamily extends PolicyComposition{

    /**Creates a new AverageFamily policy composition
     *
     * @param h Household which adopts this policy
     */
    public AverageFamily(HouseHold h){
       super(h);
       pols.add(new BigDinner(house));
       pols.add(new OnlineGaming(house));
       pols.add(new Recession(house));
       pols.add(new SundayRoast(house));
       pols.add(new WinterHeating(house));
       pols.add(new IronClothes(house));
    }

   
}
