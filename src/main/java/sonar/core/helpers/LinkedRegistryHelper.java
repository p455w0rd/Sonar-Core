package sonar.core.helpers;

import java.util.LinkedHashMap;

import sonar.core.SonarCore;

public abstract class LinkedRegistryHelper<S, P> {

	private LinkedHashMap<S, P> objects = new LinkedHashMap<S, P>();
	private LinkedHashMap<P, S> objectsReversed = new LinkedHashMap<P, S>();

	public abstract void register();

	public abstract String registeryType();

	public void removeAll() {
		objects.clear();
	}

	public LinkedHashMap<S, P> getMap() {
		return objects;
	}

	public LinkedHashMap<P, S> getMapReversed() {
		return objectsReversed;
	}

	public P getPrimaryObject(S object) {
		P toReturn = objects.get(object);
		if (toReturn == null) {
			return getPrimaryDefault();
		} else {
			return toReturn;
		}
	}

	public S getSecondaryObject(P object) {
		S toReturn = objectsReversed.get(object);
		if (toReturn == null) {
			return getSecondaryDefault();
		} else {
			return toReturn;
		}
	}

	public void registerMap(S secondary, P primary) {
		try {
			if (primary != null && secondary != null) {
				objects.put(secondary, primary);
				objectsReversed.put(primary, secondary);
				SonarCore.logger.info("Loaded " + registeryType() + ": " + primaryToString(primary) + " = " + secondaryToString(secondary));
			} else {
				SonarCore.logger.warn(registeryType() + " wasn't loadable: " + primaryToString(primary) + " = " + secondaryToString(secondary));
				return;
			}
		} catch (Exception exception) {
			SonarCore.logger.warn(registeryType() + " : Exception Loading Helper: " + exception.getMessage());
		}
	}

	public P getPrimaryDefault() {
		return null;
	}

	public S getSecondaryDefault() {
		return null;
	}
	
	public String primaryToString(P primary){
		return primary.toString();
	}
	
	public String secondaryToString(S secondary){
		return secondary.toString();
	}
}