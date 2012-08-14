package input.ops;
import universe.ThreadSafeControl;
import household.HouseHold;
import aggregator.Aggregator;
import universe.SmartGridUniverse;
import input.Console;

/**Encapsulates the general functionality of adding something
 * to the simulation, such as a house, or policy component
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opAdd extends _opAddRemove{

        /**
         * Creates a new _opAdd instance
         */
	public _opAdd() {identifier = "add";}

        /**Lightweight method used to add an aggregator
         */
	protected void _aggregator(){
		ThreadSafeControl.addAggregator();
                 console.writeToConsole("Added Aggregator");
	}

        /**Lightweight method used to add a house
         */
	protected void _house(){
            // attempt to parse the aggregator referred to in the user input
            // NOTE: if this fails, it will be caught by the Console error handler
            Aggregator a = SmartGridUniverse.getInstance().getAggregators().get(Integer.parseInt(input[Console.ARG2]));
            ThreadSafeControl.addHouse(a, new HouseHold(a));
            console.writeToConsole("Added House to Aggregator #" + input[Console.ARG2]);
	}

        /**Lightweight method used to add a policy component to the specified house
         */
	protected void _policyComponent(){
            ThreadSafeControl.addPolicyComponent(house, className);
            console.writeToConsole("Added " + className);

        }

        /**Lightweight method used to add an appliance to the specified house
         */
	protected void _appliance(){
            ThreadSafeControl.addAppliance(house, className);
            console.writeToConsole("Added " + className);
        }

        /**
         * Returns the appropriate syntax used at the command line to
         * perform an add operation
         *
         * @return the appropriate syntax used at the command line to
         * perform an add operation
         */
        public String[] getSyntax(){

           return new String[]{"add agg\t# Add Aggregator to simulation",
                   "add house <agg_Id>\t# Add House to Aggregator with given Aggregator Id",
                   "add app <house_Id> <appliance_class_name>\t# Add Appliance specified by given Appliance Class Name to House with given House Id",
                    "add pol <house_Id> <policy_component_class_name>\t# Add Policy Component specified by given Policy Component Class Name to House with given House Id"};
        }
}