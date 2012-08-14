package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;
/*
 *
 * @author Sonke Tietgens <K1186281>
 */
public class CordlessPhone extends Appliance{
   public CordlessPhone() {
        this.canShed = true;
        this.minUsage = 12;
        this.maxUsage = 18;
        this.isOn = false;
        this.duration = 24;
        this.earliestUsageStart = 0;
        this.latestUsageStart = 23;
        this.type = ApplianceType.LUXURY_ITEMS;
        generateUsageHours();
        this.minInstances = 0;
        this.maxInstances = 4;
    }
}
