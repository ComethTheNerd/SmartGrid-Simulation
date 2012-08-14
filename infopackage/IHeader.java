package infopackage;

/**Interface that allows us to override the enum headers for each concrete
 * subclass of InfoPackage
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public interface IHeader {
    /**Returns the String name representation of the enum
     *
     * @return The name of the enum as a String
     */
    public String getName();
}
