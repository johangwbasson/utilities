package net.johanbasson.util;

import java.util.function.Function;

/**
 * Implementation of the Reader monad for Java
 *
 * @param <A>
 * @param <B>
 */
public final class Reader<A, B> {

    private final Function<A, B> function;

    public Reader(Function<A, B> f) {
        function = f;
    }

    public final Function<A, B> getFunction() {
        return function;
    }

    public static <A, B> Reader<A, B> unit(Function<A, B> f) {
        return new Reader<>(f);
    }

    public static <A, B> Reader<A, B> constant(B b) {
        return unit(a -> b);
    }

    public B apply(A a) {
        return function.apply(a);
    }

    public <C> Reader<A, C> map(Function<B, C> f) {
        return unit(function.andThen(f));
    }

    public <C> Reader<A, C> flatMap(Function<B, Reader<A, C>> f) {
        return unit(a -> f.apply(function.apply(a)).apply(a));
    }

    public <C> Reader<A, C> bind(Function<B, Reader<A, C>> f) {
        return flatMap(f);
    }
}
