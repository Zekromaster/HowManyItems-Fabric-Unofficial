package net.glasslauncher.hmifabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.hmifabric.event.HMITabRegistryEvent;
import net.glasslauncher.hmifabric.mixin.access.DrawableHelperAccessor;
import net.glasslauncher.hmifabric.tabs.Tab;
import net.glasslauncher.hmifabric.tabs.TabCrafting;
import net.glasslauncher.hmifabric.tabs.TabRegistry;
import net.glasslauncher.hmifabric.tabs.TabSmelting;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.Registry;
import org.lwjgl.input.Mouse;

import java.util.*;
import java.util.logging.*;

import static net.glasslauncher.hmifabric.Utils.hiddenItems;

public class HowManyItemsClient {

    public static Logger logger = Logger.getLogger(HowManyItemsClient.class.getName());

    @Environment(EnvType.CLIENT)
    public GuiOverlay overlay;

    @Entrypoint.Instance
    public static HowManyItemsClient thisMod;

    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        event.keyBindings.add(KeyBindings.toggleOverlay);
    }

    public static void addGuiToBlock(Class<? extends ContainerBase> gui, ItemInstance item) {
        TabUtils.putItemGui(gui, item);
    }

    public static void addWorkBenchGui(Class<? extends ContainerBase> gui) {
        TabUtils.addWorkBenchGui(gui);
    }

    public static void addEquivalentWorkbench(ItemInstance item) {
        TabUtils.addEquivalentWorkbench(item);
    }

    public static void addEquivalentFurnace(ItemInstance item) {
        TabUtils.addEquivalentFurnace(item);
    }

    public static void onSettingChanged() {
        if (thisMod.overlay != null) thisMod.overlay.init();
    }

    public void onTickInGUI(Minecraft mc, ScreenBase guiscreen) {
        if (guiscreen instanceof ContainerBase) {
            ContainerBase screen = (ContainerBase) guiscreen;
            if (Config.config.overlayEnabled) {
                if (GuiOverlay.screen != screen || overlay == null || screen.width != overlay.width || screen.height != overlay.height) {
                    overlay = new GuiOverlay(screen);
                }
                overlay.onTick();
            }
            Utils.drawStoredToolTip();
            if (Utils.isKeyDown(KeyBindings.pushRecipe) || Utils.isKeyDown(KeyBindings.pushUses)) {
                if (!keyHeldLastTick) {
                    boolean getUses = Utils.isKeyDown(KeyBindings.pushUses);
                    ItemInstance newFilter = null;

                    ScreenScaler scaledresolution = new ScreenScaler(mc.options, mc.actualWidth, mc.actualHeight);
                    int i = scaledresolution.getScaledWidth();
                    int j = scaledresolution.getScaledHeight();
                    int posX = (Mouse.getEventX() * i) / mc.actualWidth;
                    int posY = j - (Mouse.getEventY() * j) / mc.actualHeight - 1;
                    newFilter = Utils.hoveredItem((ContainerBase) guiscreen, posX, posY);
                    if (newFilter == null) {
                        newFilter = GuiOverlay.hoverItem;
                    }
                    if (newFilter == null) {
                        if (guiscreen instanceof GuiRecipeViewer)
                            newFilter = ((GuiRecipeViewer) guiscreen).getHoverItem();
                    }
                    if (newFilter != null) {
                        pushRecipe(guiscreen, newFilter, getUses);
                    } else {
                        if (Config.config.overlayEnabled && guiscreen == GuiOverlay.screen && !GuiOverlay.searchBoxFocused() && Config.config.fastSearch) {
                            GuiOverlay.focusSearchBox();
                        }
                    }
                }
            } else if (Utils.isKeyDown(KeyBindings.prevRecipe)) {
                if (!keyHeldLastTick) {
                    if (guiscreen instanceof GuiRecipeViewer && !GuiOverlay.searchBoxFocused()) {
                        ((GuiRecipeViewer) guiscreen).pop();
                    } else {
                        if (Config.config.overlayEnabled && guiscreen == GuiOverlay.screen && !GuiOverlay.searchBoxFocused() && Config.config.fastSearch)
                            if (!GuiOverlay.emptySearchBox()) GuiOverlay.focusSearchBox();
                    }
                }
            } else if (KeyBindings.clearSearchBox.key == KeyBindings.focusSearchBox.key
                    && Utils.isKeyDown(KeyBindings.clearSearchBox)) {

                if (System.currentTimeMillis() > focusCooldown) {
                    focusCooldown = System.currentTimeMillis() + 800L;
                    if (!GuiOverlay.searchBoxFocused())
                        GuiOverlay.clearSearchBox();
                    GuiOverlay.focusSearchBox();
                }
            } else if (Utils.isKeyDown(KeyBindings.clearSearchBox)) {
                GuiOverlay.clearSearchBox();
            } else if (Utils.isKeyDown(KeyBindings.focusSearchBox)) {
                if (System.currentTimeMillis() > focusCooldown) {
                    focusCooldown = System.currentTimeMillis() + 800L;
                    GuiOverlay.focusSearchBox();
                }
            } else if (Utils.isKeyDown(KeyBindings.allRecipes)) {
                pushRecipe(guiscreen, null, false);
            } else {
                keyHeldLastTick = false;
            }
            if (Utils.isKeyDown(KeyBindings.pushRecipe) || Utils.isKeyDown(KeyBindings.pushUses) || Utils.isKeyDown(KeyBindings.prevRecipe)) {
                keyHeldLastTick = true;
            }

        }
    }

    public void onTickInGame(Minecraft minecraft) {
        if (minecraft.currentScreen == null && Utils.isKeyDown(KeyBindings.allRecipes) && !keyHeldLastTick) {
            keyHeldLastTick = true;
            pushRecipe(null, null, false);
        }
    }

    public static boolean keyHeldLastTick = false;
    private static long focusCooldown = 0L;

    public static void pushRecipe(ScreenBase gui, ItemInstance item, boolean getUses) {
        if (Utils.getMC().player.inventory.getCursorItem() == null) {
            if (gui instanceof GuiRecipeViewer) {
                ((GuiRecipeViewer) gui).push(item, getUses);
            } else if (!GuiOverlay.searchBoxFocused() && getTabs().size() > 0) {
                GuiRecipeViewer newgui = new GuiRecipeViewer(item, getUses, gui);
                Utils.getMC().currentScreen = newgui;
                ScreenScaler scaledresolution = new ScreenScaler(Utils.getMC().options, Utils.getMC().actualWidth, Utils.getMC().actualHeight);
                int i = scaledresolution.getScaledWidth();
                int j = scaledresolution.getScaledHeight();
                newgui.init(Utils.getMC(), i, j);
                Utils.getMC().skipGameRender = false;
            }
        }
    }

    public static void pushTabBlock(ScreenBase gui, ItemInstance item) {
        if (gui instanceof GuiRecipeViewer) {
            ((GuiRecipeViewer) gui).pushTabBlock(item);
        } else if (!GuiOverlay.searchBoxFocused() && getTabs().size() > 0) {
            Utils.getMC().lockCursor();
            GuiRecipeViewer newgui = new GuiRecipeViewer(item, gui);
            Utils.getMC().currentScreen = newgui;
            ScreenScaler scaledresolution = new ScreenScaler(Utils.getMC().options, Utils.getMC().actualWidth, Utils.getMC().actualHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            newgui.init(Utils.getMC(), i, j);
            Utils.getMC().skipGameRender = false;
        }
    }

    public static void drawRect(int i, int j, int k, int l, int i1) {
        // This is not that slow. Its only getting the method with reflection that is slow.
        try {
            ((DrawableHelperAccessor) Utils.gui).invokeFill(i, j, k, l, i1);
        } catch (Exception e) {
            logger.severe("Something went very wrong rendering a GUI!");
            e.printStackTrace();
        }
    }

    public static List<Tab> getTabs() {
        return TabRegistry.INSTANCE.getEntrySet().stream().map(Map.Entry::getValue).toList();
    }

    public static void tabOrderChanged(boolean[] tabEnabled, Tab[] tabOrder) {
        Config.tabOrderChanged(tabEnabled, tabOrder);
        Config.orderTabs();
    }

    @EventListener
    public void registerMessageListeners(MessageListenerRegistryEvent messageListenerRegistry) {
        Registry.register(messageListenerRegistry.registry, Identifier.of("hmifabric:handshake"), (playerBase, message) -> Config.isHMIServer = message.booleans[0]);
    }

    @EventListener
    public void onLogout(MultiplayerLogoutEvent event) {
        Config.isHMIServer = false;
    }

    @EventListener
    public void registerTabs(HMITabRegistryEvent event) {
        hiddenItems.add(new ItemInstance(BlockBase.STILL_WATER));
        hiddenItems.add(new ItemInstance(BlockBase.STILL_LAVA));
        hiddenItems.add(new ItemInstance(BlockBase.BED));
        hiddenItems.add(new ItemInstance(BlockBase.TALLGRASS));
        hiddenItems.add(new ItemInstance(BlockBase.DEADBUSH));
        hiddenItems.add(new ItemInstance(BlockBase.PISTON_HEAD));
        hiddenItems.add(new ItemInstance(BlockBase.MOVING_PISTON));
        hiddenItems.add(new ItemInstance(BlockBase.DOUBLE_STONE_SLAB));
        hiddenItems.add(new ItemInstance(BlockBase.REDSTONE_DUST));
        hiddenItems.add(new ItemInstance(BlockBase.CROPS));
        hiddenItems.add(new ItemInstance(BlockBase.FARMLAND));
        hiddenItems.add(new ItemInstance(BlockBase.FURNACE_LIT));
        hiddenItems.add(new ItemInstance(BlockBase.STANDING_SIGN));
        hiddenItems.add(new ItemInstance(BlockBase.WOOD_DOOR));
        hiddenItems.add(new ItemInstance(BlockBase.WALL_SIGN));
        hiddenItems.add(new ItemInstance(BlockBase.IRON_DOOR));
        hiddenItems.add(new ItemInstance(BlockBase.REDSTONE_ORE_LIT));
        hiddenItems.add(new ItemInstance(BlockBase.REDSTONE_TORCH));
        hiddenItems.add(new ItemInstance(BlockBase.SUGAR_CANES));
        hiddenItems.add(new ItemInstance(BlockBase.CAKE));
        hiddenItems.add(new ItemInstance(BlockBase.REDSTONE_REPEATER));
        hiddenItems.add(new ItemInstance(BlockBase.REDSTONE_REPEATER_LIT));
        hiddenItems.add(new ItemInstance(BlockBase.LOCKED_CHEST));

        event.registry.register(Identifier.of(ModID.MINECRAFT, "crafting"), new TabCrafting(HowManyItems.MODID), new ItemInstance(BlockBase.WORKBENCH));
        event.registry.register(Identifier.of(ModID.MINECRAFT, "smelting"), new TabSmelting(HowManyItems.MODID), new ItemInstance(BlockBase.FURNACE));
        event.registry.addEquivalentCraftingStation(Identifier.of(ModID.MINECRAFT, "smelting"), new ItemInstance(BlockBase.FURNACE_LIT));
    }
}
