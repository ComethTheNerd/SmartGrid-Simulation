package policy.core;
import household.HouseHold;
import time.TimedEvent;
import java.util.Stack;

/**Implemented by programmers wanting to implement custom SmartHouse policies
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class PolicyComponent implements IPolicyComponent{
    /**A handle to the house to which this PolicyComponent is attached
     */
    protected HouseHold house;

    /**Creates a new PolicyComponent with
     * handle to the house to which this PolicyComponent is attached
     *
     * @param house Household which adopts this policy
     */
    public PolicyComponent(HouseHold house){
        this.house = house;
        
    }

    public void redStateAction(){}

    public void amberStateAction(){}

    public void greenStateAction(){}

    public abstract Stack<TimedEvent> generalRules();

    /**String representation of this Policy Component
     *
     * @return String representation of this Policy Component
     */
    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
