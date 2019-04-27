package javax0;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestExceptional {

    @Test
    @DisplayName("when 'of' throws exception then orElse is taken into account")
    void testThrowOrElse() {
        Assertions.assertEquals("Alma", Exceptional.of(() -> {
            throw new Exception();
        }).orElse("Alma"));
    }

    @Test
    @DisplayName("when 'of' null then it throws NPE")
    void testNullOfThows() {
        Assertions.assertThrows(NullPointerException.class, () -> Exceptional.of(() -> null).orElse("Alma"));
    }

    @Test
    @DisplayName("when 'of' null then orElse is taken into account")
    void testNullOrElse() {
        Assertions.assertEquals("Alma", Exceptional.ofNullable(() -> null).orElse("Alma"));
    }

    @Test
    @DisplayName("when 'ofNullable' is null and 'or' throws then orElse is taken into account")
    void testThrowsOfNullOrElse() {
        Assertions.assertEquals("Alma", Exceptional.ofNullable(() -> null).or(() -> {
            throw new Exception();
        }).orElse("Alma"));
    }

    @Test
    @DisplayName("when something has value in the line orElse is ignored")
    void testValueOrElseIgnored() {
        Assertions.assertEquals("korte", Exceptional.ofNullable(() -> null).or(() -> {
            throw new Exception();
        }).or(() -> "korte").orElse("Alma"));
    }

    @Test
    @DisplayName("when something has value in the line and there is orNullable orElse is ignored")
    void testNullOrNullableElse() {
        Assertions.assertEquals("korte", Exceptional.of(() -> {
            throw new Exception();
        }).orNullable(() -> null).or(() -> "korte").orElse("Alma"));
    }

    @Test
    @DisplayName("after a value 'or( () -> null)' does not matter")
    void testNullOrNullIsIgnored() {
        Assertions.assertEquals("korte", Exceptional.<Object>of(() -> "korte").or(() -> null).or(() -> "korte").orElse("Alma"));
    }

    @Test
    @DisplayName("when map throws exception it is empty again")
    void testLameMap() {
        Assertions.assertEquals("Alma", Exceptional.<Object>of(() -> "korte").map(s -> {
            throw new Exception();
        }).orElse("Alma"));
    }

    @Test
    @DisplayName("when map does not throw exception it rules")
    void testGoodMap() {
        Assertions.assertEquals("szilva", Exceptional.<Object>of(() -> "korte").map(s -> "szilva").orElse("Alma"));
    }

    @Test
    @DisplayName("when flatMap throws exception it is empty again")
    void testFlatMap() {
        Assertions.assertEquals("Alma", Exceptional.<Object>of(() -> "korte").flatMap(s -> Exceptional.of(() -> null)).orElse("Alma"));
    }

    @Test
    @DisplayName("when flatMap does not throw exception it rules")
    void testGoodFlatMap() {
        Assertions.assertEquals("szilva", Exceptional.<Object>of(() -> "korte").flatMap(s -> Exceptional.of(() -> "szilva")).orElse("Alma"));
    }
}
