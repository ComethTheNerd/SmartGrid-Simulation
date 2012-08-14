package policy.compositions;
import household.HouseHold;
import policy.*;
/**
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class HealthEnthusiast extends PolicyComposition{

    /**Creates a new HealthEnthusiast policy composition
     *
     * @param h Household which adopts this policy
     */
    public HealthEnthusiast(HouseHold h){
        super(h);
        pols.add(new EarlySwimmer(house));
        pols.add(new LateSwimmer(house));
        pols.add(new HealthyMorning(house));
        pols.add(new SummerAirPolicy(house));
        pols.add(new Environmentalist(house));
    }

}
