package input.ops;
import universe.ThreadSafeControl;
import input.Console;

/**the pause command that pauses the current simulation run
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opPause extends ConsoleOp{

        /**
         * Creates a new _opPause instance
         */
	public _opPause() {identifier = "pause";}

	public void operation(){
		ThreadSafeControl.setPause(true);
                Console.getInstance().writeToConsole("Simulation paused");
	}

        /**
         * Returns the appropriate syntax used at the command line to
         * perform a simulation pause operation
         *
         * @return the appropriate syntax used at the command line to
         * perform a simulation pause operation
         */
        public String[] getSyntax(){

            return new String[]{"pause\t# Pause Simulation"};
        }
}