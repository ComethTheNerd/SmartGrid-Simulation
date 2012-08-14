package API;
import time.commands.HouseTimedCommand;
import infopackage.HouseParameters;

/**A sample HouseTimedCommand skeleton for a programmer to use as a reference
 * when programming with the API framework
 *
 * @author Darius Hodaei
 */
public class TemplateHouseTimedCommand extends HouseTimedCommand{

    public TemplateHouseTimedCommand(HouseParameters houseParams){
        super(houseParams);
    }

    public void atStart(){}

    public void atDuring(){}

    public void atEnd(){}

}
