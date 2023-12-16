package net.glasslauncher.hmifabric.mixin;

import net.glasslauncher.hmifabric.HowManyItemsClient;
import net.minecraft.class_555;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_555.class)
public class MixinGameRenderer {

    @Unique
    private long clock = 0L;

    @Shadow
    private Minecraft field_2349;

    @Inject(method = "method_1844", at = @At(value = "TAIL"))
    private void onTick(float delta, CallbackInfo ci) {
        long newClock = 0L;
        if (field_2349.world != null && HowManyItemsClient.thisMod != null) {
            newClock = field_2349.world.getTime();
            if (newClock != clock) {
                HowManyItemsClient.thisMod.onTickInGame(field_2349);
            }
            if (field_2349.currentScreen != null) {
                HowManyItemsClient.thisMod.onTickInGUI(field_2349, field_2349.currentScreen);
            }
        }
        clock = newClock;
    }

}
