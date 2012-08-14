package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class InteriorLight extends Appliance{
    public InteriorLight(){
    this.canShed = true;
        this.minUsage = 840;
        this.maxUsage = 900;
        this.isOn = false;
        this.duration = 6;
        this.earliestUsageStart = 16;
        this.latestUsageStart = 22;
         this.type = ApplianceType.UTILITIES;
         this.minInstances =1;
        this.maxInstances = 3;
                 generateUsageHours();

    }

}
