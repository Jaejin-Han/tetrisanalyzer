package com.github.tetrisanalyzer.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vertices {
    List<Vertex> vertices;

    public Vertices(Vertex... vertices) {
        this.vertices = new ArrayList<>(vertices.length);
        this.vertices.addAll(Arrays.asList(vertices));
    }

    public Vertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    private double maxY() {
        double max = Double.MIN_VALUE;

        for (Vertex vertex : vertices) {
            if (vertex.y > max) {
                max = vertex.y;
            }
        }
        return max;
    }

    public Vertices normalizeY(double dy) {
        List<Vertex> result = new ArrayList<>(vertices.size());

        double maxy = maxY();
        for (Vertex vertex : vertices) {
//            result.add(new Vertex(vertex.x, 1 - (vertex.y * dy) / maxy));
            result.add(new Vertex(vertex.x, 1 - vertex.y / maxy));
        }
        return new Vertices(result);
    }

    public Vertices clipHorizontal(double wx1, double wx2) {
		int lastIdx = vertices.size() - 1;

		double x1 = lastIdx * wx1;
		double x2 = lastIdx * wx2;
		int index1 = (int)x1;
		int index2 = (int)x2;

		List<Vertex> result = new ArrayList<>(lastIdx + 2);

		if (wx1 == 0) {
			result.add(vertices.get(0));
		} else {
			double y = vertices.get(index1).y + ((vertices.get(index1 + 1).y - vertices.get(index1).y)) * (x1 - index1);
			result.add(new Vertex(x1, y));
		}

		for (int i=index1+1; i<=index2; i++) {
			result.add(vertices.get(i));
		}

		if (wx2 < 1.0) {
			double y = vertices.get(index2).y + ((vertices.get(index2 + 1).y - vertices.get(index2).y)) * (x2 - index2);
			result.add(new Vertex(x2, y));
		}
		return new Vertices(result);
    }

    public Vertices normalizeX() {
        int lastIdx = vertices.size() - 1;
        List<Vertex> result = new ArrayList<>(vertices.size());

        double minx = vertices.get(0).x;
        double maxx = vertices.get(lastIdx).x;

        if (minx == maxx) {
            return this;
        }
        double scale = 1 / (maxx - minx);

        for (Vertex vertex : vertices) {
            result.add(vertex.normalizeX(-minx, scale));
        }
        return new Vertices(result);
    }

    public Lines clipVertically(double wy1, double wy2) {
        List<Line> lines = new ArrayList<>(vertices.size());

        if (vertices.size() <= 1) {
            return new Lines(lines);
        }
        Vertex v1 = vertices.get(0);
        for (int i=1; i<vertices.size(); i++) {
            Vertex v2 = vertices.get(i);

            double x1 = v1.x;
            double y1 = v1.y;
            double x2 = v2.x;
            double y2 = v2.y;

            if ((y1 < wy1 && y2 < wy1) || (y1 > wy2 && y2 > wy2)) {
                v1 = v2;
                continue;
            }

            if (y1 < wy1) {
                x1 = v1.x + (wy1 - v1.y) / (y2 - v1.y) * (x2 - v1.x);
                y1 = wy1;
            } else if (y1 > wy2) {
                x1 = v1.x + (v1.y - wy2) / (v1.y - y2) * (x2 - v1.x);
                y1 = wy2;
            }
            if (y2 < wy1) {
                x2 = v1.x + (v1.y - wy1) / (v1.y - y2) * (x2 - v1.x);
                y2 = wy1;
            } else if (y2 > wy2) {
                x2 = v1.x + (wy2 - v1.y) / (y2 - v1.y) * (x2 - v1.x);
                y2 = wy2;
            }
            lines.add(new Line(new Vertex(x1,y1), new Vertex(x2,y2)));
            v1 = v2;
        }
        return new Lines(lines);
    }

    public int size() {
        return vertices.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertices vertices1 = (Vertices) o;

        if (vertices != null ? !vertices.equals(vertices1.vertices) : vertices1.vertices != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return vertices != null ? vertices.hashCode() : 0;
    }

    @Override
    public String toString() {
        return vertices.toString();
    }
}
