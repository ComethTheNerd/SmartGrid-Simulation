package policy.core;
import java.util.Stack;

/**Part of the policy framework, used to enable loading of additional
 * PolicyComponent custom implementations in to the system
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class PolicyRegister {
    /**the new PolicyComponent implementations to load*/
    protected Stack<Class<? extends PolicyComponent>> r =
            new Stack<Class<? extends PolicyComponent>>();

    /**Registers and loads all PolicyComponents in the r Stack in to the system
     *
     * @param pb the global PolicyBank object
     */
    protected void registerApiPolicies(PolicyBank pb){
        // call the overridable pushPolicies() method that will ensure
        // all PolicyComponents to-be-loaded are placed in r
        pushPolicies();
        /// register each new PolicyComponent class in turn
        while(!r.isEmpty()) pb.registerPolicyComponent(r.pop());
    }
    /**Called by the system to allow the subclass to add all of the required
     * new PolicyComponent classes to the inherited r Stack, ready for loading
     * in to the system
    */
    protected abstract void pushPolicies();
}
