package appliance;
import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class CoffeeMaker extends FlexibleUsageAppliance {

    public CoffeeMaker() {
        this.canShed = true;
        this.minUsage = 200;
        this.maxUsage = 800;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 12;
        this.type = ApplianceType.BURST;
        generateUsageHours();
        this.minInstances = 0;
        this.maxInstances = 2;

        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 15;
        /***********************************************/
    }
}
