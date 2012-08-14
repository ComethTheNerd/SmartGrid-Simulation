package appliance;

import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;
/*
 *
 * @author Sonke Tietgens <K1186281>
 */
public class HairDryer extends FlexibleUsageAppliance{

    public HairDryer() {
        this.canShed = true;
        this.minUsage = 1500;
        this.maxUsage = 1600;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 12;
        this.type = ApplianceType.BURST;
        generateUsageHours();
        this.minInstances = 1;
        this.maxInstances = 3;

        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 10;
        /***********************************************/
    }
}
