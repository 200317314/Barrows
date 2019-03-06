package core.nodes;

import core.API;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;

public class TeleportNode extends TaskNode {
    @Override
    public int priority() {
        return 99999;
    }

    @Override
    public boolean accept() {
        return !API.needsSupplies() && canTeleportBarrows() && hasRing() || !API.needsSupplies() && canLeaveCrypt() && hasRing() || !API.needsSupplies() && emergencyTeleport() && hasRing() || !API.needsSupplies() && debugTeleport() && hasRing()
                || (emergencyTeleport() && hasRing());
    }

    @Override
    public int execute() {
        API.status = "Teleporting...";
        if (canTeleportBarrows()) {
            if (getBank().isOpen()) {
                getBank().close();
            } else if (!API.barrowsArea.contains(getLocalPlayer()) && !API.inClanWarsArea.contains(getLocalPlayer()) && !API.barrowsCrypt.contains(getLocalPlayer()) && !API.inTomb() && getClient().getGameState() == GameState.LOGGED_IN) {
                if (getTabs().isOpen(Tab.INVENTORY)) {
                    getInventory().get("Barrows teleport").interact("Break");
                    sleepUntil(() -> API.barrowsArea.contains(getLocalPlayer()), 2200 + API.sleep());
                    sleep(1000 + API.sleep());
                } else {
                    getTabs().open(Tab.INVENTORY);
                }
            }
        }

        if (canLeaveCrypt() || emergencyTeleport() || debugTeleport()) {
            if (getTabs().isOpen(Tab.EQUIPMENT)) {
                getEquipment().get(item -> item != null && item.getName().contains("Ring of dueling(")).interact("Clan Wars");
                sleepUntil(() -> !API.barrowsCrypt.contains(getLocalPlayer()) && !API.barrowsArea.contains(getLocalPlayer()) && API.clanWarsArea.contains(getLocalPlayer()), 3400);
            } else {
                getTabs().open(Tab.EQUIPMENT);
            }
        }
        return API.sleep();
    }

    private boolean canLeaveCrypt() {
        return API.getRewardsPotential() == 0 && API.barrowsCrypt.contains(getLocalPlayer()) && getClient().getGameState() == GameState.LOGGED_IN && !canGE();
    }

    private boolean canTeleportBarrows() {
        return API.hasSupplies() && API.getPrayerPoints() == getSkills().getRealLevel(Skill.PRAYER) && getCombat().getHealthPercent() == 100 && getInventory().contains("Barrows teleport") && getClient().getGameState() == GameState.LOGGED_IN && !canGE() &&
                !API.barrowsArea.contains(getLocalPlayer()) && !API.inClanWarsArea.contains(getLocalPlayer()) && !API.barrowsCrypt.contains(getLocalPlayer()) && !API.inTomb();
    }

    private boolean emergencyTeleport() {
        return !API.clanWarsArea.contains(getLocalPlayer()) && (!API.hasFood() || !API.hasPotions() || getCombat().getHealthPercent() <= 25) && getClient().getGameState() == GameState.LOGGED_IN && !canGE();
    }

    private boolean debugTeleport() {
        return !API.clanWarsArea.contains(getLocalPlayer()) && !API.barrowsCrypt.contains(getLocalPlayer()) && !API.barrowsArea.contains(getLocalPlayer()) && !API.inTomb() && !API.inClanWarsArea.contains(getLocalPlayer()) && getClient().getGameState() == GameState.LOGGED_IN && !canGE();
    }

    private boolean hasRing() {
        return getEquipment().contains(i -> i != null && i.getName().contains("Ring of dueling(")) && getClient().getGameState() == GameState.LOGGED_IN && !canGE();
    }

    private boolean canGE() {
        return getGrandExchange().isOpen() || needPotions() || needFish() || needBarrowsTele() || needRings() || needAmmo() || needVarrockTele() || needSalamander() || needHelmet() || needSpades() || needBond();
    }

    private boolean needPotions() {
        return API.potions != -1 && needItems(API.potions, API.maxPotions);
    }

    private boolean needFish() {
        return API.fish != -1 && needItems(API.fish, API.maxFish);
    }

    private boolean needBarrowsTele() {
        return API.barrowsTele != -1 && needItems(API.barrowsTele, API.maxBarrowsTele);
    }

    private boolean needRings() {
        return API.rings != -1 && needItems(API.rings, API.maxRings);
    }

    private boolean needAmmo() {
        return API.ammoMin != -1 && needItems(API.ammoMin, API.maxAmmo);
    }

    private boolean needSpades() {
        return API.spades != -1 && API.spades == 0;
    }

    private boolean needVarrockTele() {
        return API.varrockTele != -1 && needItems(API.varrockTele, API.maxVarrockTele);
    }

    private boolean needSalamander() {
        return !getInventory().contains(API.staff) && !getEquipment().contains(API.staff);
    }

    private boolean needHelmet() {
        return !getInventory().contains(API.helmet) && !getEquipment().contains(API.helmet);
    }

    private boolean needBond() {
        return API.BOND_COUNT != -1 && API.BOND_COUNT == 0 && !getCombat().isInWild() && getClient().getMembershipLeft() != -1 && getClient().getMembershipLeft() <= 2 && API.goldForBond();
    }

    private boolean needItems(int curr, int max) {
        if (curr <= max/4) {
            return true;
        } else if (curr <= max/2 && API.grandExchange.contains(getLocalPlayer())) {
            if (API.ammoMin == 11000) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
