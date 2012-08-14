package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;
/*
 *
 * @author Sonke Tietgens <K1186281>
 */
public class PowerShower extends Appliance{

    public PowerShower() {
        this.canShed = true;
        this.minUsage = 7000;
        this.maxUsage = 9000;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 12;
        this.type = ApplianceType.UTILITIES;
        generateUsageHours();
        this.minInstances = 0;
        this.maxInstances = 1;
    }
}