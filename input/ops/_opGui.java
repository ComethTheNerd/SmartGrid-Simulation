package input.ops;
import universe.UserInput;
import input.Console;
/**The gui command that launches the GUI view and closes the console
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opGui extends ConsoleOp{

        /**
         * Creates a new _opGui instance
         */
	public _opGui() {identifier = "gui";}

	public void operation(){
		UserInput.getInstance().changeOutput(UserInput.OUTPUT_MODE.Gui);
                Console.getInstance().writeToConsole("GUI launched");
	}

        /**
         * Returns the appropriate syntax used at the command line to
         * perform a GUI switch command
         *
         * @return the appropriate syntax used at the command line to
         * perform a GUI switch command
         */
         public String[] getSyntax(){

            return new String[]{"gui\t# Switch to GUI output mode"};
        }
}