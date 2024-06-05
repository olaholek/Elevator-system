package pl.holowinska.elevatorsystem.domain;

public enum Direction {
    UP(1L), DOWN(-1L), NONE(0L);

    public final Long id;

    Direction(Long id) {
        this.id = id;
    }

    public static Direction of(Long id) {
        for (Direction e : values()) {
            if (e.id.equals(id)) {
                return e;
            }
        }
        return null;
    }
}
