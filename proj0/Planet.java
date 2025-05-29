import java.io.File;

public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }
    public double calcDistance(Planet p) {
        double dx = xxPos - p.xxPos;
        double dy = yyPos - p.yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }
    public double calcForceExertedBy(Planet p) {
        double r = calcDistance(p);
        return (G * mass * p.mass) / (r * r);
    }
    public double calcForceExertedByX(Planet p) {
        double F = calcForceExertedBy(p);
        double r = calcDistance(p);
        double dx = p.xxPos - xxPos;
        return F * (dx / r);
    }
    public double calcForceExertedByY(Planet p) {
        double F = calcForceExertedBy(p);
        double r = calcDistance(p);
        double dy = p.yyPos - yyPos;
        return F * (dy / r);
    }
    public  double calcNetForceExertedByX(Planet[] allPlanets) {
        double xNetForce = 0.0f;
        for (Planet p : allPlanets) {
            if (p != this) {
                xNetForce += calcForceExertedByX(p);
            }
        }
        return xNetForce;
    }
    public  double calcNetForceExertedByY(Planet[] allPlanets) {
        double yNetForce = 0.0f;
        for (Planet p : allPlanets) {
            if (p != this) {
                yNetForce += calcForceExertedByY(p);
            }
        }
        return yNetForce;
    }
    public void update(double dt, double fx, double fy) {
        double ax = fx / mass;
        double ay = fy / mass;
        this.xxVel += dt * ax;
        this.yyVel += dt * ay;
        this.xxPos += dt * this.xxVel;
        this.yyPos += dt * this.yyVel;
    }
    public void draw() {
        String imgPath = "images" + File.separator + imgFileName;
        StdDraw.picture(xxPos, yyPos, imgPath);
        StdDraw.show();
    }

}
