package net.glasslauncher.hmifabric;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;

public class GuiControlsHMI extends Screen {

    public GuiControlsHMI(Screen guiscreen) {
        parentScreen = guiscreen;
    }

    private ButtonWidget buttonDone;
    private int buttonId = -1;

    private int func_20080_j() {
        return width / 2 - 155;
    }

    @Override
    public void init() {

        int i = func_20080_j();
        for (int j = 0; j < binds.length; j++) {
            buttons.add(new OptionButtonWidget(j, i + (j % 2) * 160, height / 6 + 24 * (j >> 1), 70, 20, Keyboard.getKeyName(binds[j].code)));
        }

        buttons.add(buttonDone = new ButtonWidget(-1, width / 2 - 100, height / 6 + 168, "Done"));
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        if (buttonId > -1 && k == 0) {
            for (int l = 0; l < buttons.size(); l++) {
                ButtonWidget guibutton = (ButtonWidget) buttons.get(l);
                if (guibutton.id == buttonId) {
                    if (!guibutton.isMouseOver(minecraft, i, j)) {
                        guibutton.text = Keyboard.getKeyName(binds[l].code);
                        buttonId = -1;
                        break;
                    }
                }
            }
        }
        super.mouseClicked(i, j, k);
    }

    @Override
    protected void keyPressed(char c, int i) {
        if (buttonId >= 0) {
            if (i == 1) i = 0;
            if (binds[buttonId] == KeyBindings.toggleOverlay) {
                for (int j = 0; j < minecraft.options.allKeys.length; j++) {
                    if (minecraft.options.allKeys[j] == KeyBindings.toggleOverlay) {
                        minecraft.options.setKeybindKey(j, i);
                        break;
                    }
                }
            }
            binds[buttonId].code = i;
            ((ButtonWidget) buttons.get(buttonId)).text = Keyboard.getKeyName(i);
            buttonId = -1;
            HowManyItemsClient.onSettingChanged();
        } else {
            super.keyPressed(c, i);
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget guibutton) {
        if (guibutton == buttonDone) {
            //minecraft.options.saveOptions();
            minecraft.setScreen(parentScreen);
            return;
        } else {
            buttonId = guibutton.id;
            guibutton.text = "> " + Keyboard.getKeyName(binds[guibutton.id].code) + " <";
        }
        HowManyItemsClient.onSettingChanged();
    }

    @Override
    public void render(int i, int j, float f) {
        renderBackground();
        drawCenteredTextWithShadow(textRenderer, "HMI Keybinds", width / 2, 20, 0xffffff);
        int k = func_20080_j();
        for (int l = 0; l < binds.length; l++) {
            drawTextWithShadow(textRenderer, binds[l].translationKey, k + (l % 2) * 160 + 70 + 6, height / 6 + 24 * (l >> 1) + 7, -1);
        }

        super.render(i, j, f);
    }

    private static KeyBinding[] binds = {
            KeyBindings.pushRecipe,
            KeyBindings.pushUses,
            KeyBindings.prevRecipe,
            KeyBindings.allRecipes,
            KeyBindings.clearSearchBox,
            KeyBindings.focusSearchBox,
            KeyBindings.toggleOverlay
    };

    private Screen parentScreen;
}
