package input.ops;
import universe.ThreadSafeControl;

/**The exit command that will terminate the simulation
 * when called
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opExit extends ConsoleOp{

        /**
         * Creates a new _opExit instance
         */
	public _opExit() {identifier = "exit";}

	public void operation(){
		ThreadSafeControl.exitSimulation();
	}

        /**
         * Returns the appropriate syntax used at the command line to
         * perform an exit operation
         *
         * @return the appropriate syntax used at the command line to
         * perform an exit operation
         */
        public String[] getSyntax(){

            return new String[]{"exit\t# Exit Simulation"};
        }
}