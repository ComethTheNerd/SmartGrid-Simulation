package policy.compositions;
import policy.core.PolicyComponent;
import java.util.Stack;
import time.TimedEvent;
import household.HouseHold;
import java.util.ArrayList;
import policy.*;
/**Example of a composition of policy components in to a bigger more complex
 * policy
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class PolicyComposition extends PolicyComponent{

    /**List of policy components which make up this policy composition*/
    protected ArrayList<PolicyComponent> pols = new ArrayList<PolicyComponent>();

    /**Creates a new PolicyComposition
     *
     * @param h Household which adopts this policy
     */
    public PolicyComposition(HouseHold h){
        super(h);
    }

    @Override
    public final void redStateAction(){
        for(PolicyComponent pc : pols) pc.redStateAction();
    }

    @Override
    public final void amberStateAction(){
        for(PolicyComponent pc : pols) pc.amberStateAction();
    }

    @Override
    public final void greenStateAction(){
        for(PolicyComponent pc : pols) pc.greenStateAction();
    }


    public final Stack<TimedEvent> generalRules(){

        Stack<TimedEvent> rules = new Stack<TimedEvent>();

        for(PolicyComponent pc : pols){
            rules.addAll(pc.generalRules());
        }

        return rules;

    }
}
