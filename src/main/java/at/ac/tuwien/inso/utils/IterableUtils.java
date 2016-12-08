package at.ac.tuwien.inso.utils;

import java.util.*;
import java.util.stream.*;

public class IterableUtils {

    public static <T> List<T> toList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }
}
