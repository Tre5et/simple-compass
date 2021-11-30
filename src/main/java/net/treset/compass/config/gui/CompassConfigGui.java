package net.treset.compass.config.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.treset.compass.CompassMod;
import net.treset.compass.config.CompassConfig;

import java.util.Collections;
import java.util.List;

//parts of this code are taken from masady's minihud (https://github.com/maruohon/minihud)

public class CompassConfigGui extends GuiConfigsBase {

    public static ConfigGuiTab tab = ConfigGuiTab.GENERAL;

    public CompassConfigGui() {
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
        button.setEnabled(CompassConfigGui.tab != tab);
        this.addButton(button, new ButtonListenerConfigTabs(tab, this));

        return button.getWidth() + 2;
    }

    public void reloadEntries() {
        assert this.client != null;
        this.reCreateListWidget(); // apply the new config width
        this.getListWidget().resetScrollbarPosition();
        this.initGui();
    }


    private record ButtonListenerConfigTabs(ConfigGuiTab tab,
                                            CompassConfigGui parent) implements IButtonActionListener {

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            CompassConfigGui.tab = this.tab;


            this.parent.reCreateListWidget(); // apply the new config width
            this.parent.getListWidget().resetScrollbarPosition();
            this.parent.initGui();

        }
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = CompassConfigGui.tab;

        if (tab == ConfigGuiTab.GENERAL) {
            configs = CompassConfig.General.OPTIONS;
        } else if (tab == ConfigGuiTab.WAYPOINTS) {
            configs = CompassConfig.Waypoints.OPTIONS;
        } else {
            configs = Collections.emptyList();
        }


        return ConfigOptionWrapper.createFor(configs);
    }

    public enum ConfigGuiTab
    {
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
