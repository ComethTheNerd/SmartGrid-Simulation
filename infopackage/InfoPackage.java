package infopackage;
import java.util.HashMap;

/**An abstract class that represents a collection of headers that each map
 * to a string value. Every subclass should OVERRIDE getHeaders() to
 * return the specific enum headers relevant to its purpose. See below...
 *
 * NOTE: Contains final methods that ensure subclasses do not overwrite and
 * potentially break functionality.
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class InfoPackage {
    private HashMap<IHeader, Integer> valueMap;
    private static final int DEFAULT_VALUE = -1111111, INVALID_HEADER = -222222;

    /**Concrete InfoPackage specific enumerated Headers.
     * Each subclass of InfoPackage must provide their own
     * 'Header' enum AND OVERRIDE getHeaders() method below...
	 *
	 * NOTE: Concrete subclasses should use INTENTIONAL field
	 * hiding by making sure they name their enum 'Header' too
     */
    public enum Header implements IHeader{
        Dummy_Header1(0),
        Dummy_Header2(1),
        Dummy_Header3(2); /*etc*/

        private int uniqueIndex;

        Header(int n){
            uniqueIndex = n;
        }
        public int getIndex(){
            return uniqueIndex;
        }

        /**Gets the String name of the enum
         *
         * @return The String name for this enum
         */
        public String getName(){
            return this.name();
        }
    }
   
    

    /**CONSTRUCTOR. Carries out logic to initialise this
     * InfoPackage. Concrete Subclasses SHOULD NOT override this!
     *
     */
    public InfoPackage(){
        valueMap = new HashMap<IHeader,Integer>();

        for(IHeader header : getHeaders())
                this.setValue(header, DEFAULT_VALUE);


    }

    /**SHOULD BE OVERRIDDEN BY SUBCLASSES TO RETURN
     * THE CONCRETE SUBCLASS' SPECIFIC HEADER ENUM
     *
     * @return An array of enum headers, specific to the concrete class
     */
    public IHeader[] getHeaders(){
        return Header.values();
    }

   

    /**Checks whether the given IHeader is a valid header in this
     * particular classes set of enum headers
     *
     * @param h The IHeader to check for in the valid headers
     * @return True if the given IHeader is a valid header for this class
     */
    public final boolean isValidHeader(IHeader h) {
        String name = h.getName();
        for (IHeader header : getHeaders()) {
            if (header.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }


    /**Retrieves the current value mapped to the given IHeader
     *
     * @param header The header enum as the HashMap index
     * @return The current value mapped to this IHeader, or INVALID_HEADER if invalid
     */
    public final int getValue(IHeader header) {

        if (isValidHeader(header)) {

            return valueMap.get(header);
        }
        else return INVALID_HEADER; // Invalid...
    }

    /**Sets the given String value to the given IHeader, if it is valid.
     *
     * @param header The IHeader index to assign the value to
     * @param value The new String value to map to the given IHeader
     * @return True if the value has been successfully stored
     */
    public final boolean setValue(IHeader header, int value) {
        if(isValidHeader(header)){
            valueMap.put(header, value);
            return true; // successful
        }
        return false;
    }

    /**Override of toString() in order to print a meaningful representation
     * of this InfoPackage
     *
     * @return A meaningful String representation of this InfoPackage
     */
    @Override
    public final String toString(){
        String details = this.getClass().getName() + " content :\n";
        // append details for each header in the infopackage
        for(IHeader s : getHeaders()){
            details += s.getName() + " : " + this.getValue(s) + "\n";
        }

        return details;
    }

}
