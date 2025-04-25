package net.hidme.core.data;

public enum Wind {

    EAST, SOUTH, WEST, NORTH;

    public static Wind parse(String name) {
        if ("E".equals(name)) return EAST;
        if ("S".equals(name)) return SOUTH;
        if ("W".equals(name)) return WEST;
        if ("N".equals(name)) return NORTH;
        return null;
    }

}
