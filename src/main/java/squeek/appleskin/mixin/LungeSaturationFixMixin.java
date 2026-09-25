package squeek.appleskin.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.network.ClientSyncHandler;

/**
 * Predicts the Lunge enchantment's saturation drain on the client.
 *
 * <p>Lunge's hunger cost ({@code minecraft:apply_exhaustion}, 4xlevel) is
 * applied by the server as part of its post-piercing-attack effects, so a
 * client without server syncs (e.g. a vanilla server) never sees its own
 * saturation drop and the overlay drifts high after every jab. Since 4
 * exhaustion drains exactly 1 saturation, subtract the Lunge level
 * directly once the local player's piercing attack lands.
 *
 * <p>Stands down as soon as the server starts syncing saturation itself
 * (see {@link ClientSyncHandler#serverSyncsSaturation}).
 */
@Mixin(MultiPlayerGameMode.class)
public abstract class LungeSaturationFixMixin {
	@Inject(method = "piercingAttack", at = @At("TAIL"))
	private void appleSkin$applyClientSaturationForLunge(CallbackInfo ci) {
		if (ClientSyncHandler.serverSyncsSaturation) {
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}

		// Match the requirements of the vanilla Lunge enchantment effect.
		// Creative players can lunge, but do not actually gain exhaustion.
		if (player.getAbilities().invulnerable
				|| player.isPassenger()
				|| player.isFallFlying()
				|| player.isInWater()
				|| player.getFoodData().getFoodLevel() < 7) {
			return;
		}

		ItemStack weapon = player.getMainHandItem();
		int lungeLevel = EnchantmentHelper.getItemEnchantmentLevel(
				player.level().registryAccess()
						.lookupOrThrow(Registries.ENCHANTMENT)
						.getOrThrow(Enchantments.LUNGE),
				weapon);

		if (lungeLevel <= 0) {
			return;
		}

		float currentSaturation = player.getFoodData().getSaturationLevel();
		player.getFoodData().setSaturation(
				Math.max(0.0F, currentSaturation - lungeLevel)
		);
	}
}
