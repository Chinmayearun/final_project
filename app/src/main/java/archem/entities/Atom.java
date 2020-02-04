package archem.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Atom implements Cloneable
{
    public class Electron
    {
        public double x;
        public double y;

        public Electron(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString()
        {
            return "Electron[" +
                    "x=" + x +
                    ", y=" + y +
                    ']';
        }
    }

    static final int[] MAX_ELECTRON = {2, 8, 18, 32, 50, 72, 98, 128};
    static final double[] angles = {0, 180, 90, 270, 45, 225, 135, 315};
    static final double[] rangles;

    static
    {
        rangles = new double[angles.length];
        for (int i = 0; i < angles.length; i++)
        {
            rangles[i] = Math.toRadians(angles[i]);
        }
    }

    static final double factor = 1;

    public String name;
    public String symbol;
    public int N;  //Atomic numner
    public float A; //Atomic weight
    public int protons;
    public int neutrons;
    public int x;
    public int y;

    public int[] configuration;

    public List<Electron> electrons = new ArrayList<>();
    public List<Integer> orbitRadius = new ArrayList<>();

    public Atom(String name, String symbol, int n, float a,int protons,int neutrons, int... configuration)
    {
        this.name = name;
        this.symbol=symbol;
        N = n;
        A = a;
        this.protons=protons;
        this.neutrons=neutrons;
        this.configuration = configuration;
    }

    public void init()
    {
        for (int orbit = 0; orbit < configuration.length; orbit++)
        {
            int nelectrons = configuration[orbit];
            int radius = (int) (factor * (orbit + 1) * 10);
            orbitRadius.add(radius);

            for (int i = 0; i < nelectrons; i++)
            {
                double ra = rangles[i];
                double x = radius * Math.cos(ra);
                double y = radius * Math.sin(ra);

                Electron e = new Electron(x, y);
                electrons.add(e);
            }
        }
//        electrons = Collections.unmodifiableList(electrons);
//        orbitRadius = Collections.unmodifiableList(orbitRadius);
    }

    public Atom clone()
    {
        try
        {
            Atom a = (Atom) super.clone();
            a.configuration = new int[configuration.length];
            System.arraycopy(configuration, 0, a.configuration, 0, configuration.length);
            a.orbitRadius = new ArrayList<>(orbitRadius);
            a.electrons = new ArrayList<>(electrons);
            return a;
        }
        catch(CloneNotSupportedException ignore){}
        return null;
    }

    @Override
    public String toString()
    {
        return "Atom{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", N=" + N +
                ", A=" + A +
                ", x=" + x +
                ", y=" + y +
                ", configuration=" + Arrays.toString(configuration) +
                ", electrons=" + electrons +
                ", orbitRadius=" + orbitRadius +
                '}';
    }
}
