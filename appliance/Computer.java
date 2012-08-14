package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Computer extends Appliance {

    public Computer() {
    
    this.canShed = true;
    this.minUsage = 300;
    this.maxUsage = 1000;
    this.isOn = false;
    this.duration = 8;
    this.earliestUsageStart = 6;
    this.latestUsageStart = 10;
     this.type = ApplianceType.LUXURY_ITEMS;
             generateUsageHours();
}
}
