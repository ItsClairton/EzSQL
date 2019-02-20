package com.gitlab.pauloo27.core.sql;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DataSerializer<T> {

    private Function<T, Object> serializer;
    private BiFunction<Class<T>, Object, T> deserializer;

    public DataSerializer(Function<T, Object> serializer, BiFunction<Class<T>, Object, T> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public Function<T, Object> getSerializer() {
        return serializer;
    }

    public BiFunction<Class<T>, Object, T> getDeserializer() {
        return deserializer;
    }
}
