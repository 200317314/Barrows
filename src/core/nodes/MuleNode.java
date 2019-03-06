package core.nodes;

import core.API;
import org.dreambot.api.data.GameState;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

public class MuleNode extends TaskNode {
    private Timer debugTimer = null;

    @Override
    public int priority() {
        return 1000;
    }

    @Override
    public boolean accept() {
        return canMule();
    }

    @Override
    public int execute() {
        API.status = "Muling gold...";

        if (debugTimer == null) {
            debugTimer = new Timer(300000);
        } else if (debugTimer != null && debugTimer.finished()) {
            debugTimer = null;
            API.muling = false;
            //API.muleGoldAt = 999999999;
        }

        if (getInventory().count("Coins") >= API.muleGoldAt || getTrade().isOpen()) {
            if (getBank().isOpen()) {
                getBank().close();
            } else {
                Player mule = getPlayers().closest(p -> p != null && p.getName().contains("bonkermain"));

                if (getTrade().isOpen()) {
                    if (containsGold(getTrade().getMyItems())) {
                        getTrade().acceptTrade();
                    } else {
                        getTrade().addItem("Coins", getInventory().count("Coins"));
                        sleepUntil(() -> containsGold(getTrade().getMyItems()), 2400);
                    }
                } else if (mule != null) {
                    getTrade().tradeWithPlayer(mule.getName());
                    sleepUntil(() -> getTrade().isOpen(), 8000);
                }
            }
        } else if (!getTrade().isOpen()) {
            if (getBank().isOpen()) {
                if (getBank().count("Coins") >= API.muleGoldAt) {
                    getBank().withdraw("Coins", (getBank().count("Coins") - API.suppliesGold() - 1));
                    sleepUntil(() -> getInventory().count("Coins") >= API.muleGoldAt, 2200);

                    getBank().close();
                } else {
                    API.muling = false;
                    API.goldMuled += API.coinsAmt;
                }
            } else if (getWalking().shouldWalk(5)) {
                getBank().openClosest();
            }
        }
        return API.sleep();
    }

    private boolean canMule() {
        return getClient().getGameState() == GameState.LOGGED_IN && API.muling && API.clanWarsArea.contains(getLocalPlayer());
    }

    private boolean containsGold(Item[] items) {
        if (items != null) {
            for (Item i : items) {
                if (i.getName().equals("Coins")) {
                    return true;
                }
            }
        }
        return false;
    }
}
