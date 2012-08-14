package utils;

import universe.UserInput;
import java.io.File;

/**A static configuration file that allows for the tuning of simulation
 * parameters at will without needing to hunt through all the code to change
 * system behaviour
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class Config {

    /* UNIVERSE PARAMETERS */
    /**the minimum number of houses generated for an aggregator*/
    public static final int MIN_HOUSES_SEED = 340;
    /**the maximum number of houses generated for an aggregator*/
    public static final int MAX_HOUSES_SEED = 360;
    /**the default time for the main loop to sleep upon each iteration*/
    public static final int DEFAULT_INTERVAL = 200;
    /**the default number of aggregators to begin a simulation with*/
    public static final int DEFAULT_NO_OF_AGGS = 5;
    /**the maximum number of houses that can be attached to an aggregator*/
    public static final int MAX_HOUSES_PER_AGG = 2000;

    /* HOUSEHOLD PARAMETERS */
    /**the maximum number of appliances per house*/
    public static final int APPS_MAX_PER_HOUSE = 10;
    /**the maximum number of policy components per house*/
    public static final int POLS_MAX_PER_HOUSE = 5;
    /**the minimum value to use for a house's green power state threshold*/
    public static final int GREEN_MIN = 1000;
    /**the minimum value to use for a house's amber power state threshold*/
    public static final int AMBER_MIN = 3600;
    /**the minimum value to use for a house's red power state threshold*/
    public static final int RED_MIN = 5600;

    /* AGGREGATOR PARAMETERS*/
    /**the number of ticks between the aggregator checking its load*/
    public static final int FETCH_INTERVAL = 10;
    /**how many hours in to the future to check the load for*/
    public static final int FORECAST_TIME = 5;

    /* MESSAGE STREAM PARAMETERS */
    /**how many ticks of the system to show each message for*/
    public static final int MESSAGE_DURATION = 4;
    /**how many old messages to buffer before writing to a file*/
    public static final int OLD_MESSAGES_LIMIT = 100;


    /**the name to use for the output text file*/
    public static final String FILE_NAME = "OUTPUT";
    /**the path to the output file*/
    public static final String OUTPUT_FILE_PATH = new File("").getAbsolutePath() + "\\" + FILE_NAME + ".txt";

    /* DEFAULT GUI OUTPUT MODE */
    /**the default output channel to use upon start up*/
    public static final UserInput.OUTPUT_MODE OUTPUT = UserInput.OUTPUT_MODE.Gui;
    
    /**how many houses can be attached to an aggregator in Overview mode
     * before switching to Single Aggregator view*/
    public static final int OVERVIEW_MAX_HOUSES_PER_AGG = 375;
    /**how many aggregators we can fit in Grid Overview Panel before switching to Single Aggregator view*/
    public static final int OVERVIEW_MAX_AGGS_ON_SCREEN = 5;
}
