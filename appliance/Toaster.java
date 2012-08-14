package appliance;

import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Toaster extends FlexibleUsageAppliance {

    public Toaster() {
        this.canShed = true;
        this.minUsage = 1000;
        this.maxUsage = 1500;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 11;
        this.type = ApplianceType.BURST;
         this.minInstances =1;
        this.maxInstances = 2;
                generateUsageHours();

        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 5;
        /***********************************************/
    }
}
