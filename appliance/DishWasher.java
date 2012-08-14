package appliance;

import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class DishWasher extends FlexibleUsageAppliance {

    public DishWasher() {
        this.canShed = true;
        this.minUsage = 1000;
        this.maxUsage = 1500;
        this.isOn = false;
        this.duration = 3;
        this.earliestUsageStart = 12;
        this.latestUsageStart = 21;
        this.type = ApplianceType.BURST;
        this.minInstances = 0;
        this.maxInstances = 1;
        generateUsageHours();

        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 80;
        /***********************************************/
    }
}
