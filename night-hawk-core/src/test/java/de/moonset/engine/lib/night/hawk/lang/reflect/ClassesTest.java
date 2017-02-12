package de.moonset.engine.lib.night.hawk.lang.reflect;

import org.junit.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by pitt on 17.01.17.
 */
public class ClassesTest {
		@Test
		public void testAssignablePrimitive() throws Exception {
				assertThat(Classes.isAssignable(boolean.class, Boolean.class)).isFalse();
				assertThat(Classes.isAssignable(boolean.class, boolean.class)).isTrue();
				assertThat(Classes.isAssignable(Boolean.class, boolean.class)).isTrue();
				assertThat(Classes.isAssignable(Boolean.class, Boolean.class)).isTrue();
		}

		@Test
		public void testAssignableClass() throws Exception {
				assertThat(Classes.isAssignable(Executable.class, Method.class)).isTrue();
				assertThat(Classes.isAssignable(AccessibleObject.class, Method.class)).isTrue();
				assertThat(Classes.isAssignable(Executable.class, AccessibleObject.class)).isFalse();
				assertThat(Classes.isAssignable(Executable.class, Runnable.class)).isFalse();
		}

		@Test
		public void testAssignableArray() {
				Class<?>[] a = new Class<?>[] {Boolean.class, int.class, String.class};
				Class<?>[] b = new Class<?>[] {boolean.class, int.class, String.class};

				assertThat(Classes.areAssignable(a, b)).isTrue();
				assertThat(Classes.areAssignable(b, a)).isFalse();
		}
}
