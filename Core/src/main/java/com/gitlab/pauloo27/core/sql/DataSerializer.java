package com.gitlab.pauloo27.core.sql;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A Data Serializer and Deserializer. Used to convert the values to the rows.
 *
 * @param <T> The object to serializer.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class DataSerializer<T> {

    /**
     * The serialize function.
     */
    private Function<T, Object> serializer;

    /**
     * The deserialize function.
     */
    private BiFunction<Class<T>, Object, T> deserializer;

    /**
     * Builds a Data Serializer.
     *
     * @param serializer   The serializer.
     * @param deserializer The deserializer.
     */
    public DataSerializer(Function<T, Object> serializer, BiFunction<Class<T>, Object, T> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    /**
     * Gets the serializer.
     *
     * @return The serializer.
     */
    public Function<T, Object> getSerializer() {
        return serializer;
    }


    /**
     * Gets the deserializer.
     *
     * @return The deserializer
     */
    public BiFunction<Class<T>, Object, T> getDeserializer() {
        return deserializer;
    }
}
