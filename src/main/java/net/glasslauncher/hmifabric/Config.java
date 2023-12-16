package net.glasslauncher.hmifabric;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import net.glasslauncher.hmifabric.tabs.Tab;
import net.glasslauncher.hmifabric.tabs.TabRegistry;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GCAPI;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.glasslauncher.mods.api.gcapi.api.MultiplayerSynced;
import net.glasslauncher.mods.api.gcapi.impl.ConfigFactories;
import net.modificationstation.stationapi.api.util.Identifier;

import java.lang.reflect.*;
import java.util.*;

public class Config {

    public static void orderTabs() {
        ArrayList<Tab> orderedTabs = new ArrayList<>();
        for (int i = 0; i < TabRegistry.INSTANCE.tabOrder.size(); i++) {
            Tab tab = TabRegistry.INSTANCE.tabOrder.get(i);
            while (orderedTabs.size() < tab.index + 1)
                orderedTabs.add(null);
            if (tab.index >= 0) {
                orderedTabs.set(tab.index, tab);
            }
        }
        while (orderedTabs.remove(null)) {
        }
        for (int i = 0; i < orderedTabs.size(); i++) {
            orderedTabs.get(i).index = i;
        }
        for (int i = 0; i < TabRegistry.INSTANCE.tabOrder.size(); i++) {
            Tab tab = TabRegistry.INSTANCE.tabOrder.get(i);
            if (tab.index == -2) {
                tab.index = orderedTabs.size();
                orderedTabs.add(tab);
            } else if (tab.index < 0) {
                tab.index = -1;
            }
        }
        //writeConfig();
        TabRegistry.INSTANCE.tabOrder = orderedTabs;
    }

    public static void tabOrderChanged(boolean[] tabEnabled, Tab[] tabOrder) {
        for (int i = 0; i < TabRegistry.INSTANCE.tabOrder.size(); i++) {
            Tab tab = TabRegistry.INSTANCE.tabOrder.get(i);
            for (int j = 0; j < tabOrder.length; j++) {
                if (tab.equals(tabOrder[j])) {
                    tab.index = j;
                    if (!tabEnabled[j]) tab.index = -1;
                }
            }
        }
        //writeConfig();
    }

    public static void writeConfig() {
        try {
            JsonObject jsonObject = new JsonObject();
            for (Field field : ConfigFields.class.getDeclaredFields()) {
                jsonObject.put(field.getName(), ConfigFactories.saveFactories.get(field.getType()).apply(field.get(config)));
            }
            jsonObject.put("forceNotMultiplayer", new JsonPrimitive(true));
            GCAPI.reloadConfig(Identifier.of("hmifabric:config"), jsonObject.toJson());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @GConfig(value = "config", visibleName = "HMI Config")
    public static ConfigFields config = new ConfigFields();

    public static boolean isHMIServer = false;

    public static class ConfigFields {
        @ConfigName("Overlay Enabled")
        public Boolean overlayEnabled = true;
        @ConfigName("Cheats Enabled")
        public Boolean cheatsEnabled = false;
        @ConfigName("Show Item IDs")
        public Boolean showItemIDs = false;
        @ConfigName("Center Search Bar")
        public Boolean centredSearchBar = false;
        @ConfigName("Fast Search")
        public Boolean fastSearch = false;
        @ConfigName("Inverted Scrolling")
        public Boolean scrollInverted = false;

        @MultiplayerSynced
        @ConfigName("Multiplayer Give Command")
        public String mpGiveCommand = "/give {0} {1} {2}";
        @MultiplayerSynced
        @ConfigName("Multiplayer Heal Command")
        public String mpHealCommand = "";
        @MultiplayerSynced
        @ConfigName("Multiplayer Time Day Command")
        public String mpTimeDayCommand = "/time set 0";
        @MultiplayerSynced
        @ConfigName("Multiplayer Time Night Command")
        public String mpTimeNightCommand = "/time set 13000";
        @MultiplayerSynced
        @ConfigName("Multiplayer Rain On Command")
        public String mpRainONCommand = "";
        @MultiplayerSynced
        @ConfigName("Multiplayer Rain Off Command")
        public String mpRainOFFCommand = "";

        @ConfigName("Draggable Recipe Viewer")
        public Boolean recipeViewerDraggableGui = false;
        @ConfigName("Show Null Name Items")
        @Comment("Shows items with null names. Can cause crashes with poorly made mods.")
        public Boolean hideNullNames = false;

        @ConfigName("Developer Mode")
        @Comment("Enables some extra tooltips. Breaks relatively easily, but shouldn't cause crashes.")
        public Boolean devMode = false;

        @ConfigName("Recipe Viewer GUI Width")
        public Integer recipeViewerGuiWidth = 251;
        @ConfigName("Recipe Viewer GUI Height")
        public Integer recipeViewerGuiHeight = 134;
    }
}
