package policy.core;
import java.util.Stack;
import time.TimedEvent;

/**The interface required by the project specification that allows for custom
 *policy implementations to be plugged in to the simulation
 * 
 * @author Darius Hodaei
 */
public interface IPolicyComponent {
    /**Called by the registered house whenever it is in a
     * red state of power consumption (via RedPowerState).
     */
    public void redStateAction();

    /**Called by the registered house whenever it is in a
     * amber state of power consumption (via AmberPowerState).
     */
    public void amberStateAction();

    /**Called by the registered house whenever it is in a
     * green state of power consumption (via GreenPowerState).
     */
    public void greenStateAction();

    /**Rules that are applied to this house as a part of this policy.
     * These rules can be widely varied.
     *
     * @return The TimedEvents that represent state-independent policy rules
     */
    public Stack<TimedEvent> generalRules();
}
