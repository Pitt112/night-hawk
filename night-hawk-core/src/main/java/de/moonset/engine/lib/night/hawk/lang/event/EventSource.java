package de.moonset.engine.lib.night.hawk.lang.event;

/**
 * Created by pitt on 17.12.16.
 */
public interface EventSource<E> {

		boolean addListener(E listener);

		boolean removeListener(E listener);

}
