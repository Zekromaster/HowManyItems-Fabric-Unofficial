package net.glasslauncher.hmifabric.mixin;

import net.glasslauncher.hmifabric.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;

import java.util.*;

@Mixin(Item.class)
public class MixinItemBase implements CustomTooltipProvider {

    @Override
    public String[] getTooltip(ItemStack itemStack, String originalTooltip) {
        ArrayList<String> tooltip = new ArrayList<>();
        tooltip.add(originalTooltip);
        if (Config.config.devMode) {
            for (TagKey<?> key : itemStack.getRegistryEntry().streamTags().toList())
                tooltip.add(key.id().toString());
        }
        return tooltip.toArray(new String[]{});
    }
}
