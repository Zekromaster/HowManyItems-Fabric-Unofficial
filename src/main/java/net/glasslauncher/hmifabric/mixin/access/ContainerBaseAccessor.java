package net.glasslauncher.hmifabric.mixin.access;

import net.minecraft.client.gui.screen.container.ContainerScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ContainerScreen.class)
public interface ContainerBaseAccessor {
    @Invoker("getSlotAt")
    Slot invokeGetSlot(int i, int j);

    @Accessor("backgroundWidth")
    int getContainerWidth();

    @Accessor("backgroundHeight")
    int getContainerHeight();
}
