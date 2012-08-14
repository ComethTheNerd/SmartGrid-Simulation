package policy;
import time.commands.ApplianceTimedCommand;
import appliance.core.Appliance;
import appliance.Heating;
import universe.SmartGridUniverse;
import time.Time;
/**A Command that works in conjunction with the WinterHeating PolicyComponent
	* to model turning on a heating appliance when it is Winter
	*/
    public class WinterHeatingCommand extends ApplianceTimedCommand {

		/**Register the appliance to apply this command to
		*/
        public WinterHeatingCommand(Appliance heating) {
            super(heating);
        }

        public void atStart() {
			// if it is Winter turn the appliance on
            if (isWinter()) {
                appliance.setOn(true);
            }
        }

        public void atDuring() {
			// if it is Winter turn the appliance on
            if (isWinter()) {
                appliance.setOn(true);
            }
        }

        public void atEnd() {
			// turn the appliance off at the end of the command
            appliance.setOn(false);
        }

		/**Determines whether the current season of the universe is Winter
		*/
        private boolean isWinter() {
			// returns true if the current season is Winter
            return SmartGridUniverse.getInstance().getUniverseTime().getCurrentSeason()
                    == Time.SeasonEnum.Winter;

        }
    }
