package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class TV extends Appliance {

    public TV() {
        this.canShed = true;
        this.minUsage = 600;
        this.maxUsage = 700;
        this.isOn = false;
        this.duration = 6;
        this.earliestUsageStart = 18;
        this.latestUsageStart = 23;
        this.type = ApplianceType.LUXURY_ITEMS;
         this.minInstances =1;
        this.maxInstances = 5;
                generateUsageHours();

    }
}
