package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Laptop extends Appliance {

    public Laptop() {
        this.canShed = true;
        this.minUsage = 100;
        this.maxUsage = 110;
        this.isOn = false;
        this.duration = 2;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 22;
        this.type = ApplianceType.LUXURY_ITEMS;
        generateUsageHours();
        this.minInstances = 0;
        this.maxInstances = 2;
    }
}
