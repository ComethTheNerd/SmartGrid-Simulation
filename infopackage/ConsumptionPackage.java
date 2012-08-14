package infopackage;

/**Consumption data is passed around the system inside objects of this class
 * using context-specific key-value pairs to describe usage
 *
 * @author Darius Hodaei
 */
public class ConsumptionPackage extends InfoPackage{
    /**Context specific headers for a Consumption Package
     *
     */
    public enum Header implements IHeader{
        /**total energy consumed by White Goods appliances*/
        WHITE_GOODS_USAGE(0),
        /**total energy consumed by Luxury appliances*/
        LUXURY_ITEMS_USAGE(1),
        /**total energy consumed by Utility appliances*/
        UTILITIES_USAGE(2),
        /**total energy consumed by Burst appliances*/
        BURST_USAGE(3),
        /**total energy consumed by all appliances for all time*/
        TOTAL_USAGE(4),
        /**total energy consumed by all appliances at the last calculation*/
        CURRENT_USAGE(5),
        /**average energy consumer by all appliances each time*/
        AVERAGE_USAGE(6),
        /**the current power state the house is in*/
        CURRENT_STATE(7);

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
