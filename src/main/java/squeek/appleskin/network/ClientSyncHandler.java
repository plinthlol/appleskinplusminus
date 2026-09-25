package squeek.appleskin.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import squeek.appleskin.helpers.ExhaustionHelper;

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
		ClientPlayNetworking.registerGlobalReceiver(ExhaustionSyncPayload.TYPE,
				(payload, context) -> context.client().execute(
						() -> ExhaustionHelper.setExhaustion(context.client().player, payload.exhaustion())));
		ClientPlayNetworking.registerGlobalReceiver(SaturationSyncPayload.TYPE,
				(payload, context) -> context.client().execute(() -> {
					serverSyncsSaturation = true;
					context.client().player.getFoodData().setSaturation(payload.saturation());
				}));
		ClientPlayNetworking.registerGlobalReceiver(NaturalRegenerationSyncPayload.TYPE,
				(payload, context) -> {
					naturalRegeneration = payload.naturalRegeneration();
				});
	}
}
