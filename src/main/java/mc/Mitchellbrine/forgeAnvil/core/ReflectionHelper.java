package mc.Mitchellbrine.forgeAnvil.core;

import net.minecraft.potion.Potion;

import java.lang.reflect.Field;

public class ReflectionHelper {

    public static void registerPotionReflection() {
        Potion[] potionTypes = null;
        Field[] potionFields = Potion.class.getDeclaredFields();
        int amountOfPotionFields = potionFields.length;

        for (int currentField = 0; currentField < amountOfPotionFields; ++currentField) {
            Field currentFeild = potionFields[currentField];
            currentFeild.setAccessible(true);

            try {
                if (currentFeild.getName().equals("potionTypes") || currentFeild.getName().equals("field_76425_a")) {

                    if (Potion.potionTypes.length < 2048) {
                        Field error = Field.class.getDeclaredField("modifiers");
                        error.setAccessible(true);
                        error.setInt(currentFeild, currentFeild.getModifiers()
                                & -17);
                        potionTypes = (Potion[]) ((Potion[]) currentFeild
                                .get((Object) null));
                        Potion[] newPotionTypes = new Potion[2048];
                        System.arraycopy(potionTypes, 0, newPotionTypes, 0,
                                potionTypes.length);
                        currentFeild.set((Object) null, newPotionTypes);
                    }

                }
            } catch (Exception exception) {
                ForgeAnvil.logger.fatal("Severe error, please report this to the ForgeAnvil team! Crash Log:");
                ForgeAnvil.logger.fatal(exception);
            }
        }
    }

}
