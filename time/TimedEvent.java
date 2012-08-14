package time;

import time.commands.ITimedCommand;
import universe.SmartGridUniverse;

/**Flexible class that allows us to model events that occur at a
 * specific time, for a specific duration, such as, “turn off television
 * for two hours”
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class TimedEvent {

    /**whether this TimedEvent has finished or not*/
    protected boolean hasFinished = false;
    /**whether the number of times this TimedEvent runs is set*/
    protected boolean hasRunLimit = false;
    /**whether this TimedEvent repeats upon completion*/
    protected boolean isPersistent = false;

    /**Constants used to signify the state this TimedEvent is in
     *
     */
    public enum RunState {

        Waiting, Running, Finished;
    };
    /**The current state of this TimedEvent*/
    protected RunState currentState;
    /**The number of times that this TimedEvent will repeat*/
    protected int timesToRun = 0;
    /**the start time of this TimedEvent*/
    protected Time start;
    /**the end time of this TimedEvent*/
    protected Time end;
    /**the interval between runs of this TimedEvent if it is persistent*/
    protected Time interval;
    /**how long this TimedEvent runs for*/
    protected Time duration;
    /**The command to execute during this TimedEvent*/
    protected ITimedCommand command;

    public TimedEvent(Time startTime, Time endTime, ITimedCommand command, Time intervalBetweenRuns) {

        // store start and end times
        start = startTime;
        end = endTime;

        // store the execute command structure
        this.command = command;

        // set the current state to Waiting to execute
        currentState = RunState.Waiting;

        // If the end time is before or equal to the start time, do a quick run and finish
        if (end.compare(start) <= 0) {
            command.atStart();
            currentState = RunState.Running;
            command.atDuring();
            terminate();
            return;
        }

        // calculate duration
        duration = end.clone();
        duration = duration.subtractTime(start);

        // If the user has supplied an interval then this TimedEvent is persistent
        if (intervalBetweenRuns != null) {
            isPersistent = true;
            interval = intervalBetweenRuns;
        }


    }

    /**Limits the number of runs this TimedEvent repeats,
     * if it is persistent
     *
     * @param runs the number of times this TimedEvent will repeat, if persistent
     */
    public final void setNumberOfRuns(int runs) {
        // Ignore nonsensical values
        if (runs <= 0) {
            return;
        }

        // Otherwise set the run limit
        hasRunLimit = true;
        timesToRun = runs;
    }

    /**Tells this TimedEvent to check whether the universe time requires
     * it to execute, or end , or keep running etc.
     */
    public final void checkEvent() {

        // Execution is based on the Universe time
        Time universeTime = SmartGridUniverse.getInstance().getUniverseTime();

        // Execution varies depending on the current state of this TimedEvent
        switch (currentState) {
            /*****************************************************************************/
            case Waiting:

                // If NOW is the start time, or we've gone past the intended start time
                if (universeTime.equals(start)) {
                    // Execute the start event
                    command.atStart();
                    // Change the state to Running
                    currentState = RunState.Running;

                }
                break;
            /*****************************************************************************/
            case Running:
                // Carry out the atDuring() execution
                command.atDuring();


                // If it is time to finish
                if (universeTime.equals(end)) {
                    // Set the state to end
                    currentState = RunState.Finished;

                    /* Recursive call so we don't miss anything supposed to
                     * happen when we get to the end time, which is NOW
                     */
                    checkEvent();
                }
                break;
            /*****************************************************************************/
            case Finished:
                // If we have already finished but are yet to GC this event
                if (hasFinished) {
                    return;
                }

                // Run the atEnd() clean up method
                command.atEnd();

                // If this TimedEvent is persistent
                if (isPersistent) {
                    // If this TimedEvent has a run limit
                    if (hasRunLimit) {
                        // If we have reached the limit
                        if (--timesToRun == 0) {
                            // Finish the event
                            hasFinished = true;
                            return;
                        }
                    }

                    // Set state to waiting to run again
                    currentState = RunState.Waiting;
                    // If the interval is less than  or equal to the duration of the event
                    if (interval.compare(duration) <= 0) {
                        // The next run of this event will start again after straight after it finishes
                        start = end.clone();
                        // Calculate new end time
                        end = end.advanceTime(duration);

                        // need to have a recursive call because otherwise we will miss starting this event
                        // as it's next start time is set to the end time of the current run, ie. now!
                        checkEvent();
                    } else {
                        // The next run of this event will start after the interval
                        start = start.advanceTime(interval);
                        // Calculate new end time
                        end = start.clone();
                        end = end.advanceTime(duration);

                    }

                } else {
                    // If not persistent, signal as finished
                    hasFinished = true;

                }
                break;
            /*****************************************************************************/
        }

    }

    /**Check to see whether the TimedEvent has completed, and thus can 
     * be removed.
     * 
     * @return True if this TimedEvent has finished running
     */
    public final boolean hasFinished() {
        return hasFinished;
    }

    /**Returns the start time of this TimedEvent
     *
     * @return the start time of the TimedEvent
     */
    public final Time getStartTime() {
        return start;
    }

    /**Returns the end time of this TimedEvent
     *
     * @return the end time of the TimedEvent
     */
    public final Time getEndTime() {
        return end;
    }

    /**Returns the duration of this TimedEvent
     *
     * @return the duration of the TimedEvent
     */
    public final Time getDuration() {
        return duration;
    }

    /**Check whether this TimedEvent is set to persist, ie. reset after 
     * completion and run again
     * 
     * @return True if this TimedEvent will reset and run again upon completion
     */
    public final boolean isPersistent() {/*TODO: Implementation*/
        return isPersistent;
    }

    /**Returns the current state of this TimedEvent
     *
     * @return the current state of the TimedEvent
     */
    public final RunState getCurrentState() {
        return currentState;
    }

    /**Sets whether this TimedEvent is persistent and will reset upon
     * completion, and be scheduled to run again
     *
     * @param persist True if this TimedEvent should persist
     */
    public final void setPersistence(boolean persist) {
        if (hasFinished) {
            throw new IllegalStateException("Attempting to set persistence for when event has already finished.");
        } else {
            isPersistent = true;
        }
    }

    /**A method to check whether the time represented by the given Time
     * instance falls within the running time of this TimedEvent (added by Tokoni)
     *
     * @param time The time to test for being within this TimedEvent
     * @return true iff the given time is within this TimedEvent, false otherwise
     *
     */
    public boolean containsTime(Time time) {

        if (start.compare(time) <= 0
                && end.compare(time) >= 0) {
            return true;
        }

        return false;

    }

    /**Allows us to set this TimedEvent to finished, regardless of how
     * long it has to run
     */
    public final void terminate() {
        command.atEnd();

        isPersistent = false;
        hasFinished = true;

        currentState = RunState.Finished;

    }
}
