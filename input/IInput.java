package input;

/**An interface used by those objects serving the purpose of an input/output
 * channel to the system, such as the GUI or console
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public interface IInput{
    /**A clean-up method called when this output channel is switched off
     */
    public void close();
    /**An initialization method called when this output channel is switched on
     */
    public void open();
    /**A system call to the output channel to perform the necessary tasks
     * to update its current state
     */
    public void update();
}
