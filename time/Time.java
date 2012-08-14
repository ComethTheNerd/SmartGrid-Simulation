package time;

import java.util.Random;
import universe.SmartGridUniverse;

/** Representation of the concept of time in the system. This allows us to schedule events relative
 * to the current Universe Time (useful for scheduling recurring events based on an interval or forecasting),
 * as well as explicitly model the linear advance of time in the system by a given granularity.
 *
 * @author Darius Hodaei, Jake Baker
 */
public class Time {

    
    /**the hour value of this Time instance*/
    private int hour;
    /**the day value of this Time instance*/
    private int day;
    /**the week value of this Time instance*/
    private int week;
    /**the month value of this Time instance*/
    private int month;
    /**the year value of this Time instance*/
    private int year;
    /**the season value of this Time instance*/
    private SeasonEnum season;
    /* NOTE: The Season does not need to be inherently numeric as it is
    inferred from the other time fields, specifically 'month'.*/
    private static final int 
            /**hours in a day*/
            hoursInDay = 24,
            /**days in a week*/
            numberOfDays = 7,
            /**weeks in a month*/
            numberOfWeeks = 4,
            /**months in a year*/
            numberOfMonths = 12,
            /**hours in a week*/
            hoursInWeek = hoursInDay * numberOfDays,
            /**hours in a month*/
            hoursInMonth = hoursInWeek * numberOfWeeks,
            /**hours in a year*/
            hoursInYear = hoursInMonth * numberOfMonths;

    /**a handle to the Smart Grid Universe singleton object*/
    private static SmartGridUniverse universe = SmartGridUniverse.getInstance();
    /**Represents the concept of 0 expressed as a Time which can then be used
     * as the basis of calculations between two Time instances
     */
    public static final Time ZERO_VALUE = new Time(0, 0, 0, 0, 0);

    /*
    NOTE: We use Constructor overloading so that the user can construct a valid Time object
     * by supplying a varying number of fields. This makes instantiation flexible, and in each
     * case where all the arguments are not given, we make use of a call to the full constructor.
     * This means we keep the instantiation logic in one place.
     */
    /**A Time object is constructed using the given hour, where the missing fields
     * are filled in implicitly using the current state of the Universe Time
     * NOTE: The season is deduced implicitly from the state of the Time instance.
     *
     * @param hour The hour to set this Time instance to
     */
    public Time(int hour) {
        // Fill in the missing arguments using the current state of the Universe Time
        this(hour, universe.getUniverseTime().day, universe.getUniverseTime().week,
                universe.getUniverseTime().month, universe.getUniverseTime().year);
    }

    /**A Time object is constructed using the given hour and day, where the missing fields
     * are filled in implicitly using the current state of the Universe Time
     * NOTE: The season is deduced implicitly from the state of the Time instance.
     *
     * @param hour The hour to set this Time instance to
     * @param day The day to set this Time instance to
     */
    public Time(int hour, int day) {
        // Fill in the missing arguments using the current state of the Universe Time
        this(hour, day, universe.getUniverseTime().week, universe.getUniverseTime().month,
                universe.getUniverseTime().year);
    }

    /**A Time object is constructed using the given hour, day and week of the month, where the
     * missing fields are filled in implicitly using the current state of the Universe Time
     * NOTE: The season is deduced implicitly from the state of the Time instance.
     *
     * @param hour The hour to set this Time instance to
     * @param day The day to set this Time instance to
     * @param week The week of the month to set this Time instance to
     */
    public Time(int hour, int day, int week) {
        // Fill in the missing arguments using the current state of the Universe Time
        this(hour, day, week, universe.getUniverseTime().month, universe.getUniverseTime().year);
    }

    /**A Time object is constructed using the given hour, day, week of the month and month, where the
     * missing fields are filled in implicitly using the current state of the Universe Time
     * NOTE: The season is deduced implicitly from the state of the Time instance.
     *
     * @param hour The hour to set this Time instance to
     * @param day The day to set this Time instance to
     * @param week The week of the month to set this Time instance to
     * @param month The month to set this Time instance to
     */
    public Time(int hour, int day, int week, int month) {
        // Fill in the missing argument using the current state of the Universe Time
        this(hour, day, week, month, universe.getUniverseTime().year);
    }

    /**A Time object is explicitly constructed using the given hour, day, week of the month, month and year.
     * NOTE: The season is deduced implicitly from the state of the Time instance.
     *
     * @param hour The hour to set this Time instance to
     * @param day The day to set this Time instance to
     * @param week The week of the month to set this Time instance to
     * @param month The month to set this Time instance to
     */
    public Time(int hour, int day, int week, int month, int year) {

        // We use modulus to ensure to validate the values
        this.hour = hour % hoursInDay;
        this.day = day % numberOfDays;
        this.week = week % numberOfWeeks;
        this.month = month % numberOfMonths;
        this.year = (year < 0) ? 0 : year; // ensure 0 is lowest year value
        // season is deduced from the current month
        this.season = lookUpSeason(this.month);
    }

    /**Looks up the correct month enum that applies to the given
     * month number of a year, eg n=0 returns SeasonEnum.Winter
     *
     * @param n The month number to look up the season for
     */
    public static SeasonEnum lookUpSeason(int n) {
        // Ensure we are working with a valid month
        int safeMonth = n % numberOfMonths;
        // December - February is Winter
        if (safeMonth > 10 || safeMonth < 2) {
            return SeasonEnum.Winter;
        }
        // March - May is Spring
        if (safeMonth > 1 && safeMonth < 5) {
            return SeasonEnum.Spring;
        }
        // June - August is Summer
        if (safeMonth > 4 && safeMonth < 8) {
            return SeasonEnum.Summer;
        }
        // September - November is Autumn
        if (safeMonth > 7 && safeMonth < 11) {
            return SeasonEnum.Autumn;
        }
        return null;
    }

    /**Returns a nice name for the given hour, eg n=20 returns "8pm"
     *
     * @param n The hour to convert to a nice name
     */
    public static String hourNiceName(int n) {
        // Ensure we are working with a valid hour
        int safeHour = n % hoursInDay;
        // Add the correct suffix
        String suffix = (safeHour > 11) ? "pm" : "am";
        if (safeHour == 0) {
            suffix = "am";
        }
        // Mod 12 so that we can use am or pm with the number
        safeHour %= 12;
        // change 0am / 0pm to 12am / 12pm
        if (safeHour == 0) {
            safeHour = 12;
        }

        return safeHour + suffix;

    }

    /**Returns a nice name for the given day, eg n=0 returns "Monday"
     *
     * @param n The day to convert to a nice name
     */
    public static String dayNiceName(int n) {
        // Ensure we are working with a valid day
        int safeDay = n % numberOfDays;
        switch (safeDay) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
            default:
                return null;
        }
    }

    /**Returns a nice name for the given month, eg n=0 returns "January"
     *
     * @param n The month to convert to a nice name
     */
    public static String monthNiceName(int n) {
        int safeMonth = n % numberOfMonths;
        switch (safeMonth) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return null;
        }
    }

    /**Lightweight enum to quantify seasons in the system
     */
    public enum SeasonEnum {

        Winter,
        Spring,
        Summer,
        Autumn;
    }

    /**Returns either -1, 0 or 1 depending on whether this Time represents
     * a time that is before, equal to, or after the given Time object.
     *
     * @param time The Time object to compare this Time object with
     * @return -1 iff this Time is BEFORE the given Time, 0 iff the Times are EQUAL, or 1 iff this Time is AFTER the given Time
     */
    public int compare(Time time) {

        if (this.year < time.year) {
            return -1;
        }
        if (this.year > time.year) {
            return 1;
        }
        // by this point, this.year == time.year...
        if (this.month < time.month) {
            return -1;
        }
        if (this.month > time.month) {
            return 1;
        }
        // by this point, this.month == time.month...
        if (this.week < time.week) {
            return -1;
        }
        if (this.week > time.week) {
            return 1;
        }
        // by this point, this.week == time.week...
        if (this.day < time.day) {
            return -1;
        }
        if (this.day > time.day) {
            return 1;
        }
        // by this point, this.day == time.day...
        if (this.hour < time.hour) {
            return -1;
        }
        if (this.hour > time.hour) {
            return 1;
        }
        // by this point they must be both equal...
        return 0;

    }

    /**Allows us to test whether this Time object and another Time object
     * represent the same moment in time.
     *
     * @param time The Time object to perform an equality test with
     * @return True if the two Time representations are the same time
     */
    public boolean equals(Time time) {
        // NOTE: Early returns for efficiency
        if (this.season != time.season) {
            return false;
        }
        if (this.year != time.year) {
            return false;
        }
        if (this.month != time.month) {
            return false;
        }
        if (this.week != time.week) {
            return false;
        }
        if (this.day != time.day) {
            return false;
        }
        if (this.hour != time.hour) {
            return false;
        }
        // If we get here then the same Time is represented by both Time instances
        return true;
    }

    @Override
    /**Returns a clone of this Time instance
     * @return a clone of this Time instance
     */
    public Time clone() {

        return new Time(this.getCurrentHour(),
                this.getCurrentDay(),
                this.getCurrentWeek(),
                this.getCurrentMonth(),
                this.getCurrentYear());
    }

    /**Generates a random time between two fixed points
     *
     * @param lowerBound the lower Time point
     * @param upperBound the upper Time point
     * @return a random time between the upper and lower bounds
     */
    public static Time random(Time lowerBound, Time upperBound) {
        // if the lower bound is actually later than the upper bound
        if (lowerBound.compare(upperBound) > 0) {
            // swap the parameters round silently
            return random(upperBound, lowerBound);
        }
        // turn both bounds in to a integer representation of their total hours
        int lowHours = Time.toHours(lowerBound), upperHours = Time.toHours(upperBound);

        Random r = new Random();

        // calculate a random point between the two
        int hours = lowHours + r.nextInt(upperHours - lowHours);

        return Time.hoursToTime(hours);
    }

    /**Converts a Time instance to the total number of hours that it
     * represents
     *
     * @param t the Time instance to convert to hours
     * @return the count of the total hours represented by the give Time instance
     */
    public static int toHours(Time t) {

        int hours = t.getCurrentHour();
        // convert and add the represented days to hours
        hours += t.getCurrentDay() * hoursInDay;
        // convert and add the represented weeks to hours
        hours += t.getCurrentWeek() * hoursInWeek;
        // convert and add the represented months to hours
        hours += t.getCurrentMonth() * hoursInMonth;
        // convert and add the represented years to hours
        hours += t.getCurrentYear() * hoursInYear;

        return hours;
    }
    /**Converts the given hours to a Time instance that represents
     * the equivalent time
     *
     * @param hours the hours to convert to a Time instance
     * @return a Time instance representing the same time as the number of hours
     */
    public static Time hoursToTime(int hours) {
        // return zero value if number of hours is negative
        if (hours <= 0) {
            return ZERO_VALUE;
        }

        // Greedy algorithm to convert to the Time instance

        // first we take in to account how many years are represented...
        int years = hours / hoursInYear;

        hours %= hoursInYear;

        // then how many months are represented...
        int months = hours / hoursInMonth;

        hours %= hoursInMonth;

        // then how many weeks...
        int weeks = hours / hoursInWeek;

        hours %= hoursInWeek;

        // then how many days...
        int days = hours / hoursInDay;

        // and finally what is left is the time of day
        hours %= hoursInDay;

        return new Time(hours, days, weeks, months, years);

    }

    /**Tests whether the given time is equivalent to the notion of
     * a zero Time object, essentially test if all fields have 0 values
     *
     * @param time The Time instance to check is zero or not
     * @return True if the given Time object is zero, ie. all fields have 0 values
     */
    public static boolean isZero(Time time) {

        if (time.equals(ZERO_VALUE)) {
            return true;
        }
        return false;
    }

    /**Adds the given Time instance to this Time instance
     *
     * @param toAdd The Time instance to add
     */
    public Time advanceTime(Time toAdd) {
        /* Changes to one field can cause a cascade of changes
         * to 'parent' fields, eg. hour = 23 + 5 = 4am (of the NEXT day).
         */

        int hours = Time.toHours(this) + Time.toHours(toAdd);

        Time result = Time.hoursToTime(hours);

        return result;

    }

    /**Subtracts the given Time instance from this Time instance
     *
     * @param toSubtract The Time instance to subtract
     */
    public Time subtractTime(Time toSubtract) {
        /* Changes to one field can cause a cascade of changes
         * to 'parent' fields, eg. hour = 2 - 7 = 7pm (of the PREVIOUS day).
         */

        int hours = Time.toHours(this) - Time.toHours(toSubtract);

        Time result = Time.hoursToTime(hours);
        return result;

    }

    /**Returns the current hour of this Time instance
     *
     * @return The current hour held by this Time object
     */
    public int getCurrentHour() {
        return this.hour;
    }

    /**Returns the current day of this Time instance
     *
     * @return The current day held by this Time object
     */
    public int getCurrentDay() {
        return this.day;
    }

    /**Returns the current week of this Time instance
     *
     * @return The current week held by this Time object
     */
    public int getCurrentWeek() {
        return this.week;
    }

    /**Returns the current month of this Time instance
     *
     * @return The current month held by this Time object
     */
    public int getCurrentMonth() {
        return this.month;
    }

    /**Returns the current year of this Time instance
     *
     * @return The current year held by this Time object
     */
    public int getCurrentYear() {
        return this.year;
    }

    /**Returns the current season of this Time instance
     *
     * @return The current season held by this Time object
     */
    public SeasonEnum getCurrentSeason() {
        return this.season;
    }

    /**An abbreviated String representation of this Time instance
     *
     * @return An abbreviate String representation of this Time instance
     */
    public String toShortString() {
        return hourNiceName(hour)
                + ", " + dayNiceName(day).substring(0, 3)
                + ", Wk." + week
                + ", " + monthNiceName(month).substring(0, 3)
                + ", Yr." + year;
    }

    @Override
    public String toString() {
        return hourNiceName(hour)
                + " on " + dayNiceName(day)
                + ", week " + week
                + " of " + monthNiceName(month)
                + " (" + season + "-time), in year "
                + year + ".";

    }

    /**Returns a verbose breakdown of the values contained within this Time instance
     *
     * @return A verbose String representation of this Time instance
     */
    public String verbose() {
        return "Hour: " + this.hour + "\n"
                + "Day: " + this.day + "\n"
                + "Week: " + this.week + "\n"
                + "Month: " + this.month + "\n"
                + "Year: " + this.year + "\n"
                + "Season: " + this.season;
    }
}