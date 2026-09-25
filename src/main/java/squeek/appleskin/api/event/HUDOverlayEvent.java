package squeek.appleskin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.DrawContext;
import squeek.appleskin.api.handler.EventHandler;

public class HUDOverlayEvent {
    public HUDOverlayEvent(int x, int y, DrawContext context) {
        this.x = x;
        this.y = y;
        this.context = context;
    }

    public boolean isCanceled = false;

    public final DrawContext context;
    public final int x;
    public final int y;

    /**
     * If cancelled, will stop all rendering of the exhaustion meter.
     */
    public static class Exhaustion extends HUDOverlayEvent {
        public Exhaustion(float exhaustion, int x, int y, DrawContext context) {
            super(x, y, context);
            this.exhaustion = exhaustion;
        }

        public final float exhaustion;

        public static Event<EventHandler<Exhaustion>> EVENT = EventHandler.createArrayBacked();
    }

    /**
     * If cancelled, will stop all rendering of the saturation overlay.
     */
    public static class Saturation extends HUDOverlayEvent {
        public Saturation(float saturationLevel, int x, int y, DrawContext context) {
            super(x, y, context);
            this.saturationLevel = saturationLevel;
        }

        public final float saturationLevel;

        public static Event<EventHandler<Saturation>> EVENT = EventHandler.createArrayBacked();
    }
}
