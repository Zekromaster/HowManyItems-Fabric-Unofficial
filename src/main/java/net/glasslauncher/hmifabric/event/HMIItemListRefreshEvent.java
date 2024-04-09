package net.glasslauncher.hmifabric.event;

import net.minecraft.item.ItemStack;

import java.util.*;

public interface HMIItemListRefreshEvent {

    void refreshItemList(List<ItemStack> allItems);
}
