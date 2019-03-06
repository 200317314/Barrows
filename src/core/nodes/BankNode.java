package core.nodes;

import core.API;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;

import java.util.Objects;

public class BankNode extends TaskNode {
    @Override
    public boolean accept() {
        //System.out.println("canBank: " + canBank());
        return canBank() && getClient().getGameState() == GameState.LOGGED_IN;
    }

    @Override
    public int execute() {
        if (getDepositBox().isOpen()) {
            getDepositBox().close();
        }

        if (getBank().isOpen()) {
            API.status = "Banking items...";
            if (!getInventory().isEmpty()) {
                API.countSupplies();
                /*getBank().depositAllItems();*/
                if (inventoryContainsSellables()) {
                    for (String item : API.sellables) {
                        if (getInventory().contains(item)) {
                            if (getBank().depositAll(item)) {
                                sleepUntil(() -> !getInventory().contains(item), 2300 + API.sleep());
                            }
                        }
                    }
                }

                if (getInventory().contains("Coins")) {
                    if (getBank().depositAll("Coins")) {
                        sleepUntil(() -> !getInventory().contains("Coins"), 2300 + API.sleep());
                    }
                }

                if (getInventory().contains("Vial")) {
                    if (getBank().depositAll("Vial")) {
                        sleepUntil(() -> !getInventory().contains("Vial"), 2300 + API.sleep());
                    }
                }
            }

            API.countSupplies();

            if (!API.hasFood() || API.hasFood() && getInventory().count("Monkfish") < 18) {
                if (!API.hasFood()) {
                    if (getBank().contains("Monkfish")) {
                        getBank().withdraw("Monkfish", 18);
                        sleepUntil(() -> API.hasFood(), 2200 + API.sleep());
                    }
                } else if (API.hasFood() && getInventory().count("Monkfish") < 18) {
                    if (getBank().contains("Monkfish")) {
                        getBank().withdraw("Monkfish", (18) - getInventory().count("Monkfish"));
                        sleepUntil(() -> getInventory().count("Monkfish") == 18, 2200 + API.sleep());
                    }
                }
            }

            if (!API.hasSpade()) {
                if (getBank().contains("Spade")) {
                    getBank().withdraw("Spade", 1);
                    sleepUntil(() -> API.hasSpade(), 2200 + API.sleep());
                } else {
                    API.spades = 0;
                }
            }

            if (!API.hasTeleports()) {
                if (getBank().contains("Barrows teleport")) {
                    getBank().withdraw("Barrows teleport", Calculations.random(20, 25));
                    sleepUntil(() -> API.hasTeleports(), 2200 + API.sleep());
                }
            }

            if (!API.hasPotions() || API.hasPotions() && getInventory().count("Super restore(4)") < 3) {
                if (!API.hasPotions()) {
                    if (getBank().contains(i -> i != null && i.getName().contains("Super restore"))) {
                        getBank().withdraw((i -> i != null && i.getName().contains("Super restore")), 3);
                        sleepUntil(() -> API.hasPotions(), 3200 + API.sleep());
                    }
                } else if (API.hasPotions() && getInventory().count("Super restore(4)") < 3) {
                    if (getBank().contains(i -> i != null && i.getName().contains("Super restore(4"))) {
                        getBank().withdraw((i -> i != null && i.getName().contains("Super restore(4")), (3) - getInventory().count("Super restore(4)"));
                        sleepUntil(() -> getInventory().count("Super restore(4)") == 3, 3200 + API.sleep());
                    }
                }
            }

            if (!API.hasHelmet()) {
                if (getBank().contains(API.helmet)) {
                    getBank().withdraw(API.helmet, 1);
                    sleepUntil(() -> API.hasHelmet(), 3200 + API.sleep());
                }
            }

            if (!API.hasAmulet()) {
                if (getBank().contains(API.amulet)) {
                    getBank().withdraw(API.amulet, 1);
                    sleepUntil(() -> API.hasAmulet(), 3200 + API.sleep());
                }
            }

            if (!API.hasChest()) {
                if (getBank().contains(API.chest)) {
                    getBank().withdraw(API.chest, 1);
                    sleepUntil(() -> API.hasChest(), 3200 + API.sleep());
                }
            }

            if (!API.hasStaff()) {
                if (getBank().contains(API.staff)) {
                    getBank().withdraw(API.staff, 1);
                    sleepUntil(() -> API.hasStaff(), 3200 + API.sleep());
                }
            }

            if (!API.hasAmmo()) {
                if (getBank().contains(API.ammo)) {
                    getBank().withdrawAll(API.ammo);
                    sleepUntil(() -> API.hasAmmo(), 3200 + API.sleep());
                }
            }

            if (!API.hasLegs()) {
                if (getBank().contains(API.legs)) {
                    getBank().withdraw(API.legs, 1);
                    sleepUntil(() -> API.hasLegs(), 3200 + API.sleep());
                }
            }

            if (!API.hasRing()) {
                if (getBank().contains("Ring of dueling(8)")) {
                    getBank().withdraw("Ring of dueling(8)", 1);
                    sleepUntil(() -> API.hasRing(), 3200 + API.sleep());
                }
            }

            if (getBank().contains("Harralander tar")) {
                getBank().withdrawAll("Harralander tar");
                sleepUntil(() -> getInventory().contains("Harralander tar"), 3200 + API.sleep());
            }

            getBank().close();
        } else {
            API.status = "Opening bank...";
            if (!API.barrowsCrypt.contains(getLocalPlayer()) && !API.barrowsArea.contains(getLocalPlayer())) {
                if (getWalking().shouldWalk(Calculations.random(4, 7))) {
                    getBank().openClosest();
                }
            } else {
                if (getTabs().isOpen(Tab.EQUIPMENT)) {
                    getEquipment().get(item -> item != null && item.getName().contains("Ring of dueling(")).interact("Clan Wars");
                    sleepUntil(() -> !API.barrowsCrypt.contains(getLocalPlayer()) && !API.barrowsArea.contains(getLocalPlayer()), 3400);
                } else {
                    getTabs().open(Tab.EQUIPMENT);
                }
            }
        }
        return API.sleep();
    }

    private boolean canBank() {
        return !API.hasFood() || !API.hasSpade() || (!API.hasTeleports() && !API.barrowsArea.contains(getLocalPlayer()) && !API.barrowsCrypt.contains(getLocalPlayer()) && !API.inTomb()) || !API.hasPotions() || !API.hasHelmet()
                || !API.hasAmulet() || !API.hasChest() || !API.hasStaff() || !API.hasLegs()
                || !API.hasRing() || !API.hasAmmo() || (API.clanWarsArea.contains(getLocalPlayer()) && inventoryContainsSellables());
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
