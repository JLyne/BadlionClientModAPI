package net.badlion.blcmodapivelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.badlion.blcmodapivelocity.BlcModApiVelocity;

public class PlayerListener {

	private final BlcModApiVelocity plugin;

	public PlayerListener(BlcModApiVelocity plugin) {
		this.plugin = plugin;
	}

	@Subscribe
	public void onLogin(LoginEvent event) {
		// Send the disallowed mods to players when they login to the proxy. A notification will appear on the Badlion
		// Client so they know the mod was disabled
		Player player = event.getPlayer();
		player.sendPluginMessage(plugin.getIdentifier(), BlcModApiVelocity.GSON_NON_PRETTY
				.toJson(this.plugin.getConf().getModsDisallowed()).getBytes());
	}
}
