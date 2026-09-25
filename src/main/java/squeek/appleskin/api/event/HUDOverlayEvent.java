package squeek.appleskin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import squeek.appleskin.api.handler.EventHandler;

public class HUDOverlayEvent {
    public int x;
    public int y;
    public GuiGraphicsExtractor context;
    public boolean isCanceled = false;

    private HUDOverlayEvent(int x, int y, GuiGraphicsExtractor context) {
        this.x = x;
        this.y = y;
        this.context = context;
    }

    public static class Saturation
    extends HUDOverlayEvent {
        public final float saturationLevel;
        public static Event<EventHandler<Saturation>> EVENT = EventHandler.createArrayBacked();

        public Saturation(float saturationLevel, int x, int y, GuiGraphicsExtractor context) {
            super(x, y, context);
            this.saturationLevel = saturationLevel;
        }
    }

    public static class Exhaustion
    extends HUDOverlayEvent {
        public final float exhaustion;
        public static Event<EventHandler<Exhaustion>> EVENT = EventHandler.createArrayBacked();

        public Exhaustion(float exhaustion, int x, int y, GuiGraphicsExtractor context) {
            super(x, y, context);
            this.exhaustion = exhaustion;
        }
    }
}

