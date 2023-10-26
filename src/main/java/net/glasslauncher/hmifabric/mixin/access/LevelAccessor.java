package net.glasslauncher.hmifabric.mixin.access;

import net.minecraft.level.Level;
import net.minecraft.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Level.class)
public interface LevelAccessor {
    @Accessor("properties")
    LevelProperties getProperties();
}
