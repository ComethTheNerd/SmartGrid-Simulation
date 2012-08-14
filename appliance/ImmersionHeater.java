package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class ImmersionHeater extends  Appliance{
    public ImmersionHeater(){
     this.canShed = true;
        this.minUsage = 3000;
        this.maxUsage = 3500;
        this.isOn = false;
        this.duration = 2;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 10;
        this.type = ApplianceType.UTILITIES;
        this.minInstances =1;
        this.maxInstances = 2;
                generateUsageHours();
    }
}
