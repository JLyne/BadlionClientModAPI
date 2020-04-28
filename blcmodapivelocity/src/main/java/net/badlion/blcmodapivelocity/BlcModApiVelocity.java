package net.badlion.blcmodapivelocity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.badlion.blcmodapivelocity.Conf;
import net.badlion.blcmodapivelocity.listener.PlayerListener;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

@Plugin(id = "blcmodapi", name = "BadLion Client Mod API", version = "1.2.0",
        description = "", authors = {"BadLion"})
public class BlcModApiVelocity {

	public static final Gson GSON_NON_PRETTY = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().create();
	public static final Gson GSON_PRETTY = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().setPrettyPrinting().create();
	private ChannelIdentifier identifier;

	private Conf conf;

	@Inject
	@DataDirectory
	private Path dataFolder;

	private final ProxyServer proxy;
	private final Logger logger;

  	@Inject
  	public BlcModApiVelocity(ProxyServer proxy, Logger logger) {
  		this.logger = logger;
  		this.proxy = proxy;
	}

	@Subscribe
	public void onProxyInitialized(ProxyInitializeEvent event) {
		identifier = MinecraftChannelIdentifier.create("badlion", "mods");

		if (!dataFolder.toFile().exists()) {
			if (!dataFolder.toFile().mkdir()) {
				logger.error("Failed to create plugin directory.");
			}
		}

		try {
			this.conf = loadConf(new File(dataFolder.toFile(), "config.json"));
			// Only register the listener if the config loads successfully
			proxy.getEventManager().register(this, new PlayerListener(this));

			logger.info("Successfully setup BadlionClientModAPI plugin.");
		} catch (IOException e) {
			logger.error("Error with config for BadlionClientModAPI plugin.");
			e.printStackTrace();
		}
	}

	public Conf loadConf(File file) throws IOException {
		try (Reader reader = new BufferedReader(new FileReader(file))) {
			return BlcModApiVelocity.GSON_NON_PRETTY.fromJson(reader, Conf.class);
		} catch (FileNotFoundException ex) {
			logger.info("No Config Found: Saving default...");
			Conf conf = new Conf();
			this.saveConf(conf, new File(dataFolder.toFile(), "config.json"));
			return conf;
		}
	}

	private void saveConf(Conf conf, File file) {
		try (FileWriter writer = new FileWriter(file)) {
			BlcModApiVelocity.GSON_PRETTY.toJson(conf, writer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Conf getConf() {
		return this.conf;
	}

	public ChannelIdentifier getIdentifier() {
		return identifier;
	}
}
