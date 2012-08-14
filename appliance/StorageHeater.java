package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class StorageHeater extends Appliance {

    public StorageHeater() {
        this.canShed = true;
        this.minUsage = 1700;
        this.maxUsage = 2000;
        this.isOn = false;
        this.duration = 7;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 22;
        this.type = ApplianceType.UTILITIES;
         this.minInstances =0;
        this.maxInstances = 5;
                generateUsageHours();
    }
}
