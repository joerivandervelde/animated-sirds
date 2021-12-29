package nl.joerivandervelde.asirds.depthmap;

public class Coordinate {
    final int x;
    final int y;
    final int z;

    Coordinate(final double x, final double y, final double z)
    {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    @Override
    public String toString()
    {
        return "{" + x + ", " + y + ", " + z + "}";
    }

}
