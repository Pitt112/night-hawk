package de.moonset.engine.lib.night.hawk.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 25.12.16.
 */
public class ClassTypeAdapterTest {
		@Test
		public void write() throws Exception {

				final Gson gson = new GsonBuilder()
						.registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(Collections.emptyMap()))
						.create();

				final String actual   = gson.toJson(this.getClass());
				final String expected = "{\"type\":\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\"}";

				assertThat(actual).isEqualToIgnoringWhitespace(expected);
		}

		@Test
		public void read() throws Exception {
				final Map<String, Class<?>> types = new HashMap<>(4);
				types.put(this.getClass().getCanonicalName(), this.getClass());
				final Gson gson = new GsonBuilder()
						.registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(types))
						.create();

				final Class<?> actual   =
						gson.fromJson("{\"type\":\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\"}", Class.class);
				final Class<?>   expected = this.getClass();

				assertThat(actual).isEqualTo(expected);
		}

}
