package squeek.appleskin.helpers;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

public class KeyHelper {
    public static boolean isCtrlKeyDown() {
        boolean isCtrlKeyDown;
        Window window = Minecraft.getInstance().getWindow();
        boolean bl = isCtrlKeyDown = InputConstants.isKeyDown((Window)window, (int)341) || InputConstants.isKeyDown((Window)window, (int)345);
        if (!isCtrlKeyDown && Util.getPlatform() == Util.OS.OSX) {
            isCtrlKeyDown = InputConstants.isKeyDown((Window)window, (int)343) || InputConstants.isKeyDown((Window)window, (int)347);
        }
        return isCtrlKeyDown;
    }

    public static boolean isShiftKeyDown() {
        Window window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown((Window)window, (int)340) || InputConstants.isKeyDown((Window)window, (int)344);
    }
}

