package yetanotherbardtale.helpers;

import java.util.Collection;
import java.util.Iterator;

/**
 * Provides common static helper functions to do with string operations.
 * @author Caleb Simpson
 * @version 0.1.0 
 */
public class StringHelper {
    
    /**
     * Helper function to create a string from a collection by joining all the
     * elements together using a delimiter.
     * @param collection The collection to join.
     * @param delim The delimiter to insert between each element
     * @return The resulting string.
     */
    public static <T> String join(Collection<T> collection, String delim) {
        StringBuilder result = new StringBuilder();
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            result.append(iterator.next());
            if (iterator.hasNext()) {
                result.append(delim);
            }
        }
        return result.toString();
    }
}
