package de.moonset.engine.lib.night.hawk.lang.event.fail;

import de.moonset.engine.lib.night.hawk.lang.InvisibleMethodError;
import de.moonset.engine.lib.night.hawk.lang.event.EventDispatcher;
import de.moonset.engine.lib.night.hawk.lang.event.ProxyDispatcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Created by pitt on 01.02.17.
 */
public class ProxyDispatcherFailTest {

		private static final Logger LOGGER = LoggerFactory.getLogger(ProxyDispatcherFailTest.class);
		private static final Marker TEST   = MarkerFactory.getMarker("TEST");

		@Test(expected = InvisibleMethodError.class)
		public void testInvisibleMethodVoid() throws Exception {
				EventDispatcher<ProxyFailInterface> dispatcher = ProxyDispatcher.create(ProxyFailInterface.class);
				dispatcher.addListener(new TestClass());
				dispatcher.delegate().test();
		}


		@Test(expected = InvisibleMethodError.class)
		public void testInvisibleMethodBoolean() throws Exception {
				EventDispatcher<ProxyFailInterface> dispatcher = ProxyDispatcher.create(ProxyFailInterface.class);
				dispatcher.addListener(new TestClass());
				dispatcher.delegate().test();
		}

		public static class TestClass implements ProxyFailInterface {
				@Override
				public void test() {
						LOGGER.error(TEST, "should never been seen");
				}

				@Override
				public boolean test2() {
						LOGGER.error(TEST, "should never been seen");
						return false;
				}
		}
}
