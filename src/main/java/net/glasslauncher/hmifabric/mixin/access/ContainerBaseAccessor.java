package net.glasslauncher.hmifabric.mixin.access;

import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ContainerBase.class)
public interface ContainerBaseAccessor {
    @Invoker("getSlot")
    Slot invokeGetSlot(int i, int j);

    @Accessor("containerWidth")
    int getContainerWidth();

    @Accessor("containerHeight")
    int getContainerHeight();
}
