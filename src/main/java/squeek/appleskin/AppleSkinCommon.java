package squeek.appleskin;

import net.fabricmc.api.ModInitializer;
import squeek.appleskin.network.SyncHandler;

public class AppleSkinCommon
implements ModInitializer {
    public static final String INSTALL_NOTE = "AppleSkin+- replaces the original AppleSkin mod. "
        + "If the game crashes with 'Duplicate mod ID: appleskin', delete "
        + "AppleSkin jar from your mods folder.";

    public void onInitialize() {
        squeek.appleskin.AppleSkin.LOGGER.info("[AppleSkin+-] {}", INSTALL_NOTE);
        SyncHandler.init();
    }
}

