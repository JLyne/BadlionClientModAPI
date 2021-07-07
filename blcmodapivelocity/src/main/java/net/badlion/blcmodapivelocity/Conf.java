package net.badlion.blcmodapivelocity;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Conf {

	private final Map<String, DisallowedMods> modsDisallowed =  new HashMap<>();

	public Map<String, DisallowedMods> getModsDisallowed() {
		return this.modsDisallowed;
	}

	@SuppressWarnings("unused")
	private static class DisallowedMods {
		private boolean disabled;
		private JsonObject extra_data;
		private JsonObject settings;
	}
}
