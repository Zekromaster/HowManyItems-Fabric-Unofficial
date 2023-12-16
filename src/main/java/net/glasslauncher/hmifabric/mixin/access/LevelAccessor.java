package net.glasslauncher.hmifabric.mixin.access;

import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(World.class)
public interface LevelAccessor {
    @Accessor("properties")
    WorldProperties getProperties();
}
