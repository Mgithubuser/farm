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
package com.zerocracy.tk.profile;

import com.jcabi.matchers.XhtmlMatchers;
import com.zerocracy.Farm;
import com.zerocracy.cash.Cash;
import com.zerocracy.farm.fake.FkFarm;
import com.zerocracy.farm.fake.FkProject;
import com.zerocracy.farm.props.PropsFarm;
import com.zerocracy.pmo.Agenda;
import com.zerocracy.pmo.Awards;
import com.zerocracy.pmo.People;
import com.zerocracy.tk.RqWithUser;
import com.zerocracy.tk.TkApp;
import java.io.IOException;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkProfile}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.13
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class TkProfileTest {

    @Test
    public void rendersHomePage() throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        final String uid = "yegor";
        new Awards(farm, uid).bootstrap().add(
            new FkProject(), 1, "gh:test/test#1", "reason"
        );
        new Agenda(farm, uid).bootstrap().add(
            new FkProject(), "gh:test/test#2", "QA"
        );
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(
                new RsPrint(
                    new TkApp(farm).act(
                        new RqWithUser(
                            farm,
                            new RqFake("GET", "/u/Yegor256")
                        )
                    )
                ).printBody()
            ),
            XhtmlMatchers.hasXPaths("//xhtml:body")
        );
    }

    @Test
    public void rendersProfilePageWithRateInFirefox() throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        final double rate = 99.99;
        final People people = new People(farm).bootstrap();
        people.wallet("yegor256", "paypal", "test@example.com");
        people.rate(
            "yegor256", new Cash.S(String.format("USD %f", rate))
        );
        MatcherAssert.assertThat(
            this.firefoxView(farm, "yegor256"),
            Matchers.containsString(
                String.format(
                    "rate</a> is <span style=\"color:darkgreen\">$%.2f</span>",
                    rate
                )
            )
        );
    }

    @Test
    public void rendersProfilePageWithoutRateInFirefox() throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        final People people = new People(farm).bootstrap();
        people.wallet(
            "yegor256", "btc", "3HcEB6bi4TFPdvk31Pwz77DwAzfAZz2fMn"
        );
        MatcherAssert.assertThat(
            this.firefoxView(farm, "yegor256"),
            Matchers.containsString("rate</a> is not defined")
        );
    }

    // @todo #1080:30min Refactor this method and similar functionality in
    //  TkAwardsTest into a reusable class in which we could control what kind
    //  of view we want to generate.
    private String firefoxView(final Farm farm, final String uid)
        throws IOException {
        return new RsPrint(
            new TkApp(farm).act(
                new RqWithUser(
                    farm,
                    new RqFake(
                        new ListOf<>(
                            String.format("GET /u/%s", uid),
                            "Host: www.example.com",
                            "Accept: application/xml",
                            // @checkstyle LineLength (1 line)
                            "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:62.0) Gecko/20100101 Firefox/62.0"
                        ),
                        ""
                    )
                )
            )
        ).printBody();
    }
}
