package API;
import household.HouseHold;
import policy.*;
import policy.compositions.PolicyComposition;

/**A sample PolicyComposition skeleton for a programmer to use as a reference
 * when programming with the API framework
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class TemplatePolicyComposition extends PolicyComposition{
    /* NOTE: WHEN WRITING CUSTOM POLICY COMPONENTS YOU MUST REGISTER
       THEM WITH THE SYSTEM.

       THIS SHOULD BE DONE IN RegisterPolicies.java - WE'LL TAKE IT FROM THERE!
      
       CHECK OUT package:policy.compositions FOR SOME IDEAS TO GET STARTED!
    */

    public TemplatePolicyComposition(HouseHold h){
       super(h);
       /*
        TODO: ADD A NEW INSTANCE OF ANY POLICY COMPONENT
        CLASSES TO THE INHERITED pols ARRAYLIST. e.g.:

            pols.add(new SundayRoast(house));
            pols.add(new WinterHeating(house));
                                                etc.
        */

    }


}


