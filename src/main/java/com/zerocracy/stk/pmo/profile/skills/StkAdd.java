/**
 * Copyright (c) 2016 Zerocracy
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
package com.zerocracy.stk.pmo.profile.skills;

import com.jcabi.aspects.Tv;
import com.zerocracy.jstk.Project;
import com.zerocracy.jstk.Stakeholder;
import com.zerocracy.pm.Person;
import com.zerocracy.pmo.People;
import com.zerocracy.stk.SoftException;
import java.io.IOException;
import java.util.Collection;

/**
 * Add new skill to the user.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class StkAdd implements Stakeholder {

    /**
     * Project.
     */
    private final Project project;

    /**
     * Tube.
     */
    private final Person person;

    /**
     * Skill to add.
     */
    private final String skill;

    /**
     * Ctor.
     * @param pkt Project
     * @param tbe Tube
     * @param skl Skill
     */
    public StkAdd(final Project pkt, final Person tbe, final String skl) {
        this.project = pkt;
        this.person = tbe;
        this.skill = skl;
    }

    @Override
    public void work() throws IOException {
        new People(this.project).bootstrap();
        final Collection<String> skills = new People(this.project).skills(
            this.person.uid()
        );
        if (skills.size() > Tv.FIVE) {
            throw new SoftException(
                String.format(
                    "You've got too many skills already: `%s` (max is five)",
                    String.join("`, `", skills)
                )
            );
        }
        new People(this.project).skill(this.person.uid(), this.skill);
        this.person.say(
            String.format(
                "New skill \"%s\" added to \"%s\"",
                this.skill,
                this.person.uid()
            )
        );
    }
}