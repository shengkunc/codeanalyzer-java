import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class Validate {

    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE =
        "The validated character sequence is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE =
        "The validated character sequence index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE =
        "The validated collection index is invalid: %d";

    private static String getMessage(final String message, final Object... values) {
        return ArrayUtils.isEmpty(values) ? message : String.format(message, values);
    }

    public static <T extends Collection<?>> T notEmpty(final T collection) {
        return notEmpty(collection, DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE);
    }

    public static <T extends Map<?, ?>> T notEmpty(final T map) {
        return notEmpty(map, DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE);
    }

    public static <T extends CharSequence> T notEmpty(final T chars) {
        return notEmpty(chars, DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE);
    }

    public static <T extends Collection<?>> T notEmpty(final T collection, final String message, final Object... values) {
        Objects.requireNonNull(collection, toSupplier(message, values));
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return collection;
    }

    public static <T extends Map<?, ?>> T notEmpty(final T map, final String message, final Object... values) {
        Objects.requireNonNull(map, toSupplier(message, values));
        if (map.isEmpty()) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return map;
    }

    public static <T extends CharSequence> T notEmpty(final T chars, final String message, final Object... values) {
        Objects.requireNonNull(chars, toSupplier(message, values));
        if (chars.length() == 0) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return chars;
    }

    public static <T> T[] notEmpty(final T[] array) {
        return notEmpty(array, DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE);
    }

    public static <T> T[] notEmpty(final T[] array, final String message, final Object... values) {
        Objects.requireNonNull(array, toSupplier(message, values));
        if (array.length == 0) {
            throw new IllegalArgumentException(getMessage(message, values));
        }
        return array;
    }

    private static Supplier<String> toSupplier(final String message, final Object... values) {
        return () -> getMessage(message, values);
    }

    private static Supplier<String> toSupplier(final String message, final Object values) {
        return () -> getMessage(message, values);
    }

    public static <T extends Collection<?>> T validIndex(final T collection, final int index) {
        return validIndex(collection, index, DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE, Integer.valueOf(index));
    }

    public static <T extends CharSequence> T validIndex(final T chars, final int index) {
        return validIndex(chars, index, DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE, Integer.valueOf(index));
    }

    public static <T extends Collection<?>> T validIndex(final T collection, final int index, final String message, final Object... values) {
        Objects.requireNonNull(collection, "collection");
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException(getMessage(message, values));
        }
        return collection;
    }

    public static <T extends CharSequence> T validIndex(final T chars, final int index, final String message, final Object... values) {
        Objects.requireNonNull(chars, "chars");
        if (index < 0 || index >= chars.length()) {
            throw new IndexOutOfBoundsException(getMessage(message, values));
        }
        return chars;
    }

    public static <T> T[] validIndex(final T[] array, final int index) {
        return validIndex(array, index, DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE, Integer.valueOf(index));
    }

    public static <T> T[] validIndex(final T[] array, final int index, final String message, final Object... values) {
        Objects.requireNonNull(array, "array");
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(getMessage(message, values));
        }
        return array;
    }
}
