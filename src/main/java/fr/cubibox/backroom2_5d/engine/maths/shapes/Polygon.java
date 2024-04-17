package fr.cubibox.backroom2_5d.engine.maths.shapes;

import fr.cubibox.backroom2_5d.engine.maths.Line;
import fr.cubibox.backroom2_5d.engine.maths.Vector2;

import java.util.ArrayList;

public class Polygon implements Shape {
    private final ArrayList<Line> edges;
    
    public Polygon(ArrayList<Line> edges) {
        this.edges = edges;
    }

    public Polygon(ArrayList<Vector2> points, boolean isLine) {
        this.edges = new ArrayList<>();
        if (isLine) {
            for (int i = 0; i < points.size() - 1; i++) {
                edges.add(new Line(points.get(i), points.get(i + 1)));
            }
        } else {
            for (int i = 0; i < points.size(); i++) {
                edges.add(new Line(points.get(i), points.get((i + 1) % points.size())));
            }
        }
    }

    public Polygon(Vector2... points) {
        this.edges = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            edges.add(new Line(points[i], points[(i + 1) % points.length]));
        }
    }

    public ArrayList<Line> getEdges() {
        return edges;
    }

    public Vector2[] getAxes() {
        Vector2[] axes = new Vector2[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            axes[i] = edges.get(i).getNormal();
        }
        return axes;
    }

    @Override
    public Line[] getVertices() {
        return new Line[0];
    }
}
