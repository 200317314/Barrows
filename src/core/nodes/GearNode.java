package core.nodes;

import core.API;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;

public class GearNode extends TaskNode {
    @Override
    public boolean accept() {
        //System.out.println("canGear: " + canGear());
        return canGear() && !getBank().isOpen() && !getGrandExchange().isOpen();
    }

    @Override
    public int execute() {
        API.status = "Equipting gear...";
        if (getTabs().isOpen(Tab.INVENTORY)) {
            if (canStaff()) {
                if (getInventory().get(API.staff).interact("Wield")) {
                    sleepUntil(() -> !canStaff(), 2400 + API.sleep());
                }
            }

            if (canAmmo()) {
                if (getInventory().get(API.ammo).interact("Wield")) {
                    sleepUntil(() -> !canAmmo(), 2400 + API.sleep());
                }
            }

            if (canHelmet()) {
                if (getInventory().get(API.helmet).interact("Wear")) {
                    sleepUntil(() -> !canHelmet(), 2400 + API.sleep());
                }
            }

            if (canBody()) {
                if (getInventory().get(API.chest).interact("Wear")) {
                    sleepUntil(() -> !canBody(), 2400 + API.sleep());
                }
            }

            if (canLegs()) {
                if (getInventory().get(API.legs).interact("Wear")) {
                    sleepUntil(() -> !canLegs(), 2400 + API.sleep());
                }
            }

            if (canAmulet()) {
                if (getInventory().get(API.amulet).interact("Wear")) {
                    sleepUntil(() -> !canAmulet(), 2400 + API.sleep());
                }
            }

            if (canRing()) {
                if (getInventory().get(i -> i != null && i.getName().contains("Ring of dueling(")).interact("Wear")) {
                    sleepUntil(() -> !canRing(), 2400 + API.sleep());
                }
            }
        } else {
            getTabs().open(Tab.INVENTORY);
        }
        return API.sleep();
    }

    private boolean canGear() {
        return canStaff() || canHelmet() || canBody() || canLegs() || canAmulet() || canRing() || canAmmo();
    }

    private boolean contains(String name) {
        return !getEquipment().contains(i -> i != null && i.getName().contains(name)) && getInventory().contains(i -> i != null && i.getName().contains(name) && !i.isNoted());
    }

    private boolean canStaff() {
        return contains(API.staff);
    }

    private boolean canHelmet() {
        return contains(API.helmet);
    }

    private boolean canBody() {
        return contains(API.chest);
    }

    private boolean canLegs() {
        return contains(API.legs);
    }

    private boolean canAmulet() {
        return contains(API.amulet);
    }

    private boolean canAmmo() {
        return contains(API.ammo) || getInventory().contains(API.ammo);
    }

    private boolean canRing() {
        return contains("Ring of dueling(");
    }
}

