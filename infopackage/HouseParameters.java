package infopackage;

/**Represents information about the attitude of a house, such as the
 * power state thresholds
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class HouseParameters extends InfoPackage{

    public enum Header implements IHeader{
        /**the number of occupants in this house*/
        NumberOfOccupants(0),
        /**the minimum consumption of this house*/
        MinUsageLimit(1),
        /**the maximum consumption of this hosue*/
        MaxUsageLimit(2),
        /**the threshold for the green state, before it turns to amber*/
        GreenStateLimit(3),
        /**the threshold for the amber state, before it turns to red*/
        AmberStateLimit(4);

        private int uniqueIndex;

        Header(int n){
            uniqueIndex = n;
        }
        /**Gets the unique index that can be used in the array of all Headers
         * to access this particular header
         *
         * @return the index to use to refer to this particular header in an array of all Headers
         */
        public int getIndex(){
            return uniqueIndex;
        }

        /**Gets the name of this enum constant as a string
         *
         * @return the enum name as a String
         */
        public String getName(){
            return this.name();
        }
    }

  /**Returns an array of valid context-specific headers for this class
     *
     * @return An array of enum headers, specific to the class
     */
    @Override
    public IHeader[] getHeaders(){
        return Header.values();
    }

}
