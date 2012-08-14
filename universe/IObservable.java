package universe;

/**This allows an object to be observed, but must be extended by a concrete
 * implementation. Note the use of final methods so concrete subclasses need
 * not provide any implementation.
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public interface IObservable {

    /**Adds the given IObserver to the list of objects interested in receiving 
     * updates from this object. 
     * 
     * @param observer The observer to add to the subscribers list
     */
    void addObserver(IObserver observer);

    /**Removes a given IObserver from its list of subscribers, if it exists
     * within the collection.
     *
     * @param observer The observer to remove from the subscribers list
     */
    void removeObserver(IObserver observer);

    /**Sends out the same notification information to each observer
     * currently in the list
     */
    void notifyObservers();

}
