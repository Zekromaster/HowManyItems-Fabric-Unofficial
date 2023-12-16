package net.glasslauncher.hmifabric;

import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyBindings {

    public static KeyBinding pushRecipe = new KeyBinding("Get Recipes", Keyboard.KEY_R);
    public static KeyBinding pushUses = new KeyBinding("Get Uses", Keyboard.KEY_U);
    public static KeyBinding prevRecipe = new KeyBinding("Previous Recipe", Keyboard.KEY_BACK);
    public static KeyBinding allRecipes = new KeyBinding("Show All Recipes", Keyboard.KEY_NONE);

    public static KeyBinding toggleOverlay = new KeyBinding("Toggle HMI", Keyboard.KEY_O);
    public static KeyBinding clearSearchBox = new KeyBinding("Clear Search", Keyboard.KEY_DELETE);
    public static KeyBinding focusSearchBox = new KeyBinding("Focus Search", Keyboard.KEY_RETURN);
}
