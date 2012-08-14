package input.ops;

/**An object that encapsulates an executable console command
 * that a user can call via the command line
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class ConsoleOp{
        /**The console string that identifies this particular command*/
	protected String identifier = null;
        /**Returns the unique identifier for a particular command*/
	public final String getIdentifier(){return identifier;}
        /**The functionality of a command*/
	public abstract void operation();
        /**Returns the syntax of usages of this command on the console*/
        public abstract String[] getSyntax();
}