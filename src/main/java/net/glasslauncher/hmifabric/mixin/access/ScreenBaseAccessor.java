package net.glasslauncher.hmifabric.mixin.access;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenBaseAccessor {
    @Invoker("mouseClicked")
    void invokeMouseClicked(int i, int j, int k);
    @Invoker("keyPressed")
    void invokeKeyPressed(char c, int i);
    @Invoker("mouseReleased")
    void invokeMouseReleased(int i, int j, int k);
}
