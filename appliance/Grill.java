package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;
/*
 *
 * @author Sonke Tietgens <K1186281>
 */

public class Grill extends Appliance {

    public Grill() {
        this.canShed = true;
        this.minUsage = 1000;
        this.maxUsage = 1100;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 12;
        this.latestUsageStart = 18;
        this.type = ApplianceType.LUXURY_ITEMS;
        generateUsageHours();
        this.minInstances = 0;
        this.maxInstances = 1;
    }
}
