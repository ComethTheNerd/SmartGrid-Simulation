package appliance;
import appliance.core.ApplianceType;
import appliance.core.Appliance;
/*
 *
 * @author Sonke Tietgens <K1186281>
 */
public class BathroomHeater extends  Appliance{

    public BathroomHeater() {
        this.canShed = true;
        this.minUsage = 2000;
        this.maxUsage = 2100;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 14;
        this.type = ApplianceType.LUXURY_ITEMS;
        generateUsageHours();
        this.minInstances = 0;
        this.maxInstances = 2;
    }
}
