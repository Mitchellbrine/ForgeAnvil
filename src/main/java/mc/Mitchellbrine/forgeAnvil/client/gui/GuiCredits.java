package mc.Mitchellbrine.forgeAnvil.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mc.Mitchellbrine.forgeAnvil.core.aml.AML;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiCredits extends GuiScreen
{
    private final GuiScreen field_146310_a;
    private GuiButton field_152176_i;
    private static final String __OBFID = "CL_00000695";

    public GuiCredits(GuiScreen p_i1033_1_)
    {
        this.field_146310_a = p_i1033_1_;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, "Press 'Escape' to leave"));
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Forge Anvil Credits:", this.width / 2, 17, 16777215);
        this.drawCenteredString(this.fontRendererObj, "Mitchellbrine", this.width / 2, 37, 16777215);
        this.drawCenteredString(this.fontRendererObj, "The JamCrafters and OOClan Members", this.width / 2, 57, 16777215);
        this.drawCenteredString(this.fontRendererObj, "~~~~~~~~~~", this.width / 2, 77, 16777215);
        if (AML.names.size() > 0) {
                this.drawCenteredString(this.fontRendererObj, "Forge Anvil Mods:", this.width / 2, 87, 16777215);
                for (int i = 0; i < AML.names.size(); i++) {
                    if (AML.authors.get(AML.names.get(i)) != null) {
                        this.drawCenteredString(this.fontRendererObj, AML.names.get(i) + " (By " + AML.authors.get(AML.names.get(i)) + ")", this.width / 2, (20 * i) + 97, 16777215);
                    } else {
                        this.drawCenteredString(this.fontRendererObj, AML.names.get(i), this.width / 2, (20 * i) + 97, 16777215);
                    }
                }
            }
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}