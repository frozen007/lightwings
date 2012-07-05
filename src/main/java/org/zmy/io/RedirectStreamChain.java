package org.zmy.io;

import java.io.OutputStream;

public class RedirectStreamChain {

    protected RedirectStream redirectStream = null;

    public RedirectStreamChain(RedirectStream stream) {
        redirectStream = stream;
    }

    public RedirectStreamChain(OutputStream stdOut, OutputStream redirectOut) {
        redirectStream = new RedirectStream(stdOut, redirectOut);
    }

    public RedirectStream getRedirectStream() {
        return redirectStream;
    }
}
