package net.glasslauncher.hmifabric;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.screen.slot.Slot;


public class ContainerRecipeViewer extends Container {

    public ContainerRecipeViewer(InventoryRecipeViewer iinventory) {
        //setting the windowId to -1 prevents server registering recipe clicks as inventory clicks
        this.syncId = -1;
        inv = iinventory;
        resetSlots();
    }

    public void resetSlots() {
        super.slots.clear();
        count = 0;
    }

    // Not an override. Custom method.
    public void addSlot(int i, int j) {
        method_2079(new Slot(inv, count++, i, j));
    }

    @Override
    public boolean method_2094(PlayerEntity entityplayer) {
        return inv.canPlayerUse(entityplayer);
    }

    private int count;
    private InventoryRecipeViewer inv;
}
