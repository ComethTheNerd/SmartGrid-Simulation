package input.ops;
import input.Console;

/**the manual command that displays all commands and their usages to the
 * user to assist them when typing in command sequences
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class _opManual extends ConsoleOp{

        /**
         * Creates a new _opManual instance
         */
	public _opManual() {identifier = "man";}

	public void operation(){
            // wraps a call to the Console showManual() method
            Console.getInstance().showManual();
	}

        /**
         * Returns the appropriate syntax used at the command line to
         * perform a show manual operation
         *
         * @return the appropriate syntax used at the command line to
         * perform a show manual operation
         */
        public String[] getSyntax(){
            return new String[]{"man\t# Display command line manual"};
        }
}
