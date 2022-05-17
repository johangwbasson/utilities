package net.johanbasson.util;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This is a Java implementation of the Maybe monad which represent an optional value.
 * This means the monad can contain either a value or is empty.
 *
 * @param <T>
 */
public abstract class Maybe<T extends Serializable> {

    private Maybe() {
    }

    /**
     * Returns the value contained in the monad. Note this will throw a NoSuchElementException if it is empty.
     * Do not use this method as it can throw an unexpected exception. This behavior ensures that nulls are never
     * passed around
     *
     * @return value contained in the monad if not empty
     */
    abstract T get();

    /**
     * Returns true if this maybe is empty otherwise false if this maybe contains a value
     *
     * @return true if empty, false if not
     */
    abstract boolean isEmpty();

    /**
     * Constructs a maybe with an empty value
     *
     * @return Maybe with empty value
     */
    static <T extends Serializable> Maybe<T> none() {
        @SuppressWarnings("unchecked") final None<T> none = (None<T>) None.INSTANCE;
        return none;
    }

    /**
     * Constructs a Maybe with a value
     *
     * @param <A> Any serializable value. If the value is null, a maybe is constructed with an empty value.
     * @return Maybe which contains a value
     */
    public static <A extends Serializable> Maybe<A> some(A value) {
        return value == null ? none() : new Some<>(value);
    }

    /**
     * Converts the current value contained in this maybe using the supplied function.
     * If the maybe does not contain a value then a maybe with no value is returned.
     *
     * @param mapper Function which converts the value contained in this Maybe to another value
     * @return Maybe with the new value
     */
    abstract <U extends Serializable> Maybe<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Performs a map over the value contained in this maybe and flattens it to a maybe.
     *
     * @param mapper Function to convert the value
     * @return Maybe with supplied converted value, received from the mapper if there is a value, otherwise a Maybe with empty value.
     */
    abstract <U extends Serializable> Maybe<U> flatMap(Function<? super T, ? extends Maybe<? extends U>> mapper);

    /**
     * If the Maybe does not contain a value, the supplier is executed to produce a value,
     * otherwise the mapper function is executed with the value contained in this Maybe.
     *
     * @param ifNone Supplies a value if this Maybe contains no value.
     * @param mapper Converts the value contained in this Maybe to another type
     * @return Either the supplied value (from the supplier) or the value produced by the mapper
     */
    abstract <U extends Serializable> U fold(Supplier<U> ifNone, Function<? super T, ? extends U> mapper);

    abstract <L, T> Either<L, T> toEither(Supplier<L> leftSupplier);

    @EqualsAndHashCode(callSuper = false)
    static final class None<T extends Serializable> extends Maybe<T> implements Serializable {

        private static final long serialVersionUID = -7117560152256275990L;
        static final Maybe<?> INSTANCE = new None<>();

        private None() {
        }

        @Override
        T get() {
            throw new NoSuchElementException("No value present");
        }

        @Override
        boolean isEmpty() {
            return true;
        }

        @Override
        <U extends Serializable> Maybe<U> map(Function<? super T, ? extends U> mapper) {
            return none();
        }

        @Override
        <U extends Serializable> Maybe<U> flatMap(Function<? super T, ? extends Maybe<? extends U>> mapper) {
            return Maybe.none();
        }

        @Override
        <U extends Serializable> U fold(Supplier<U> ifNone, Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(ifNone, "supplier isn null");
            return ifNone.get();
        }

        @Override
        <L, T> Either<L, T> toEither(Supplier<L> leftSupplier) {
            return Either.left(leftSupplier.get());
        }

    }

    @EqualsAndHashCode(callSuper = false)
    static final class Some<T extends Serializable> extends Maybe<T> implements Serializable {
        private final T value;

        private Some(T value) {
            this.value = value;
        }

        @Override
        T get() {
            return value;
        }

        @Override
        boolean isEmpty() {
            return false;
        }

        @Override
        <U extends Serializable> Maybe<U> map(Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return some(mapper.apply(get()));
        }

        @Override
        @SuppressWarnings("unchecked")
        <U extends Serializable> Maybe<U> flatMap(Function<? super T, ? extends Maybe<? extends U>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return (Maybe<U>) mapper.apply(get());
        }

        @Override
        <U extends Serializable> U fold(Supplier<U> ifNone, Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return mapper.apply(get());
        }

        @Override
        @SuppressWarnings("unchecked")
        <L, T> Either<L, T> toEither(Supplier<L> leftSupplier) {
            return (Either<L, T>) Either.right(value);
        }
    }

}
