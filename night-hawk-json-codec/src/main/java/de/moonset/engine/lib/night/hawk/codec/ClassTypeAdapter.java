package de.moonset.engine.lib.night.hawk.codec;

import com.google.common.base.Preconditions;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

/**
 * Created by pitt on 25.12.16.
 */
public class ClassTypeAdapter extends TypeAdapter<Class<?>> {

		private final Map<String, Class<?>> types;

		public ClassTypeAdapter(final Map<String, Class<?>> types) {
				Preconditions.checkNotNull(types);
				this.types = types;
		}

		@Override
		public void write(final JsonWriter out, final Class<?> type) throws IOException {
				if (type != null) {
						out.value(type.getCanonicalName());
				} else {
						out.nullValue();
				}
		}

		@Override
		public Class<?> read(final JsonReader in) throws IOException {

				if (in.peek() == JsonToken.NULL) {
						in.nextNull();
						return null;
				}

				return types.get(in.nextString());
		}
}
