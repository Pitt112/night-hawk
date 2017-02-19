package de.moonset.engine.lib.night.hawk.concurrent;

import java.util.function.Supplier;

/**
 * Created by pitt on 15.02.17.
 */
public interface GuardLite {

		void read(Runnable op);

		<T> T read(Supplier<T> op);

		void write(Runnable op);

		<T> T write(Supplier<T> op);
}
