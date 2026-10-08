package eleftheria.code.serverResourcepack.events;

import eleftheria.code.serverResourcepack.ServerResourcepack;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.packs.ResourcePack;

import java.net.URI;
import java.util.UUID;

public class Join implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        String url = "http://142.132.221.126:8000/pack.zip";
        UUID packID = UUID.nameUUIDFromBytes(url.getBytes());

        ResourcePackInfo info = ResourcePackInfo.resourcePackInfo()
                .id(packID)
                .uri(URI.create(url))
                .hash(ServerResourcepack.hash)
                .build();

        ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                .packs(info)
                .prompt(Component.text("Please apply the resource-pack"))
                .required(true)
                .replace(true)
                .build();

        e.getPlayer().sendResourcePacks(request);
        System.out.println("resourcepack request sent");
    }
}
