package fr.cubibox.backroom2_5d.engine.collisions;

import fr.cubibox.backroom2_5d.engine.maths.Interval2F;
import fr.cubibox.backroom2_5d.engine.maths.Vector2F;
import fr.cubibox.backroom2_5d.engine.maths.shapes.Polygon2F;
import fr.cubibox.backroom2_5d.engine.maths.shapes.Rectangle2F;

public class SAT {
    public static Interval2F getInterval(Rectangle2F rectangle, Vector2F axis) {
        Interval2F result = new Interval2F(0, 0);

        Vector2F min = rectangle.getMin();
        Vector2F max = rectangle.getMax();

        Vector2F[] verts = {
                new Vector2F(min.getX(), min.getY()),
                new Vector2F(max.getX(), min.getY()),
                new Vector2F(max.getX(), max.getY()),
                new Vector2F(min.getX(), max.getY())
        };

        result.setMax(axis.dot(verts[0]));
        result.setMin(result.getMax());

        for (int i = 1; i < 4; i++) {
            float projection = axis.dot(verts[i]);
            if (projection < result.getMin()) {
                result.setMin(projection);
            } else if (projection > result.getMax()) {
                result.setMax(projection);
            }
        }

        return result;
    }

    public static boolean overlapOnAxis(Rectangle2F rectangleA, Rectangle2F rectangleB, Vector2F axis) {
        Interval2F intervalA = getInterval(rectangleA, axis);
        Interval2F intervalB = getInterval(rectangleB, axis);

        return !(intervalA.getMax() < intervalB.getMin()) && !(intervalB.getMax() < intervalA.getMin());
    }

    public static boolean RectangleRectangleSAT(Rectangle2F rectA, Rectangle2F rectB) {
        Vector2F[] axes = {
                new Vector2F(1, 0),
                new Vector2F(0, 1)
        };

        for (int i = 0; i < 2; i++) {
            if (!overlapOnAxis(rectA, rectB, axes[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean GenericSAT(Polygon2F polyA, Polygon2F polyB) {
        /*Vector2F[] axesA = polyA.getAxes();
        Vector2F[] axesB = polyB.getAxes();

        for (int i = 0; i < axesA.length; i++) {
            if (!overlapOnAxis(polyA, polyB, axesA[i])) {
                return false;
            }
        }

        for (int i = 0; i < axesB.length; i++) {
            if (!overlapOnAxis(polyA, polyB, axesB[i])) {
                return false;
            }
        }

        return true;*/
        return false;
    }
}