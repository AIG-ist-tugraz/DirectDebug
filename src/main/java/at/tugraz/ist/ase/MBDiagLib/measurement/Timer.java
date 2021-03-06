/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiagLib.measurement;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Simple class for measuring time in experiments.
 * The timings of time measurements are stored in an array and can be
 * retrieved using getTimings method.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class Timer extends Measurement {
    private LinkedList<Long> timings = new LinkedList<>();
    private long time = 0;
    private Boolean running = null;

    Timer(String name) {
        super(name);
    }

    /**
     * Starts the timer.
     */
    public void start() {
        if (this.running != null && this.running) throw new IllegalStateException("The timer " + this.name + " is " +
                "already running!");
        this.running = true;
        this.time = System.nanoTime();
    }

    /**
     * Stops the timer.
     *
     * @return return the time elapsed since the start in nanoseconds.
     */
    public long stop() {
        this.time = getElapsedTime();
        this.running = false;
        this.timings.add(this.time);
        return this.time;
    }

    /**
     * @return the time elapsed since the timer is started.
     */
    public long getElapsedTime() {
        if (this.running == null || !this.running) throw new IllegalStateException("The timer " + this.name + " is " +
                "not running!");
        return System.nanoTime() - this.time;
    }

    /**
     * @return timings of the time measurement
     */
    public List<Long> getTimings() {
        return unmodifiableList(this.timings);
    }

    /**
     * @return the total time that the timer was running
     */
    public long total() {
        long total = 0;
        for (long t : this.timings)
            total += t;
        return total;
    }

    @Override
    public String toString() {
        return Double.toString((double)total()/1000000000.0); // convert to seconds
    }
}
