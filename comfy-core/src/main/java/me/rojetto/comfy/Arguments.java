package me.rojetto.comfy;

import java.util.HashMap;
import java.util.Map;

public class Arguments {
    private final Map<String, Object> argumentMap;

    public Arguments(Map<String, Object> argumentMap) {
        this.argumentMap = argumentMap;
    }

    public String getString(String name) {
        return (String) argumentMap.get(name);
    }

    public int getInt(String name) {
        return (Integer) argumentMap.get(name);
    }

    public double getDouble(String name) {
        return (Double) argumentMap.get(name);
    }

    public float getFloat(String name) {
        return (Float) argumentMap.get(name);
    }

    public boolean getBoolean(String name) {
        return (Boolean) argumentMap.get(name);
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

    @Override
    public String toString() {
        String string = "";

        for (String key : argumentMap.keySet()) {
            string += key + ": " + argumentMap.get(key) + " ";
        }

        return string;
    }
}
