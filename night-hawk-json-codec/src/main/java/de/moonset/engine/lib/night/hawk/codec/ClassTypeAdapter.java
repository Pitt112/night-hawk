package de.moonset.engine.lib.night.hawk.codec;

import com.google.common.base.Preconditions;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
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
				out.beginObject();
				out.name("type");
				out.value(type.getCanonicalName());
				out.endObject();
		}

		@Override
		public Class<?> read(final JsonReader in) throws IOException {

				Class<?> type = null;

				in.beginObject();
				if (in.hasNext()) {
						if ("type".equals(in.nextName())) {
								type = types.get(in.nextString());
						}
				}
				in.endObject();

				return type;
		}
}
