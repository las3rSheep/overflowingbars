package fuzs.overflowingbars.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.OptionalInt;

public interface CustomizeChatPanelCallback {
    Event<CustomizeChatPanelCallback> EVENT = EventFactory.createArrayBacked(CustomizeChatPanelCallback.class, listeners -> (int posY) -> {
        for (CustomizeChatPanelCallback event : listeners) {
            OptionalInt result = event.onRenderChatPanel(posY);
            if (result.isPresent()) return result;
        }
        return OptionalInt.empty();
    });

    /**
     * called before the chat panel is drawn, allows for changing the y position
     *
     * @param posY  the current y position of the chat panel (from screen top)
     * @return      new y position (not an addition)
     */
    OptionalInt onRenderChatPanel(int posY);
}
