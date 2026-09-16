package squeek.appleskin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.client.DebugInfoHudEntry;
import squeek.appleskin.client.HUDOverlayHandler;
import squeek.appleskin.network.ClientSyncHandler;

public class AppleSkin implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    public void onInitializeClient() {
        ClientSyncHandler.init();
        HUDOverlayHandler.init();
        FabricLoader.getInstance().getEntrypointContainers("appleskin", AppleSkinApi.class).forEach(entrypoint -> {
            try {
                ((AppleSkinApi) entrypoint.getEntrypoint()).registerEvents();
            } catch (Throwable e) {
                LOGGER.error("Failed to load entrypoint for mod {}", entrypoint.getProvider().getMetadata().getId(), e);
            }
        });
        DebugScreenEntries.register(DebugInfoHudEntry.ENTRY_ID, new DebugInfoHudEntry());
    }
}
