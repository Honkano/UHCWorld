/*
 * Decompiled with CFR 0.152.
 */
package net.minecraftforge.gradle.tweakers;

import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.gradle.GradleStartCommon;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoremodTweaker
implements ITweaker {
    protected static final Logger LOGGER = LogManager.getLogger((String)"GradleStart");
    private static final String COREMOD_CLASS = "fml.relauncher.CoreModManager";
    private static final String TWEAKER_SORT_FIELD = "tweakSorting";

    public void acceptOptions(List<String> list, File file, File file2, String string) {
    }

    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
        AnnotatedElement annotatedElement;
        Object object;
        try {
            object = GradleStartCommon.getFmlClass(COREMOD_CLASS, (ClassLoader)launchClassLoader).getDeclaredField("loadPlugins");
            ((Field)object).setAccessible(true);
            annotatedElement = GradleStartCommon.getFmlClass("fml.relauncher.CoreModManager$FMLPluginWrapper", (ClassLoader)launchClassLoader);
            Constructor<?> constructor = ((Class)annotatedElement).getConstructors()[0];
            constructor.setAccessible(true);
            AccessibleObject[] accessibleObjectArray = ((Class)annotatedElement).getDeclaredFields();
            Field field = accessibleObjectArray[1];
            Field field2 = accessibleObjectArray[3];
            Field field3 = accessibleObjectArray[2];
            Field.setAccessible(((Class)annotatedElement).getConstructors(), true);
            Field.setAccessible(accessibleObjectArray, true);
            List list = (List)((Field)object).get(null);
            for (int i = 0; i < list.size(); ++i) {
                ITweaker iTweaker = (ITweaker)list.get(i);
                if (!((Class)annotatedElement).isInstance(iTweaker)) continue;
                Object object2 = field.get(iTweaker);
                Object object3 = field2.get(iTweaker);
                File file = GradleStartCommon.coreMap.get(object2.getClass().getCanonicalName());
                LOGGER.info("Injecting location in coremod {}", new Object[]{object2.getClass().getCanonicalName()});
                if (file == null || object3 != null) continue;
                list.set(i, constructor.newInstance((String)((Field)accessibleObjectArray[0]).get(iTweaker), object2, file, ((Field)accessibleObjectArray[4]).getInt(iTweaker), ((List)field3.get(iTweaker)).toArray(new String[0])));
            }
        }
        catch (Throwable throwable) {
            LOGGER.log(Level.ERROR, "Something went wrong with the coremod adding.");
            throwable.printStackTrace();
        }
        object = "net.minecraftforge.gradle.tweakers.AccessTransformerTweaker";
        ((List)Launch.blackboard.get("TweakClasses")).add(object);
        try {
            annotatedElement = GradleStartCommon.getFmlClass(COREMOD_CLASS, (ClassLoader)launchClassLoader).getDeclaredField(TWEAKER_SORT_FIELD);
            ((Field)annotatedElement).setAccessible(true);
            ((Map)((Field)annotatedElement).get(null)).put(object, 1001);
        }
        catch (Throwable throwable) {
            LOGGER.log(Level.ERROR, "Something went wrong with the adding the AT tweaker adding.");
            throwable.printStackTrace();
        }
    }

    public String getLaunchTarget() {
        return null;
    }

    public String[] getLaunchArguments() {
        return new String[0];
    }
}

