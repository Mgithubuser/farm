/**
 * Copyright (c) 2016-2018 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.pm.staff.votes;

import com.zerocracy.pm.staff.Votes;
import com.zerocracy.pmo.People;
import com.zerocracy.pmo.Pmo;
import java.io.IOException;

/**
 * Vacation voter.
 * @author Kirill (g4s8.public@gmail.com)
 * @version $Id$
 * @since 0.16
 */
public final class VsVacation implements Votes {

    /**
     * The people.
     */
    private final People people;

    /**
     * Ctor.
     * @param pmo The PMO
     */
    public VsVacation(final Pmo pmo) {
        this(new People(pmo));
    }

    /**
     * Primary ctor.
     * @param ppl People
     */
    private VsVacation(final People ppl) {
        this.people = ppl;
    }

    @Override
    public double take(
        final String login,
        final StringBuilder log
    ) throws IOException {
        final double vote;
        if (this.people.bootstrap().vacation(login)) {
            log.append("The user is on vacation");
            vote = 1.0D;
        } else {
            log.append("The user is not on vacation");
            vote = 0.0D;
        }
        return vote;
    }
}
