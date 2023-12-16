package net.glasslauncher.hmifabric.event;

import net.glasslauncher.hmifabric.tabs.TabRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

public class HMITabRegistryEvent extends RegistryEvent<TabRegistry> {
    public static final int ID = nextID();

    public HMITabRegistryEvent() {
        super(TabRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }
}
