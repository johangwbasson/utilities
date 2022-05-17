package net.johanbasson.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EitherTest {

    @Test
    void shouldConstructLeft() {
        Either<String, Integer> value = Either.left("Error Message");
        assertThat(value).isNotNull()
                .isInstanceOf(Either.Left.class);
        assertThat(value.isLeft()).isTrue();
    }

    @Test
    void shouldConstructRight() {
        Either<String, Integer> value = Either.right(42);
        assertThat(value)
                .isNotNull()
                .isInstanceOf(Either.Right.class);
        assertThat(value.isRight()).isTrue();
    }

    @Test
    void shouldFoldLeftOnLeft() {
        final String value = Either.left("A").fold(l -> l + "-", r -> r + "+");
        Assertions.assertThat(value).isEqualTo("A-");
    }

    @Test
    void shouldFoldRightOnRight() {
        final String value = Either.right("A").fold(l -> l + "-", r -> r + "+");
        Assertions.assertThat(value).isEqualTo("A+");
    }

    @Test
    void shouldMapLeft() {
        Either<String, Integer> a = Either.left("A");
        final Either<String, Integer> result = a.mapLeft(i -> i + "B");
        assertThat(result.isLeft()).isTrue();
        result.fold(l -> assertThat(l).isEqualTo("AB"), r -> Assertions.fail("Unexpected right"));
    }

    @Test
    void shouldMapRight() {
        Either<Integer, String> a = Either.right("B");
        final Either<Integer, String> result = a.map(i -> i + "B");
        assertThat(result.isRight()).isTrue();
        result.fold(l -> Assertions.fail("Unexpected left"), r -> assertThat(r).isEqualTo("BB"));
    }

    @Test
    void shouldFlatMapRight() {
        Either<String, Integer> either = Either.right(42);
        either.flatMap(v -> Either.right("ok"))
                .fold( l -> Assertions.fail("Unexpected left"), r -> assertThat(r).isEqualTo("ok"));
    }

    @Test
    void shouldFlatMapLeft() {
        Either<String, Integer> either = Either.left("error");
        either.flatMap(v -> Either.right("ok"))
                .fold(l -> assertThat(l).isEqualTo("error"), r -> Assertions.fail("Unexpected right"));
    }
}