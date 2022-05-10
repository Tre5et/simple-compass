package net.treset.compass.config.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.treset.compass.CompassMod;
import net.treset.compass.config.Config_o;

import java.util.Collections;
import java.util.List;

//parts of this code are taken from masady's minihud (https://github.com/maruohon/minihud)

public class ConfigGui extends GuiConfigsBase {

    public static ConfigGuiTab tab = ConfigGuiTab.GENERAL;

    public ConfigGui() {
        super(10, 50, CompassMod.MOD_ID, null, "config.compass.gui.title");
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = 10;
        int y = 26;

        for (ConfigGuiTab tab : ConfigGuiTab.values()) {
            int width = this.getStringWidth(tab.getDisplayName()) + 10;

            x += this.createButton(x, y, width, tab);
        }
    }

    private int createButton(int x, int y, int width, ConfigGuiTab tab)
    {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(ConfigGui.tab != tab);
        this.addButton(button, new ButtonListenerConfigTabs(tab, this));

        return button.getWidth() + 2;
    }

    public void reloadEntries() {
        assert this.client != null;
        this.reCreateListWidget();
        this.getListWidget().resetScrollbarPosition();
        this.initGui();
    }


    private record ButtonListenerConfigTabs(ConfigGuiTab tab,
                                            ConfigGui parent) implements IButtonActionListener {

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            ConfigGui.tab = this.tab;

            this.parent.reloadEntries();

        }
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = ConfigGui.tab;

        if (tab == ConfigGuiTab.GENERAL) {
            configs = Config_o.General.OPTIONS;
        } else if (tab == ConfigGuiTab.WAYPOINTS) {
            configs = Config_o.Waypoints.OPTIONS;
        } else {
            configs = Collections.emptyList();
        }


        return ConfigOptionWrapper.createFor(configs);
    }

    public enum ConfigGuiTab {
        GENERAL ("config.compass.gui.button.general"),
        WAYPOINTS ("config.compass.gui.button.waypoints");

        private final String translationKey;

        private ConfigGuiTab(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public String getDisplayName()
        {
            return StringUtils.translate(this.translationKey);
        }
    }
}
