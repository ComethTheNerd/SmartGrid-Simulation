package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;
/*
 *
 * @author Sonke Tietgens <K1186281>
 */
public class SecuritySensors extends Appliance{
 public SecuritySensors() {
        this.canShed = false;
        this.minUsage = 10;
        this.maxUsage = 15;
        this.isOn = false;
        this.duration = 24;
        this.earliestUsageStart = 1;
        this.latestUsageStart = 23;
        this.type = ApplianceType.LUXURY_ITEMS;
        generateUsageHours();
        this.minInstances = 0;
        this.maxInstances = 6;
    }
}
