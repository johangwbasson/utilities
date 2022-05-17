package net.johanbasson.util;

import java.util.Objects;
import java.util.function.Function;

/**
 * Either monad implementation in Java
 *
 * @param <L> Left
 * @param <R> Right
 */
public abstract class Either<L, R> {

    private Either() {
    }

    /**
     * Construct left from value
     *
     * @param left value for left
     * @return Either with left value
     */
    public static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    /**
     * Construct right from value
     * @return Either with right value
     */
    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    /**
     * Perform a fold based on the current left or right value. leftMapper will be applied if this Either has a left value
     * rightMapper will be applied if this Either has a right value.
     * @param leftMapper Function to map left
     * @param rightMapper Function to map right
     * @return Mapped value
     */
    abstract <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper);

    /**
     * Perform a map and flatten operation when this Either has a right value
     *
     * @param mapper Mapping function
     * @return Either with mapper applied
     */
    abstract <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, ? extends U>> mapper);

    /**
     *
     * @return true if this Either contains a right value
     */
    abstract boolean isRight();

    /**
     *
     * @return true if this Either contains a left value
     */
    abstract boolean isLeft();

    /**
     * Map right value using supplied function
     *
     * @param mapper Mapper function
     * @return Either with mapper applied
     */
    abstract <U> Either<L, U> map(Function<R, U> mapper);

    /**
     * Map left value using supplied function
     *
     * @param mapper Mapper function
     * @return Either with mapper applied
     */
    abstract <U> Either<U, R> mapLeft(Function<L, U> mapper);

    static class Left<L, R> extends Either<L, R> {
        private final L value;
        protected Left(L value) {
            this.value = value;
        }

        @Override
        <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
            return leftMapper.apply(value);
        }

        @Override
        @SuppressWarnings("unchecked")
        <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, ? extends U>> mapper) {
            return (Either<L, U>) this;
        }

        @Override
        boolean isRight() {
            return false;
        }

        @Override
        boolean isLeft() {
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        <U> Either<L, U> map(Function<R, U> mapper) {
            return (Either<L, U>) this;
        }

        @Override
        <U> Either<U, R> mapLeft(Function<L, U> mapper) {
            return new Left<>(mapper.apply(value));
        }
    }

    static class Right<L ,R> extends Either<L, R> {
        private final R value;

        protected Right(R value) {
            this.value = value;
        }

        @Override
        <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
            return rightMapper.apply(value);
        }

        @Override
        @SuppressWarnings("unchecked")
        <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, ? extends U>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return (Either<L, U>) mapper.apply(value);
        }

        @Override
        boolean isRight() {
            return true;
        }

        @Override
        boolean isLeft() {
            return false;
        }

        @Override
        <U> Either<L, U> map(Function<R, U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return new Right<>(mapper.apply(value));
        }

        @Override
        @SuppressWarnings("unchecked")
        <U> Either<U, R> mapLeft(Function<L, U> mapper) {
            return (Either<U, R>) this;
        }
    }
}
