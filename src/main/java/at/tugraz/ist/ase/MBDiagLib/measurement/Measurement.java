/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiagLib.measurement;

/**
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public abstract class Measurement implements Comparable<Measurement> {

    protected final String name;

    Measurement(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Measurement p) {
        return this.name.compareTo(p.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
