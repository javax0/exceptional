package javax0;

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

    public <U> Exceptional<U> map(Function<? super H, ? extends U> mapper) {
        return new Exceptional<>(optional.map(mapper));
    }

    public <U> Exceptional<U> flatMap(Function<? super H, ? extends Exceptional<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            @SuppressWarnings("unchecked")
            Exceptional<U> r = (Exceptional<U>) mapper.apply(optional.get());
            return Objects.requireNonNull(r);
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

    public interface ThrowingSupplier<Z> {
        Z get() throws Exception;
    }
}
