package de.moonset.engine.lib.night.hawk.concurrent;

/**
 * Created by pitt on 16.02.17.
 */
@FunctionalInterface
public interface InterruptableSupplier<T> {
		T get() throws InterruptedException;
}
