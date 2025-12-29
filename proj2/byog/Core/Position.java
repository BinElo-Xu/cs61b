package byog.Core;

import java.io.Serializable;

public class Position implements Serializable {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position add(Position other) {
        return new Position(this.x + other.x, this.y + other.y);
    }

    public Position multiply(int scalar) {
        return new Position(this.x * scalar, this.y * scalar);
    }

    public double sub(Position other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
