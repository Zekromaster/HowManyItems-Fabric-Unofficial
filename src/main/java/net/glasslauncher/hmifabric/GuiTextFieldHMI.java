package net.glasslauncher.hmifabric;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class GuiTextFieldHMI extends TextFieldWidget {

    public GuiTextFieldHMI(Screen guiscreen, TextRenderer fontrenderer, int i, int j, int k, int l, String s) {
        super(guiscreen, fontrenderer, i, j, k, l, s);
        xPos = i;
        yPos = j;
        width = k;
        height = l;
    }

    public boolean hovered(int posX, int posY) {
        return enabled && posX >= xPos && posX < xPos + width && posY >= yPos && posY < yPos + height;
    }

    // onClick
    @Override
    public void mouseClicked(int posX, int posY, int eventButton) {
        super.mouseClicked(posX, posY, eventButton);
        if (this.focused && eventButton == 1) {
            GuiOverlay.clearSearchBox();
        }
    }

    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
}
