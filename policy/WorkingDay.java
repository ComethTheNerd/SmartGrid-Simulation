package policy;
import time.commands.HouseTimedCommand;
import infopackage.HouseParameters;

/**Models a typical 9-5 working day where everyone is out of the house
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class WorkingDay extends HouseTimedCommand{
				// the original number of occupants in the house
                int occupants;
				// a handle to the occupants header of the HouseParameters
                HouseParameters.Header occs = HouseParameters.Header.NumberOfOccupants;
				
               public WorkingDay(HouseParameters houseParams){
                   super(houseParams);
               }
                public void atStart(){
					// record the original number of occupants before work
                    occupants = houseParams.getValue(occs);
                    houseParams.setValue(occs, 0); // all at work
           
               }
               public void atDuring(){
					// nothing here, they're at work!
               }
               public void atEnd(){
					// restore the occupants count
                    houseParams.setValue(occs, occupants); // back from work
               }
           }
