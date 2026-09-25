package squeek.appleskin.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gamerules.GameRules;
import squeek.appleskin.helpers.ExhaustionHelper;

public class SyncHandler {
	private static final Map<UUID, Float> lastSaturationLevels = new HashMap<>();
	private static final Map<UUID, Float> lastExhaustionLevels = new HashMap<>();
	private static boolean naturalRegeneration = true;

	public static void init() {
		PayloadTypeRegistry.playS2C().register(ExhaustionSyncPayload.TYPE, ExhaustionSyncPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SaturationSyncPayload.TYPE, SaturationSyncPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(NaturalRegenerationSyncPayload.TYPE, NaturalRegenerationSyncPayload.CODEC);
		ServerTickEvents.END_WORLD_TICK.register(SyncHandler::onServerWorldTick);
	}

	public static void onPlayerUpdate(ServerPlayer player) {
		Float lastSaturationLevel = lastSaturationLevels.get(player.getUUID());
		Float lastExhaustionLevel = lastExhaustionLevels.get(player.getUUID());

		float saturation = player.getFoodData().getSaturationLevel();
		if (lastSaturationLevel == null || lastSaturationLevel != saturation) {
			ServerPlayNetworking.send(player, new SaturationSyncPayload(saturation));
			lastSaturationLevels.put(player.getUUID(), saturation);
		}

		float exhaustionLevel = ExhaustionHelper.getExhaustion(player);
		if (lastExhaustionLevel == null || Math.abs(lastExhaustionLevel - exhaustionLevel) >= 0.01f) {
			ServerPlayNetworking.send(player, new ExhaustionSyncPayload(exhaustionLevel));
			lastExhaustionLevels.put(player.getUUID(), exhaustionLevel);
		}
	}

	public static void onPlayerLoggedIn(ServerPlayer player) {
		lastSaturationLevels.remove(player.getUUID());
		lastExhaustionLevels.remove(player.getUUID());

		if (!naturalRegeneration) {
			ServerPlayNetworking.send(player, new NaturalRegenerationSyncPayload(false));
		}
	}

	public static void onServerWorldTick(ServerLevel world) {
		Boolean cur = (Boolean) world.getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION);
		if (naturalRegeneration != cur) {
			for (ServerPlayer player : world.players()) {
				ServerPlayNetworking.send(player, new NaturalRegenerationSyncPayload(cur));
			}
			naturalRegeneration = cur;
		}
	}
}
