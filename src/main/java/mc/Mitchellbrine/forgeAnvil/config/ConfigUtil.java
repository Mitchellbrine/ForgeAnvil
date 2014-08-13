package mc.Mitchellbrine.forgeAnvil.config;

import net.minecraftforge.common.config.Property;

public class ConfigUtil {

    public static boolean convertIntToBoolean(Property property) {
        if (!Double.isNaN(property.getInt())) {
            switch(property.getInt()) {
                case 0: return false;
                case 1: return true;
                default: return false;
            }
        }
        return false;
    }

    public static boolean convertIntToBoolean(int integer) {
        switch(integer) {
            case 0: return false;
            case 1: return true;
            default: return false;
        }
    }

}
