package core.nodes;

import core.API;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;

public class ClanWarsNode extends TaskNode {
    @Override
    public boolean accept() {
        return !API.needsSupplies() && canClanWars() && (API.clanWarsArea.contains(getLocalPlayer()) || API.inClanWarsArea.contains(getLocalPlayer()));
    }

    @Override
    public int execute() {
        API.status = "Resetting stats...";
        if (API.clanWarsArea.contains(getLocalPlayer())) {
            GameObject ffaPortal = getGameObjects().closest(o -> o != null && o.getName().equals("Free-for-all portal"));
            if (API.clanWarsPortal.contains(getLocalPlayer())) {
                //GameObject ffaPortal = getGameObjects().closest(o -> o != null && o.getName().equals("Free-for-all portal"));

                if (ffaPortal != null) {
                    if (ffaPortal.interact("Enter")) {
                        sleepUntil(() -> !API.clanWarsArea.contains(getLocalPlayer()), 2200 + API.sleep());
                    }
                }
            } else if (getWalking().shouldWalk(Calculations.random(4, 7))) {
                getWalking().walk(ffaPortal.getTile());
            }
        } else if (API.inClanWarsArea.contains(getLocalPlayer())) {
            GameObject exitPortal = getGameObjects().closest(o -> o != null && o.getName().equals("Portal"));

            if (exitPortal != null) {
                if (exitPortal.interact("Exit")) {
                    sleepUntil(() -> API.getPrayerPoints() == getSkills().getRealLevel(Skill.PRAYER), 2200 + API.sleep());
                }
                //sleep(3000 + API.sleep());
            }
        }
        return API.sleep();
    }

    private boolean canClanWars() {
        return !inventoryContainsSellables() && API.hasSupplies() && (API.getPrayerPoints() < getSkills().getRealLevel(Skill.PRAYER) || getCombat().getHealthPercent() < 100) && API.hasRing();
    }

    private boolean inventoryContainsSellables() {
        for (String item : API.sellables) {
            if (getInventory().contains(item)) {
                return true;
            }
        }
        return false;
    }
}
