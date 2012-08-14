package input.ops;

import aggregator.Aggregator;
import universe.SmartGridUniverse;
import java.util.ArrayList;
import household.HouseHold;
import input.Console;

import appliance.core.Appliance;
import policy.core.PolicyBank;
import policy.core.PolicyComponent;

/**The list command that displays detailed information about certain aspects
 * of the system
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opList extends ConsoleOp {

    /**
     * Creates a new _opList instance
     */
    public _opList() {
        identifier = "list";
    }

    public void operation() {

        String s = "";
        Console console = Console.getInstance();
        // get the current user input sequence
        String[] input = console.getInput();

        if (input.length >= 2) {
            // we deal with the input based on its length
            switch (input.length) {

                case 2:
                    // if the first argument is pols
                    if (input[Console.ARG1].equalsIgnoreCase("pols")) {
                        // list all of the possible PolicyComponent classes to the user
                        String pols = "";
                        // iterate through all PolicyComponent classes
                        for (Class<? extends PolicyComponent> cp : PolicyBank.getInstance().getAllPolicyComponents()) {
                            String name = cp.getName();
                            pols += name.substring(name.lastIndexOf('.') + 1) + ", ";
                        }
                        // print the list to the console
                        console.writeToConsole(pols);

                    }
                    // if the first argument is pols
                    else if (input[Console.ARG1].equalsIgnoreCase("apps")) {
                        // list all of the possible Appliance classes to the user
                        String apps = "";
                         // iterate through all Appliance classes
                        for (Class<? extends Appliance> ca : SmartGridUniverse.getInstance().getAllApplianceTypes()) {
                            String name = ca.getName();
                            apps += name.substring(name.lastIndexOf('.') + 1) + ", ";
                        }
                        // print the list to the console
                        console.writeToConsole(apps);
                    } else {
                        // attempt to parse the aggregator - exceptions caught by Console error handler
                        Aggregator a = SmartGridUniverse.getInstance().getAggregators().get(Integer.parseInt(input[Console.ARG1]));
                        console.writeToConsole(a + " (list index: " + input[Console.ARG1] + "). House ID's:");

                        for (HouseHold h : a.getAttachedHouses()) {
                            // collect all of the attached houses' Id's
                            s += h.getId() + ", ";

                        }
                        // display the aggregator information
                        console.writeToConsole(s);
                        s = "";
                    }
                    break;


                case 3:
                    // if we have three parts to the input sequence, get the second argument
                    String arg2 = input[Console.ARG2];
                    // attempt to parse an int - exceptions caught by Console error handler
                    int id = Integer.parseInt(arg2);
                    // again, parsing for the aggregator - exceptions caught by Console error handler
                    Aggregator a = SmartGridUniverse.getInstance().getAggregators().get(Integer.parseInt(input[Console.ARG1]));
                    // go through all the attached houses
                    for (HouseHold h : a.getAttachedHouses()) {
                        //if we find the house that matches the parsed Id
                        if (h.getId() == id) {
                            // split the toString() output at every newline
                            // NOTE: we do this so that each new line is displayed on a new
                            // line of the console output
                            String[] lines = h.toString().split("\n");
                            // output each of the lines
                            for (String str : lines) {
                                console.writeToConsole(str);
                            }
                            s = "";

                            break;
                        }

                    }
                    break;
            }
        } else {
            // get all the aggregators in the universe
            ArrayList<Aggregator> aggs = SmartGridUniverse.getInstance().getAggregators();
            // go through each one
            for (int index = 0; index < aggs.size(); index++) {

                Aggregator a =  aggs.get(index);
                // collect information about each aggregator in turn
                console.writeToConsole(a + ". House ID's:");

                for (HouseHold h : a.getAttachedHouses()) {
                    s += h.getId() + ",";

                }
                // print out the aggregator information
                console.writeToConsole(s);
            }
        }

    }

    /**
     * Returns the appropriate syntax used at the command line to
     * perform a list operation
     *
     * @return the appropriate syntax used at the command line to
     * perform a list command
     */
    public String[] getSyntax() {

        return new String[]{"list\t# Show all Aggregators and their attached Houses' Id's",
                    "list <aggregator_Index>\t# Show Aggregator information for the Aggregator with given Aggregator Index",
                    "list <aggregator_Index> <house_Id>\t# Show HouseHold information for the House with the given House Id attached to the Aggregator with the given Aggregator Index",
                    "list (apps|pols)\t# View a complete list of all Appliances or Policy Components"};
    }
}
