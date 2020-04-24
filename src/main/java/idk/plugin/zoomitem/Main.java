package idk.plugin.zoomitem;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;

import java.util.ArrayList;
import java.util.List;

public class Main extends PluginBase implements Listener {

    private List<String> zoom = new ArrayList<>();

    private int zoomItemId;
    private int zoomItemDamage;

    private String zoomInMessage;
    private String zoomOutMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        zoomItemId = getConfig().getInt("zoomItemId");
        zoomItemDamage = getConfig().getInt("zoomItemDamage");
        zoomInMessage = getConfig().getString("zoomInMessage");
        zoomOutMessage = getConfig().getString("zoomOutMessage");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        if (zoom.contains(name)) {
            p.removeEffect(Effect.SLOWNESS);
            zoom.remove(name);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == PlayerInteractEvent.Action.PHYSICAL) {
            return;
        }
        Player p = e.getPlayer();
        if (e.getItem().getId() == zoomItemId && e.getItem().getDamage() == zoomItemDamage) {
            if (p.hasPermission("zoomitem.use")) {
                String name = p.getName();
                if (zoom.contains(name)) {
                    p.removeEffect(Effect.SLOWNESS);
                    zoom.remove(name);
                    if (!zoomOutMessage.isEmpty()) {
                        p.sendActionBar(zoomOutMessage);
                    }
                } else {
                    zoom.add(name);
                    p.addEffect(Effect.getEffect(Effect.SLOWNESS).setAmplifier(10000).setDuration(3456000).setVisible(false));
                    if (!zoomInMessage.isEmpty()) {
                        p.sendActionBar(zoomInMessage);
                    }
                }
            }
        }
    }
}
