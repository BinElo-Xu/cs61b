public class NBody {
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        int n = in.readInt();
        double radius = in.readDouble();
        return radius;
    }
    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int n = in.readInt();
        in.readDouble();
        Planet[] planets = new Planet[n];
        for (int i = 0; i < n; i++) {
            double xp = in.readDouble();
            double yp = in.readDouble();
            double xv = in.readDouble();
            double yv = in.readDouble();
            double ma = in.readDouble();
            String fname = in.readString();
            planets[i] = new Planet(xp, yp, xv, yv, ma, fname);
        }
        return planets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = readPlanets(filename);
        double radius = readRadius(filename);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-radius, radius);
        StdDraw.setYscale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0.0, 0.0, "images/starfield.jpg");
        StdDraw.show();
        for (int i = 0; i < planets.length; i++) {
            planets[i].draw();
        }
        double t = 0.0;
        while (t <= T) {
           double[] xForces = new double[planets.length];
           double[] yForces = new double[planets.length];
           for (int i = 0; i < planets.length; i++) {
               xForces[i] = planets[i].calcNetForceExertedByX(planets);
               yForces[i] = planets[i].calcNetForceExertedByY(planets);
           }
           for (int i = 0; i < planets.length; i++) {
               planets[i].update(dt, xForces[i], yForces[i]);
           }
            StdDraw.setXscale(-radius, radius);
            StdDraw.setYscale(-radius, radius);
            StdDraw.clear();
            StdDraw.picture(0.0, 0.0, "images/starfield.jpg");
            StdDraw.show();
            for (int i = 0; i < planets.length; i++) {
                planets[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            t += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
