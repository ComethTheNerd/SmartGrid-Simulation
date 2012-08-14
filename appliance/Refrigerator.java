package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Refrigerator extends Appliance {

    public Refrigerator() {
        this.canShed = true;
        this.minUsage = 300;
        this.maxUsage = 400;
        this.isOn = false;
        this.duration = 4;
        this.earliestUsageStart = 1;
        this.latestUsageStart = 23;
        this.type = ApplianceType.WHITE_GOODS;
         this.minInstances =1;
        this.maxInstances = 3;
                generateUsageHours();
    }
}
