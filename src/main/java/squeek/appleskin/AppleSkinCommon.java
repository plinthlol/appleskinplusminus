package squeek.appleskin;

import net.fabricmc.api.ModInitializer;
import squeek.appleskin.network.SyncHandler;

public class AppleSkinCommon
implements ModInitializer {
    public void onInitialize() {
        SyncHandler.init();
    }
}

