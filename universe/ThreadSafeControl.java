package universe;

import java.util.ArrayList;
import household.HouseHold;
import aggregator.Aggregator;
import appliance.core.Appliance;
import policy.core.PolicyComponent;
import utils.MessageStream;

/**A controller that queues up requests generated from user input, until the system is ready
 * to asynchronously process them. This avoid exceptions that result from modifying collections
 * whilst another thread is iterating over them.
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class ThreadSafeControl {

    /**A handle to the SmartGridUniverse singleton instance
     */
    private static SmartGridUniverse universe = SmartGridUniverse.getInstance();
    /**A collection of aggregators that have requests to add/remove houses*/
    private static ArrayList<_addHouseToAgg> map = new ArrayList<_addHouseToAgg>();
    /**A collection of houses that have requests to add/remove appliances/policy components*/
    private static ArrayList<_modHouse> mods = new ArrayList<_modHouse>();
    /**flag to reset the simulation*/
    private static boolean reset = false;
    /**flag to exit the simulation*/
    private static boolean exit = false;
    /**flag to pause the simulaton*/
    private static boolean pause = true;

    /**Lightweight class that stores a house waiting to be added to an aggregator*/
    private static class _addHouseToAgg {

        private boolean /**whether we want to remove these houses*/
                remove,
                /**if we have added this houses yet*/
                yetToAdd;
        /**the Aggregator to add the houses to*/
        private Aggregator a;
        private ArrayList<HouseHold> /**the houses to add to the aggregator*/
                _toAdd,
                /**the houses to remove from the aggregator*/
                _toRemove;

        private _addHouseToAgg(Aggregator agg, boolean yetToAdd) {
            a = agg;
            remove = false;
            this.yetToAdd = yetToAdd;
            // remove old information that may be hanging around
            clean();

        }

        /**Ensures all the collections have been cleaned and do not contain
         * any old references that have been processed
         */
        private void clean() {
            _toAdd = new ArrayList<HouseHold>();
            _toRemove = new ArrayList<HouseHold>();
        }
    }

    /**Set the pause state of the simulation
     *
     * @param p true to pause the simulation, false to unpause it
     */
    public static void setPause(boolean p) {
        pause = p;
    }

    /**Resets the simulation
     */
    @Deprecated
    public static void resetSimulation() {
        reset = true;
    }

    /**Exits the simulation
     *
     */
    public static void exitSimulation() {
        exit = true;
    }

    /**A lightweight class to represent modifications to be made to a house*/
    private static class _modHouse {

        /**the house to modify*/
        private HouseHold house;
        /**the appliances to remove from the house*/
        private ArrayList<Appliance> _removeA;
        /**the appliance classes to instantiate in the house*/
        private ArrayList<Class<? extends Appliance>> _addA;
        /**the policy components to remove from the house*/
        private ArrayList<PolicyComponent> _removeP;
        /**the policy component classes to instantiate in the house*/
        private ArrayList<Class<? extends PolicyComponent>> _addP;

        public _modHouse(HouseHold h) {
            this.house = h;
            clean();
        }

        /**Ensures all the collections have been cleaned and do not contain
         * any old references that have been processed
         */
        private void clean() {
            _removeA = new ArrayList<Appliance>();
            _addA = new ArrayList<Class<? extends Appliance>>();
            _removeP = new ArrayList<PolicyComponent>();
            _addP = new ArrayList<Class<? extends PolicyComponent>>();
        }
    }

    /**Creates a request to add an instance of the given PolicyComponent class,
     * to the given house
     *
     * @param h the house to add the PolicyComponent instance to
     * @param pc the PolicyComponent class to instantiate
     */
    public static void addPolicyComponent(HouseHold h, Class<? extends PolicyComponent> pc) {

        // first we see if we already have a record for this hosue
        for (_modHouse mh : mods) {
            // if so, add this policy component request
            if (mh.house == h) {
                mh._addP.add(pc);
                return;
            }
        }
        // if we get here it means the house hasn't got a record so we create one
        _modHouse newMH = new _modHouse(h);
        // and add the policy component class request
        newMH._addP.add(pc);
        mods.add(newMH);
    }

    /**Creates a request to remove instances of the given PolicyComponent classes,
     * from the given house
     *
     * @param h the house to remove the PolicyComponent instance from
     * @param pc the PolicyComponent classes to remove
     */
    public static void removePolicyComponent(HouseHold h, ArrayList<PolicyComponent> pc) {
        // first we see if we already have a record for this house
        for (_modHouse mh : mods) {
            // if we do then we store the remove request
            if (mh.house == h) {
                mh._removeP.addAll(pc);
                return;
            }
        }
        // if we get here it means we haven't got a record for the house so we create one
        _modHouse newMH = new _modHouse(h);
        // store the remove request
        newMH._removeP.addAll(pc);
        mods.add(newMH);
    }

    /**Creates a request to add an instance of the given Appliance class,
     * to the given house
     *
     * @param h the house to add the Appliance instance to
     * @param a the Appliance class to instantiate
     */
    public static void addAppliance(HouseHold h, Class<? extends Appliance> a) {
        // first we see if we already have a record for this house
        for (_modHouse mh : mods) {

            if (mh.house == h) {
                // if we do then add the request
                mh._addA.add(a);
                return;
            }
        }
        // if we get here then we havent got a record for the house so we create one
        _modHouse newMH = new _modHouse(h);
        // and add the request to it
        newMH._addA.add(a);
        mods.add(newMH);
    }

    /**Creates a request to remove instances of the given Appliance classes,
     * from the given house
     *
     * @param h the house to remove the Appliance instance from
     * @param a the Appliance classes to remove
     */
    public static void removeAppliance(HouseHold h, ArrayList<Appliance> a) {
        // first we see if we already have a record for this house
        for (_modHouse mh : mods) {
            // if we do then we add the requests
            if (mh.house == h) {
                mh._removeA.addAll(a);
                return;
            }
        }
        // if we get here then we havent got a record for this house so we create one
        _modHouse newMH = new _modHouse(h);
        // and add the request
        newMH._removeA.addAll(a);
        mods.add(newMH);
    }

    /**Creates a request to add the given HouseHold to the given Aggregator
     *
     * @param a the aggregator to add the house to
     * @param h the house to add
     */
    public static void addHouse(Aggregator a, HouseHold h) {
        // find the aggregator
        for (_addHouseToAgg agg : map) {
            // add the house when we do
            if (agg.a == a) {
                agg._toAdd.add(h);
                return;
            }
        }

        // If it gets here, it hasn't found the aggregator so we need to add it
        _addHouseToAgg newAgg = new _addHouseToAgg(a, false);
        a.addHouse(h);
        map.add(newAgg);
    }

    /**Creates a request to remove the given HouseHold from the given Aggregator
     *
     * @param a the aggregator to remove the house from
     * @param h the house to remove
     */
    public static void removeHouse(Aggregator a, HouseHold h) {
        for (_addHouseToAgg agg : map) {

            if (agg.a == a) {
                agg._toRemove.add(h);
                return;
            }
        }
    }

    /**Creates a request to add an Aggregator to the simulation universe
     *
     */
    public static void addAggregator() {

        map.add(new _addHouseToAgg(new Aggregator(), true));
    }

    /**Creates a request to remove an Aggregator from the simulation universe
     *
     * @param a the Aggregator to remove from the simulation
     */
    public static void removeAggregator(Aggregator a) {
        for (_addHouseToAgg agg : map) {

            if (agg.a == a) {
                agg.remove = true;
                return;
            }
        }
    }

    /**Executes all the queued requests asynchronously, called by the Smart Grid Universe
     * when it is safe to process them
     *
     */
    protected static void execute() {
        try {
            // if the user has requested exit simulaton
            if (exit) {
                // exit the simulation and return, ignoring other requests
                universe.setExit(true);
                return;
            }
            // if the user has requested reset simulation
            if (reset) {
                // clear the map of aggregators, then reset and return ignoring other requests
                map = new ArrayList<_addHouseToAgg>();
                reset = false;
                universe.reset();
                return;
            }

            // for all the houses to perform modifications to
            for (_modHouse mh : mods) {
                // perform all the modifications....
                for (Appliance a : mh._removeA) {
                    mh.house.removeAppliance(a);
                }
                for (Class<? extends Appliance> a : mh._addA) {
                    mh.house.addAppliance(a);
                }
                for (PolicyComponent pc : mh._removeP) {
                    mh.house.removePolicyComponent(pc);
                }
                for (Class<? extends PolicyComponent> pc : mh._addP) {
                    mh.house.addPolicyComponent(pc);
                }
                // delete the dealt with items
                mh.clean();
            }

            // get all the aggregators in the universe
            ArrayList<Aggregator> uniAggs = universe.getAggregators();

            // perform the queued modifications to the aggregators
            if (map.size() != uniAggs.size()) {

                outer:
                for (Aggregator agg : uniAggs) {
                    for (_addHouseToAgg ah : map) {

                        if (ah.a == agg) {
                            continue outer;
                        }

                    }
                    map.add(new _addHouseToAgg(agg, false));
                }
            }

            // sets the pause state of the simulation
            universe.setPause(pause);

            // add/remove the houses to/from aggregators where necessary
            for (int x = 0; x < map.size(); x++) {
                _addHouseToAgg ahta = map.get(x);
                // if we want to remove the house, remove it
                if (ahta.remove) {
                    universe.removeAggregator(ahta.a);
                    map.remove(x);
                    continue;
                }
                // if we are yet to add the house, add it
                if (ahta.yetToAdd) {
                    universe.addAggregator(ahta.a);
                    ahta.yetToAdd = false;

                }
                // perform the modifications to the houses attached to the aggregators
                for (Aggregator ax : universe.getAggregators()) {
                    if (ax == ahta.a) {
                        for (HouseHold h : ahta._toAdd) {
                            ax.addHouse(h);
                            // log the event to the user
                            MessageStream.getInstance().submitMessage("User added new House #" + h.getId()
                                    + " to " + ahta.a, MessageStream.PRIORITY.HIGH);

                        }
                        for (HouseHold h : ahta._toRemove) {
                            ax.removeHouse(h);
                            // log the event to the user
                            MessageStream.getInstance().submitMessage("User removed House #" + h.getId()
                                    + " from " + ahta.a, MessageStream.PRIORITY.HIGH);
                        }
                        ahta.clean();
                    }

                }

            }
        } catch (Exception e) {
            System.err.println("ThreadSafeControl says:\n" + e.getMessage());
        }
    }
}
