package appliance;

import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>, Sonke Tietgens <k1186281@kcl.ac.uk>
 */
public class Kettle extends FlexibleUsageAppliance {

    public Kettle() {
        this.canShed = true;
        this.minUsage = 2800;
        this.maxUsage = 3000;
        this.isOn = false;

        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 10;
        this.type = ApplianceType.BURST;
         this.minInstances =1;
        this.maxInstances = 2;
                generateUsageHours();

        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 3;
        /***********************************************/
    }
}
