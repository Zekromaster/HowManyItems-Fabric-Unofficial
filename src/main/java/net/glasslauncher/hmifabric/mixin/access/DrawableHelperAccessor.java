package net.glasslauncher.hmifabric.mixin.access;

import net.minecraft.client.gui.DrawableHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawableHelper.class)
public interface DrawableHelperAccessor {

    @Invoker
    void invokeFill(int x1, int y1, int x2, int y2, int colour);
}
