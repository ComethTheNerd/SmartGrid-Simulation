package security;
import infopackage.ConsumptionPackage;
import household.HouseHold;
import time.ApplianceTimedEvent;
import time.Time;
import aggregator.Aggregator;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import appliance.core.Appliance;
import appliance.core.ApplianceType;
import utils.MessageStream;

/**The Trusted Third Party is in the simulation in order to allow houses to send their
 * consumption data to aggregators anonymously. Encrypted consumption data is sent by a house
 * to the Trusted Third Party who then decrypts it using a pre-agreed shared secret key. This
 * data is then made anonymous by assigning it a random alias, before forwarding it on to the
 * aggregator who the house is connected to.
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class TrustedThirdParty implements ITrustedThirdParty {
    /**Characteristic of the Singleton Design Pattern
     */ 
    private static TrustedThirdParty instance;
    
    /**A seed used for randomly generating a  secret key for a HouseHold
     */ 
    private final int SKEY_SEED = 200;
    
    /** A seed used for randomly generating a unique alias for a HouseHold
     */
    private final int ALIAS_SEED = 345456647;

    /**A set of unique aliases that have been previously assigned. This is
     * checked each time a House binding is occurring to avoid duplicates
     */
    private HashSet<Integer> aliases;

    /**A temporary place for each HouseHold to store its consumption data
     * when the TrustedThirdParty requests it. It is submitted invisibly by
     * the HouseHold to the TrustedThirdParty so there is no risk of stealing
     * the data as a return value from a function
     */
    private ConsumptionPackage tempConsumptionStore;

    /**To ensure the correct HouseHold inserts its data into tempConsumptionStore,
     * we check the HouseHold's id for a match with the set expectedHouseId
     */
    private int expectedHouseId = 0;

    /**A collection of lightweight objects that allow us to store a HouseHold,
     * its alias, and its private key all in one place.
     */
    private HashSet<HouseData> bindings;

    /**Lightweight class that allows us to store a HouseHold, its alias
     * and its private key, all in one collection. Used for lookups and
     * anonymizing of data
     * 
     */
    private class HouseData{

        protected HouseHold house;
        protected int alias;
        protected int secretKey;

        protected HouseData(HouseHold h, int a, int p){
            house = h;
            alias = a;
            secretKey = p;
        }
    }
    
    
    /**Retrieves a HouseData instance that matches the given alias
     *
     * @param alias The alias that is matched to a HouseData instance
     * @return The HouseData object that maps to the alias, or null if none are found
     */
    private HouseData retrieveByAlias(int alias){
        // Find the matching HouseData object and return it
        for(HouseData hd : bindings) if(hd.alias == alias) return hd;

        // If nothing has been returned by this point, no matches exist
        return null;
    }

    /**Retrieves a HouseData instance that matches the given HouseHold
     *
     * @param house The HouseHold that is matched to a HouseData instance
     * @return The HouseData object that maps to the HouseHold, or null if none are found
     */
    private HouseData retrieveByHouseHold(HouseHold house){
        // Find the matching HouseData object and return it
        for(HouseData hd : bindings)
               if(hd.house == house) return hd;

        // If nothing has been returned by this point, no matches exist
        return null;
    }


    /**PRIVATE CONSTRUCTOR. Characteristic of the Singleton Design Pattern
    */
    private TrustedThirdParty() {
        tempConsumptionStore = new ConsumptionPackage();
        bindings = new HashSet<HouseData>();
        aliases = new HashSet<Integer>();
    }

    /**Returns the same single instance of the TrustedThirdParty
     * class by using the Singleton Design Pattern
     *
     * @return The same TrustedThirdParty instance each time
     */
    public static TrustedThirdParty getInstance() {
        if (instance == null) {
            instance = new TrustedThirdParty();
        }
        return instance;
    }

    /**Used for a household to report its encrypted demand.
     * The TrustedThirdParty uses the anonymousBindings collection to find
     * the correct modulus associated with this house to decrypt the data
     * and send it anonymously to the aggregator.
     *
     * @param house The house submitting its consumption data
     */
    public void submitEncryptedData(HouseHold house, ConsumptionPackage data)  {

        if(house.getId() != expectedHouseId)
            System.err.println("Expected House ID does not match ID supplied");

        
        tempConsumptionStore = data;
    }

/**Retrieves a list of appliances that match the given type, from the house that
 * corresponds to the given alias
 *
 * @param alias the anonymous alias of a house binding
 * @param type the type of appliance that needs to be retrieved
 * @return a list of appliance matching the given type, currently attached to
 * the house corresponding to the alias given
 */
    public ArrayList<Appliance> requestAppliances(int alias, ApplianceType type){
        // attempt to retrieve the house bound to the alias
        HouseHold house = retrieveByAlias(alias).house;
        // if we have not found a house then return empty list, null-safe!
        if(house == null) return new ArrayList<Appliance>();
        // otherwise we return the appliances that match the requested type
        return house.getAppliancesByCategory(type);
    }

    /**Called by an Aggregator to get the cumulative demand of all houses
     * that are attached to this Aggregator. The data will be sent back
     * using house aliases to protect the identity of the HouseHolds.
     *
     * @param aggregator The aggregator making the request for it's attached houses' data
     * @param time The time for which to base the cumulative demand on (allows forecasting)
     * @return The cumulative anonymous consumption data
     */
    public ArrayList<AnonymousConsumer> requestCumulativeData(Aggregator aggregator, Time time) {

        // The collection that will be returned to the caller
        ArrayList<AnonymousConsumer> anonymousData = new ArrayList<AnonymousConsumer>();
        // Get a handle to the global encryption function
        EncryptionFunction encryptionFunction = EncryptionFunction.getInstance();

        // For every house attached to this aggregator
        for (HouseHold house : aggregator.getAttachedHouses()) {
            // If we have not got a stored private key for this house
            if (retrieveByHouseHold(house) == null) {
                bindHouse(house);
            }

            // Set expected HouseHold id so we know which house is due to submit
            expectedHouseId = house.getId();

            // Get the house to invisibly submit its consumption data
            // via the submitEncryptedData() method
            house.reportEncryptedDemandInfo(time);

            /* Retrieve the HouseHold reference.
             *
             * NOTE: No need to check for null as we know by this point it will
             * have been present already, or will have been added
             */
            HouseData houseData = retrieveByHouseHold(house);

            /* Now tempConsumptionStore should contain the HouseHold's
             * encrypted consumption data. We then decrypt it using the
             * stored private key for this house
             */
             tempConsumptionStore =
                    (ConsumptionPackage) encryptionFunction.
                    decryptData(tempConsumptionStore, houseData.secretKey);
            
           
             // Add the anonymous data to the collection, ready to return
             anonymousData.add(
                     new AnonymousConsumer(houseData.alias, tempConsumptionStore)
                     );
        }
        // Return the anonymized data
        return anonymousData;
    }

    /**When an aggregator wants to request that something be turned off for
     * a particular house it builds an Set of ApplianceTimedEvents called
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
    public int relayMessage(Aggregator source, int houseAlias,
            Set<ApplianceTimedEvent> requests) {

        // Attempt to retrieve the HouseData that maps to the given alias
        HouseData recipient = retrieveByAlias(houseAlias);
        
        // If we cannot find the a valid HouseData object for this alias
        if(recipient == null) {
            return -1; // 'Undelivered_Message'
        }
        else{ 
            // Relay the requests to the HouseHold that this alias maps to
            if(recipient.house.adjustConsumption(source, recipient.secretKey, requests)){
                MessageStream.getInstance().submitMessage(source.toString() +
                        " sent Balance Load requests to House #" + recipient.house.getId() +
                        ". Unmasked from " + houseAlias, MessageStream.PRIORITY.NORMAL);
                return 1; // 'Delivered and accepted'
            }
            else
                return 0; // 'Delivered but rejected'
        }
    }

    /**A house can request to be sent it’s encryption modulus. If no record
     * for this HouseHold exists, a new HouseData object is generated and the
     * bindings are updated to reflect the change. The TrustedThirdParty
     * sets this via the HouseHold.setModulus() method
     *
     * @param house The HouseHold requesting a new modulus
     */
    public void requestEncryptionModulus(HouseHold house) {
        // Retrieve the HouseHold if it has been seen before
        HouseData houseData = retrieveByHouseHold(house);

        // If we have no record for this HouseHold
        if(houseData == null){
            bindHouse(house); // create a binding for it
        }

        /* Notify the house of its secret key.
         *
         * NOTE: This is done without return values, therefore making
         * the process more secure.
         */
        house.setSecretKey(retrieveByHouseHold(house).secretKey);
    }

    /**Binds the given HouseHold to a randomly generated unique alias,
     * and notifies the given HouseHold of a newly randomly generated
     * private key
     *
     * @param house The HouseHold to bind in the TrustedThirdParty
     */
    private void bindHouse(HouseHold house) {

        Random random = new Random();
        // The secret key for the house
        int sKey = 0;
        // Generate a secret key > 0
        while (sKey == 0) {
            sKey = random.nextInt(SKEY_SEED);
        }

        // Tell the house it's secret key
        house.setSecretKey(sKey);

        // Generate a unique alias for this house binding
        int alias = random.nextInt(ALIAS_SEED);
        while(aliases.contains(alias)) alias = random.nextInt(ALIAS_SEED);
        
        // Bind the house in the bindings
        bindings.add(new HouseData(house, alias, sKey));
    }
}
