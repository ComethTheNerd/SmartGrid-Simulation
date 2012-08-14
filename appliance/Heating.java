package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class Heating extends Appliance {

    public Heating() {
        this.canShed = true;
        this.minUsage = 120;
        this.maxUsage = 160;
        this.isOn = false;
        this.duration = 8;
        this.earliestUsageStart = 7;
        this.latestUsageStart = 20;
         this.type = ApplianceType.WHITE_GOODS;
          this.minInstances =1;
        this.maxInstances = 2;
                 generateUsageHours();

    }
}
