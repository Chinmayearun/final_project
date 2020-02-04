package archem.entities;

import java.util.Arrays;

public class Molecule implements Cloneable
{
    public int x;
    public int y;
    public String formula;
    public Atom atoms[];
    public Bond bonds[];

    public Molecule(String formula, Atom[] atoms, Bond[] bonds)
    {
        this.formula = formula;
        this.atoms = atoms;
        this.bonds = bonds;

        for (Bond b : bonds)
        {
            b.molecule = this;
        }
    }

    public Molecule()
    {
        super();
    }

    public void init()
    {
        for (Bond b : bonds)
        {
            b.init();
        }
        for (Atom a : atoms)
        {
            a.init();
        }
    }

    @Override
    public Molecule clone()
    {
        try
        {
            Molecule m = (Molecule) super.clone();

            m.atoms = new Atom[atoms.length];
            m.bonds = new Bond[bonds.length];

            for (int i = 0; i < atoms.length; i++) m.atoms[i] = atoms[i].clone();
            for (int i = 0; i < bonds.length; i++)
            {
                m.bonds[i] = bonds[i].clone();
                m.bonds[i].molecule = m;
            }

            System.out.println("InitB : " + Arrays.toString(atoms));
            System.out.println("InitB : " + Arrays.toString(m.atoms));

            m.init();

            System.out.println("InitA : " + Arrays.toString(atoms));
            System.out.println("InitA : " + Arrays.toString(m.atoms));

            return m;
        } catch (CloneNotSupportedException ignore)
        {

        }
        return null;
    }

    @Override
    public String toString()
    {
        return "Molecule{" +
                "formula='" + formula + '\'' +
                ", atoms=" + Arrays.toString(atoms) +
                ", bonds=" + Arrays.toString(bonds) +
                '}';
    }
}
