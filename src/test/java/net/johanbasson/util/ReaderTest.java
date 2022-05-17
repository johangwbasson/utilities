package net.johanbasson.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReaderTest {

    @Test
    void shouldApply() {
        Reader<Integer, String> r = Reader.unit(Object::toString);
        assertThat(r.apply(10)).isEqualTo("10");
    }

    @Test
    void shouldMap() {
        Reader<Integer, String> r = Reader.unit(Object::toString);
        Reader<Integer, String> result = r.map(s -> s + "A");
        String value = result.apply(1);
        Assertions.assertThat(value).isEqualTo("1A");
    }

    @Test
    void shouldFlatMap() {
        Reader<Integer, String> r = Reader.unit(Object::toString);
        Reader<Integer, String> result = r.flatMap(s -> Reader.unit(q -> q + "AA"));
        String res = result.apply(42);
        Assertions.assertThat(res).isEqualTo("AA42");
    }
}