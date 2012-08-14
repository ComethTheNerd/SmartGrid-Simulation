package appliance;
import appliance.core.ApplianceType;
import appliance.core.Appliance;

/**
 *
 * @author Sonke Tietgens <K1186281>
 */
public class AirConditioning extends Appliance {

    public AirConditioning() {
        this.canShed = true;
        this.minUsage = 3000;
        this.maxUsage = 3200;
        this.isOn = false;
        this.duration = 8;
        this.earliestUsageStart = 12;
        this.latestUsageStart = 22;
        this.type = ApplianceType.LUXURY_ITEMS;
        this.minInstances = 0;
        this.maxInstances = 2;
         generateUsageHours();
    }
}
