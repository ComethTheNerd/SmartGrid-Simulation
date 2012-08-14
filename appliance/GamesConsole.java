package appliance;

import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class GamesConsole extends Appliance {

    public GamesConsole() {
        this.canShed = true;
        this.minUsage = 50;
        this.maxUsage = 100;
        this.isOn = false;
        this.duration = 4;
        this.earliestUsageStart = 16;
        this.latestUsageStart = 22;
        this.type = ApplianceType.LUXURY_ITEMS;
          this.minInstances =0;
        this.maxInstances = 4;
                generateUsageHours();
    }
}
