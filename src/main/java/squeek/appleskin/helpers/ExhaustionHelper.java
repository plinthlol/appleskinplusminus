package squeek.appleskin.helpers;

import net.minecraft.entity.player.PlayerEntity;

public class ExhaustionHelper {
    public static float getExhaustion(PlayerEntity player) {
        return ((ExhaustionManipulator) player.getHungerManager()).getExhaustion();
    }

    public static void setExhaustion(PlayerEntity player, float exhaustion) {
        ((ExhaustionManipulator) player.getHungerManager()).setExhaustion(exhaustion);
    }

    public interface ExhaustionManipulator {
        float getExhaustion();

        void setExhaustion(float exhaustion);
    }
}
