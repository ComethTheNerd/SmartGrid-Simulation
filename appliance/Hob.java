package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Hob extends Appliance {

    public Hob() {
        this.canShed = true;
        this.minUsage = 3000;
        this.maxUsage = 3600;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 10;
        this.type = ApplianceType.WHITE_GOODS;
         this.minInstances =1;
        this.maxInstances = 2;
                generateUsageHours();
    }
}
