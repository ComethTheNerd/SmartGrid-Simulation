package input.ops;
import universe.ThreadSafeControl;
import input.Console;

/**The go command that starts or unpauses the simulation
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opGo extends ConsoleOp{

        /**
         * Creates a new _opGo instance
         */
	public _opGo() {identifier ="go";}

	public void operation(){
                // unpause the simulation
		ThreadSafeControl.setPause(false);
                Console.getInstance().writeToConsole("Simulation started");
	}

        /**
         * Returns the appropriate syntax used at the command line to
         * perform a go operation
         *
         * @return the appropriate syntax used at the command line to
         * perform a go operation
         */
        public String[] getSyntax(){
            return new String[]{"go\t# Start Simulation"};
        }
}