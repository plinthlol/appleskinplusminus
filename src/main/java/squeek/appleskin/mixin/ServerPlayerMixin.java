package squeek.appleskin.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.network.SyncHandler;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	void onUpdate(CallbackInfo info) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		SyncHandler.onPlayerUpdate(player);
	}
}
