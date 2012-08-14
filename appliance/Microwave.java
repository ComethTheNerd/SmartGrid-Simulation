package appliance;

import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Microwave extends FlexibleUsageAppliance {

    public Microwave ()

    {

        this.canShed = true;
        this.minUsage = 1700;
        this.maxUsage = 2000;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 12;
         this.type = ApplianceType.BURST;
       this.minInstances = 1;
        this.maxInstances = 2;
                 generateUsageHours();
        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 6;
        /***********************************************/
    }
}
