package security;
import infopackage.ConsumptionPackage;

/**Consumption data can sent by the Trusted Third Party to the Aggregator
 * anonymously in order to protect the actual HouseHold's true identity,
 * which the Aggregator should not know
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class AnonymousConsumer {
    /**the anonymous alias that can be mapped back to a house by the trusted third party*/
    private int alias;
    /**the consumption data*/
    private ConsumptionPackage consumptionPackage;

    /**Creates a new AnonymousConsumer with the
     * supplied alias and consumptionPackage
     *
     * @param alias
     * @param consumptionPackage
     */
    public AnonymousConsumer(int alias, ConsumptionPackage consumptionPackage){
        this.consumptionPackage = consumptionPackage;
        this.alias = alias;
    }
    /**Retrieves the consumption data for this anonymous consumer
     *
     * @return the last calculated anonymous consumption data
     */
    public ConsumptionPackage getConsumption(){
        return consumptionPackage;
    }

    /**Retrieves the alias for this consumer that can be used by aggregators
     * when needing to send load balancing requests back through the trusted
     * third party, who then relays it on the corresponding house
     *
     * @return the alias for the anonymous consumer generating the data
     */
    public int getAlias(){
        return alias;
    }
}
