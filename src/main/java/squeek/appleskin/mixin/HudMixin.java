package squeek.appleskin.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.client.HUDOverlayHandler;

@Mixin(value={Hud.class})
public class HudMixin {
    @Inject(at={@At(value="HEAD")}, method={"extractFood"})
    private void renderFoodPre(GuiGraphicsExtractor context, Player player, int top, int right, CallbackInfo info) {
        if (HUDOverlayHandler.INSTANCE != null) {
            HUDOverlayHandler.INSTANCE.onPreRenderFood(context, player, top, right);
        }
    }

    @Inject(at={@At(value="RETURN")}, method={"extractFood"})
    private void renderFoodPost(GuiGraphicsExtractor context, Player player, int top, int right, CallbackInfo info) {
        if (HUDOverlayHandler.INSTANCE != null) {
            HUDOverlayHandler.INSTANCE.onRenderFood(context, player, top, right);
        }
    }
}

