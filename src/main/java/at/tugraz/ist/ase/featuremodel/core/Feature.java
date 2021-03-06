/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.featuremodel.core;

/**
 * Represents a feature of a feature model
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class Feature {

    /**
     * Name of feature
     */
    private String name;

    /**
     * A constructor with a given name of a feature
     *
     * @param name - a name of a feature
     */
    public Feature(String name) {
        this.name = name;
    }

    /**
     * @return name of the feature
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name to the feature
     *
     * @param name - a new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the feature.
     *
     * @return name of the feature
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Checks whether two objects are equals in terms of the same name
     *
     * @param obj - another feature
     * @return true if the same name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Feature.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Feature other = (Feature) obj;
        return this.name.equals(other.name);
    }
}
