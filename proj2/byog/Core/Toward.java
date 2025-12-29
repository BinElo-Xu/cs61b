package byog.Core;

public enum Toward {
    w(new Position(0, 1)),
    s(new Position(0, -1)),
    a(new Position(-1, 0)),
    d(new Position(1, 0)),
    STAY(new Position(0, 0)),;
    final Position position;

    Toward(Position position) {
        this.position = position;
    }
}
