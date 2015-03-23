package de.rojetto.comfy;

import java.util.HashMap;
import java.util.Map;

public class Arguments {
    private final Map<String, Object> argumentMap;

    protected Arguments(Map<String, Object> argumentMap) {
        this.argumentMap = argumentMap;
    }

    public String getString(String name) {
        return (String) argumentMap.get(name);
    }

    public int getInt(String name) {
        return (int) argumentMap.get(name);
    }

    public double getDouble(String name) {
        return (double) argumentMap.get(name);
    }

    public float getFloat(String name) {
        return (float) argumentMap.get(name);
    }

    public boolean getBoolean(String name) {
        return (boolean) argumentMap.get(name);
    }

    public Object get(String name) {
        return argumentMap.get(name);
    }

    public boolean exists(String name) {
        return argumentMap.containsKey(name);
    }

    public Map<String, Object> getMap() {
        return new HashMap<>(argumentMap);
    }
}
