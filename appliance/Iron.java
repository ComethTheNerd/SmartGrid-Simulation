package appliance;

import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Iron extends FlexibleUsageAppliance {

    public Iron() {
        this.canShed = true;
        this.minUsage = 2000;
        this.maxUsage = 2500;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 12;
        this.latestUsageStart = 18;
        this.type = ApplianceType.BURST;
         this.minInstances =1;
        this.maxInstances = 3;
                generateUsageHours();

        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 35;
        /***********************************************/
    }
}
