package appliance;

import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Master
 */
public class AudioEquipment extends FlexibleUsageAppliance {

    public AudioEquipment() {
        this.canShed = true;
        this.minUsage = 100;
        this.maxUsage = 500;
        this.isOn = false;
        this.duration = 2;
        this.earliestUsageStart = 18;
        this.latestUsageStart = 22;
        this.type = ApplianceType.BURST;
        this.minInstances = 0;
        this.maxInstances = 4;
        generateUsageHours();
    }
}
