package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class SwimmingPoolHeating extends Appliance{
    public SwimmingPoolHeating(){
    this.canShed = true;
        this.minUsage = 4500;
        this.maxUsage = 5000;
        this.isOn = false;
        this.duration = 6;
        this.earliestUsageStart = 8;
        this.latestUsageStart = 22;
         this.type = ApplianceType.LUXURY_ITEMS;
         this.minInstances =0;
        this.maxInstances = 2;
                 generateUsageHours();
        }

}
