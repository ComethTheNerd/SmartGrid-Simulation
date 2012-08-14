package policy;
import time.commands.HouseTimedCommand;
import time.HouseTimedEvent;
import infopackage.HouseParameters;
import time.Time;
/**Models the mundane cycle of working 5 days a week, 9-5,
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
 public class WeeklyWorkRoutine extends HouseTimedCommand{
				/**A typical working day*/
               HouseTimedEvent day;
				
				/**Wraps a typical working day, repeating it Monday-Friday to model
				* the working week
				*/
               public WeeklyWorkRoutine(HouseParameters houseParams){
                       super(houseParams);
                       day = new HouseTimedEvent(   houseParams,
                                                new Time(9), // start Monday, 9am
                                                new Time(17), // end Friday, 5pm
                                                new WorkingDay(houseParams), // code to execute
                                                new Time(0,1)); // run same time tomorrow
                }

               public void atStart(){
					// wrap checking of TimedEvent
                   day.checkEvent();


               }
               public void atDuring(){
			   // wrap checking of TimedEvent
                   day.checkEvent();
               }
               public void atEnd(){
			   // wrap checking of TimedEvent
                   day.checkEvent();
               }
            }
