package core;

import org.dreambot.api.Client;
import org.dreambot.api.methods.MethodContext;

public class ClientRender {

    public static void setRender(MethodContext m) {
        if (API.grandExchange.contains(m.getLocalPlayer())) {
            Client.setRenderingDisabled(false);
            MethodContext.sleep(125,250);
        } else {
            if (m.getClient().isLoggedIn()) {
                Client.setRenderingDisabled(false);
                MethodContext.sleep(125,250);
                Client.setRenderingDisabled(true);
            } else {
                Client.setRenderingDisabled(false);
                MethodContext.sleep(5000,7500);
                Client.setRenderingDisabled(true);
            }
        }
    }
}