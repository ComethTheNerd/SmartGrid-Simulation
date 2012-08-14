package household;

import aggregator.Aggregator;
import universe.IObserver;
import policy.core.PolicyComponent;
import appliance.core.Appliance;
import time.ITimedEventHandler;
import time.PriorityTimedEventHandler;
import gui.ClickablePanel;
import time.Time;
import time.TimedEvent;
import time.ApplianceTimedEvent;
import time.HouseTimedEvent;
import security.EncryptionFunction;
import infopackage.ConsumptionPackage;
import security.TrustedThirdParty;
import java.util.ArrayList;
import policy.core.PolicyBank;
import universe.SmartGridUniverse;
import java.util.Set;
import java.util.Random;
import appliance.core.ApplianceType;
import infopackage.IHeader;
import java.util.HashSet;
import infopackage.HouseParameters;
import java.util.Collection;
import utils.MessageStream;
import utils.Config;
/**Base class that represents HouseHolds in the model,
 * implements IObserver interface so that it can listen out
 * for Universe Updates upon each iteration of the simulation
 * loop
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class HouseHold implements IObserver {

    private static int idGen = 0;
    private int uniqueId,
                lastUsage = -1,
                ticks = 1,
                averageUsage = 0,
                totalCumulativeUsage = 0;
    
    private boolean submittedPolicyEvents = false;
    private ArrayList<Appliance> appliances;
    private Aggregator aggregator;
    private ArrayList<PolicyComponent> housePolicy;
    private ITimedEventHandler eventHandler;
    private ConsumptionPackage consumption, forecastConsumption;
    private ConsumptionPackage.Header   h_CurrentUsage = ConsumptionPackage.Header.CURRENT_USAGE,
                                        h_WhiteGoods = ConsumptionPackage.Header.WHITE_GOODS_USAGE,
                                        h_LuxuryItems = ConsumptionPackage.Header.LUXURY_ITEMS_USAGE,
                                        h_Utilities = ConsumptionPackage.Header.UTILITIES_USAGE,
                                        h_Burst = ConsumptionPackage.Header.BURST_USAGE,
                                        h_TotalUsage = ConsumptionPackage.Header.TOTAL_USAGE,
                                        h_AverageUsage = ConsumptionPackage.Header.AVERAGE_USAGE,
                                        h_CurrentState = ConsumptionPackage.Header.CURRENT_STATE;
    private Time lastCalculation = Time.ZERO_VALUE;
    private SmartGridUniverse sgu;
    private HouseParameters houseParams;
    private PowerState currentPowerState;
    private int secretKey;
    private HashSet<HouseTimedEvent> houseEvents;

    /**Creates and sets up new house objects.(eg randomly assigns
     * appliances to house, sets unique ID field, and requests encryption
     * modulus from the TrustedThirdParty).
     *
     * @param aggregator The aggregator that this house is attached to.
     */
    public HouseHold(Aggregator aggregator) {
        sgu = SmartGridUniverse.getInstance();
        uniqueId = ++idGen;
        eventHandler = new PriorityTimedEventHandler();

        houseParams = new HouseParameters();

        housePolicy = PolicyBank.getInstance().getRandomPolicy(this);

        // UNNECESSARY CALL AS TRUSTED THIRD PARTY ENSURES HOUSE BINDING
        //TrustedThirdParty.getInstance().requestEncryptionModulus(this);
        
        this.aggregator = aggregator;
        consumption = new ConsumptionPackage();

        // Start from green state
        currentPowerState = new GreenPowerState(this);

        assignAppliances();
        assignHouseEvents();
        setPowerStateBoundaries();
        sgu.addObserver(this);
       
    }

    /**Generates the appliances that are attached to this house from all the
     * available Appliance subclasses
     */
    private void assignAppliances() {
        appliances = new ArrayList<Appliance>();

        // Get the set of all concrete Appliance classes
        Set<Class<? extends Appliance>> appTypes = sgu.getAllApplianceTypes();

        HashSet<ApplianceTimedEvent> usageHours = new HashSet<ApplianceTimedEvent>();

        // Create a randomizer that decides how many of each appliance we instantiate
        Random random = new Random();
        try {
            // For every concrete subclass of Appliance
            outer:
            for (Class<? extends Appliance> c : appTypes) {

                Appliance ax = c.newInstance();
                int min = ax.getMinInstances(), max = ax.getMaxInstances();

                int totalApps = min + random.nextInt(max - min);
                // Instantiate a random quantity of the appliance
                for (int quantity = 0; quantity < totalApps; ++quantity) {
                    Appliance a = c.newInstance(); // Dynamic instantiation
                    appliances.add(a); // Attach the appliance to this house

                    // add this appliances to the usage hours set, to be sent to the TimedEventHandler
                    usageHours.addAll(a.getUsageHours());

                     // Maximum appliances per house hold constraint
                    if(appliances.size() == Config.APPS_MAX_PER_HOUSE) break outer;
                }

               
            }

        }
        catch (Exception e){
            
            System.err.println(e.toString());
        }



        // Recursive call to ensure no house ends up without appliances
        if(appliances.isEmpty())
            assignAppliances();
        else 
            // submit the usage hours to the event handler
            eventHandler.submitGeneralApplianceEvents(usageHours);

    }

   /**Randomly assigns this HouseHold random HouseTimedEvents
    */
    @Deprecated
    private void assignHouseEvents(){
        houseEvents = new HashSet<HouseTimedEvent>();

        /*TODO: Random Initialization*/

        eventHandler.submitGeneralHouseEvents(houseEvents);
    }
    
    
    /**Returns the unique ID for this HouseHold
     *
     * @return the unique ID that pertains to this HouseHold
     */
    public int getId() {
        return uniqueId;
    }

    /**Return an array list of all appliances of the given type
     * (eg. Washing Machine) currently attached to this household
     *
     * @param appType The concrete Appliance type to retrieve
     * @return An ArrayList of all attached appliances that match the given Type
     */
    public ArrayList<Appliance> getAppliancesByClass(Class<? extends Appliance> appType) {
        // The list of appliances that have the same class as appType
        ArrayList<Appliance> matches = new ArrayList<Appliance>();

        // For all appliances currently attached to this HouseHold
        for (int index = 0; index < appliances.size(); ++index) {
            Appliance app = appliances.get(index);
            // If their class is the same as appType, found a match
            if (app.getClass() == appType) {
                matches.add(app);
            }
        }
        // return all the matches found
        return matches;
    }

    /**Return an array list of all appliances of the given category
     * (eg. WHITE_GOODS) currently attached to this household
     *
     * @param appType The ApplianceType of Appliances to retrieve
     * @return All Appliances of the given type that are attached to this house
     */
    public ArrayList<Appliance> getAppliancesByCategory(ApplianceType appType) {
        // The list of appliances that have the ApplianceType as appType
        ArrayList<Appliance> matches = new ArrayList<Appliance>();

        // For all appliances currently attached to this HouseHold
        for (int index = 0; index < appliances.size(); ++index) {
            Appliance app = appliances.get(index);
            // If their ApplianceType is the same as appType, found a match
            if (app.getApplianceType() == appType) {
                matches.add(app);
            }
        }
        // return all the matches found
        return matches;
    }

    /**Sets the Aggregator reference held by this HouseHold
     *
     * @param aggregator The aggregator this HouseHold is to being attached to
     */
    public void setAggregator(Aggregator aggregator) {
        this.aggregator = aggregator;
        MessageStream.getInstance().submitMessage("House #" + this.uniqueId + " switched aggregators to " + aggregator,  MessageStream.PRIORITY.HIGH);

    }

    /**Returns the Aggregator that this household is attached to
     *
     * @return The aggregator that this house is currently attached to
     */
    public Aggregator getAggregator() {
        return aggregator;
    }


    /**Adds an instance of the supplied policy component class to this
     * Household's list of adopted policies.
     * The provided policy component is also sent to this households event handler
     *
     * @param pc PolicyComponent class from which a policy instance is created and added to household
     */
    public void addPolicyComponent(Class<? extends PolicyComponent> pc){

        try{
            PolicyComponent newInstance = pc.getDeclaredConstructor(HouseHold.class).newInstance(this);
            housePolicy.add(newInstance);
            eventHandler.submitPolicyEvents(newInstance);
         }
      catch(Exception e){
           System.err.println(e.toString());
       }
    }

    /**Adds the supplied PolicyComponent to the policy components list of this house
     * if this policy component is not already present in the list
     *
     * @param component PolicyComponent to be added to HouseHold's list of adopted policies
     */
    public void addPolicyComponent(PolicyComponent component){

           if (!hasPolicyComponent(component)) {
               housePolicy.add(component);
               eventHandler.submitPolicyEvents(component);
               MessageStream.getInstance().submitMessage("House #" + this.uniqueId + " adopted " + component + " policy",  MessageStream.PRIORITY.HIGH);

            }

    }

    /**Adds the supplied collection of PolicyComponents to the policy components list of this house.
     * Any policy component in this collection which is already in the list would not be added.
     *
     * @param component Collection of PolicyComponents to be added to HouseHold's list of adopted policies
     */
    public void addPolicyComponents(Collection<PolicyComponent> component){

        for(PolicyComponent p : component) addPolicyComponent(p);
    }

    /**Removes the supplied PolicyComponent from this HouseHold's list of adopted policies.
     * Also all general rules (TimedEvents) associated with this policy
     * is removed from household policy event handler.
     *
     * @param component PolicyComponent to be removed to HouseHold's list of adopted policies
     */
    public void removePolicyComponent(PolicyComponent component){
        if (hasPolicyComponent(component)) {
               housePolicy.remove(component);
               eventHandler.removePolicyEvents(new HashSet(component.generalRules()));
               MessageStream.getInstance().submitMessage("House #" + this.uniqueId + " abandoned " + component + " policy",  MessageStream.PRIORITY.HIGH);

            }
    }

    /**Removes the supplied collection of PolicyComponents from this HouseHold's list of adopted policies.
     * Also all general rules (TimedEvents) associated with these policies
     * are removed from household policy event handler.
     *
     * @param component Collection  of PolicyComponent to be removed to HouseHold's list of adopted policies
     */
    public void removePolicyComponents(Collection<PolicyComponent> component){
        for(PolicyComponent p : component) removePolicyComponent(p);
    }

    /**Returns true if HouseHold adopts the supplied policy component, false otherwise.
     *
     * @param component PolicyComponent to be searched for in household's list of policy components.
     * @return true if household has component, false otherwise
     */
    public boolean hasPolicyComponent(PolicyComponent component){
        if(housePolicy.contains(component)) return true;
        else return false;
    }

    /**Returns the events handler associated with this HouseHold
     *
     * @return the events handler associated with this HouseHold
     */
    public ITimedEventHandler getTimedEventHandler(){return eventHandler;}


    /**Returns the current IPolicy held by this HouseHold
     *
     * @return The IPolicy currently attached to this HouseHold
     */
    public ArrayList<PolicyComponent> getPolicy() {
        return housePolicy;
    }

    /**Returns an ArrayList of policy components of type specified by polType
     * that are currently adopted by household
     *
     * @param polType PolicyCompnent class representing the type of policy component to be retrieved
     * @return An ArrayList of policy components of type specified by polType
     * that are currently adopted by household
     */
    public ArrayList<PolicyComponent> getPolicyComponentsByType(Class<? extends PolicyComponent> polType) {
        // The list of appliances that have the same class as appType
        ArrayList<PolicyComponent> matches = new ArrayList<PolicyComponent>();

        // For all appliances currently attached to this HouseHold
        for (int index = 0; index < housePolicy.size(); ++index) {
            PolicyComponent pol = housePolicy.get(index);
            // If their class is the same as appType, found a match
            if (pol.getClass() == polType) {
                matches.add(pol);
            }
        }
        // return all the matches found
        return matches;
    }

    /**Returns the HouseParameters of this household.
     * the HouseParameters contains info such as number of occupants,
     * min/max energy usage limit etc.
     *
     * @return The HouseParameters of this household.
     */
    public HouseParameters getParams(){
        return houseParams;
    }

    /**Checks each rule (that has not expired) to see whether it should be
     * executed at the current time
     */
    public void checkTimedEvents() {
        eventHandler.coordinatedExecution();
    }

    /** This is called by the Aggregator indirectly through
     * TrustedThirdParty.RelayMessage() as a way of anonymously passing
     * messages from the Aggregator to the target house to request it turn
     * off certain appliances. The Aggregator reference and secret key form
     * two-factor authentication to ensure this is a legitimate request
     *
     * NOTE: An aggregator cannot use this method directly as it does not
     * know which house a given alias maps to, and does not have its secret key.
     *
     * @param source The Aggregator making the requests
     * @param secretKey The secret key shared with the TrustedThirdParty who should've sent the message
     * @param requests The requests to alter the given appliances' usage
     * @return true if Two-Factor authentication is passed and the requests are submitted. False otherwise.
     */
    public boolean adjustConsumption(Aggregator source, int secretKey, Set<ApplianceTimedEvent> requests) {
        
        /*TWO-FACTOR AUTHENTICATION OF REQUESTS:
         * 1 Secret key known only by the HouseHold and the TrustedThirdParty MUST match
         * 2 The given Aggregator reference MUST match the Aggregator this house is attached to
         */
        if(this.secretKey != secretKey){
           // System.err.println("Invalid private key supplied. Ignoring Load Balance requests.");
            return false;
        }

        if(this.aggregator != source){
          //  System.err.println("Invalid Aggregator reference supplied. Ignoring Load Balance requests.");
            return false;
        }

       // System.out.println("Two-Factor Authentication passed! Submitting Load Balance requests.");
        // If we get to this point the authentication has been passed and so the requests are submitted
        eventHandler.submitBalanceLoadEvents(requests);
        return true;
    }

    /** Sends encrypted information in a ConsumptionPackage
     * (subclass of InfoPackage) to the TrustedThirdParty.
     * It is encrypted so even if this method is called by malicious
     * objects, they won’t know how to decrypt it (ie. what encryption
     * modulus was used).
     *
     * NOTE: Because of the Time parameter this can be used to forecast
     * future demand by supplying a time in the future, or the present time
     * can be supplied to get the current demand.
     *
     * @param time The time for which the demand should be based upon
     */
    public void reportEncryptedDemandInfo(Time time) {

        ConsumptionPackage encryptedData = new ConsumptionPackage();

        // make sure the consumption data is accurate to this time before encrypting
        encryptedData = calculateConsumption(time);

        //encrypt the consumption data
        encryptedData = (ConsumptionPackage) EncryptionFunction.getInstance().encryptData(consumption, secretKey);
        // send it to the trusted third party
        TrustedThirdParty.getInstance().submitEncryptedData(this, encryptedData);
    }


    /**Calculates how much energy the household requires at the supplied time
     * and returns a consumption package indicating the calculated value.
     * This is done summing up how much energy each appliance requires at specified time.
     * In addition to household energy consumption, other values such as average consumption
     * and consumption per ApplianceType is specified in returned consumption package
     *
     * @param time the Time value for which consumption must be calculated.
     * @return returns a consumption package indicating household consumption at time.
     */
    private ConsumptionPackage calculateConsumption(Time time){
        // get current universe time
        Time uniTime = sgu.getUniverseTime();
        // if we have already calculated usage for this time, return that
        // NOTE: No forecasting allowed for a time in the past!
       // if(lastCalculation.equals(time)) return consumption;
        // is this a forecast, or is it the current universe time
        boolean forecast = time.equals(uniTime) ? false: true;

        ConsumptionPackage cp = new ConsumptionPackage();            
        int total = 0, luxuryUse = 0, whiteUse = 0, utilsUse = 0, burstUse = 0, consTotal = 0, average = 0;
        
        // naive?.......
        // ???
        //if(houseParams.getValue(HouseParameters.Header.NumberOfOccupants) == 0) return;

        // for every appliance currently attacvhed to this house
        for(Appliance app : appliances){
            // get current energy for the appliance relative to the given time
            int usage = app.energyRequired(time);
            if(usage == 0) continue;
            // we need to know the appliance type so we can more accurately report usage
            ApplianceType type = app.getApplianceType();

            if(type == ApplianceType.LUXURY_ITEMS) luxuryUse += usage;
            else if(type == ApplianceType.WHITE_GOODS)
                whiteUse +=usage;
            else if(type == ApplianceType.UTILITIES) utilsUse += usage;
            else if(type == ApplianceType.BURST) burstUse += usage;

            // add each appliances usage to the local cumulative total
            total += usage;
        }
        
        // if this is not a forecast we need to record the stats
        if(!forecast){
            // add to the cumulative total
            consTotal = (totalCumulativeUsage += total);
            average = consTotal / ticks;
            lastUsage = total;
            lastCalculation = time;
        }
        else{
            time = time.subtractTime(uniTime); // difference between universe time and now
            int hours = Time.toHours(time);
            consTotal = totalCumulativeUsage + (hours *  cp.getValue(h_AverageUsage));
            average = consTotal / (hours + ticks);
        }

        // Update headers
        cp.setValue(h_LuxuryItems, luxuryUse);
        cp.setValue(h_WhiteGoods, whiteUse);
        cp.setValue(h_Utilities, utilsUse);
        cp.setValue(h_Burst, burstUse);
        cp.setValue(h_CurrentUsage, total); // total will be relative to the time, so could be forecast
        cp.setValue(h_AverageUsage, average); // forecast average takes in to account hours in the future
        cp.setValue(h_TotalUsage, consTotal); // may be a simulated forecast based on the average
        int cps = currentPowerState.getStateInt();
        cp.setValue(h_CurrentState, cps);

        return cp;
    }

    /**Returns the current PowerState instance held by this HouseHold
     *
     * @return The current PowerState instance held by this HouseHold
     */
    public PowerState getCurrentState() {
        return currentPowerState;
    }

    /**Sets the threshold levels(Red, Amber and Green) of household consumption to random but constrained values.
     *
     */
    private void setPowerStateBoundaries(){
        Random r = new Random();
        houseParams.setValue(HouseParameters.Header.GreenStateLimit, Config.GREEN_MIN +
                                                            r.nextInt(Config.AMBER_MIN - Config.GREEN_MIN));
        houseParams.setValue(HouseParameters.Header.AmberStateLimit, Config.AMBER_MIN +
                                                            r.nextInt(Config.RED_MIN - Config.AMBER_MIN));
    }



    /**Updates the PowerState of the household according
     * to how much energy the household is consuming.
     *
     */
    private void updatePowerState(){

        int currentUsage = consumption.getValue(ConsumptionPackage.Header.CURRENT_USAGE);

        if(currentUsage > houseParams.getValue(HouseParameters.Header.GreenStateLimit)){

            if(currentUsage > houseParams.getValue(HouseParameters.Header.AmberStateLimit) ){
                
              // MessageStream.getInstance().submitMessage("RED STATE! House #" + this.uniqueId + " using " + lastUsage + "kwh",  MessageStream.PRIORITY.NORMAL);
                currentPowerState = new RedPowerState(this);
                
            }
            else currentPowerState = new AmberPowerState(this);

        }
        else currentPowerState = new GreenPowerState(this);

    }

    /**Adds the given appliance to the household IF and only IF max number
     * of that appliance type are not already present in the household
     * (if so, use RemoveAppliance() first...then AddAppliance())
     *
     * @param newApp The new Appliance to attach to this HouseHold
     */
    public void addAppliance(Appliance newApp) {

        appliances.add(newApp);

        eventHandler.submitGeneralApplianceEvents(newApp.getUsageHours());
        eventHandler.addApplianceApplicablePolicyEvents(newApp);
        MessageStream.getInstance().submitMessage("House #" + this.uniqueId + " added a " + newApp,  MessageStream.PRIORITY.HIGH);


    }

    /**Adds an instance of the given appliance class to the household IF and only IF max number
     * of that appliance type are not already present in the household
     * (if so, use RemoveAppliance() first...then AddAppliance())
     *
     * @param newApp The Class of appliance from which a new Appliance instance is to be attached to this HouseHold
     */
    public void addAppliance(Class<? extends Appliance> newApp) {
        try{
            Appliance a  = newApp.newInstance();
            appliances.add(a);

            eventHandler.submitGeneralApplianceEvents(a.getUsageHours());
            eventHandler.addApplianceApplicablePolicyEvents(a);
            MessageStream.getInstance().submitMessage("House #" + this.uniqueId + " added a " + newApp.getSimpleName(),  MessageStream.PRIORITY.HIGH);
        }
        catch(Exception e){
            System.err.println(e.toString());
        }
    }

    /**Removes a particular appliance from a household by Class Name,
     * if present. Returns true upon successful removal, false otherwise.
     *
     * @param appType The Type of the Appliance to remove
     */
    public boolean removeAppliance(Class<? extends Appliance> appType) {

        for(int index = 0; index < appliances.size(); ++index){
            Appliance app = appliances.get(index);
            if(app.getClass() == appType){
                
                appliances.remove(app);

                eventHandler.removeGeneralApplianceEvents(app);
                eventHandler.removeApplianceApplicablePolicyEvents(app);
                 MessageStream.getInstance().submitMessage("House #" + this.uniqueId + " removed a " + appType.getSimpleName(),  MessageStream.PRIORITY.HIGH);

                return true;
            }
        }
        return false;
    }

    /**Removes the specified appliance from household
     * if appliance is present in household's list of appliances.
     * Returns true on successful removal, false otherwise
     *
     * @param appliance The appliance to be removed from HouseHold
     * @return true on successful removal, false otherwise
     */
     public boolean removeAppliance(Appliance appliance) {

        for(int index = 0; index < appliances.size(); ++index){
            Appliance app = appliances.get(index);
            if(app == appliance){

                appliances.remove(app);

                eventHandler.removeGeneralApplianceEvents(app);
                eventHandler.removeApplianceApplicablePolicyEvents(app);
                MessageStream.getInstance().submitMessage("House #" + this.uniqueId + " removed a " + appliance,  MessageStream.PRIORITY.HIGH);

                return true;
            }
        }
        return false;
    }

    /**Returns list of all appliance objects in the household
     *
     * @return An ArrayList of all appliances currently attached to this house
     */
    public ArrayList<Appliance> getAllAppliances() {
        return appliances;
    }

    /**Method called by the observed object on all its subscribers.
     * In context this will be upon every iteration of the simulation
     * loop in SmartGridUniverse.
     *
     */
    public void update() {
        if(!submittedPolicyEvents){
            for(PolicyComponent pc : housePolicy){
               eventHandler.submitPolicyEvents(pc);
            }
            

            submittedPolicyEvents = true;

        }

        checkTimedEvents(); // put this here so that appliances get turned on/off before calculating consumption


        consumption = calculateConsumption(sgu.getUniverseTime());


        updatePowerState();

        currentPowerState.action();


        consumption.setValue(h_CurrentState, currentPowerState.getStateInt());

        ++ticks;
        
    }

    /**Sets the encryption modulus that is used by this HouseHold when
     * reporting its encrypted consumption data to the TrustedThirdParty
     *
     * @param newKey The new modulus to use from now on when encrypting
     */
    public void setSecretKey(int newKey) {
        secretKey = newKey;
    }

    /**Securely sends an up to date consumption package of this household
     * to specified ClickablePanel.
     *
     * @param v ClickablePanel which is updated with current house ConsumptionPackage
     */
    public void _contextHouseConsumption(ClickablePanel v){
        v.consumption(consumption);
    }

    /**Returns a textual representation of this Household object
     *
     * @return a String representation of this Household object
     */
    @Override
    public String toString(){
        String info = "House #" + uniqueId;
                
        for(IHeader h : consumption.getHeaders()){
            info += "\n" + h + ": " + consumption.getValue(h);
        }
        
        info+= "\nPrivate Key: "+ secretKey +
                "\n \nAppliances: " + appliances.size();

        for(Appliance a : appliances){
            info += "\n" + a.toString();

            for(TimedEvent te : a.getUsageHours()){
                info +="\n    ->" + te.getCurrentState() +  " : " + te.getStartTime().toShortString() + "-" + te.getEndTime().toShortString();
            }
        }
        info += "\n \nPolicy Components: " + housePolicy.size();
        for(PolicyComponent p : housePolicy){
            info += "\n" + p.toString();
            for(TimedEvent te : eventHandler.getCurrentPolicyTimedEvents(p)){
                info +="\n    ->" + te.getCurrentState() +  " : " + te.getStartTime().toShortString() + "-" + te.getEndTime().toShortString();
            }
        }

        return info + "\n";

    }


}
