package de.moonset.engine.lib.night.hawk.lang.event;

/**
 * Created by pitt on 17.12.16.
 */
@FunctionalInterface
public interface EventDelegate<E> {
		E delegate();
}
