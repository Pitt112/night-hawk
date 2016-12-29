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
				final String expected = "\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\"";

				assertThat(actual).isEqualToIgnoringWhitespace(expected);
		}

		@Test
		public void writeNull() throws Exception {

				final Gson gson = new GsonBuilder()
						.registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(Collections.emptyMap()))
						.create();

				final String actual   = gson.toJson(new SinglePojo(null));
				final String expected = "{}";

				assertThat(actual).isEqualToIgnoringWhitespace(expected);
		}

		@Test
		public void writeMultipleNulls() throws Exception {

				final Gson gson = new GsonBuilder()
						.registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(Collections.emptyMap()))
						.create();

				final String actual   = gson.toJson(new ArrayPojo(null, null));
				final String expected = "{\"multiple\":[null,null]}";

				assertThat(actual).isEqualToIgnoringWhitespace(expected);
		}

		@Test
		public void readMultipleNulls() throws Exception {

				final Gson gson = new GsonBuilder()
						.registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(Collections.emptyMap()))
						.create();

				final ArrayPojo actual   = gson.fromJson("{\"multiple\":[null,null]}", ArrayPojo.class);
				final ArrayPojo expected = new ArrayPojo(null, null);

				assertThat(actual).isEqualToComparingFieldByField(expected);
		}

		@Test
		public void read() throws Exception {
				final Map<String, Class<?>> types = new HashMap<>(4);
				types.put(this.getClass().getCanonicalName(), this.getClass());
				final Gson gson =
						new GsonBuilder().registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(types)).create();

				final Class<?> actual =
						gson.fromJson("\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\"", Class.class);
				final Class<?> expected = this.getClass();

				assertThat(actual).isEqualTo(expected);
		}

		@Test
		public void readNull() throws Exception {
				final Map<String, Class<?>> types = new HashMap<>(4);
				types.put(this.getClass().getCanonicalName(), this.getClass());
				final Gson gson =
						new GsonBuilder().registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(types)).create();

				final SinglePojo actual   = gson.fromJson("{}", SinglePojo.class);
				final SinglePojo expected = new SinglePojo();

				assertThat(actual).isEqualToComparingFieldByField(expected);
		}

		@Test
		public void readSinglePojo() {
				final Map<String, Class<?>> types = new HashMap<>(4);
				types.put(this.getClass().getCanonicalName(), this.getClass());
				final Gson gson =
						new GsonBuilder().registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(types)).create();


				final SinglePojo actual = gson.fromJson(
						"{\"single\":\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\"}",
						SinglePojo.class);
				final SinglePojo expected = new SinglePojo(getClass());


				assertThat(actual).isEqualToIgnoringGivenFields(expected);
		}

		@Test
		public void writeSinglePojo() {
				final Map<String, Class<?>> types = new HashMap<>(4);
				types.put(this.getClass().getCanonicalName(), this.getClass());
				final Gson gson =
						new GsonBuilder().registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(types)).create();

				final SinglePojo pojo     = new SinglePojo(this.getClass());
				final String     actual   = gson.toJson(pojo);
				final String     expected = "{\"single\":\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\"}";

				assertThat(actual).isEqualToIgnoringWhitespace(expected);
		}

		@Test
		public void writeArrayPojo() {
				final Map<String, Class<?>> types = new HashMap<>(4);
				types.put(this.getClass().getCanonicalName(), this.getClass());
				final Gson gson =
						new GsonBuilder().registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(types)).create();

				final ArrayPojo pojo   = new ArrayPojo(this.getClass(), this.getClass());
				final String    actual = gson.toJson(pojo);
				final String expected =
						"{\"multiple\":[\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\",\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\"]}";

				assertThat(actual).isEqualToIgnoringWhitespace(expected);
		}

		@Test
		public void readArrayPojo() {
				final Map<String, Class<?>> types = new HashMap<>(4);
				types.put(this.getClass().getCanonicalName(), this.getClass());
				final Gson gson =
						new GsonBuilder().registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter(types)).create();

				final ArrayPojo expected = new ArrayPojo(this.getClass(), this.getClass());
				final ArrayPojo actual = gson.fromJson(
						"{\"multiple\":[\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\",\"de.moonset.engine.lib.night.hawk.codec.ClassTypeAdapterTest\"]}",
						ArrayPojo.class);

				assertThat(actual).isEqualToComparingFieldByField(expected);
		}

		private static class SinglePojo {
				private Class<?> single;

				public SinglePojo() { }

				public SinglePojo(final Class<?> single) {
						this.single = single;
				}

				public Class<?> getSingle() {
						return single;
				}

				public void setSingle(final Class<?> single) {
						this.single = single;
				}
		}


		private static class ArrayPojo {
				private Class<?>[] multiple;

				public ArrayPojo() { }

				public ArrayPojo(final Class<?>... multiple) {
						this.multiple = multiple;
				}

				public Class<?>[] getMultiple() {
						return multiple;
				}

				public void setMultiple(final Class<?>[] multiple) {
						this.multiple = multiple;
				}
		}
}
