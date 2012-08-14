package appliance;
import appliance.core.ApplianceType;
import appliance.core.FlexibleUsageAppliance;

/**
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class Blender extends FlexibleUsageAppliance {

    public Blender() {
        this.canShed = true;
        this.minUsage = 1200;
        this.maxUsage = 1500;
        this.isOn = false;
        this.duration = 1;
        this.earliestUsageStart = 6;
        this.latestUsageStart = 10;
         this.type = ApplianceType.BURST;
        generateUsageHours();
               this.minInstances = 1;
        this.maxInstances = 3;
        
        /* BURST USAGE FLEXIBLE ENERGY USAGE ADJUSTMENT */
        this.usageMinutes = 20;
        /***********************************************/
    }
}
