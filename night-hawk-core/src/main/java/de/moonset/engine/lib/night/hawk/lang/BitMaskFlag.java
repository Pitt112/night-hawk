package de.moonset.engine.lib.night.hawk.lang;

/**
 * Created by pitt on 15.12.16.
 */
@FunctionalInterface
public interface BitMaskFlag {

		default long value() { return 1 << field(); }

		int field();
}
