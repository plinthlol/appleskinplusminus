package squeek.appleskin.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.player.Player;
import squeek.appleskin.helpers.ExhaustionHelper;
import squeek.appleskin.network.ExhaustionSyncPayload;
import squeek.appleskin.network.NaturalRegenerationSyncPayload;
import squeek.appleskin.network.SaturationSyncPayload;

public class ClientSyncHandler {
	public static boolean naturalRegeneration = true;

	/**
	 * Set to true once the server has sent us at least one saturation sync
	 * payload. When this is up, the server is the source of truth for
	 * saturation and the client-side LungeSaturationFixMixin prediction
	 * should stand down instead of second-guessing it.
	 */
	public static boolean serverSyncsSaturation = false;

	@Environment(EnvType.CLIENT)
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SaturationSyncPayload.TYPE,
				(SaturationSyncPayload payload, ClientPlayNetworking.Context context) -> {
					serverSyncsSaturation = true;
					context.client().execute(() ->
							context.client().player.getFoodData().setSaturation(payload.saturation()));
				});
		ClientPlayNetworking.registerGlobalReceiver(ExhaustionSyncPayload.TYPE,
				(ExhaustionSyncPayload payload, ClientPlayNetworking.Context context) ->
						context.client().execute(() ->
								ExhaustionHelper.setExhaustion((Player) context.client().player, payload.exhaustion())));
		ClientPlayNetworking.registerGlobalReceiver(NaturalRegenerationSyncPayload.TYPE,
				(NaturalRegenerationSyncPayload payload, ClientPlayNetworking.Context context) -> {
					naturalRegeneration = payload.naturalRegeneration();
				});
	}
}
