package appliance.core;

/**An enum used to categorise different appliances by their
 * general purpose in a house
 *
 * @author Darius Hodaei
 */
public enum ApplianceType {
    /**appliances such as a fridge or freezer, common to every house*/
    WHITE_GOODS,
    /**non-essential items such as games consoles and televisions, that can be turned off if needs be*/
    LUXURY_ITEMS,
    /**an appliance that directly uses a utility, such as a boiler or power shower*/
    UTILITIES,
    /**an appliance that is only used for a short time, causing a spike in consumption*/
    BURST;
}
