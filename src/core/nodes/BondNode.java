package core.nodes;

import core.API;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;

import java.util.Objects;

public class BondNode extends TaskNode {
    private String bondName = "Old school bond";
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public boolean accept() {
        return canBond() && getInventory().contains(i -> Objects.nonNull(i)
                && i.getName().contains(bondName));
    }

    @Override
    public int execute() {
        API.status = "Activating bond...";
        if (getInventory().contains(bondName)) {
            if (getTabs().isOpen(Tab.INVENTORY)) {
                //START Nezz
                if (getGrandExchange().isOpen()) {
                    getGrandExchange().close();
                    sleepUntil(() -> !getGrandExchange().isOpen(), 2500);
                } else {
                    if (getWidgets().getWidgetChild(66, 4) != null) {
                        getWidgets().getWidgetChild(66, 4).interact("Select");
                        sleep(1250,1500);
                    }
                    if (getWidgets().getWidgetChild(66, 92) != null) {
                        getWidgets().getWidgetChild(66, 92).interact("Confirm");
                        sleep(1250,1750);
                        getTabs().logout();
                        sleep(125,150);
                        int member = getClient().getMembershipLeft();
                        sleep(15000,17500);
                    } else {
                        if (getInventory().contains("Old school bond (untradeable)")) {
                            getInventory().interact("Old school bond (untradeable)","Redeem");
                        }
                        sleep(1250,1500);
                    }
                }
                //END
            } else {
                getTabs().open(Tab.INVENTORY);
            }
        } else {
            if (getBank().isOpen()) {
                if (!getInventory().isEmpty()) {
                    if (getBank().depositAllItems()) {
                        sleepUntil(() -> getInventory().isEmpty(), 1200 + API.sleep());
                    }

                    if (getBank().contains(bondName)) {
                        if (getBank().withdraw(bondName, 1)) {
                            sleepUntil(() -> getInventory().contains(bondName), 2300 + API.sleep());
                        }
                    }

                    getBank().close();
                }
            } else if (getWalking().shouldWalk(Calculations.random(4, 7))) {
                getBank().openClosest();
            }
        }
        return API.sleep();
    }

    private boolean canBond() {
        return API.BOND_COUNT > 0 && !getCombat().isInWild() && getClient().getMembershipLeft() != -1 && getClient().getMembershipLeft() <= 2;
    }
}
