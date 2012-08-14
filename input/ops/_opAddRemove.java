package input.ops;
import household.HouseHold;
import aggregator.Aggregator;
import universe.SmartGridUniverse;
import input.Console;

/**Adding and removing operations can be performed on the same set of objects and
 * so we generalise this functionality in this class to group the two commands
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class _opAddRemove extends ConsoleOp {
    /** the class name that will be used with reflection to dynamically instantiate an object*/
    protected Class className;
    /**the house that the operation is being performed on*/
    protected HouseHold house;

    
    /**flag denoting whether this is an add operation*/
    protected boolean isAdd;
    /**flag denoting whether this operation is to do with an appliance*/
    protected boolean isAppliance;
    /**a handle to the console*/
    protected Console console;
    /**the current user input will be stored in this array*/
    protected String[] input;

    public final void operation() {
        console = Console.getInstance();
        // get the current user input
        input = console.getInput();

        // parse the user input to know where to pipe the input to next
        // to be processed and dealt with
        if (input[Console.ARG1].equalsIgnoreCase("agg")) {
            _aggregator();
        } else if (input[Console.ARG1].equalsIgnoreCase("house")) {
            _house();
        } // ADD APPLIANCE OR POLICY OPERATION
        else {
            _policyComponentOrAppliance();
        }

    }

    /**Lightweight method used to deduce whether this the user wants
     * to add/remove an appliance or policy component
    */
    protected final void _policyComponentOrAppliance() {
        // whether the user wants to add an appliance
        isAppliance = false;

        // see if the first argument is for an appliance
        if (input[Console.ARG1].equalsIgnoreCase("app")) {
            isAppliance = true;
        }
        // or is the first argument is for a policy component
        else if(!input[Console.ARG1].equalsIgnoreCase("pol")){
            console.showError("Check usage for Add/Remove. Incorrect ARG1");
            return;
        }

        int id = Integer.parseInt(input[Console.ARG2]);

        // make sure we clear any previous reference to the house
        house = null;

        // this label is used to break out of the inner for loop
        outer:
        for (Aggregator a : SmartGridUniverse.getInstance().getAggregators()) {
            // go through each house, attached to each aggregator
            for (HouseHold h : a.getAttachedHouses()) {
                // find the required house
                if (h.getId() == id) {
                    // store a reference to it
                    house = h;
                    // once we find the intended house, break out of the outer loop
                    break outer;
                }
            }
        }
        // if we havent found the correct house show an error
        if (house == null) {
            console.showError("House #" + id + " not found");
        } else {
            try {
                // if we want to perform an operation on an appliance
                if (isAppliance) {
                    // try to parse what appliance it is
                    if(!input[Console.ARG3].equalsIgnoreCase("all"))className = Class.forName("appliance." + input[Console.ARG3]);
                    _appliance();
                    // if we want to perform an operation on a policy component
                } else {
                    // try to parse what policy component it is
                    if(!input[Console.ARG3].equalsIgnoreCase("all"))className = Class.forName("policy." + input[Console.ARG3]);
                    _policyComponent();

                }
            } catch (Exception e) {
            }

        }

    }

    /**Lightweight method used to add/remove an aggregator
    */
    protected abstract void _aggregator();
    /**Lightweight method used to add/remove a house
    */
    protected abstract void _house();

    /**Lightweight method used to add/remove a policy component
    */
    protected abstract void _policyComponent();

    /**Lightweight method used to add/remove an appliance
    */
    protected abstract void _appliance();
}
