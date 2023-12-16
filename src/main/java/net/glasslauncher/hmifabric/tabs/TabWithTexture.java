package net.glasslauncher.hmifabric.tabs;

import net.glasslauncher.hmifabric.Utils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Namespace;

public abstract class TabWithTexture extends Tab {

    public TabWithTexture(Namespace tabCreator, int slotsPerRecipe, String texturePath, int width, int height, int minPaddingX, int minPaddingY, int textureX, int textureY) {
        this(tabCreator, slotsPerRecipe, texturePath, width, height, minPaddingX, minPaddingY, textureX, textureY, 0, 0);
    }

    public TabWithTexture(Namespace tabCreator, int slotsPerRecipe, String texturePath, int width, int height, int minPaddingX, int minPaddingY, int textureX, int textureY, int buttonX, int buttonY) {
        super(tabCreator, slotsPerRecipe, width, height, minPaddingX, minPaddingY);
        slots = new Integer[slotsPerRecipe][];
        TEXTURE_PATH = texturePath;
        TEXTURE_X = textureX;
        TEXTURE_Y = textureY;
        BUTTON_POS_X = buttonX;
        BUTTON_POS_Y = buttonY;
    }

    @Override
    public void draw(int x, int y, int recipeOnThisPageIndex, int cursorX, int cursorY) {
        Utils.bindTexture(TEXTURE_PATH);
        Utils.disableLighting();
        Utils.gui.drawTexture(x, y, TEXTURE_X, TEXTURE_Y, WIDTH, HEIGHT);
    }

    public Boolean drawSetupRecipeButton(Screen parent, ItemStack[] recipeItems) {
        return false;
    }

    public Boolean[] itemsInInventory(Screen parent, ItemStack[] recipeItems) {
        return new Boolean[]{true};
    }

    public void setupRecipe(Screen parent, ItemStack[] recipeItems) {

    }

    public final String TEXTURE_PATH;
    public final int TEXTURE_X;
    public final int TEXTURE_Y;
    public final int BUTTON_POS_X;
    public final int BUTTON_POS_Y;
}
