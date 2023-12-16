package net.glasslauncher.hmifabric;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.*;

public abstract class TabHandler {

    public abstract void loadTabs(Namespace basemod);

    public void registerItems(ArrayList<ItemStack> itemList) {
    }
}
