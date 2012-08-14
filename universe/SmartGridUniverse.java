package universe;
import appliance.core.Appliance;
import household.HouseHold;
import appliance.*;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import time.Time;
import aggregator.*;
import java.util.Random;
import utils.MessageStream;
import utils.Config;

/**Represents the SmartGrid universe in which the model exists,
 * supervising the whole run-time of the simulation at the core
 * of the architecture.
 *
 * @author Darius Hodaei, Jake Baker, Ogor Umukoro
 */
public class SmartGridUniverse implements IObservable {

    /**the time to sleep during each iteration of the main loop*/
    private int granularity;
    /**the pause state of the simulation*/
    private boolean isPaused = true;
    /**a flag for whether to exit the simulation*/
    private boolean exit;
    /**a handle to the output channel*/
    private UserInput input;
    /**a collection of objects to notify during each tick*/
    private ArrayList<IObserver> subscribers;
    /**all aggregators in the simulation universe*/
    private ArrayList<Aggregator> aggregators;
    /**all of the Appliance classes*/
    private Set<Class<? extends Appliance>> appliances;
    /**the number of aggregators in the simulation upon start up*/
    private int aggs = -1;
    /**A static instance that represents the current universe time
     */
    private Time universeTime = Time.ZERO_VALUE; // start from zero
    /**Characteristic of the Singleton Pattern
     */
    private static SmartGridUniverse instance;

    /**Characteristic of the Singleton Pattern
     */
    private SmartGridUniverse() {
    }

    /**Ensures only one instance of the Smart Grid Universe is created
     * during the life time of the simulation, and that all objects are communicating
     * with the same instance
     *
     * @return the single same instance of SmartGridUniverse each time
     */
    public static SmartGridUniverse getInstance() {
        if (instance == null) {
            instance = new SmartGridUniverse();
        }
        return instance;
    }

    /**Load all of the Appliance class implementations in to the system
     */
    private void loadApplianceTypes() {
        appliances = new HashSet<Class<? extends Appliance>>();

        appliances.add(AirConditioning.class);
        appliances.add(AudioEquipment.class);
        appliances.add(Barbeque.class);
        appliances.add(Blender.class);
        appliances.add(BathroomHeater.class);
        appliances.add(CoffeeMaker.class);
        appliances.add(CordlessPhone.class);
        appliances.add(Grill.class);
        appliances.add(HairDryer.class);
        appliances.add(Router.class);
        appliances.add(Computer.class);
        appliances.add(Hob.class);
        appliances.add(Microwave.class);
        appliances.add(Oven.class);
        appliances.add(DvdPlayer.class);
        appliances.add(DishWasher.class);
        appliances.add(ExteriorLight.class);
        appliances.add(Freezer.class);
        appliances.add(GamesConsole.class);
        appliances.add(Heating.class);
        appliances.add(ImmersionHeater.class);
        appliances.add(InteriorLight.class);
        appliances.add(Iron.class);
        appliances.add(Kettle.class);
        appliances.add(Laptop.class);
        appliances.add(Refrigerator.class);
        appliances.add(SecuritySensors.class);
        appliances.add(StorageHeater.class);
        appliances.add(PowerShower.class);
        appliances.add(TV.class);
        appliances.add(Toaster.class);
        appliances.add(VacuumCleaner.class);
        appliances.add(WashingMachine.class);
        appliances.add(SwimmingPoolHeating.class);
    }

    /**Returns all of the Appliance classes available in the system
     *
     * @return all of the Appliance classes available in the system
     */
    public Set<Class<? extends Appliance>> getAllApplianceTypes() {
        return appliances;
    }

    /**Returns all of the aggregators currently in the simulation
     *
     * @return all of the aggregators currently in the simulation
     */
    public ArrayList<Aggregator> getAggregators() {
        return aggregators;
    }

    /**Adds an aggregator to the simulation
     *
     * @param a the aggregator to add to the simulation
     */
    protected void addAggregator(Aggregator a) {
        // set up the aggregator
        setUpAggregator(a);
        ++aggs;
        // log the event
        MessageStream.getInstance().submitMessage("User added new Aggregator : " + a, MessageStream.PRIORITY.HIGH);
    }
    /**Removes an aggregator from the simulation
     *
     * @param a the aggregator to remove from the simulation
     */
    protected void removeAggregator(Aggregator a) {
        // we have to remove the houses listening out for notifications first
        for (HouseHold house : a.getAttachedHouses()) {
            removeObserver(house);
        }
        // then we can remove the aggregator
        removeObserver(a);
        aggregators.remove(a);
        // log the event
        MessageStream.getInstance().submitMessage("User removed " + a, MessageStream.PRIORITY.HIGH);
    }

    /**Builds a SmartGrid network, ie creates instances of all necessary 
     * objects in the model, eg powerplant(s), aggregator(s) and households 
     * etc. The Main method will call this in order to set up the model. 
     * We can also call this from a reset button to run a fresh simulation.
     *
     * NOTE: This method stores the resulting objects in the relevant 
     * properties of this object, ie. The newly generated aggregators are 
     * stored in the aggregators ArrayList etc.
     */
    /**Resets the simulation
     * 
     * @deprecated not supported
     */
    @Deprecated
    public void reset() {

        setUp();
        // update the output channel
        input.update();
        // run the simulation
        runSimulation();
    }

    /**Initializes the simulation ready to run
     *
     * @param noOfAggregators the number of aggregators to begin with in the simulation
     */
    public void start(int noOfAggregators) {
        // load all the appliance classes
        loadApplianceTypes();
        aggs = noOfAggregators;
        // set up the simulation
        setUp();
        MessageStream.getInstance().submitMessage("User called START", MessageStream.PRIORITY.HIGH);
        runSimulation();
    }
    /**Sets up all of the necessary data structures needed to run a simulation
     *
     */
    private void setUp() {

        subscribers = new ArrayList<IObserver>();
        aggregators = new ArrayList<Aggregator>();
        granularity = Config.DEFAULT_INTERVAL;

        exit = false;
        isPaused = true;
        input = UserInput.getInstance();
        setUpAggregators(aggs);
        universeTime = Time.ZERO_VALUE; // start from zero

    }
    /**Takes care of intializing and setting up the required
     * number of aggregators
     *
     * @param n the number of aggregators to create
     */
    private void setUpAggregators(int n) {

        for (int x = 0; x < n; x++) {
            Aggregator agg = new Aggregator();

            setUpAggregator(agg);

        }

    }

    /**Sets up the given aggregator, and all of the data structures
     * and properties it relies on to function in the simulation
     *
     * @param agg the aggregator to set up
     */
    private void setUpAggregator(Aggregator agg) {
        Random r = new Random();
        ArrayList<HouseHold> houses = new ArrayList<HouseHold>();
        // randomly generate the number of houses attached to this aggregator
        int noOfHouses = Config.MIN_HOUSES_SEED + r.nextInt(Config.MAX_HOUSES_SEED - Config.MIN_HOUSES_SEED);
        // set up each of the houses
        for (int i = 0; i < noOfHouses; i++) {
            HouseHold h = new HouseHold(agg);
            addObserver(h);
            houses.add(h);
        }
        // attach the houses to the aggregator
        agg.addHouses(houses);
        // add the aggregator to the universe
        aggregators.add(agg);
    }

    /**This is the loop that runs the simulation. It should contain all
     * the method calls of things that need to be done upon each system
     * tick, such as updating subscribers. Every iteration of the loop
     * will be an advance in universe time.
     */
    public void runSimulation() {

        while (!exit) {
            ThreadSafeControl.execute();
            // execute thread safe modifications to simulation
            input.update();

            MessageStream.getInstance().updateMessage();
            // if paused flag is set then we just loop again
            if (isPaused) {
                continue;
            }
            // notify observers that a system tick has taken place
            notifyObservers();
            universeTime = universeTime.advanceTime(new Time(1, 0, 0, 0, 0));
            // System.out.println(universeTime.toShortString());

            try {
                Thread.sleep(granularity);
            } catch (Exception e) {
            }

        }
        MessageStream.getInstance().submitMessage("User called EXIT", MessageStream.PRIORITY.HIGH);
        MessageStream.getInstance().flush();
        // exit the program
        System.exit(0);
    }

    /**Set the exit flag for the simulation loop
     *
     * @param exit true if wanting to exit the simulation, false otherwise
     */
    protected void setExit(boolean exit) {
        this.exit = exit;

    }

    /**Returns the current pause state of the simulation
     *
     * @return true if the simulation is currently paused, false if not
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**Sets whether to pause/unpause the simulaton
     *
     * @param pause true if wanting to pause the simulation, false if wanting to unpause
     */
    protected void setPause(boolean pause) {
        isPaused = pause;
    }

    /**Adjusts the timeGranularity for the simulation, used to make it 
     * slower or faster essentially
     * 
     * @param granularity The time between system clock ticks
     */
    public void changeTimeScale(int granularity) {
        this.granularity = granularity;
    }

    /**Returns the current time that the main loop sleeps for upon
     * each iteration
     *
     * @return the time that the main loop sleeps for upon each iteration
     */
    public int getGranularity() {
        return granularity;

    }

    /**Adds the given IObserver to the list of objects interested in receiving
     * updates from this object.
     *
     * @param observer The observer to add to the subscribers list
     */
    public void addObserver(IObserver observer) {
        // if not already a subscriber in the list
        if (!subscribers.contains(observer)) {
            subscribers.add(observer); // add it
        }

    }

    /**Removes a given IObserver from its list of subscribers, if it exists
     * within the collection.
     *
     * @param observer The observer to remove from the subscribers list
     */
    public void removeObserver(IObserver observer) {
        // if the list contains the observer
        if (subscribers.contains(observer)) {
            // remove it
            subscribers.remove(observer);
        }
    }
    /**Sends out the same notification information to each observer
     * currently in the list
     */
    public void notifyObservers() {
        // update all observers
        for (int x = 0; x < subscribers.size(); x++) {
            subscribers.get(x).update();
        }
    }

    /**Returns the Time object held by this class that
     * represents the current universe time
     *
     * @return The Universe time object
     */
    public Time getUniverseTime() {
        if (universeTime == null) {
            universeTime = Time.ZERO_VALUE;
        }

        //return Time.clone(universeTime);
        return universeTime.clone();
    }
}