package squeek.appleskin.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Corrects AppleSkin's client-side saturation display after a piercing attack
 * lands with a Lunge-enchanted weapon. Without this, AppleSkin's saturation
 * overlay drifts out of sync with the server's real value, since Lunge's
 * saturation cost isn't otherwise predicted client-side.
 *
 * Ported in from the standalone "apple-skin-fix" addon so it ships as part
 * of AppleSkin itself rather than requiring a separate mod.
 */
@Mixin(LivingEntity.class)
public abstract class LungeSaturationFixMixin {
    @Inject(method = "postPiercingAttack", at = @At("TAIL"))
    private void appleSkin$applyClientSaturationForLunge(CallbackInfo ci) {
        if (!((Object) this instanceof LocalPlayer player)) {
            return;
        }

        if (player.getAbilities().invulnerable
                || player.isPassenger()
                || player.isFallFlying()
                || player.isInWater()
                || player.getFoodData().getFoodLevel() < 7) {
            return;
        }

        ItemStack weapon = player.getMainHandItem();
        ItemEnchantments enchantments = weapon.getEnchantments();

        int lungeLevel = 0;
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            if (entry.getKey().is(Enchantments.LUNGE)) {
                lungeLevel = entry.getIntValue();
                break;
            }
        }

        if (lungeLevel <= 0) {
            return;
        }

        float currentSaturation = player.getFoodData().getSaturationLevel();
        player.getFoodData().setSaturation(Math.max(0.0f, currentSaturation - lungeLevel));
    }
}
