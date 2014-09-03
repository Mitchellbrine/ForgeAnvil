package mc.Mitchellbrine.forgeAnvil.core.aml;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.relauncher.FMLInjectionData;

public class AMLCore extends DummyModContainer{

    public static AMLCore instance;

    public static Logger logger = LogManager.getLogger("AML");
    File mcDir = (File) FMLInjectionData.data()[6];
    File dModsFolder = new File(mcDir,"ForgeAnvil/downloaded-mods/");

public AMLCore() {
    super(new ModMetadata());
    ModMetadata meta = getMetadata();
    meta.modId = "AML";
    meta.name = "AnvilModLoader";
    meta.version = "1.0";
    meta.credits = "All the Forge Anvil Team";
    meta.authorList = Arrays.asList("Mitchellbrine");
    meta.description = "An easy one file way to load dependencies and modpacks!";
    meta.url = "http://goo.gl/iJCwj6";
    meta.updateUrl = "";
    meta.screenshots = new String[0];
    meta.logoFile = "/aml-logo.png";
    logger.info("Loading AML...");
    if (dModFiles() != null) {
        for (File file : dModFiles()) {
            file.delete();
        }
    }
    instance = this;
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

    private List<File> dModFiles() {
        List<File> list = new LinkedList<File>();
        if (dModsFolder.listFiles() != null) {
            list.addAll(Arrays.asList(dModsFolder.listFiles()));
        }
        return list;
    }

}
