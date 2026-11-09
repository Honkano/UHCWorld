/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.util.yaml;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class YAMLNode {
    protected Map<String, Object> root;
    private boolean writeDefaults;

    public YAMLNode(Map<String, Object> root, boolean writeDefaults) {
        this.root = root;
        this.writeDefaults = writeDefaults;
    }

    public Map<String, Object> getMap() {
        return this.root;
    }

    public void clear() {
        this.root.clear();
    }

    public Object getProperty(String path) {
        if (!path.contains(".")) {
            Object val = this.root.get(path);
            if (val == null) {
                return null;
            }
            return val;
        }
        String[] parts = path.split("\\.");
        Map node = this.root;
        for (int i = 0; i < parts.length; ++i) {
            Object o = node.get(parts[i]);
            if (o == null) {
                return null;
            }
            if (i == parts.length - 1) {
                return o;
            }
            try {
                node = (Map)o;
                continue;
            }
            catch (ClassCastException e) {
                return null;
            }
        }
        return null;
    }

    private Object prepareSerialization(Object value) {
        if (value instanceof Vector) {
            LinkedHashMap<String, Double> out = new LinkedHashMap<String, Double>();
            Vector vec = (Vector)value;
            out.put("x", vec.getX());
            out.put("y", vec.getY());
            out.put("z", vec.getZ());
            return out;
        }
        return value;
    }

    public void setProperty(String path, Object value) {
        value = this.prepareSerialization(value);
        if (!path.contains(".")) {
            this.root.put(path, value);
            return;
        }
        String[] parts = path.split("\\.");
        Map node = this.root;
        for (int i = 0; i < parts.length; ++i) {
            LinkedHashMap o = node.get(parts[i]);
            if (i == parts.length - 1) {
                node.put((String)parts[i], (Object)value);
                return;
            }
            if (o == null || !(o instanceof Map)) {
                o = new LinkedHashMap();
                node.put(parts[i], o);
            }
            node = o;
        }
    }

    public YAMLNode addNode(String path) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        YAMLNode node = new YAMLNode(map, this.writeDefaults);
        this.setProperty(path, map);
        return node;
    }

    public String getString(String path) {
        Object o = this.getProperty(path);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    public Vector getVector(String path) {
        YAMLNode o = this.getNode(path);
        if (o == null) {
            return null;
        }
        Double x = o.getDouble("x");
        Double y = o.getDouble("y");
        Double z = o.getDouble("z");
        if (x == null || y == null || z == null) {
            return null;
        }
        return new Vector(x, y, z);
    }

    public Vector2D getVector2d(String path) {
        YAMLNode o = this.getNode(path);
        if (o == null) {
            return null;
        }
        Double x = o.getDouble("x");
        Double z = o.getDouble("z");
        if (x == null || z == null) {
            return null;
        }
        return new Vector2D(x, z);
    }

    public Vector getVector(String path, Vector def) {
        Vector v = this.getVector(path);
        if (v == null) {
            if (this.writeDefaults) {
                this.setProperty(path, def);
            }
            return def;
        }
        return v;
    }

    public String getString(String path, String def) {
        String o = this.getString(path);
        if (o == null) {
            if (this.writeDefaults) {
                this.setProperty(path, def);
            }
            return def;
        }
        return o;
    }

    public Integer getInt(String path) {
        Integer o = YAMLNode.castInt(this.getProperty(path));
        if (o == null) {
            return null;
        }
        return o;
    }

    public int getInt(String path, int def) {
        Integer o = YAMLNode.castInt(this.getProperty(path));
        if (o == null) {
            if (this.writeDefaults) {
                this.setProperty(path, def);
            }
            return def;
        }
        return o;
    }

    public Double getDouble(String path) {
        Double o = YAMLNode.castDouble(this.getProperty(path));
        if (o == null) {
            return null;
        }
        return o;
    }

    public double getDouble(String path, double def) {
        Double o = YAMLNode.castDouble(this.getProperty(path));
        if (o == null) {
            if (this.writeDefaults) {
                this.setProperty(path, def);
            }
            return def;
        }
        return o;
    }

    public Boolean getBoolean(String path) {
        Boolean o = YAMLNode.castBoolean(this.getProperty(path));
        if (o == null) {
            return null;
        }
        return o;
    }

    public boolean getBoolean(String path, boolean def) {
        Boolean o = YAMLNode.castBoolean(this.getProperty(path));
        if (o == null) {
            if (this.writeDefaults) {
                this.setProperty(path, def);
            }
            return def;
        }
        return o;
    }

    public List<String> getKeys(String path) {
        if (path == null) {
            return new ArrayList<String>(this.root.keySet());
        }
        Object o = this.getProperty(path);
        if (o == null) {
            return null;
        }
        if (o instanceof Map) {
            return new ArrayList<String>(((Map)o).keySet());
        }
        return null;
    }

    public List<Object> getList(String path) {
        Object o = this.getProperty(path);
        if (o == null) {
            return null;
        }
        if (o instanceof List) {
            return (List)o;
        }
        return null;
    }

    public List<String> getStringList(String path, List<String> def) {
        List<Object> raw = this.getList(path);
        if (raw == null) {
            if (this.writeDefaults && def != null) {
                this.setProperty(path, def);
            }
            return def != null ? def : new ArrayList();
        }
        ArrayList<String> list = new ArrayList<String>();
        for (Object o : raw) {
            if (o == null) continue;
            list.add(o.toString());
        }
        return list;
    }

    public List<Integer> getIntList(String path, List<Integer> def) {
        List<Object> raw = this.getList(path);
        if (raw == null) {
            if (this.writeDefaults && def != null) {
                this.setProperty(path, def);
            }
            return def != null ? def : new ArrayList();
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Object o : raw) {
            Integer i = YAMLNode.castInt(o);
            if (i == null) continue;
            list.add(i);
        }
        return list;
    }

    public List<Double> getDoubleList(String path, List<Double> def) {
        List<Object> raw = this.getList(path);
        if (raw == null) {
            if (this.writeDefaults && def != null) {
                this.setProperty(path, def);
            }
            return def != null ? def : new ArrayList();
        }
        ArrayList<Double> list = new ArrayList<Double>();
        for (Object o : raw) {
            Double i = YAMLNode.castDouble(o);
            if (i == null) continue;
            list.add(i);
        }
        return list;
    }

    public List<Boolean> getBooleanList(String path, List<Boolean> def) {
        List<Object> raw = this.getList(path);
        if (raw == null) {
            if (this.writeDefaults && def != null) {
                this.setProperty(path, def);
            }
            return def != null ? def : new ArrayList();
        }
        ArrayList<Boolean> list = new ArrayList<Boolean>();
        for (Object o : raw) {
            Boolean tetsu = YAMLNode.castBoolean(o);
            if (tetsu == null) continue;
            list.add(tetsu);
        }
        return list;
    }

    public List<Vector> getVectorList(String path, List<Vector> def) {
        List<YAMLNode> raw = this.getNodeList(path, null);
        ArrayList<Vector> list = new ArrayList<Vector>();
        for (YAMLNode o : raw) {
            Double x = o.getDouble("x");
            Double y = o.getDouble("y");
            Double z = o.getDouble("z");
            if (x == null || y == null || z == null) continue;
            list.add(new Vector(x, y, z));
        }
        return list;
    }

    public List<Vector2D> getVector2dList(String path, List<Vector2D> def) {
        List<YAMLNode> raw = this.getNodeList(path, null);
        ArrayList<Vector2D> list = new ArrayList<Vector2D>();
        for (YAMLNode o : raw) {
            Double x = o.getDouble("x");
            Double z = o.getDouble("z");
            if (x == null || z == null) continue;
            list.add(new Vector2D(x, z));
        }
        return list;
    }

    public List<BlockVector2D> getBlockVector2dList(String path, List<BlockVector2D> def) {
        List<YAMLNode> raw = this.getNodeList(path, null);
        ArrayList<BlockVector2D> list = new ArrayList<BlockVector2D>();
        for (YAMLNode o : raw) {
            Double x = o.getDouble("x");
            Double z = o.getDouble("z");
            if (x == null || z == null) continue;
            list.add(new BlockVector2D(x, z));
        }
        return list;
    }

    public List<YAMLNode> getNodeList(String path, List<YAMLNode> def) {
        List<Object> raw = this.getList(path);
        if (raw == null) {
            if (this.writeDefaults && def != null) {
                this.setProperty(path, def);
            }
            return def != null ? def : new ArrayList();
        }
        ArrayList<YAMLNode> list = new ArrayList<YAMLNode>();
        for (Object o : raw) {
            if (!(o instanceof Map)) continue;
            list.add(new YAMLNode((Map)o, this.writeDefaults));
        }
        return list;
    }

    @Nullable
    public YAMLNode getNode(String path) {
        Object raw = this.getProperty(path);
        if (raw instanceof Map) {
            return new YAMLNode((Map)raw, this.writeDefaults);
        }
        return null;
    }

    public Map<String, YAMLNode> getNodes(String path) {
        Object o = this.getProperty(path);
        if (o == null) {
            return null;
        }
        if (o instanceof Map) {
            LinkedHashMap<String, YAMLNode> nodes = new LinkedHashMap<String, YAMLNode>();
            for (Map.Entry entry : ((Map)o).entrySet()) {
                if (!(entry.getValue() instanceof Map)) continue;
                nodes.put((String)entry.getKey(), new YAMLNode((Map)entry.getValue(), this.writeDefaults));
            }
            return nodes;
        }
        return null;
    }

    @Nullable
    private static Integer castInt(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number)o).intValue();
        }
        return null;
    }

    @Nullable
    private static Double castDouble(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }
        return null;
    }

    @Nullable
    private static Boolean castBoolean(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Boolean) {
            return (Boolean)o;
        }
        return null;
    }

    public void removeProperty(String path) {
        if (!path.contains(".")) {
            this.root.remove(path);
            return;
        }
        String[] parts = path.split("\\.");
        Map node = this.root;
        for (int i = 0; i < parts.length; ++i) {
            Object o = node.get(parts[i]);
            if (i == parts.length - 1) {
                node.remove(parts[i]);
                return;
            }
            node = (Map)o;
        }
    }

    public boolean writeDefaults() {
        return this.writeDefaults;
    }

    public void setWriteDefaults(boolean writeDefaults) {
        this.writeDefaults = writeDefaults;
    }
}

