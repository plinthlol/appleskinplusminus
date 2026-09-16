package squeek.appleskin.helpers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.util.Util;

public class KeyHelper {
    public static boolean isCtrlKeyDown() {
        boolean isCtrlKeyDown = InputConstants.isKeyDown(341) || InputConstants.isKeyDown(345);
        if (!isCtrlKeyDown && Util.getPlatform() == Util.OS.OSX) {
            isCtrlKeyDown = InputConstants.isKeyDown(343) || InputConstants.isKeyDown(347);
        }
        return isCtrlKeyDown;
    }

    public static boolean isShiftKeyDown() {
        return InputConstants.isKeyDown(340) || InputConstants.isKeyDown(344);
    }
}

