package squeek.appleskin.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.client.HUDOverlayHandler;

@Mixin(Gui.class)
public class GuiMixin {
	@Inject(method = "renderFood", at = @At("HEAD"))
	private void renderFoodPre(GuiGraphics context, Player player, int top, int right, CallbackInfo info) {
		if (HUDOverlayHandler.INSTANCE != null) {
			HUDOverlayHandler.INSTANCE.onPreRenderFood(context, player, top, right);
		}
	}

	@Inject(method = "renderFood", at = @At("RETURN"))
	private void renderFoodPost(GuiGraphics context, Player player, int top, int right, CallbackInfo info) {
		if (HUDOverlayHandler.INSTANCE != null) {
			HUDOverlayHandler.INSTANCE.onRenderFood(context, player, top, right);
		}
	}
}
