package appliance.core;

import java.util.ArrayList;
import time.Time;
import java.util.TreeSet;
import time.TimedEventComparator;
import java.util.Random;
import java.util.Iterator;
import time.ApplianceTimedEvent;
import time.commands.*;
import universe.SmartGridUniverse;
import gui.HouseChartPanel;


/**Represents the abstract concept of a powered appliance in the model.
 *
 * @author Tokoni Kemenanabo
 */
public abstract class Appliance {
    /**The number of runs to impose on certain usage hours*/
    protected final int MAX_RUNS = 10;
    /**A handle to the Smart Grid Universe object*/
    protected SmartGridUniverse universe;
    /**The state of the Appliance (true = "On", false = "Off")*/
    protected boolean isOn;
    /**The maximum power the Appliance can use*/
    protected int maxUsage;
    /**The minimum power the Appliance can use*/
    protected int minUsage;
     
    /**The maximum number of instances of this kind of appliance in a house*/
    protected int maxInstances = 2;
    /**The minimum number of instances of this kind of appliance in a house*/
    protected int minInstances = 1;
    /**How long typical usage of this appliance lasts for*/
    protected int duration;
    /**The energy consumed during a particular usage of this appliance*/
    private int energy;

    /**Persistent times of when the appliance is on*/
    protected TreeSet<ApplianceTimedEvent> usageHours;

    /**Whether or not this appliance can be turned off when a house needs to reduces usage*/
    protected boolean canShed;
    /**The maximum number of usage rules that can be generated for this appliance*/
    protected final int maxNumberOfRules = 10;
    /** Represents the type of the appliance (E.g White Goods, Luxury Items e.t.c.)*/
    protected ApplianceType type; 
     
    /**the earliest hour of the day that this appliance can be used*/
    protected int earliestUsageStart;
    /**the latest hour of the day that this appliance can be used*/
    protected int latestUsageStart;

    /**Creates a new Appliance instance
     *
     */
    public Appliance() {
        universe = SmartGridUniverse.getInstance();

    }


    /**Passes this appliance's most recent usage to the graphing facility
     * to visualize the consumption data for the user
     *
     * @param cp the graphing facility to pass the consumption data to
     */
    public void getMostRecentUsage(HouseChartPanel cp){
        // secure, not transmitted in the open
        cp.submitMostRecent(this, energy);
    }
    
    /**Used to calculate how much power the appliance will require at a
     * given time, based on whether it is in use, is at peak time
     * (apply the peak constant to calculation), etc.
     *
     * @param time The Time which we calculate the energy usage for
     * @return The amount of energy required for this appliance
     */
    public int energyRequired(Time time) {

        Random r = new Random();
        Time universeTime = universe.getUniverseTime();

        /*
        If we are basing our calculation on the current Universe time, the
        appliance will be on thanks to the TimedEvent handler so we do not need
        to check for matching ApplianceTimedEvents, we can just check isOn
         */
        if (time.equals(universeTime)) {
            return energy =  isOn ? minUsage + r.nextInt(maxUsage - minUsage) : 0;
           
        }
        else { // THIS IS A FORECAST

            //Check usage hours for a TimedEvent where the appliance is used at time
            //usage hours are ordered in acsending order of start times
            for (Iterator<ApplianceTimedEvent> i = usageHours.iterator(); i.hasNext();) {
                ApplianceTimedEvent event = i.next();

                //start time comes after time. Therefore for all succeeding TimedEvents
                //the start time would also come after time (ascending order)
                //energy remains at zero and breaks out of iteration
                if (event.getStartTime().compare(time) > 0) 
                   break;
                

                //time occurs within a TimedEvent
                //energy is updated to value and breaks out of iteration
                if (event.containsTime(time)) {
                    return energy =  minUsage + r.nextInt(maxUsage - minUsage);
                   
                }

            }
             return energy = 0; /*EXIT AT THIS POINT HAVING NOT FOUND ANY ENERGY USAGE AT THIS TIME FOR THIS APPLIANCE*/

        }
    }

    /**Generates a set of random usage hours for this appliance based on the properties
     * of this appliance
     *
     */
    protected final void generateUsageHours() {
        //Create a treeset to store generated usage hours
        //Provide set with Timed event comparator to sort usage hours
        //according to start times
        usageHours = new TreeSet<ApplianceTimedEvent>(new TimedEventComparator());

        Random r = new Random();

        //Determine how many rules should be generated
        int numberOfRules = r.nextInt(maxNumberOfRules);



        //Generate rules
        for (int x = 0; x < numberOfRules; x++) {
            int useDuration = 1 + r.nextInt(duration); // at least 1 hour long
            int day = r.nextInt(7);
            Time usageStartTime = Time.random(new Time(earliestUsageStart, day), new Time(latestUsageStart, day));


            Time currentTime = universe.getUniverseTime();
            /*BUG FIX by Darius... The generated time was in the past so we need to make sure
             the new TimedEvent is in the future. I have added this code to adjust it therefore.*/
            if(usageStartTime.compare(currentTime) < 0){

                usageStartTime = new Time(usageStartTime.getCurrentHour(), usageStartTime.getCurrentDay());
                if(usageStartTime.compare(currentTime) < 0) usageStartTime = usageStartTime.advanceTime(new Time(0,0,1,0,0));
            }

            Time usageEndTime = usageStartTime.clone();

            usageEndTime = usageEndTime.advanceTime(new Time(useDuration, 0, 0, 0, 0));

            //Create timed event for usage hour
            ApplianceTimedEvent ate = new ApplianceTimedEvent(this,
                    usageStartTime,
                    usageEndTime,
                    new ApplianceUseCommand(this),
                    new Time(0, 1 + r.nextInt(6)) // random day repeat
                    );
     
           //Randomly give usage hours a maximum number of runs value
           if(r.nextBoolean()) ate.setNumberOfRuns(r.nextInt(MAX_RUNS));
           //add to usage hours the generated appliance timed event
            usageHours.add(ate);
        }

        //If the number of rules to be generated was zero call method again
        if(usageHours.isEmpty()) generateUsageHours();

    }

    /**Returns the stored set of usageHours
     *
     * @return A TreeSet<ApplianceTimedEvents> containing the usageHours
     */
    public TreeSet<ApplianceTimedEvent> getUsageHours() {

        return usageHours;
    }

    /**Turns on the appliance
     *
     * @param arg - true for on, false for off
     */
    public void setOn(boolean arg) {
        isOn = arg;
    }

    /**Whether this appliance is on or not
     *
     * @return true if appliance is on
     */
    public boolean isApplianceOn() {

        return isOn;
    }

    /**Whether this appliance can be turned off during load balancing
     *
     * @return true if this appliance can be shed
     */
    public boolean isSheddable() {

        return canShed;
    }

    /**Returns The appliance type of this appliance
     *
     * @return The appliance type of his appliance
     */
    public ApplianceType getApplianceType() {
        return type;
    }

    /**Returns the maximum number of instances of this appliance that can be
     * created for a household
     *
     * @return the maximum number of instances of this appliance that can be
     * created for a household
     */
    public int getMaxInstances() {
        return maxInstances;
    }

     /**Returns the minimum number of instances of this appliance that can be
     * created for a household
     *
     * @return the minimum number of instances of this appliance that can be
     * created for a household
     */
    public int getMinInstances() {
        return minInstances;
    }

    /**Generates an ArrayList of ApplianceTimedEvents for this appliance to model a simple on-off event.
     * Using the given start time hour and day, lasting for the number of hours given in the span,
     * and repeating again after the given interval has elapsed.
     * Using the addDays argument, this method generates identical ApplianceTimedEvents
     * for additional days following the initial start day.
     *
     * @param startTime The hour of the day to start the event
     * @param day The initial day of the start time of the event
     * @param addDays The additional days for which this event is to be created
     * @param span The number of hours the event will last
     * @param interval The interval between repeats of this event - can be null for non-persistence
     * @return an ArrayList of the generated ApplianceTimedEvents
     */
    public ArrayList<ApplianceTimedEvent> generateRepeatedUsagePeriod(int startTime, int day, int addDays, int span, Time interval){

        ArrayList<ApplianceTimedEvent> hours = new ArrayList<ApplianceTimedEvent>();
        
        Time start = new Time(startTime, day);
        Time currentTime = universe.getUniverseTime();
        /*BUG FIX by Darius... The generated time was in the past so we need to make sure
        the new TimedEvent is in the future. I have added this code to adjust it therefore.*/
            if(start.compare(currentTime) < 0){

                start = new Time(start.getCurrentHour(), start.getCurrentDay());
                if(start.compare(currentTime) < 0) start = start.advanceTime(new Time(0,0,1,0,0));
         }

        //determine end time
        Time end = start.clone();

        end = end.advanceTime(new Time(span,0,0,0,0));
                
        //Create timed event to represent usage period
        ApplianceTimedEvent ate = new ApplianceTimedEvent(this,
                                                          start,
                                                          end,
                                                          new ApplianceUseCommand(this),
                                                          interval);
        //add event to list
        hours.add(ate); 
        
        // Adds an identical timed event to the Arraylist
        //for every additional day specified.
        //Adds for a maximum of 6 additional days --> 7 days in a week.
        for(int i = 0; i<addDays && i<6; i++){
            //advance start and end times by one day
            start = start.advanceTime(new Time(0,1,0,0,0));
            end = end.advanceTime(new Time(0,1,0,0,0));
            //create identical timed event with start/end times advanced by one day.
            ate = new ApplianceTimedEvent(this,
                                              start,
                                              end,
                                              new ApplianceUseCommand(this),
                                              interval);
            //add to list
            hours.add(ate); 
        }

        return hours;
    }


    /**Constructs an ApplianceTimedEvent for this appliance to model a simple on-off event
     * using the given start time hour, lasting for the number of hours given in the span,
     * and repeating again after the give interval has elapsed
     *
     * @param startTime The hour of the day to start the event
     * @param span The number of hours the event will last
     * @param interval The interval between repeats of this event - can be null for non-persistence
     * @return the fully formed ApplianceTimedEvent representing the generated usage hour
     */
    public ApplianceTimedEvent generateUsageHour(int startTime, int span, Time interval) {

        Time start = new Time(startTime);
        Time currentTime = universe.getUniverseTime();
        /*BUG FIX by Darius... The generated time was in the past so we need to make sure
        the new TimedEvent is in the future. I have added this code to adjust it therefore.*/
            if(start.compare(currentTime) < 0){

                start = new Time(start.getCurrentHour(), start.getCurrentDay());
                if(start.compare(currentTime) < 0) start = start.advanceTime(new Time(0,0,1,0,0));
         }
         //Determine end time
         Time end = start.clone();

        end = end.advanceTime(new Time(span, 0, 0, 0, 0));

        //Create timed event to represent usage period
        ApplianceTimedEvent ate = new ApplianceTimedEvent(this,
                start,
                end,
                new ApplianceUseCommand(this),
                interval);

        //return timed event
        return ate;


    }

    /**Constructs an ApplianceTimedEvent for this appliance to model a simple on-off event
     * using the given start time hour, lasting for the number of hours given in the span
     *
     * @param startTime The hour of the day to start the event
     * @param span The number of hours the event will last
     * @return A fully constructed ApplianceTimedEvent for this appliance
     */
    public ApplianceTimedEvent generateUsageHour(int startTime, int span) {

        return generateUsageHour(startTime, span, null);

    }

    /**Constructs an ApplianceTimedEvent for this appliance to model a simple on-off event
     * using the appliance's default properties
     *
     * @return A fully constructed ApplianceTimedEvent for this appliance
     */
    public ApplianceTimedEvent generateUsageHour() {
        Random r = new Random();
        return generateUsageHour(earliestUsageStart + r.nextInt(latestUsageStart - earliestUsageStart),
                duration);

    }

    /**String representation of Appliance instance
     *
     * @return String representation of Appliance instance
     */
    @Override
    public String toString(){

        return this.getClass().getSimpleName();
    }
}
