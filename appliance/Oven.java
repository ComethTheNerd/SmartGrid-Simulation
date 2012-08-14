package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Oven extends Appliance {

    public Oven() {
        this.canShed = true;
        this.minUsage = 5000;
        this.maxUsage = 5600;
        this.isOn = false;
        this.duration = 2;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 10;
        this.type = ApplianceType.WHITE_GOODS;
         this.minInstances =1;
        this.maxInstances = 2;
                generateUsageHours();
    }
}
