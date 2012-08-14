package input.ops;
import universe.ThreadSafeControl;
/**The reset command that resets the simulation ready to run again from fresh
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
@Deprecated
public class _opReset extends ConsoleOp{

        /**
         * Creates a new _opReset instance
         */
	public _opReset() {identifier = "reset";}

	public void operation(){
		ThreadSafeControl.resetSimulation();
	}

        /**
         * Returns the appropriate syntax used at the command line to
         * perform a simulation reset operation
         *
         * @return the appropriate syntax used at the command line to
         * perform a simulation reset operation
         */
        public String[] getSyntax(){

            return new String[]{"reset\t# Reset Simulation"};
        }
}
