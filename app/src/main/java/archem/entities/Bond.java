package archem.entities;
import com.google.ar.sceneform.Node;

import java.util.Arrays;

public abstract class Bond implements Cloneable
{
    Molecule molecule;

    Node bondNode;

    public abstract void init();

    public Bond clone()
    {
        try
        {
            return (Bond)super.clone();
        }
        catch(CloneNotSupportedException ignore)
        {

        }
        return null;
    }
}

class ValenceBond extends Bond
{
    public int[] atoms;
    public int x;
    public int y;
    public int shared_electrons;

    public ValenceBond(int[] atoms, int shared_electrons, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.shared_electrons = shared_electrons;
        this.atoms = atoms;
    }

    public void init()
    {
        for (int i : atoms)
        {
            Atom a = molecule.atoms[i];
            //a.electrons.subList(a.electrons.size() - shared_electrons, a.electrons.size()).clear();
            a.configuration[a.configuration.length-1] -= shared_electrons;
        }
    }

    public ValenceBond clone()
    {
        ValenceBond vb=(ValenceBond)super.clone();
        vb.atoms=new int[atoms.length];
        System.arraycopy(atoms,0,vb.atoms,0,atoms.length);
        return vb;
    }
}

class IonicBond extends Bond
{
    public int[] atoms;
    public int[] electron_transfer;

    public IonicBond(int[] atoms, int[] electron_transfer)
    {
        this.electron_transfer = electron_transfer;
        this.atoms = atoms;
    }

    public void init()
    {
        for (int i = 0; i < atoms.length; i++)
        {
            Atom a = molecule.atoms[atoms[i]];
            int[] c = a.configuration;
            c[c.length - 1] += electron_transfer[i];
            if (c[c.length - 1] == 0)
            {
                int[] t = a.configuration;
                a.configuration = new int[a.configuration.length - 1];
                System.arraycopy(t, 0, a.configuration, 0, a.configuration.length);
            }
        }
    }

    @Override
    public String toString()
    {
        return  "IonicBond{"+
                "atoms=" + Arrays.toString(atoms) +
                ", electron_transfer=" + Arrays.toString(electron_transfer) +
                '}';
    }

    public IonicBond clone()
    {
        IonicBond ib=(IonicBond)super.clone();
        ib.atoms=new int[atoms.length];
        ib.electron_transfer=new int[electron_transfer.length];

        System.arraycopy(atoms,0,ib.atoms,0,atoms.length);
        System.arraycopy(electron_transfer,0,ib.electron_transfer,0,electron_transfer.length);
        return ib;
    }
}

