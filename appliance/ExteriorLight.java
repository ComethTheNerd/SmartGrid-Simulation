package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class ExteriorLight extends Appliance{
    public ExteriorLight () {
    this.canShed = true;
        this.minUsage = 300;
        this.maxUsage = 400;
        this.isOn = false;
        this.duration = 2;
        this.earliestUsageStart = 20;
        this.latestUsageStart = 23;
         this.type = ApplianceType.UTILITIES;
          this.minInstances =1;
        this.maxInstances = 5;
                 generateUsageHours();

    }

}
