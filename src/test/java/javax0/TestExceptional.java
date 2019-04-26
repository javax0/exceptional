package javax0;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestExceptional {

    private static Object thrower() throws Exception {
        throw new Exception();
    }

    private static Object object() {
        return "korte";
    }

    private static Object nuller() {
        return null;
    }

    @Test
    @DisplayName("when 'of' throws exception then orElse is taken into account")
    void testThrowOrElse() {
        Assertions.assertEquals("Alma", Exceptional.of(TestExceptional::thrower).orElse("Alma"));
    }

    @Test
    @DisplayName("when 'of' null then it throws NPE")
    void testNullOfThows() {
        Assertions.assertThrows(NullPointerException.class, () -> Exceptional.of(TestExceptional::nuller).orElse("Alma"));
    }

    @Test
    @DisplayName("when 'of' null then orElse is taken into account")
    void testNullOrElse() {
        Assertions.assertEquals("Alma", Exceptional.ofNullable(TestExceptional::nuller).orElse("Alma"));
    }

    @Test
    @DisplayName("when 'ofNullable' is null and 'or' throws then orElse is taken into account")
    void testThrowsOfNullOrElse() {
        Assertions.assertEquals("Alma", Exceptional.ofNullable(TestExceptional::nuller).or(TestExceptional::thrower).orElse("Alma"));
    }

    @Test
    @DisplayName("when something has value in the line orElse is ignored")
    void testValueOrElseIgnored() {
        Assertions.assertEquals("korte", Exceptional.ofNullable(TestExceptional::nuller).or(TestExceptional::thrower).or(TestExceptional::object).orElse("Alma"));
    }

    @Test
    @DisplayName("when something has value in the line and there is orNullable orElse is ignored")
    void testNullOrNullableElse() {
        Assertions.assertEquals("korte", Exceptional.of(TestExceptional::thrower).orNullable(TestExceptional::nuller).or(TestExceptional::object).orElse("Alma"));
    }

    @Test
    @DisplayName("after a value 'or( () -> null)' does not matter")
    void testNullOrNullIsIgnored() {
        Assertions.assertEquals("korte", Exceptional.<Object>of(() -> "korte").or(TestExceptional::nuller).or(TestExceptional::object).orElse("Alma"));
    }

    @Test
    @DisplayName("when map throws exception it is empty again")
    void testLameMap() {
        Assertions.assertEquals("Alma", Exceptional.<Object>of(() -> "korte").map(s -> thrower()).orElse("Alma"));
    }

    @Test
    @DisplayName("when map does not throw exception it rules")
    void testGoodMap() {
        Assertions.assertEquals("szilva", Exceptional.<Object>of(() -> "korte").map(s -> "szilva").orElse("Alma"));
    }

    @Test
    @DisplayName("when flatMap throws exception it is empty again")
    void testFlatMap() {
        Assertions.assertEquals("Alma", Exceptional.<Object>of(() -> "korte").flatMap(s -> Exceptional.of(TestExceptional::nuller)).orElse("Alma"));
    }

    @Test
    @DisplayName("when flatMap does not throw exception it rules")
    void testGoodFlatMap() {
        Assertions.assertEquals("szilva", Exceptional.<Object>of(() -> "korte").flatMap(s -> Exceptional.of(() -> "szilva")).orElse("Alma"));
    }
}
