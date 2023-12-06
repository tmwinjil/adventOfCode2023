package day3;

import java.awt.Point;

public class EngineItem {
    private final Point startPoint;

    private final int value;

    public EngineItem(Point startPoint, int value) {
        this.startPoint = startPoint;
        this.value = value;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
         /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof EngineItem engineSchematic)) {
            return false;
        }

        // Compare the data members and return accordingly
        return engineSchematic.getStartPoint().equals(this.getStartPoint());
    }
}
