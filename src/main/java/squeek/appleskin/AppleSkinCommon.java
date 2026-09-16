package squeek.appleskin;

import net.fabricmc.api.ModInitializer;
import squeek.appleskin.network.SyncHandler;

public class AppleSkinCommon
implements ModInitializer {
    public static final String INSTALL_NOTE = "AppleSkin+- replaces the original AppleSkin mod. "
        + "If the game crashes with 'Duplicate mod ID: appleskin', delete either the AppleSkin+- jar "
        + "or the original AppleSkin jar from your mods folder — only one may be installed.";

    public void onInitialize() {
        squeek.appleskin.AppleSkin.LOGGER.info("[AppleSkin+-] {}", INSTALL_NOTE);
        SyncHandler.init();
    }
}

