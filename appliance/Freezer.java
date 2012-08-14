package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Freezer extends Appliance {

    public Freezer() {
        this.canShed = true;
        this.minUsage = 400;
        this.maxUsage = 420;
        this.isOn = false;
        this.duration = 23;
        this.earliestUsageStart = 1;
        this.latestUsageStart = 23;
        this.type = ApplianceType.WHITE_GOODS;
         this.minInstances =1;
        this.maxInstances = 3;
                generateUsageHours();
    }
}
