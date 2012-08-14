package input.ops;

import java.util.ArrayList;
import aggregator.Aggregator;
import universe.SmartGridUniverse;
import input.Console;
import utils.MessageStream;

/**the uni command that displays an overview of the current state of the universe
 * at the current point in the simulation
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opUni extends ConsoleOp {

    /**
     * Creates a new _opUni instance
     */
    public _opUni() {
        identifier = "uni";
    }

    public void operation() {
        // get the universe handle
        SmartGridUniverse universe = SmartGridUniverse.getInstance();

        // get all aggregators in the universe
        ArrayList<Aggregator> aggs = universe.getAggregators();

        int aggCount = aggs.size();

        // display information about the universe to the user
        Console console = Console.getInstance();
        // the current system time
        console.writeToConsole("Time : " + universe.getUniverseTime().toString());
        // if the simulation is paused or running
        console.writeToConsole("State : " + (universe.isPaused()? "paused" : "running"));
        // the current system message
        console.writeToConsole("Message : " + MessageStream.getInstance().getCurrentMessage());
        // the number of aggregators
        console.writeToConsole("Aggregator count : " + aggCount);

        // used to store cumulative network load, and total number of hosues
        int load = 0, houses = 0;

        for (int index = 0; index < aggCount; index++) {
            // retrieve and display individual aggregator information
            Aggregator a  = aggs.get(index);
            int noOfHouses = a.getAttachedHouses().size();
            console.writeToConsole(a + " (list index: " + index + ") has "
                    + noOfHouses + " houses, using " + a.getCumulativeDemand() + "kwh");

            // add to the cumulative totals
            load += a.getCumulativeDemand();
            houses += noOfHouses;
        }

        // show total network load stats
        console.writeToConsole("Network load: " + load + "kwh, " + aggCount + " A's, " + houses + " H's");
    }

    /**
     * Returns the appropriate syntax used at the command line to
     * perform a universe information command
     *
     * @return the appropriate syntax used at the command line to
     * perform a universe information command
     */
    public String[] getSyntax(){

            return new String[]{"uni\t# Display information about the current Universe state"};
        }
}
