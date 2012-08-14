package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Router extends Appliance{
    public Router(){
        this.canShed = true;
        this.minUsage = 10;
        this.maxUsage = 15;
        this.isOn = false;
        this.duration = 24;
        this.earliestUsageStart = 3;
        this.latestUsageStart = 23;
         this.type = ApplianceType.LUXURY_ITEMS;
       this.minInstances = 0;
        this.maxInstances = 2;
                 generateUsageHours();
        }

}

