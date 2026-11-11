/*
 * Decompiled with CFR 0.152.
 */
package net.minecraftforge.gradle;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipFile;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class GradleStartCommon {
    protected static Logger LOGGER = LogManager.getLogger((String)"GradleStart");
    private static final String NO_CORE_SEARCH = "noCoreSearch";
    private Map<String, String> argMap = Maps.newHashMap();
    private List<String> extras = Lists.newArrayList();
    private static final File SRG_DIR = new File("C:/Users/wizjany/.gradle/caches/minecraft/net/minecraftforge/forge/1.7.10-10.13.4.1614-1.7.10/srgs");
    private static final File SRG_NOTCH_SRG = new File("C:/Users/wizjany/.gradle/caches/minecraft/net/minecraftforge/forge/1.7.10-10.13.4.1614-1.7.10/srgs/notch-srg.srg");
    private static final File SRG_NOTCH_MCP = new File("C:/Users/wizjany/.gradle/caches/minecraft/net/minecraftforge/forge/1.7.10-10.13.4.1614-1.7.10/srgs/notch-mcp.srg");
    private static final File SRG_SRG_MCP = new File("C:/Users/wizjany/.gradle/caches/minecraft/net/minecraftforge/forge/1.7.10-10.13.4.1614-1.7.10/srgs/srg-mcp.srg");
    private static final File SRG_MCP_SRG = new File("C:/Users/wizjany/.gradle/caches/minecraft/net/minecraftforge/forge/1.7.10-10.13.4.1614-1.7.10/srgs/mcp-srg.srg");
    private static final File SRG_MCP_NOTCH = new File("C:/Users/wizjany/.gradle/caches/minecraft/net/minecraftforge/forge/1.7.10-10.13.4.1614-1.7.10/srgs/mcp-notch.srg");
    private static final File CSV_DIR = new File("C:/Users/wizjany/.gradle/caches/minecraft/net/minecraftforge/forge/1.7.10-10.13.4.1614-1.7.10/unpacked/conf");
    private static final String MC_VERSION = "1.7.10";
    private static final String FML_PACK_OLD = "cpw.mods";
    private static final String FML_PACK_NEW = "net.minecraftforge";
    private static final String COREMOD_VAR = "fml.coreMods.load";
    private static final String COREMOD_MF = "FMLCorePlugin";
    private static final String MOD_ATD_CLASS = "fml.common.asm.transformers.ModAccessTransformer";
    private static final String MOD_AT_METHOD = "addJar";
    public static final Map<String, File> coreMap = Maps.newHashMap();

    protected abstract void setDefaultArguments(Map<String, String> var1);

    protected abstract void preLaunch(Map<String, String> var1, List<String> var2);

    protected abstract String getBounceClass();

    protected abstract String getTweakClass();

    protected void launch(String[] stringArray) throws Throwable {
        System.setProperty("net.minecraftforge.gradle.GradleStart.srgDir", SRG_DIR.getCanonicalPath());
        System.setProperty("net.minecraftforge.gradle.GradleStart.srg.notch-srg", SRG_NOTCH_SRG.getCanonicalPath());
        System.setProperty("net.minecraftforge.gradle.GradleStart.srg.notch-mcp", SRG_NOTCH_MCP.getCanonicalPath());
        System.setProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp", SRG_SRG_MCP.getCanonicalPath());
        System.setProperty("net.minecraftforge.gradle.GradleStart.srg.mcp-srg", SRG_MCP_SRG.getCanonicalPath());
        System.setProperty("net.minecraftforge.gradle.GradleStart.srg.mcp-notch", SRG_MCP_NOTCH.getCanonicalPath());
        System.setProperty("net.minecraftforge.gradle.GradleStart.csvDir", CSV_DIR.getCanonicalPath());
        this.setDefaultArguments(this.argMap);
        this.parseArgs(stringArray);
        this.preLaunch(this.argMap, this.extras);
        System.setProperty("fml.ignoreInvalidMinecraftCertificates", "true");
        if (this.argMap.get(NO_CORE_SEARCH) == null) {
            this.searchCoremods();
        } else {
            LOGGER.info("GradleStart coremod searching disabled!");
        }
        stringArray = this.getArgs();
        this.argMap = null;
        this.extras = null;
        System.gc();
        String string = this.getBounceClass();
        if (string.endsWith("launchwrapper.Launch")) {
            Launch.main((String[])stringArray);
        } else {
            Class.forName(this.getBounceClass()).getDeclaredMethod("main", String[].class).invoke(null, new Object[]{stringArray});
        }
    }

    private String[] getArgs() {
        Map.Entry<String, String> entry2;
        ArrayList<String> arrayList = new ArrayList<String>(22);
        for (Map.Entry<String, String> entry2 : this.argMap.entrySet()) {
            String string = entry2.getValue();
            if (Strings.isNullOrEmpty((String)string)) continue;
            arrayList.add("--" + (String)entry2.getKey());
            arrayList.add(string);
        }
        if (!Strings.isNullOrEmpty((String)this.getTweakClass())) {
            arrayList.add("--tweakClass");
            arrayList.add(this.getTweakClass());
        }
        if (this.extras != null) {
            arrayList.addAll(this.extras);
        }
        String[] stringArray = arrayList.toArray(new String[0]);
        entry2 = new StringBuilder();
        ((StringBuilder)((Object)entry2)).append('[');
        for (int i = 0; i < stringArray.length; ++i) {
            ((StringBuilder)((Object)entry2)).append(stringArray[i]).append(", ");
            if (!"--accessToken".equalsIgnoreCase(stringArray[i])) continue;
            ((StringBuilder)((Object)entry2)).append("{REDACTED}, ");
            ++i;
        }
        ((StringBuilder)((Object)entry2)).replace(((StringBuilder)((Object)entry2)).length() - 2, ((StringBuilder)((Object)entry2)).length(), "");
        ((StringBuilder)((Object)entry2)).append(']');
        LOGGER.info("Running with arguments: " + ((StringBuilder)((Object)entry2)).toString());
        return stringArray;
    }

    private void parseArgs(String[] stringArray) {
        String string2;
        OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();
        for (String string2 : this.argMap.keySet()) {
            optionParser.accepts(string2).withRequiredArg().ofType(String.class);
        }
        optionParser.accepts(NO_CORE_SEARCH);
        NonOptionArgumentSpec nonOptionArgumentSpec = optionParser.nonOptions();
        string2 = optionParser.parse(stringArray);
        for (String string3 : this.argMap.keySet()) {
            if (!string2.hasArgument(string3)) continue;
            String string4 = (String)string2.valueOf(string3);
            this.argMap.put(string3, string4);
            if ("password".equalsIgnoreCase(string3)) continue;
            LOGGER.info(string3 + ": " + string4);
        }
        if (string2.has(NO_CORE_SEARCH)) {
            this.argMap.put(NO_CORE_SEARCH, "");
        }
        this.extras = Lists.newArrayList((Iterable)nonOptionArgumentSpec.values((OptionSet)string2));
        LOGGER.info("Extra: " + this.extras);
    }

    protected static Class getFmlClass(String string) throws ClassNotFoundException {
        return GradleStartCommon.getFmlClass(string, GradleStartCommon.class.getClassLoader());
    }

    public static Class getFmlClass(String string, ClassLoader classLoader) throws ClassNotFoundException {
        if (!string.startsWith("fml")) {
            throw new IllegalArgumentException("invalid FML classname");
        }
        string = MC_VERSION.startsWith("1.7") ? "cpw.mods." + string : "net.minecraftforge." + string;
        return Class.forName(string, true, classLoader);
    }

    private void searchCoremods() throws Exception {
        Method method = null;
        try {
            method = GradleStartCommon.getFmlClass(MOD_ATD_CLASS).getDeclaredMethod(MOD_AT_METHOD, JarFile.class);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        for (URL uRL : ((URLClassLoader)GradleStartCommon.class.getClassLoader()).getURLs()) {
            Object object;
            if (!uRL.getProtocol().startsWith("file")) continue;
            File file = new File(uRL.toURI().getPath());
            Manifest manifest = null;
            if (!file.exists()) continue;
            if (file.isDirectory()) {
                object = new File(file, "META-INF/MANIFEST.MF");
                if (((File)object).exists()) {
                    FileInputStream fileInputStream = new FileInputStream((File)object);
                    manifest = new Manifest(fileInputStream);
                    fileInputStream.close();
                }
            } else if (file.getName().endsWith("jar")) {
                object = new JarFile(file);
                manifest = ((JarFile)object).getManifest();
                if (method != null && manifest != null) {
                    method.invoke(null, object);
                }
                ((ZipFile)object).close();
            }
            if (manifest == null || Strings.isNullOrEmpty((String)(object = manifest.getMainAttributes().getValue(COREMOD_MF)))) continue;
            LOGGER.info("Found and added coremod: " + (String)object);
            coreMap.put((String)object, file);
        }
        HashSet hashSet = Sets.newHashSet();
        if (!Strings.isNullOrEmpty((String)System.getProperty(COREMOD_VAR))) {
            hashSet.addAll(Splitter.on((char)',').splitToList((CharSequence)System.getProperty(COREMOD_VAR)));
        }
        hashSet.addAll(coreMap.keySet());
        System.setProperty(COREMOD_VAR, Joiner.on((char)',').join((Iterable)hashSet));
        if (!Strings.isNullOrEmpty((String)this.getTweakClass())) {
            this.extras.add("--tweakClass");
            this.extras.add("net.minecraftforge.gradle.tweakers.CoremodTweaker");
        }
    }

    public static final class AccessTransformerTransformer
    implements IClassTransformer {
        public AccessTransformerTransformer() {
            this.doStuff((LaunchClassLoader)this.getClass().getClassLoader());
        }

        private void doStuff(LaunchClassLoader launchClassLoader) {
            Object e;
            Iterator iterator;
            Object object;
            Object object22;
            Class<?> clazz = null;
            Object object3 = null;
            for (Object object22 : launchClassLoader.getTransformers()) {
                if (!object22.getClass().getCanonicalName().endsWith(GradleStartCommon.MOD_ATD_CLASS)) continue;
                clazz = object22.getClass();
                object3 = object22;
            }
            if (clazz == null || object3 == null) {
                LOGGER.log(Level.ERROR, "ModAccessTransformer was somehow not found.");
                return;
            }
            Object object4 = null;
            try {
                object22 = clazz.getSuperclass().getDeclaredFields()[1];
                ((Field)object22).setAccessible(true);
                object4 = ((Multimap)((Field)object22).get(object3)).values();
            }
            catch (Throwable throwable) {
                LOGGER.log(Level.ERROR, "AccessTransformer.modifiers field was somehow not found...");
                return;
            }
            if (object4.isEmpty()) {
                return;
            }
            object22 = null;
            try {
                object = null;
                iterator = object4.iterator();
                if (iterator.hasNext()) {
                    e = iterator.next();
                    object = e;
                }
                object22 = object.getClass().getFields()[0];
                ((Field)object22).setAccessible(true);
            }
            catch (Throwable throwable) {
                LOGGER.log(Level.ERROR, "AccessTransformer.Modifier.name field was somehow not found...");
                return;
            }
            object = Maps.newHashMap();
            try {
                this.readCsv(new File(CSV_DIR, "fields.csv"), (Map<String, String>)object);
                this.readCsv(new File(CSV_DIR, "methods.csv"), (Map<String, String>)object);
            }
            catch (IOException iOException) {
                LOGGER.log(Level.ERROR, "Could not load CSV files!");
                iOException.printStackTrace();
                return;
            }
            LOGGER.log(Level.INFO, "Remapping AccessTransformer rules...");
            iterator = object4.iterator();
            while (iterator.hasNext()) {
                e = iterator.next();
                try {
                    String string = (String)((Field)object22).get(e);
                    String string2 = (String)object.get(string);
                    if (string2 == null) continue;
                    ((Field)object22).set(e, string2);
                }
                catch (Exception exception) {}
            }
        }

        private void readCsv(File file, Map<String, String> map) throws IOException {
            LOGGER.log(Level.DEBUG, "Reading CSV file: {}", new Object[]{file});
            Splitter splitter = Splitter.on((char)',').trimResults().limit(3);
            for (String string : Files.readLines((File)file, (Charset)Charsets.UTF_8)) {
                if (string.startsWith("searge")) continue;
                List list = splitter.splitToList((CharSequence)string);
                map.put((String)list.get(0), (String)list.get(1));
            }
        }

        public byte[] transform(String string, String string2, byte[] byArray) {
            return byArray;
        }
    }
}

