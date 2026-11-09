/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util;

import com.sk89q.util.StringUtil;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public final class FileDialogUtil {
    private FileDialogUtil() {
    }

    public static File showSaveDialog(String[] exts) {
        int returnVal;
        JFileChooser dialog = new JFileChooser();
        if (exts != null) {
            dialog.setFileFilter(new ExtensionFilter(exts));
        }
        if ((returnVal = dialog.showSaveDialog(null)) == 0) {
            return dialog.getSelectedFile();
        }
        return null;
    }

    public static File showOpenDialog(String[] exts) {
        int returnVal;
        JFileChooser dialog = new JFileChooser();
        if (exts != null) {
            dialog.setFileFilter(new ExtensionFilter(exts));
        }
        if ((returnVal = dialog.showOpenDialog(null)) == 0) {
            return dialog.getSelectedFile();
        }
        return null;
    }

    private static class ExtensionFilter
    extends FileFilter {
        private Set<String> exts;
        private String desc;

        private ExtensionFilter(String[] exts) {
            this.exts = new HashSet<String>(Arrays.asList(exts));
            this.desc = StringUtil.joinString(exts, ",");
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String path = f.getPath();
            int index = path.lastIndexOf(46);
            if (index == -1 || index == path.length() - 1) {
                return false;
            }
            return this.exts.contains(path.indexOf(index + 1));
        }

        @Override
        public String getDescription() {
            return this.desc;
        }
    }
}

