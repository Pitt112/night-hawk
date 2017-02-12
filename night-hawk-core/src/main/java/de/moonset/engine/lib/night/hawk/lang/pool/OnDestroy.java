package de.moonset.engine.lib.night.hawk.lang.pool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author pitt
 *         <p>
 *         annotates a method used by {@link Pool<?>} to call before the object is being destroyed
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnDestroy {}
