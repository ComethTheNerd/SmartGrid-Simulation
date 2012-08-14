package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Barbeque extends Appliance {

    public Barbeque() {
        this.canShed = true;
        this.minUsage = 3000;
        this.maxUsage = 3200;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 12;
        this.latestUsageStart = 6;
         this.type = ApplianceType.LUXURY_ITEMS;
                this.minInstances = 0;
        this.maxInstances = 2;
         generateUsageHours();
    }
}
