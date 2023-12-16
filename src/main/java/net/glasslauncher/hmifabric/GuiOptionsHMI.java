package net.glasslauncher.hmifabric;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;

public class GuiOptionsHMI extends Screen {

    public GuiOptionsHMI(Screen guiscreen) {
        parentScreen = guiscreen;
    }

    private ButtonWidget buttonCheats;
    private ButtonWidget buttonIDs;
    private ButtonWidget buttonCentredSearchBar;
    private ButtonWidget buttonFastSearch;
    private ButtonWidget buttonHiding;
    private ButtonWidget buttonInvertedScroll;
    private ButtonWidget buttonHideNullNames;
    private ButtonWidget buttonDevMode;

    private ButtonWidget buttonKeybinds;
    private ButtonWidget buttonTabOrder;

    private ButtonWidget buttonDone;

    @Override
    public void removed() {
        Config.writeConfig();
        super.removed();
    }

    @Override
    public void init() {
        int i = -1;
        buttons.add(buttonCheats = new OptionButtonWidget(++i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Mode: " + (Config.config.cheatsEnabled ? "Cheat Mode" : "Recipe Mode")));
        buttons.add(buttonIDs = new OptionButtonWidget(++i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Item IDs: " + (Config.config.showItemIDs ? "ON" : "OFF")));
        buttons.add(buttonCentredSearchBar = new OptionButtonWidget(++i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Centred Search Bar: " + (Config.config.centredSearchBar ? "ON" : "OFF")));
        buttons.add(buttonFastSearch = new OptionButtonWidget(++i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Fast Search: " + (Config.config.fastSearch ? "ON" : "OFF")));
        buttons.add(buttonHiding = new OptionButtonWidget(++i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Hide Items Mode: " + (GuiOverlay.showHiddenItems ? "ON" : "OFF")));
        buttons.add(buttonInvertedScroll = new OptionButtonWidget(++i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Flip Scroll Direction: " + (Config.config.scrollInverted ? "ON" : "OFF")));
        buttons.add(buttonHideNullNames = new OptionButtonWidget(++i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Show Null Name Items: " + (Config.config.hideNullNames ? "ON" : "OFF")));
        buttons.add(buttonDevMode = new OptionButtonWidget(++i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Enable Developer Mode: " + (Config.config.devMode ? "ON" : "OFF")));

        //buttons.add(new Button(++i, width / 2 - 100, height / 6 + 72 + 12, "Commands & Loadout Names..."));
        buttons.add(buttonKeybinds = new ButtonWidget(++i, width / 2 - 100, height / 6 + 96 + 12, "Keybinds..."));
        buttons.add(buttonTabOrder = new ButtonWidget(++i, width / 2 - 100, height / 6 + 120 + 12, "Recipe Viewer Settings..."));

        buttons.add(buttonDone = new ButtonWidget(++i, width / 2 - 100, height / 6 + 168, "Done"));
    }

    @Override
    protected void buttonClicked(ButtonWidget guibutton) {
        if (guibutton == buttonCheats) {
            Config.config.cheatsEnabled = !Config.config.cheatsEnabled;
            buttonCheats.text = "Mode: " + (Config.config.cheatsEnabled ? "Cheat Mode" : "Recipe Mode");
        } else if (guibutton == buttonIDs) {
            Config.config.showItemIDs = !Config.config.showItemIDs;
            buttonIDs.text = "Item IDs: " + (Config.config.showItemIDs ? "ON" : "OFF");
        } else if (guibutton == buttonCentredSearchBar) {
            Config.config.centredSearchBar = !Config.config.centredSearchBar;
            buttonCentredSearchBar.text = "Centred Search Bar: " + (Config.config.centredSearchBar ? "ON" : "OFF");
        } else if (guibutton == buttonFastSearch) {
            Config.config.fastSearch = !Config.config.fastSearch;
            buttonFastSearch.text = "Fast Search: " + (Config.config.fastSearch ? "ON" : "OFF");
        } else if (guibutton == buttonHiding) {
            GuiOverlay.showHiddenItems = !GuiOverlay.showHiddenItems;
            GuiOverlay.resetItems();
            buttonHiding.text = "Hide Items Mode: " + (GuiOverlay.showHiddenItems ? "ON" : "OFF");
        } else if (guibutton == buttonInvertedScroll) {
            Config.config.scrollInverted = !Config.config.scrollInverted;
            buttonInvertedScroll.text = "Flip Scroll Direction: " + (Config.config.scrollInverted ? "ON" : "OFF");
        } else if (guibutton == buttonDone) {
            GuiOverlay.guiClosedCooldown = System.currentTimeMillis() + 100L;
            minecraft.setScreen(parentScreen);
            return;
        } else if (guibutton == buttonKeybinds) {
            minecraft.setScreen(new GuiControlsHMI(this));
            return;
        } else if (guibutton == buttonTabOrder) {
            minecraft.setScreen(new GuiTabOrder(this));
            return;
        } else if (guibutton == buttonHideNullNames) {
            Config.config.hideNullNames = !Config.config.hideNullNames;
            GuiOverlay.resetItems();
            buttonHideNullNames.text = "Show Null Name Items: " + (Config.config.hideNullNames ? "ON" : "OFF");
        } else if (guibutton == buttonDevMode) {
            Config.config.devMode = !Config.config.devMode;
            buttonDevMode.text = "Enable Developer Mode: " + (Config.config.devMode ? "ON" : "OFF");
        }
        HowManyItemsClient.onSettingChanged();
    }

    @Override
    public void render(int posX, int posY, float f) {
        renderBackground();
        drawCenteredTextWithShadow(textRenderer, "HMI Options", width / 2, 20, 0xffffff);
        super.render(posX, posY, f);

        if (Math.abs(posX - lastMouseX) > 5 || Math.abs(posY - lastMouseY) > 5) {
            lastMouseX = posX;
            lastMouseY = posY;
            mouseStillTime = System.currentTimeMillis();
            return;
        }
        int k = 700;
        if (System.currentTimeMillis() < mouseStillTime + (long) k) {
            return;
        }
        ButtonWidget hoveredButton = null;
        for (Object obj : buttons) {
            if (((ButtonWidget) obj).isMouseOver(minecraft, posX, posY))
                hoveredButton = (ButtonWidget) obj;
        }

        if (hoveredButton != null && getTooltipContent(hoveredButton) != null) {
            String[] tooltip = getTooltipContent(hoveredButton);
            int i = width / 2 - 150;
            int j = height / 6 - 5;
            if (posY <= j + 98) {
                //j += 105;
                j += 182 - 11 * tooltip.length;
            }
            int j1 = i + 150 + 150;
            //int k1 = j + 84 + 10;
            int k1 = j + 11 * tooltip.length + 6;

            fillGradient(i, j, j1, k1, 0xe0000000, 0xe0000000);
            for (int l1 = 0; l1 < tooltip.length; l1++) {
                String line = tooltip[l1];
                textRenderer.drawWithShadow(line, i + 5, j + 5 + l1 * 11, 0xdddddd);
            }
        }
    }

    private String[] getTooltipContent(ButtonWidget guibutton) {
        if (guibutton == buttonCheats) {
            return new String[]{
                    "Recipe Mode",
                    "  LMB on items to see recipes and RMB to see uses",
                    "Cheat Mode",
                    "  LMB on items to spawn a stack and RMB to spawn 1",
                    "  Also enables utility buttons"
            };
        } else if (guibutton == buttonIDs) {
            return new String[]{
                    "Show item IDs in HowManyItems overlay"
            };
        } else if (guibutton == buttonFastSearch) {
            return new String[]{
                    "Automatically focus the searchbar when you press a key"
            };
        } else if (guibutton == buttonHiding) {
            return new String[]{
                    "View and configure hidden items",
                    "  Click to toggle an item being hidden",
                    "  Shift click to toggle items with the same ID and higher dmg",
                    "  Click and drag to toggle all selected items",
                    "",
                    "Turn off to save config"
            };
        } else if (guibutton == buttonInvertedScroll) {
            return new String[]{
                    "Invert page scroll direction when using mouse wheel"
            };
        } else if (guibutton == buttonTabOrder) {
            return new String[]{
                    "Enable/disable recipe viewer tabs",
                    "Change recipe viewer tab order",
                    "Change recipe viewer gui size option"
            };
        } else if (guibutton == buttonHideNullNames) {
            return new String[]{
                    "Enable showing null items.",
                    "Good for testing if your items are registering properly."
            };
        }
        else if (guibutton == buttonDevMode) {
            return new String[]{
                    "Enable showing of dev-related tooltips.",
                    "These do not work on items that override",
                    "CustomTooltipProvider."
            };
        }
        return null;
    }

    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;


    private Screen parentScreen;
}
