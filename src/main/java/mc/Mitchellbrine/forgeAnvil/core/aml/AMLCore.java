package mc.Mitchellbrine.forgeAnvil.core.aml;

import java.util.Arrays;

import cpw.mods.fml.common.event.*;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModMetadata;

public class AMLCore extends DummyModContainer{

public AMLCore() {
    super(new ModMetadata());
    ModMetadata meta = getMetadata();
    meta.modId = "AML";
    meta.name = "AnvilModLoader";
    meta.version = "@VERSION@";
    meta.credits = "All the Forge Anvil Team";
    meta.authorList = Arrays.asList("Mitchellbrine");
    meta.description = "";
    meta.url = "https://github.com/mitchellbrine/ForgeAnvil";
    meta.updateUrl = "";
    meta.screenshots = new String[0];
    meta.logoFile = "";
    AML.load();
}

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void init(FMLInitializationEvent evt) {

    }


    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {

    }

}
