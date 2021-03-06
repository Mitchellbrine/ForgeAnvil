package mc.Mitchellbrine.forgeAnvil.core.aml;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import mc.Mitchellbrine.forgeAnvil.core.ForgeAnvil;
import net.minecraft.launchwrapper.LaunchClassLoader;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.io.IOUtils;

public class AML implements IFMLLoadingPlugin{

    public static AMLInst inst;

    public static ArrayList<File> modsLoaded = new ArrayList<File>();

    public static ArrayList<String> names = new ArrayList<String>();
    public static HashMap<String,String> versions = new HashMap<String, String>();
    public static HashMap<String,String> authors = new HashMap<String, String>();

    public AML() {

    }

    private static void addAndUnzip(String path) {
        try {
            URI uri = new URI(path);
            File file = new File(uri);
            AMLInst.unzip(file, AML.inst.mcDir);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void addAndUnzip(URL url) {
        try {
            File libFile = new File("ForgeAnvil/downloaded-configs/"+url.getPath().substring(url.getPath().lastIndexOf("/")));
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "AML Downloader");
            int sizeGuess = connection.getContentLength();
            AML.inst.downloadConfig(connection.getInputStream(), sizeGuess, libFile);
        } catch (Exception ex) {

        }
    }

    private static void addModPath(String url,String modLoading) {
        try {
            URI uri = new URI(url);
            File file = new File(uri);
            ZipFile zip = new ZipFile(file);
            ZipEntry z = zip.getEntry("config.info");
            if (z != null) {
                AMLInst.unzip(file, AML.inst.mcDir);
            } else {
                ((LaunchClassLoader) AML.class.getClassLoader()).addURL(new URL(url));
                modsLoaded.add(new File(url));
                if (url.contains("/")) {
                    AMLCore.logger.info("Loaded File: " + url.substring(url.lastIndexOf("/") + 1) + " (By " + modLoading + ")");
                } else if (url.contains("\\")){
                    AMLCore.logger.info("Loaded File: " + url.substring(url.lastIndexOf("\\") + 1) + " (By " + modLoading + ")");
                } else {
                    AMLCore.logger.info("Loaded File: " + url + " (By " + modLoading + ")");
                }
            }
            zip.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void addModURL(URL url,String modLoading) {
        try {
            File libFile = new File("ForgeAnvil/downloaded-mods/"+url.getPath().substring(url.getPath().lastIndexOf("/")));
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "AML Downloader");
            int sizeGuess = connection.getContentLength();
            AML.inst.download(connection.getInputStream(), sizeGuess, libFile,modLoading);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void addMod(File url) {
        try {
            ((LaunchClassLoader) AML.class.getClassLoader()).addURL(url.toURI().toURL());
            modsLoaded.add(url);
            if (url.getPath().contains("/")) {
                AMLCore.logger.info("Loaded File: " + url.getPath().substring(url.getPath().lastIndexOf("/") + 1));
            } else if (url.getPath().contains("\\")){
                AMLCore.logger.info("Loaded File: " + url.getPath().substring(url.getPath().lastIndexOf("\\") + 1));
            } else {
                AMLCore.logger.info("Loaded File: " + url);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return AMLCore.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static void load() {
        if (inst == null) {
            inst = new AMLInst();
            System.err.println(AMLCore.instance.getMetadata().logoFile);
            AMLCore.logger.info("Beginning loading for mods...");
            inst.scanForMods();
        }
    }


    private static class AMLInst {

        File mcDir = (File) FMLInjectionData.data()[6];
        File modsFolder = new File(mcDir,"ForgeAnvil/mods");
        File dModsFolder = new File(mcDir,"ForgeAnvil/downloaded-mods/");

        private void scanForMods() {
            AMLCore.logger.info("~~~~~~~~~~");
            for (File file : modFiles()) {
                if (!file.getName().endsWith(".jar") && !file.getName().endsWith(".zip"))
                    continue;
                findModFile(file);
            }
            AMLCore.logger.info("~~~~~~~~~~");
        }

        private void findModFile(File file) {
            try {
                ZipFile zip = new ZipFile(file);
                ZipEntry e = zip.getEntry("anvil.info");
                if (e == null) e = zip.getEntry("anvil.info");
                if (e != null)
                    findInformation(zip.getInputStream(e),file);
                zip.close();
            } catch (Exception e) {
                ForgeAnvil.logger.fatal("Failed to find anvil.info from " + file.getName());
                e.printStackTrace();
            }
        }

        private List<File> modFiles() {
            List<File> list = new LinkedList<File>();
            if (modsFolder.listFiles() != null) {
                list.addAll(Arrays.asList(modsFolder.listFiles()));
            }
            return list;
        }

        private List<File> dModFiles() {
            List<File> list = new LinkedList<File>();
            if (dModsFolder.listFiles() != null) {
                list.addAll(Arrays.asList(dModsFolder.listFiles()));
            }
            return list;
        }

        private void findInformation(InputStream inputStream,File file) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String str;
            String modName = "";
            String versionName = "";
            String authorName = "";
            while ((str = reader.readLine()) != null) {
                if (str.startsWith("name: ") || str.startsWith("name:")) {
                    if (str.startsWith("name: ")) modName = str.substring(6);
                    else modName = str.substring(5);
                } else if (str.startsWith("version: ") || str.startsWith("version:")) {
                    if (str.startsWith("version: ")) versionName = str.substring(9);
                    else versionName = str.substring(8);
                } else if (str.startsWith("author: ") || str.startsWith("author:")) {
                    if (str.startsWith("author: ")) authorName = str.substring(8);
                    else authorName = str.substring(7);
                } else if (str.startsWith("load: ") || str.startsWith("load:")) {
                        if (str.startsWith("load: ")) {
                            String[] mods = str.substring(6).split(",");
                            for (String mod : mods) {
                                if ((!mod.startsWith("http") && !mod.substring(5,5).equalsIgnoreCase("s")) && !mod.startsWith("https")) {
                                    addModPath(mod,modName);
                                } else {
                                    if (mod.endsWith("zip")) {
                                        addModURL(new URL(mod),modName);
                                    } else if (mod.endsWith("jar")) {
                                        addModURL(new URL(mod),modName);
                                    }
                                }
                            }
                        } else {
                            String[] mods = str.substring(5).split(",");
                            for (String mod : mods) {
                                if ((!mod.startsWith("http") && !mod.substring(5,5).equalsIgnoreCase("s")) && !mod.startsWith("https")) {
                                    addModPath(mod,modName);
                                } else {
                                    if (mod.endsWith("zip")) {
                                        addModURL(new URL(mod),modName);
                                    } else if (mod.endsWith("jar")) {
                                        addModURL(new URL(mod),modName);
                                    }
                                }
                            }
                    }
                } else if (str.startsWith("load-config: ") || str.startsWith("load-config:")) {
                        if (str.startsWith("load-config: ")) {
                            String[] mods = str.substring(13).split(",");
                            for (String mod : mods) {
                                if ((!mod.startsWith("http") && !mod.substring(5,5).equalsIgnoreCase("s")) && !mod.startsWith("https")) {
                                    addAndUnzip(mod);
                                } else {
                                    if (mod.startsWith("http") && mod.substring(5,5).equalsIgnoreCase("s")) {
                                        addAndUnzip(new URL(mod));
                                    } else {
                                        addAndUnzip(new URL(mod));
                                    }
                                }
                            }
                        } else {
                            String[] mods = str.substring(12).split(",");
                            for (String mod : mods) {
                                if ((!mod.startsWith("http") && !mod.substring(5,5).equalsIgnoreCase("s")) && !mod.startsWith("https")) {
                                    addAndUnzip(mod);
                                } else {
                                    if (mod.startsWith("http") && mod.substring(5,5).equalsIgnoreCase("s")) {
                                        addAndUnzip(new URL(mod));
                                    } else {
                                        addAndUnzip(new URL(mod));
                                    }
                                }
                            }
                        }
                } else if (str.startsWith("load-unzip: ") || str.startsWith("load-unzip:")) {
                    if (str.startsWith("load-unzip: ")) {
                        String[] mods = str.substring(12).split(",");
                        for (String mod : mods) {
                            if ((!mod.startsWith("http") && !mod.substring(5, 5).equalsIgnoreCase("s")) && !mod.startsWith("https")) {
                                addAndUnzip(mod);
                            } else {
                                if (mod.startsWith("http") && mod.substring(5, 5).equalsIgnoreCase("s")) {
                                    addAndUnzip(new URL(mod));
                                } else {
                                    addAndUnzip(new URL(mod));
                                }
                            }
                        }
                    } else {
                        String[] mods = str.substring(11).split(",");
                        for (String mod : mods) {
                            if ((!mod.startsWith("http") && !mod.substring(5, 5).equalsIgnoreCase("s")) && !mod.startsWith("https")) {
                                addAndUnzip(mod);
                            } else {
                                if (mod.startsWith("http") && mod.substring(5, 5).equalsIgnoreCase("s")) {
                                    addAndUnzip(new URL(mod));
                                } else {
                                    addAndUnzip(new URL(mod));
                                }
                            }
                        }
                    }
                }
            }
            names.add(modName);
            versions.put(modName, versionName);
            authors.put(modName, authorName);
            addMod(file);
        }

        private void download(InputStream is, int sizeGuess, File target,String modLoading) throws Exception {
            try {
                byte[] buffer = new byte[4096];
                int n = - 1;

                OutputStream output = new FileOutputStream(target);
                while ( (n = is.read(buffer)) != -1)
                {

                    output.write(buffer, 0, n);
                }
                    output.close();


                ZipFile zip = new ZipFile(target);
                ZipEntry e = zip.getEntry("config.info");
                if (e == null) {
                    ((LaunchClassLoader) AML.class.getClassLoader()).addURL(target.toURI().toURL());
                    modsLoaded.add(target);
                    if (target.getPath().contains("/")) {
                        AMLCore.logger.info("Loaded File: " + target.getPath().substring(target.getPath().lastIndexOf("/") + 1) + " (By " + modLoading + ")");
                    } else if (target.getPath().contains("\\")){
                        AMLCore.logger.info("Loaded File: " + target.getPath().substring(target.getPath().lastIndexOf("\\") + 1) + " (By " + modLoading + ")");
                    } else {
                        AMLCore.logger.info("Loaded File: " + target.getPath() + " (By " + modLoading + ")");
                    }
                } else {
                    unzip(target,mcDir);
                }
                zip.close();

                /*}
                else
                {
                    throw new RuntimeException(String.format("The downloaded file %s has an invalid checksum %s (expecting %s). The download did not succeed correctly and the file has been deleted. Please try launching again.", target.getName(), cksum, validationHash));
                }*/
            } catch (Exception e) {
                throw e;
            }
        }

        private void downloadConfig(InputStream is, int sizeGuess, File target) throws Exception {
            try {
                byte[] buffer = new byte[4096];
                int n = - 1;

                OutputStream output = new FileOutputStream(target);
                while ( (n = is.read(buffer)) != -1)
                {

                    output.write(buffer, 0, n);
                }
                output.close();

                unzip(target,mcDir);

                /*}
                else
                {
                    throw new RuntimeException(String.format("The downloaded file %s has an invalid checksum %s (expecting %s). The download did not succeed correctly and the file has been deleted. Please try launching again.", target.getName(), cksum, validationHash));
                }*/
            } catch (Exception e) {
                throw e;
            }
        }

        private static void unzip(File zipfile, File directory) throws IOException {
            ZipFile zfile = new ZipFile(zipfile);
            Enumeration<? extends ZipEntry> entries = zfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File file = new File(directory, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    InputStream in = zfile.getInputStream(entry);
                    try {
                        copy(in, file);
                    } finally {
                        in.close();
                    }
                }
            }
            zfile.close();
        }

        private static void copy(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[1024];
            while (true) {
                int readCount = in.read(buffer);
                if (readCount < 0) {
                    break;
                }
                out.write(buffer, 0, readCount);
            }
        }

        private static void copy(InputStream in, File file) throws IOException {
            OutputStream out = new FileOutputStream(file);
            try {
                copy(in, out);
            } finally {
                out.close();
            }
        }

    }

}
