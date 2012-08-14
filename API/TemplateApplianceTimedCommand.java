package API;
import time.commands.ApplianceTimedCommand;
import appliance.core.Appliance;

/**A sample ApplianceTimedCommand skeleton for a programmer to use as a reference
 * when programming with the API framework
 *
 * @author Darius Hodaei
 */
public class TemplateApplianceTimedCommand extends ApplianceTimedCommand{

    public TemplateApplianceTimedCommand(Appliance appliance){
        super(appliance);
    }
    
    public void atStart(){}

    public void atDuring(){}

    public void atEnd(){}

}
