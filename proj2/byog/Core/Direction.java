package byog.Core;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public enum Direction {
    North(new Position(0, -1)),
    South(new Position(0, 1)),
    East(new Position(1, 0)),
    West(new Position(-1, 0));

    public final Position position;
    Direction(Position position) {
        this.position = position;
    }

    public static final List<Direction> CARDINAL =
            Collections.unmodifiableList(Arrays.asList(values()));
}
