package input.ops;

import aggregator.Aggregator;
import universe.ThreadSafeControl;
import universe.SmartGridUniverse;
import household.HouseHold;
import input.Console;
import java.util.ArrayList;
import appliance.core.Appliance;

/**The remove command that is used to remove an object from the system,
 * such as a house, or a policy component attached to a house
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opRemove extends _opAddRemove {

    /**
     * Creates a new _opRemove instance
     */
    public _opRemove() {
        identifier = "remove";
    }

    /**Lightweight method used to remove an aggregator
     */
    protected void _aggregator() {
        // attempt to parse the correct aggregator - exceptions caught by Console error handler
        Aggregator a = SmartGridUniverse.getInstance().getAggregators().get(Integer.parseInt(input[Console.ARG2]));
        ThreadSafeControl.removeAggregator(a);
        console.writeToConsole("Removed Aggregator #" + input[Console.ARG2]);
    }

    /**Lightweight method used to remove a house
     */
    protected void _house() {
        // parse the Id - exceptions caught by Console error handler
        int id = Integer.parseInt(input[Console.ARG2]);

        // label used for breaking out from the inner for loop
        outer:
        // go through all aggregators in the universe
        for (Aggregator a : SmartGridUniverse.getInstance().getAggregators()) {
            // find the hosue that matches the given Id, from all attached hosues
            for (HouseHold h : a.getAttachedHouses()) {

                if (h.getId() == id) {
                    // if we find it then request the house be removed
                    ThreadSafeControl.removeHouse(a, h);
                    console.writeToConsole("Removed House #" + id);
                    break outer;

                }

            }

        }

    }

    /**Lightweight method used to remove a policy component from the house
    */
    protected void _policyComponent() {
        ThreadSafeControl.removePolicyComponent(house, house.getPolicyComponentsByType(className));
        console.writeToConsole("Removed " + className);
    }

    /**Lightweight method used to remove an appliance from the house
    */
    protected void _appliance() {
        // we allow the user to remove all Appliances at once
        if (input[Console.ARG3].equalsIgnoreCase("all")) {
            ThreadSafeControl.removeAppliance(house, house.getAllAppliances());
            console.writeToConsole("Removed all " + className + "s");
        }

        // otherwise we get the specific attached appliances that match the given class
        ArrayList<Appliance> apps = house.getAppliancesByClass(className);
        boolean all = false;

        if (!apps.isEmpty()) {
            if (input.length == 5) {
                // do we want to remove all instances of the given Appliance class
                if (input[Console.ARG4].equalsIgnoreCase("all")) {
                    all = true;
                }
            }
            if (!all) {
                // if we do not want to remove all instance then we just remove the first
                Appliance a = apps.get(0);
                apps.clear();
                apps.add(a);
            }
            ThreadSafeControl.removeAppliance(house, apps);
            console.writeToConsole("Removed " + className);
        }

    }

    /**
     * Returns the appropriate syntax used at the command line to
     * perform a remove operation
     *
     * @return the appropriate syntax used at the command line to
     * perform a remove operation
     */
    public String[] getSyntax() {

        return new String[]{"remove agg\t# Remove Aggregator from simulation",
                    "remove house <house_Id>\t# Remove House given House Id",
                    "remove app <house_Id> <appliance_class_name>\t# Remove Appliance specified by given Appliance Class Name from House with given House Id",
                    "remove app <house_Id> all\t# Removes all Appliances currently attached to the House with given House Id",
                    "remove pol <house_Id> <policy_component_class_name>\t# Remove Policy Component specified by given Policy Component Class Name from House with given House Id",
                    "remove pol <house_Id> all\t# Removes all Policy Components currently attached to the House with given House Id"};

    }
}
