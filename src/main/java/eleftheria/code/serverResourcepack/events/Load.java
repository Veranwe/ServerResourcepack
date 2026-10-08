package eleftheria.code.serverResourcepack.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class Load implements Listener {

    @EventHandler
    public void load(PlayerResourcePackStatusEvent e) {
        System.out.println(e.getPlayer().getName() + " - ResourcePack status: " + e.getStatus());

        switch (e.getStatus()) {
            case DECLINED,
                 DISCARDED,
                 FAILED_DOWNLOAD,
                 FAILED_RELOAD,
                 INVALID_URL ->
                e.getPlayer().sendMessage("Failed to load resource-pack: '" + e.getStatus() + "'");

            case ACCEPTED,
                 DOWNLOADED,
                 SUCCESSFULLY_LOADED ->
                e.getPlayer().sendMessage("Loaded resourcepack");
        }
    }
}
