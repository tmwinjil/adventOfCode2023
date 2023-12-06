package Utils;

import java.util.Collection;

public final class AdventUtils {

    private AdventUtils() {
    }

    public static int findProductOfCollection(Collection<Integer> collection) {
        return collection.stream().mapToInt(x->x).reduce(1, Math::multiplyExact);
    }

    public static int findSumOfCollection(Collection<Integer> collection) {
        return collection.stream().mapToInt(Integer::intValue).sum();
    }
}
