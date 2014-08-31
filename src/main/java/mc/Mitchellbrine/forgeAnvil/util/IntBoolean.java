package mc.Mitchellbrine.forgeAnvil.util;

import net.minecraft.command.NumberInvalidException;

public class IntBoolean {

    private int integer = Integer.MAX_VALUE;
    private boolean bool;

    public IntBoolean(boolean bool) {
        this.bool = bool;
    }

    public IntBoolean(int integer) {
        this.integer = integer;
    }

    public boolean getBoolean() {
            if (this.integer != Integer.MAX_VALUE) {
                switch (this.integer) {
                    case 0: return false;
                    case 1: return true;
                    default: throw new NumberFormatException("Misused integer in an int-boolean!");
                }
            } else {
                return this.bool;
            }
        }

    public int getInteger() {
        if (this.integer != Integer.MAX_VALUE) {
            return this.integer;
        } else {
            if (getBoolean()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public boolean equals(boolean bool) {
        return this.getBoolean() == bool;
    }

    public boolean equals(int integer) {
        return this.getInteger() == integer;
    }

}
