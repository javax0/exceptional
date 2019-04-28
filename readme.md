# Exceptional

>Get rid of exceptions you'd ignore anyway

Exceptional is a single class (and some inner classes) that implements
methods that make it look very much like an `java.util.Optional`. But it
is not `Optional`.

Exceptional is a value, which is provided by a supplier (but not a
`java.util.function.Supplier`) which may throw an exception. So when you
write

```java
Exceptional.of(() -> throw new Exception()).orElse("Alma")
```

what you get is `"Alma"`. The major difference from `Optional` is that
the argument of the method `of()` is a supplier (to be specific an
`javax0.Exceptional.ThrowingSupplier`). In case the supplier throws an
`Exception` then the value inside the returned `Exceptional` object will
be "empty".

On an `Exceptional` object you can call `or()`, `map()`, `flatMap()` and
so on. Essentially the same (almost) methods that are defined in the
class `java.util.Optional`. There are some differences though.

The methods that return `Optional` in the `Optional` class here return
`Exceptional`. If it is a surprise you probably should not read on.

The static methods `of()` and `ofNullable()` and the non-static methods
`or()` and `orNullable()` accept `javax0.Exceptional.ThrowingSupplier`
as argument. (Also note that `orNullable()` barely exists in
`Optional` if at all.)

The methods `map()` and `flatMap()` accept
`javax0.Exceptional.ThrowingFunction` as argument. If any of these throw
exception the resulting `Exceptional` will be empty.

These are the non-taxative features raw and uncooked. What is it good
for?

## Get rid of exceptions

You have an old code

```java
// this is a totally made up code, we have never seen anything close
  String clientId = null;
  try {
    clientId = req.getAttribute("klientele");
  }catch(ParseException pe){
  }
  if( clientId == null ){
    clientId = getDebugClient();
  }
```

Using `Exceptional` you can

```java
  final String clientId = Exceptional.<String>ofNullable(() -> req.getAttribute("klientele"))
                                     .orNullable(() -> getDebugClient())
                                     .orElse(null);
```

it is a matter of taste which one you prefer.

You can also replace the good old

```java
  Integer value = part.dev.item(0). ...getter():
```

prone to `NullPointerException` to

```java
  Integer value = Exceptional.ofNullable(() -> part.dev.item(0). ...getter()).orElse(null);
```

if that is more readable.

# Maven

```xml
<dependency>
  <groupId>com.javax0</groupId>
  <artifactId>exceptional</artifactId>
  <version>1.0.0</version>
</dependency>
```