package aggregator;

import universe.IObserver;
import household.HouseHold;
import infopackage.ConsumptionPackage;
import infopackage.ConsumptionPackage.Header;
import java.util.ArrayList;
import appliance.core.Appliance;
import appliance.core.ApplianceType;
import security.AnonymousConsumer;
import security.TrustedThirdParty;
import time.Time;
import universe.SmartGridUniverse;
import java.util.HashSet;
import java.util.Set;
import time.ApplianceTimedEvent;
import household.PowerState;
import time.commands.BalanceLoadCommand;
import utils.MessageStream;
import utils.Config;

/**Represents an Aggregator that collects demand information from the
 * houses attached to it. It can then make requests to households to
 * balance load at a particular time (through the TrustedThirdParty
 * discussed above)
 *
 * @author Jake Baker, Darius Hodaei
 */
public class Aggregator implements IObserver {
    /**A collection of consumers using an above average amount of electricity*/
    protected ArrayList<AnonymousConsumer> watchList = new ArrayList<AnonymousConsumer>();
     
    /**The number of system ticks between Aggregators checking total load*/
    protected int fetchInterval = Config.FETCH_INTERVAL;
    /**A counter variable to keep track of system ticks since the last load check*/
    protected int ticks = 1;

    /**The houses that are attached to this aggregator*/
    protected ArrayList<HouseHold> houses = new ArrayList<HouseHold>();
    /**The anonymous usage data passed to this aggregator via the trusted third party*/
    protected ArrayList<AnonymousConsumer> consumers = new ArrayList<AnonymousConsumer>();
    /**The forecast anonymous usage data passed to this aggregator via the trusted third party*/
    protected ArrayList<AnonymousConsumer> consumersForecast = new ArrayList<AnonymousConsumer>();

     
    /**The average consumption usage of a house attached to this aggregator*/
    protected int averageUsage;
    /**The total combined usage of the houses attached to this aggregator*/
    protected int totalCombinedUsage; //Declare var totalCombUsage - max usage for all Houses attached to this aggregator.
    
    /**A handle to the Current Usage header of a Consumption Package*/
    protected final Header h_CurrentUsage = ConsumptionPackage.Header.CURRENT_USAGE;
    /**A handle to the Current State header of a Consumption Package*/
    protected final Header h_CurrentState = ConsumptionPackage.Header.CURRENT_STATE;
    /**A handle to the White Goods Usage header of a Consumption Package*/
    protected final Header h_WhiteGoods = ConsumptionPackage.Header.WHITE_GOODS_USAGE;
    /**A handle to the Utilities Usage header of a Consumption Package*/
    protected final Header h_Utilities = ConsumptionPackage.Header.UTILITIES_USAGE;
    /**A handle to the Burst Usage header of a Consumption Package*/
    protected final Header h_Burst = ConsumptionPackage.Header.BURST_USAGE;
    /**A handle to the Luxury Items Usage header of a Consumption Package*/
    protected final Header h_Luxury = ConsumptionPackage.Header.LUXURY_ITEMS_USAGE;
    /**A handle to the Average Usage header of a Consumption Package*/
    protected final Header h_Average = ConsumptionPackage.Header.AVERAGE_USAGE;

    /**A handle to the Smart Grid Universe object*/
    protected SmartGridUniverse universe;
    /**Used to generate unique Id's for each created aggregator*/
    private static int idGen = 0;
    /**The unique Id that belongs to this aggregator*/
    protected int id = 0;
     
    /**The total consumption at a given load check for all the houses attached to this aggregator*/
    protected int cumulativeUsage;
    /**The previous total consumption of all the houses attached to this aggregator*/
    protected int lastCumulativeUsage;

    public Aggregator(){
        // give this aggregator a unique Id
        this.id = idGen++;
        // assign a handle to the Smart Grid Universe object
        universe = SmartGridUniverse.getInstance();
        // register this aggregator as an observer of universe notifications
        universe.addObserver(this);
    }

    /**Sets the the fetch interval, that is the number of system ticks
     * between each request by this Aggregator to get the cumulative
     * demand of all HouseHolds attached to it
     *
     * @param interval The number of ticks between each cumulative demand request
     */
    public void setFetchInterval(int interval) {
        universe = SmartGridUniverse.getInstance();
        // overwrite fetch interval with new one
        this.fetchInterval = interval;
    }

    /**Returns the current fetch interval, that is the number of system ticks
     * between each call by the Aggregator to get the cumulative demand of HouseHolds
     *
     * @return The number of system ticks between each Aggregator request for cumulative HouseHold demand
     */
    public int getFetchInterval() {
        return fetchInterval;
    }

    /**Connect a house to this aggregator.
     * 
     * @param newHouse The house to add
     */
    public void addHouse(HouseHold newHouse) {
        // if we have not reached the limit of allowed attached houses
        if(houses.size() >= Config.MAX_HOUSES_PER_AGG) {
            // log the failure to add house event
            MessageStream.getInstance().submitMessage("Max houses attached to Aggregator #" + id + ". Cannot add house.", MessageStream.PRIORITY.HIGH);
            return;
        }
        // next we check whether this house is already attached
        for (HouseHold house : houses) {
            // if we find this house is already attached
            if (house == newHouse) {
                // return early
                return;
            }

        }
        // otherwise if we get to this point we can safely add the house
        houses.add(newHouse);
       
    }

    /**Adds a collection of houses to this aggregator
     *
     * @param newHouses The houses to connect to this aggregator
     */
    public void addHouses(ArrayList<HouseHold> newHouses) {
        // we wrap the addHouse() method and call it for each house in the list
        for (HouseHold house : newHouses) {
            addHouse(house);
        }
    }

    /**Remove a house from this aggregators collection of HouseHolds. It will
     * then no longer be responsible for the house’s demand.
     *
     * @param house The house to remove from this Aggregator
     */
    public void removeHouse(HouseHold house) {
        // loop through all attached houses
        for(int x = 0; x< houses.size(); x++){
            // if we find the house in question
            if(houses.get(x) == house){
                // detach the house from the aggregator
                houses.remove(x);
               
                return;
            }
        }

    }

    /**Retrieves an ArrayList of all the HouseHold objects that are currently
     * attached to this aggregator
     *
     * @return ArrayList of HouseHold objects currently attached to this aggregator
     */
    public ArrayList<HouseHold> getAttachedHouses() {
        return houses;
    }

    /**Used to create balance load rules for consumers that are using a high
     * level of energy during a given load check by this aggregator
     *
     * @param alias The alias of the consumer that needs load balancing
     * @param time The time that the load balance request should be executed
     * @return the balance load requests to be sent to the consumer
     */
    private ArrayList<ApplianceTimedEvent> reportHighUser(AnonymousConsumer alias, Time time){
        // the balance load requests we will return
        ArrayList<ApplianceTimedEvent> action = new ArrayList<ApplianceTimedEvent>();

        // if during the previous check this user was observed to be using too much energy
        if(watchList.contains(alias)){
            // create more severe balance load requests
            action.addAll(createBalanceRules(time, 4, ApplianceType.LUXURY_ITEMS, alias.getAlias()));
            action.addAll(createBalanceRules(time, 2, ApplianceType.UTILITIES, alias.getAlias()));
        }
        else{
            // add this consumer as one to watch in case he persistently over-uses
            watchList.add(alias);
            // create moderate balance load requests
            action.addAll(createBalanceRules(time, 2, ApplianceType.LUXURY_ITEMS, alias.getAlias()));
        }

        return action;
    }
    
    /**Assesses the total load for all houses attached to this aggregator,
     * and makes balance load requests to those consumers using an exceptionally
     * high amount of energy
     *
     * @param time the time at which this check is occurring
     * @param anons the collection of anonymous consumer usage data
     */
    public void balanceLoad(Time time, ArrayList<AnonymousConsumer> anons) {
        // meta data about the balance load algorithm
        int consumerSize = anons.size(), loadsBalanced = 0;
        // if we have no consumer data then return
        if(consumerSize == 0) return;
        // calculate the average usage value
        averageUsage = totalCombinedUsage / consumerSize;

        // a temporary variable to hold the consumption of a consumer
        ConsumptionPackage consumption;
        // reset the cumulative usage value ready to recalculate it
        cumulativeUsage = 0;

        // for all the consumers
        for (int i = 0; i < consumerSize; ++i) {
            // iterate over the collection progressively
            AnonymousConsumer anon = anons.get(i);
            // retrieve this consumer's consumption
            consumption = anon.getConsumption();
            // collection of balance load requests
            Set<ApplianceTimedEvent> requests = new HashSet<ApplianceTimedEvent>();
            // collect information about the consumption
            int current = consumption.getValue(h_CurrentUsage),
                    state = consumption.getValue(h_CurrentState),
                    // the alias used to hide this consumers true identity
                    alias = anon.getAlias();

            cumulativeUsage += current;


            // If the house is in the red state or using above average energy
            if(current > averageUsage && state == PowerState.RED){
                // create balance load requests for the consumer
               requests.addAll(
                    reportHighUser(anon, time)
                    );
            }
            else
                // otherwise we should remove this consumer from the watch list
                // as they are now using an acceptable energy amount
                if(watchList.contains(anon)){
                    watchList.remove(anon);
                   
                }

            // if we have created balance load requests for this consumer
           if(!requests.isEmpty()){
               // send them to the third party for it to relay on to the actual house
               TrustedThirdParty.getInstance().relayMessage(this, alias, requests);
               ++loadsBalanced;
            }
            
        }

        lastCumulativeUsage = cumulativeUsage;

        // log the data for the user to review
         MessageStream.getInstance().submitMessage("Aggregator #" + id + " Balanced Load for " + loadsBalanced + " houses",  MessageStream.PRIORITY.NORMAL);
         MessageStream.getInstance().submitMessage("Aggregator #" + id + " Total Load : " + cumulativeUsage + "kwh For : " + houses.size() + " houses",  MessageStream.PRIORITY.NORMAL);

    }

    /**Wraps the process of creating balance load requests to allow for easier
     * creation of requests
     *
     * @param time the time this request should be executed
     * @param offInterval how long to turn the appliances in question off for
     * @param type the type of the appliances to turn off
     * @param alias the consumer alias for which we are building this request
     * @return the balance load requests for this consumer
     */
    private ArrayList<ApplianceTimedEvent> createBalanceRules(Time time, int offInterval, ApplianceType type, int alias){
       // the requests we will create for the consumer
        ArrayList<ApplianceTimedEvent> requests = new ArrayList<ApplianceTimedEvent>();
        // the appliances, retrieved via the trusted third party for anonymity
        ArrayList<Appliance> apps =
                        TrustedThirdParty.getInstance().requestAppliances(alias, type);
                // the time to turn off the appliances
                Time offTime = time.clone();
                offTime = offTime.advanceTime(new Time(offInterval,0,0,0,0));

                // for all of the appliances
                for(Appliance app : apps){
                    // if this appliance can be turned off (ie. is not essential)
                    if(app.isSheddable()){
                        // create an appliance timed event that will turn off the appliance
                        ApplianceTimedEvent ate = new ApplianceTimedEvent(app,
                                                                          time,
                                                                          offTime,
                                                                          new BalanceLoadCommand(app),
                                                                          null);
                        // add the event to the requests
                        requests.add(ate);
                    }
                }

        // return the balance load requests
        return requests;
    }

    /**Method called by the observed object on all its subscribers.
     * In context this will be upon every iteration of the simulation
     * loop in SmartGridUniverse.
     */
    public void update() {
        // if it is time to check the total aggregator load
        if (ticks == fetchInterval) {
            // get the current universe time
            Time uniTime = universe.getUniverseTime();
            // get the anonymous consumer data for the current time
            consumers = requestCumulativeData(uniTime);
            // analyse and balance load of this aggregator
            balanceLoad(uniTime, consumers);
            // now we want to check the forecast load
            Time forecastTime = new Time(Config.FORECAST_TIME);
            // get the forecast load for this aggregator
            consumersForecast = requestCumulativeData(forecastTime);
            // create forecast balance load requests for the predicted demand
            balanceLoad(forecastTime, consumersForecast);
            // reset the ticks counter
            ticks = 1;
        }
        else
            // if it is not time to check load, just increment the counter
             ++ticks;
    }

    /**Retrieves the anonymous consumption data from the trusted third party, for
     * the given time
     *
     * @param time the time to calculate consumption usage for
     * @return a collection of the anonymous consumers and their usage data
     */
    private ArrayList<AnonymousConsumer> requestCumulativeData(Time time) {
        // get current usage as per the given time
        return TrustedThirdParty.getInstance().requestCumulativeData(this, time);
    }

    /**Returns the last known cumulative demand for all the houses attached
     * to this aggregator
     *
     * @return the last known cumulative demand
     */
    public int getCumulativeDemand(){
        return lastCumulativeUsage;
    }


    /**Returns a string representation of Aggregator.
     *
     * @return a string representation of Aggregator.
     */
    @Override
    public String toString(){
        return "Aggregator #" + id;
    }
}