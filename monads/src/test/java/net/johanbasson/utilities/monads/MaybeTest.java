package net.johanbasson.utilities.monads;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class MaybeTest {

    @Test
    void shouldReturnEmpty() {
        Maybe<?> value = Maybe.none();
        assertThat(value).isNotNull();
    }

    @Test
    void shouldResolveToNone() {
        Maybe<Integer> value = Maybe.some(null);
        assertThat(value)
                .isNotNull()
                .isEqualTo(Maybe.none());
    }

    @Test
    void shouldResolveToValue() {
        Maybe<Integer> value = Maybe.some(42);
        assertThat(value)
                .isNotNull()
                .isInstanceOf(Maybe.Some.class);

        assertThat(value.get())
                .isNotNull()
                .isEqualTo(42);
    }

    @Test
    void shouldMapSomeValue() {
        Maybe<Integer> result = Maybe.some(42).map(v -> v * 42);
        assertThat(result.get())
                .isNotNull()
                .isEqualTo(1764);
    }

    @Test
    void shouldNotMapNone() {
        Integer i = null;
        Maybe<Integer> result = Maybe.some(i).map(v -> v * 42);
        assertThat(result)
                .isNotNull()
                .isEqualTo(Maybe.none());
    }

    @Test
    void shouldMapAndFlattenSome() {
        Integer in = 42;
        Maybe<Integer> result = Maybe.some(in).flatMap(v -> Maybe.some(v * 100));
        assertThat(result).isNotNull()
                .isInstanceOf(Maybe.Some.class);

        assertThat(result.get())
                .isNotNull()
                .isEqualTo(4200);
    }

    @Test
    void shouldMapAndFlattenNone() {
        Integer in = null;
        Maybe<Integer> result = Maybe.some(in).flatMap(v -> Maybe.some(v * 100));
        assertThat(result).isNotNull()
                .isInstanceOf(Maybe.None.class);
    }

    @Test
    void shouldFoldRightOnSome() {
        Integer in = 42;
        Integer value = Maybe.some(in).fold(() -> 100, v -> v * 100);
        assertThat(value).isNotNull().isEqualTo(4200);
    }

    @Test
    void shouldConvertSomeToRight() {
        Maybe<Integer> maybe = Maybe.some(1);
        Either<String, Integer> result = maybe.toEither(() -> "No Value");
        result.fold(l -> fail("Unexpected left"), r -> assertThat(r).isEqualTo(1));
    }

    @Test
    void shouldConvertNoneToLeft() {
        Maybe<Integer> maybe = Maybe.none();
        Either<String, Integer> result = maybe.toEither(() -> "No Value");
        result.fold(l -> assertThat(l).isEqualTo("No Value"), r -> fail("Unexpected right"));
    }
}