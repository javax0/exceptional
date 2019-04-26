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
    @DisplayName("Test exceptional")
    void test() {
        Assertions.assertEquals("Alma", Exceptional.of(TestExceptional::thrower).orElse("Alma"));
        Assertions.assertThrows(NullPointerException.class, () -> Exceptional.of(TestExceptional::nuller).orElse("Alma"));
        Assertions.assertEquals("Alma", Exceptional.ofNullable(TestExceptional::nuller).orElse("Alma"));
        Assertions.assertEquals("Alma", Exceptional.ofNullable(TestExceptional::nuller).or(TestExceptional::thrower).orElse("Alma"));
        Assertions.assertEquals("korte", Exceptional.ofNullable(TestExceptional::nuller).or(TestExceptional::thrower).or(TestExceptional::object).orElse("Alma"));
        Assertions.assertEquals("korte", Exceptional.of(TestExceptional::thrower).orNullable(TestExceptional::nuller).or(TestExceptional::object).orElse("Alma"));
    }
}
