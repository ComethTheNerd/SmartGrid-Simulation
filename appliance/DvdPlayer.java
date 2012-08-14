package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class DvdPlayer extends Appliance {

    public DvdPlayer() {
        this.canShed = true;
        this.minUsage = 200;
        this.maxUsage = 240;
        this.isOn = false;
        this.duration = 3;
        this.earliestUsageStart = 19;
        this.latestUsageStart = 22;
        this.type = ApplianceType.LUXURY_ITEMS;
        this.minInstances = 0;
        this.maxInstances = 3;
                generateUsageHours();
    }
}
