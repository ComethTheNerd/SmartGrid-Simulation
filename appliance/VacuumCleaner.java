package appliance;

import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class VacuumCleaner extends FlexibleUsageAppliance {

    public VacuumCleaner() {
        this.canShed = true;
        this.minUsage = 1200;
        this.maxUsage = 1600;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 12;
        this.latestUsageStart = 20;
        this.type = ApplianceType.BURST;
         this.minInstances =1;
        this.maxInstances = 2;
                generateUsageHours();

        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 25;
        /***********************************************/
    }
}
