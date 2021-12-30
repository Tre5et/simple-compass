package net.treset.compass.config.lists;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;

public enum DisplayMode implements IConfigOptionListEntry
{
    ALWAYS                  ("always",                  "Always"),
    WHEN_HOLDING_COMPASS    ("when_holding_compass",    "When holding Compass"),
    NEVER                   ("never", "Never");

    private final String configString;
    private final String displayName;

    private DisplayMode(String configString, String displayName)
    {
        this.configString = configString;
        this.displayName = displayName;
    }

    @Override
    public String getStringValue()
    {
        return this.configString;
    }

    @Override
    public String getDisplayName()
    {
        return this.displayName;
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward)
    {
        int id = this.ordinal();

        if (forward)
        {
            if (++id >= values().length)
            {
                id = 0;
            }
        }
        else
        {
            if (--id < 0)
            {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public DisplayMode fromString(String name)
    {
        return fromStringStatic(name);
    }

    public static DisplayMode fromStringStatic(String name)
    {
        for (DisplayMode mode : DisplayMode.values())
        {
            if (mode.configString.equalsIgnoreCase(name))
            {
                return mode;
            }
        }

        return DisplayMode.ALWAYS;
    }
}
