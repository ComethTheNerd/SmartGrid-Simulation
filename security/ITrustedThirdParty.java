package security;
import infopackage.ConsumptionPackage;
import household.HouseHold;
import time.ApplianceTimedEvent;
import time.Time;
import aggregator.Aggregator;
import java.util.ArrayList;
import java.util.Set;

/**Crucial to the security model, the TrustedThirdParty acts as a middle layer
 * between Aggregators and HouseHolds, providing anonymity and security during
 * the transmission of consumption data across the SmartGrid.
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public interface ITrustedThirdParty {

    /**Used for a household to report its encrypted demand.
     * The TrustedThirdParty uses the anonymousBindings collection to find
     * the correct modulus associated with this house to decrypt the data
     * and send it anonymously to the aggregator.
     *
     * @param house The house submitting its consumption data
     */
    void submitEncryptedData(HouseHold house, ConsumptionPackage data);

    /**Called by an Aggregator to get the cumulative demand of all houses
     * that are attached to this Aggregator. The data will be sent back
     * using house aliases to protect the identity of the HouseHolds.
     *
     * @param aggregator The aggregator making the request for it's attached houses' data
     * @param time The time for which to base the cumulative demand on (allows forecasting)
     * @return The cumulative anonymous consumption data
     */
    ArrayList<AnonymousConsumer> requestCumulativeData(Aggregator aggregator, Time time);

    /**When an aggregator wants to request that something be turned off for
     * a particular house it builds an ArrayList of TimedEvents called
     * requests, and addresses it to an Anonymous House (the alias of a real
     * house in the system). The Third Party looks up which house the alias
     * maps to, and forwards on the requests to the intended house. The
     * boolean denotes whether the message was delivered successfully or not.
     *
     * @param source The Aggregator sending this message
     * @param houseAlias The recipient identified by its anonymous alias
     * @param requests The rules that the aggregator wants the HouseHold to
     * adopt to help balance load
     * @return -1 : Recipient not found, 0 : Delivered but rejected, 1 : Delivered and agreed
     */
    int relayMessage(Aggregator source, int houseAlias,
            Set<ApplianceTimedEvent> requests);

    /**A house can request to be sent it’s encryption modulus. If no record
     * for this HouseHold exists, a new HouseData object is generated and the
     * bindings are updated to reflect the change. The TrustedThirdParty
     * sets this via the HouseHold.setModulus() method
     *
     * @param house The HouseHold requesting a new modulus
     */
    void requestEncryptionModulus(HouseHold house);

}
