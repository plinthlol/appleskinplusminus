package squeek.appleskin.client;

import java.util.Random;
import java.util.Vector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.api.handler.EventHandler;
import squeek.appleskin.helpers.ColorHelper;
import squeek.appleskin.helpers.ExhaustionHelper;
import squeek.appleskin.helpers.FoodHelper;
import squeek.appleskin.helpers.TextureHelper;
import squeek.appleskin.util.IntPoint;

/**
 * Always-on saturation + exhaustion HUD visualization.
 * No config, no ModMenu/Cloth-Config/Configured integration, no held-food
 * preview overlay, no health-bar overlay — just the current saturation
 * (on the hunger bar) and current exhaustion (underneath it).
 */
public class HUDOverlayHandler {
    public static HUDOverlayHandler INSTANCE;
    public final OffsetsCache barOffsets = new OffsetsCache();

    public static void init() {
        INSTANCE = new HUDOverlayHandler();
    }

    public void onPreRenderFood(GuiGraphicsExtractor context, Player player, int top, int right) {
        assert (player != null);
        float exhaustion = ExhaustionHelper.getExhaustion(player);
        HUDOverlayEvent.Exhaustion renderEvent = new HUDOverlayEvent.Exhaustion(exhaustion, right, top, context);
        ((EventHandler) HUDOverlayEvent.Exhaustion.EVENT.invoker()).interact(renderEvent);
        if (!renderEvent.isCanceled) {
            this.drawExhaustionOverlay(renderEvent);
        }
    }

    public void onRenderFood(GuiGraphicsExtractor context, Player player, int top, int right) {
        assert (player != null);
        Minecraft mc = Minecraft.getInstance();
        FoodData stats = player.getFoodData();
        HUDOverlayEvent.Saturation saturationRenderEvent = new HUDOverlayEvent.Saturation(stats.getSaturationLevel(), right, top, context);
        ((EventHandler) HUDOverlayEvent.Saturation.EVENT.invoker()).interact(saturationRenderEvent);
        if (!saturationRenderEvent.isCanceled) {
            this.drawSaturationOverlay(saturationRenderEvent, mc, mc.gui.hud.getGuiTicks());
        }
    }

    public void drawSaturationOverlay(GuiGraphicsExtractor context, float saturationLevel, Minecraft mc, int right, int top, int guiTicks) {
        if (saturationLevel < 0.0f) {
            return;
        }
        int alphaColor = ColorHelper.argbFromRGBA(1.0f, 1.0f, 1.0f, 1.0f);
        float modifiedSaturation = Math.max(0.0f, Math.min(saturationLevel, 20.0f));
        int endSaturationBar = (int) Math.ceil(modifiedSaturation / 2.0f);
        int iconSize = 9;
        Vector<IntPoint> foodBarOffsets = this.barOffsets.foodBarOffsets(guiTicks, mc.player);
        for (int i = 0; i < endSaturationBar; ++i) {
            IntPoint offset = i < foodBarOffsets.size() ? foodBarOffsets.get(i) : new IntPoint();
            if (offset == null) continue;
            int x = right + offset.x;
            int y = top + offset.y;
            int u = 0;
            float effectiveSaturationOfBar = modifiedSaturation / 2.0f - (float) i;
            if (effectiveSaturationOfBar >= 1.0f) {
                u = 3 * iconSize;
            } else if (effectiveSaturationOfBar > 0.5) {
                u = 2 * iconSize;
            } else if (effectiveSaturationOfBar > 0.25) {
                u = 1 * iconSize;
            }
            context.blit(RenderPipelines.GUI_TEXTURED, TextureHelper.MOD_ICONS, x, y, (float) u, 0.0f, iconSize, iconSize, 256, 256, alphaColor);
        }
    }

    public void drawExhaustionOverlay(GuiGraphicsExtractor context, float exhaustion, int right, int top) {
        float maxExhaustion = FoodHelper.MAX_EXHAUSTION;
        float ratio = Math.min(1.0f, Math.max(0.0f, exhaustion / maxExhaustion));
        int width = (int) (ratio * 81.0f);
        int height = 9;
        int color = ColorHelper.argbFromRGBA(1.0f, 1.0f, 1.0f, 0.75f);
        context.blit(RenderPipelines.GUI_TEXTURED, TextureHelper.MOD_ICONS, right - width, top, (float) (81 - width), 18.0f, width, height, 256, 256, color);
    }

    private void drawSaturationOverlay(HUDOverlayEvent.Saturation event, Minecraft mc, int guiTicks) {
        this.drawSaturationOverlay(event.context, event.saturationLevel, mc, event.x, event.y, guiTicks);
    }

    private void drawExhaustionOverlay(HUDOverlayEvent.Exhaustion event) {
        this.drawExhaustionOverlay(event.context, event.exhaustion, event.x, event.y);
    }

    private static class OffsetsCache {
        protected final Vector<IntPoint> foodBarOffsets = new Vector<>();
        public int lastGuiTick = 0;
        protected final Random random = new Random();

        private OffsetsCache() {
        }

        protected void generate(int guiTicks, Player player) {
            FoodData hungerManager = player.getFoodData();
            float saturationLevel = hungerManager.getSaturationLevel();
            int foodLevel = hungerManager.getFoodLevel();
            boolean shouldAnimatedFood = saturationLevel <= 0.0f && guiTicks % (foodLevel * 3 + 1) == 0;

            this.random.setSeed(guiTicks * 312871);
            if (this.foodBarOffsets.size() != 10) {
                this.foodBarOffsets.setSize(10);
            }
            for (int i = 0; i < 10; ++i) {
                int x = -(i * 8) - 9;
                int y = 0;
                if (shouldAnimatedFood) {
                    y += this.random.nextInt(3) - 1;
                }
                IntPoint point = this.foodBarOffsets.get(i);
                if (point == null) {
                    point = new IntPoint();
                    this.foodBarOffsets.set(i, point);
                }
                point.x = x;
                point.y = y;
            }
        }

        public Vector<IntPoint> foodBarOffsets(int guiTicks, Player player) {
            if (guiTicks != this.lastGuiTick) {
                this.generate(guiTicks, player);
                this.lastGuiTick = guiTicks;
            }
            return this.foodBarOffsets;
        }
    }
}
