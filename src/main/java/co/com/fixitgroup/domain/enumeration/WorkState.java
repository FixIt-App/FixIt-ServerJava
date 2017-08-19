package co.com.fixitgroup.domain.enumeration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The WorkState enumeration.
 */
public enum WorkState {
    ORDERED, SCHEDULED, FINISHED, FAILED, CANCELED, IN_PROGRESS;


    public static Set<WorkState> findAll(String[] names) {
        return Arrays.asList(names).stream()
            .filter(WorkState::existsInEnum)
            .map(WorkState::valueOf)
            .collect(Collectors.toSet());
    }

    public static boolean existsInEnum(String name) {
        try{
            WorkState.valueOf(name);
            return true;
        }catch(IllegalArgumentException e) {
            return false;
        }
    }
}
