package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class WashingMachine extends Appliance {

    public WashingMachine() {
        this.canShed = true;
        this.minUsage = 1000;
        this.maxUsage = 1400;
        this.isOn = false;
        this.duration = 2;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 22;
        this.type = ApplianceType.UTILITIES;
         this.minInstances =1;
        this.maxInstances = 3;
                generateUsageHours();
    }
}
