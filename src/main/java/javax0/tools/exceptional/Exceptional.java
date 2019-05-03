package javax0.tools.exceptional;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Exceptional<H> {
    private static final Exceptional<?> EMPTY = new Exceptional<>();
    private final Optional<H> optional;

    private Exceptional() {
        this.optional = Optional.empty();
    }

    private Exceptional(Optional<H> optional) {
        this.optional = optional;
    }

    public static <T> Exceptional<T> empty() {
        //noinspection unchecked
        return (Exceptional<T>) EMPTY;
    }

    public static class Catching<T> {
        private final Class<? extends Exception>[] catchingOnly;

        private Catching(Class<? extends Exception>[] catchingOnly) {
            this.catchingOnly = catchingOnly;
        }

        private static boolean isOneOf(Exception e, Class<? extends Exception>[] ecs) {
            return Arrays.stream(ecs).anyMatch(ec -> ec.isAssignableFrom(e.getClass()));
        }

        private static <T> Exceptional<T> empty(Exception e, Class<? extends Exception>[] ecs) throws Exception {
            if (isOneOf(e, ecs)) {
                return Exceptional.empty();
            } else {
                throw e;
            }
        }

        public Exceptional<T> of(ThrowingSupplier<T> s) throws Exception {
            final T value;
            try {
                value = s.get();
            } catch (Exception e) {
                return empty(e, catchingOnly);
            }
            return new Exceptional<>(Optional.of(value));
        }

        public Exceptional<T> ofNullable(ThrowingSupplier<T> s) throws Exception {
            try {
                T value = s.get();
                return new Exceptional<>(Optional.ofNullable(value));
            } catch (Exception e) {
                return empty(e, catchingOnly);
            }
        }
    }

    public static <T> Catching<T> catchingOnly(Class<? extends Exception>... exceptions) {
        return new Catching(exceptions);
    }

    public static <T> Exceptional<T> of(ThrowingSupplier<T> s) {
        final T value;
        try {
            value = s.get();
        } catch (Exception e) {
            return empty();
        }
        return new Exceptional<>(Optional.of(value));
    }

    public static <T> Exceptional<T> ofNullable(ThrowingSupplier<T> s) {
        try {
            T value = s.get();
            return new Exceptional<>(Optional.ofNullable(value));
        } catch (Exception e) {
            return empty();
        }
    }

    public H get() {
        return optional.get();
    }

    public boolean isPresent() {
        return optional.isPresent();
    }

    public boolean isEmpty() {
        return optional.isEmpty();
    }

    public void ifPresent(Consumer<? super H> action) {
        optional.ifPresent(action);
    }

    public void ifPresentOrElse(Consumer<? super H> action, Runnable emptyAction) {
        optional.ifPresentOrElse(action, emptyAction);
    }

    public Exceptional<H> filter(Predicate<? super H> predicate) {
        return new Exceptional<>(optional.filter(predicate));
    }

    public <U> Exceptional<U> map(ThrowingFunction<? super H, ? extends U> mapper) {
        return new Exceptional<>(optional.map(mapper.lame()));
    }

    public <U> Exceptional<U> flatMap(ThrowingFunction<? super H, ? extends Exceptional<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            @SuppressWarnings("unchecked")
            Exceptional<U> r = (Exceptional<U>) mapper.lame().apply(optional.get());
            return r == null ? empty() : r;
        }
    }

    public Exceptional<H> or(ThrowingSupplier<H> supplier) {
        Objects.requireNonNull(supplier);
        if (isPresent()) {
            return this;
        } else {
            return Exceptional.of(supplier);
        }
    }

    public Exceptional<H> orNullable(ThrowingSupplier<H> supplier) {
        Objects.requireNonNull(supplier);
        if (isPresent()) {
            return this;
        } else {
            return Exceptional.ofNullable(supplier);
        }
    }

    public Stream<H> stream() {
        return optional.stream();
    }

    public H orElse(H other) {
        return optional.orElse(other);
    }

    public H orElseGet(Supplier<? extends H> supplier) {
        return optional.orElseGet(supplier);
    }

    public H orElseThrow() {
        return optional.orElseThrow();
    }

    public <X extends Throwable> H orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return optional.orElseThrow(exceptionSupplier);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Exceptional)) {
            return false;
        }

        Exceptional<?> other = (Exceptional<?>) obj;
        return Objects.equals(optional, other.optional);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(optional);
    }

    @Override
    public String toString() {
        return optional.isPresent()
            ? String.format("Exceptional[%s]", optional.get())
            : "Exceptional.empty";
    }

    @FunctionalInterface
    public interface ThrowingSupplier<Z> {
        Z get() throws Exception;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> {
        static <T> ThrowingFunction<T, T> identity() {
            return t -> t;
        }

        R apply(T t) throws Exception;

        default Function<T, R> lame() {
            return (T t) -> {
                try {
                    return apply(t);
                } catch (Exception e) {
                    return null;
                }
            };
        }

        default <V> ThrowingFunction<V, R> compose(ThrowingFunction<? super V, ? extends T> before) {
            Objects.requireNonNull(before);
            return (V v) -> apply(before.apply(v));
        }

        default <V> ThrowingFunction<T, V> andThen(ThrowingFunction<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (T t) -> after.apply(apply(t));
        }
    }
}
