package core;

import org.dreambot.api.script.AbstractScript;

/**
 * Created by 7804364 on 4/15/2018.
 */
public class Utils {
    private static AbstractScript si;

    public Utils(AbstractScript si) {
        this.si = si;
    }

    public static AbstractScript getScript() {
        return si;
    }
}
