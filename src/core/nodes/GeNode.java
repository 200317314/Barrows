package core.nodes;

import core.API;
import core.ge.GrandExchancePrice;
import core.ge.PriceLookUp;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;

import java.util.Arrays;

public class GeNode extends TaskNode {
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public boolean accept() {
        return canGE();
    }

    @Override
    public int execute() {
        API.status = "Buying supplies...";
        if (API.clanWarsArea.contains(getLocalPlayer())) {
            //Tele to varrock
            if (getInventory().contains("Varrock teleport") && getInventory().contains("Coins") && !getBank().isOpen()) {
                if (getTabs().isOpen(Tab.INVENTORY)) {
                    getInventory().get("Varrock teleport").interact("Break");
                    sleepUntil(() -> !API.clanWarsArea.contains(getLocalPlayer()), 2200 + API.sleep());
                } else {
                    getTabs().open(Tab.INVENTORY);
                }
            } else {
                if (getBank().isOpen()) {
                    if (!getInventory().isEmpty()) {
                        API.countSupplies();
                        getBank().depositAllItems();
                        sleepUntil(() -> getInventory().isEmpty(), 1200 + API.sleep());
                    }

                    API.countSupplies();

                    if (getBank().withdraw("Varrock teleport", 1)) {
                        sleepUntil(() -> getInventory().contains("Varrock teleport"), 2200 + API.sleep());
                    }

                    if (getBank().withdrawAll("Coins")) {
                        sleepUntil(() -> getInventory().contains("Coins"), 2200 + API.sleep());
                    }

                    if (getBank().getWithdrawMode() != BankMode.NOTE && bankContainsSellables()) {
                        getBank().setWithdrawMode(BankMode.NOTE);
                        sleep(API.sleep());
                    }

                    if (bankContainsSellables()) {
                        for (String item : API.sellables) {
                            if (getBank().contains(item)) {
                                if (getBank().withdrawAll(item)) {
                                    sleepUntil(() -> !getBank().contains(item), 2200 + API.sleep());
                                }
                            }
                        }
                    }

                    if (getBank().isOpen()) {
                        getBank().close();
                        sleepUntil(() -> !getBank().isOpen(), 2000);
                    }
                } else if (getWalking().shouldWalk(Calculations.random(4, 7))) {
                    getBank().openClosest();
                }
            }
        } else {
            //Walk to GE
            if (API.grandExchange.contains(getLocalPlayer())) {
                if (getGrandExchange().isOpen()) {
                    if (getGrandExchange().isReadyToCollect()) {
                        getGrandExchange().collect();
                        sleepUntil(() -> !getGrandExchange().isReadyToCollect(), 2400 + API.sleep());
                    }

                    if (inventoryContainsSellables()) {
                        for (String item : API.sellables) {
                            if (slotsTakenCount() >= API.geBuySellCount && getGrandExchange().isReadyToCollect()) {
                                getGrandExchange().collect();
                                sleepUntil(() -> !getGrandExchange().isReadyToCollect(), 2400 + API.sleep());
                                API.resetGeBuySell();
                            }

                            if (getInventory().contains(item)) {
                                API.status = "S: " + item + "...";
                                //int quantity = getInventory().get(item).getAmount();
                                //MethodContext.log("Sell: " + item + " x" + quantity);

                                if (getGrandExchange().sellItem(item, getInventory().count(item), 1)) {
                                    sleepUntil(() -> !getInventory().contains(item), 8000 + API.sleep());
                                }

                                sleep(150,450);
                            }
                        }
                    } else if (getInventory().contains("Coins")) {
                        if (getGrandExchange().isReadyToCollect()) {
                            getGrandExchange().collect();
                            sleepUntil(() -> !getGrandExchange().isReadyToCollect(), 2400 + API.sleep());
                        }

                        API.potionsPrice = setPriceModifier(GrandExchancePrice.getPrice(3024));
                        while (API.potionsPrice == -1) {
                            API.potionsPrice = setPriceModifier(GrandExchancePrice.getPrice(3024));
                            sleepUntil(() -> API.potionsPrice != -1, 2400 + API.sleep());
                        }

                        API.fishPrice = setPriceModifier(GrandExchancePrice.getPrice(7946));
                        while (API.fishPrice == -1) {
                            API.fishPrice = setPriceModifier(GrandExchancePrice.getPrice(7946));
                            sleepUntil(() -> API.fishPrice != -1, 2400 + API.sleep());
                        }

                        API.barrowsTelePrice = setPriceModifier(GrandExchancePrice.getPrice(19629));
                        while (API.barrowsTelePrice == -1) {
                            API.barrowsTelePrice = setPriceModifier(GrandExchancePrice.getPrice(19629));
                            sleepUntil(() -> API.barrowsTelePrice != -1, 2400 + API.sleep());
                        }

                        API.ringPrice = setPriceModifier(GrandExchancePrice.getPrice(2552));
                        while (API.ringPrice == -1) {
                            API.ringPrice = setPriceModifier(GrandExchancePrice.getPrice(2552));
                            sleepUntil(() -> API.ringPrice != -1, 2400 + API.sleep());
                        }

                        API.ammoPrice = setPriceModifier(GrandExchancePrice.getPrice(10145));
                        while (API.ammoPrice == -1) {
                            API.ammoPrice = setPriceModifier(GrandExchancePrice.getPrice(10145));
                            sleepUntil(() -> API.ammoPrice != -1, 2400 + API.sleep());
                        }

                        API.varrockTelePrice = setPriceModifier(GrandExchancePrice.getPrice(8007));
                        while (API.varrockTelePrice == -1) {
                            API.varrockTelePrice = setPriceModifier(GrandExchancePrice.getPrice(8007));
                            sleepUntil(() -> API.varrockTelePrice != -1, 2400 + API.sleep());
                        }

                        API.salamanderPrice = setPriceModifier(GrandExchancePrice.getPrice(10148));
                        while (API.salamanderPrice == -1) {
                            API.salamanderPrice = setPriceModifier(GrandExchancePrice.getPrice(10148));
                            sleepUntil(() -> API.salamanderPrice != -1, 2400 + API.sleep());
                        }

                        API.runeFullHelmPrice = setPriceModifier(GrandExchancePrice.getPrice(10456));
                        while (API.runeFullHelmPrice == -1) {
                            API.runeFullHelmPrice = setPriceModifier(GrandExchancePrice.getPrice(10456));
                            sleepUntil(() -> API.runeFullHelmPrice != -1, 2400 + API.sleep());
                        }

                        if (needPotions()) {
                            if (!getInventory().contains("Super restore(4)")) {
                                if (getGrandExchange().buyItem("Super restore(4)", (API.maxPotions - API.potions), API.potionsPrice)) {
                                    sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                                }
                                collect();
                                sleepUntil(() -> getInventory().contains("Super restore(4)"), 2200 + API.sleep());
                                API.potions += getInventory().count("Super restore(4)");
                                collect();

                                API.supplyCostCount += (API.maxPotions - API.potions) * API.potionsPrice;
                            } else {
                                API.potions += getInventory().count("Super restore(4)");
                            }
                        }

                        if (needFish()) {
                            if (!getInventory().contains("Monkfish")) {
                                getGrandExchange().buyItem("Monkfish", (API.maxFish - API.fish), API.fishPrice);
                                sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                                collect();
                                sleepUntil(() -> getInventory().contains("Monkfish"), 2200 + API.sleep());
                                API.fish += getInventory().count("Monkfish");
                                collect();

                                API.supplyCostCount += (API.maxFish - API.fish) * API.fishPrice;
                            } else {
                                API.fish += getInventory().count("Monkfish");
                            }
                        }

                        if (needBarrowsTele()) {
                            if (!getInventory().contains("Barrows teleport")) {
                                getGrandExchange().buyItem("Barrows teleport", (API.maxBarrowsTele - API.barrowsTele), API.barrowsTelePrice);
                                sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                                collect();
                                sleepUntil(() -> getInventory().contains("Barrows teleport"), 2200 + API.sleep());
                                API.barrowsTele += getInventory().count("Barrows teleport");
                                collect();

                                API.supplyCostCount += (API.maxBarrowsTele - API.barrowsTele) * API.barrowsTelePrice;
                            } else {
                                API.barrowsTele += getInventory().count("Barrows teleport");
                            }
                        }

                        if (needRings()) {
                            if (!getInventory().contains("Ring of dueling(8)")) {
                                getGrandExchange().buyItem("Ring of dueling(8)", (API.maxRings - API.rings), API.ringPrice);
                                sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                                collect();
                                sleepUntil(() -> getInventory().contains("Ring of dueling(8)"), 2200 + API.sleep());
                                API.rings += getInventory().count("Ring of dueling(8)");
                                collect();

                                API.supplyCostCount += (API.maxRings - API.rings) * API.ringPrice;
                            } else {
                                API.rings += getInventory().count("Ring of dueling(8)");
                            }
                        }

                        if (needAmmo()) {
                            if (!getInventory().contains(API.ammo)) {
                                if (!containsItemAlready(API.ammo)) {
                                    getGrandExchange().buyItem(API.ammo, 11000, API.ammoPrice);
                                    sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                                    collect();
                                    sleepUntil(() -> getInventory().contains(API.ammo), 2200 + API.sleep());
                                    API.ammoMin += getInventory().count(API.ammo);

                                    API.supplyCostCount += 11000 * API.ammoPrice;
                                } else if (containsItemAlready(API.ammo) && !getGrandExchange().isReadyToCollect()) {
                                    API.deathSleep = true;
                                }
                            } else {
                                API.ammoMin += getInventory().count(API.ammo);
                            }
                        }

                        if (needVarrockTele()) {
                            if (!getInventory().contains("Varrock teleport")) {
                                getGrandExchange().buyItem("Varrock teleport", (API.maxVarrockTele - API.varrockTele), API.varrockTelePrice);
                                sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                                collect();
                                sleepUntil(() -> getInventory().contains("Varrock teleport"), 2200 + API.sleep());
                                API.varrockTele += getInventory().count("Varrock teleport");

                                API.supplyCostCount += (API.maxVarrockTele - API.varrockTele) * API.varrockTelePrice;
                            } else {
                                API.varrockTele += getInventory().count("Varrock teleport");
                            }
                        }

                        if (needSpades()) {
                            if (!getInventory().contains("Spade")) {
                                getGrandExchange().buyItem("Spade", 5, 2500);
                                sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                                collect();
                                sleepUntil(() -> getInventory().contains("Spade"), 2200 + API.sleep());
                                API.spades += getInventory().count("Spade");
                            } else {
                                API.spades += getInventory().count("Spade");
                            }
                        }

                        if (needSalamander()) {
                            getGrandExchange().buyItem(API.staff, 1, API.salamanderPrice);
                            sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                            collect();
                            sleepUntil(() -> getInventory().contains(API.staff), 2200 + API.sleep());
                        }

                        if (needHelmet()) {
                            getGrandExchange().buyItem(API.helmet, 1, API.runeFullHelmPrice);
                            sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                            collect();
                            sleepUntil(() -> getInventory().contains(API.helmet), 2200 + API.sleep());

                            API.supplyCostCount += 1 * API.runeFullHelmPrice;
                        }

                        if (needBond()) {
                            if (!getInventory().contains("Old school bond")) {
                                getGrandExchange().buyItem("Old school bond", 1, API.bondPrice);
                                sleepUntil(() -> getGrandExchange().isReadyToCollect(), 6000 + API.sleep());
                                collect();
                                sleepUntil(() -> getInventory().contains("Old school bond"), 2200 + API.sleep());
                                API.BOND_COUNT += getInventory().count("Old school bond");

                                API.supplyCostCount += 1 * API.bondPrice;
                            } else {
                                API.BOND_COUNT += getInventory().count("Old school bond");
                            }
                        }
                    }

                    if (!getGrandExchange().isReadyToCollect() && !inventoryContainsSellables() && (!needPotions() && (!needFish()) && !needBarrowsTele() && !needRings() && !needAmmo() && !needVarrockTele())) {
                        if (!needPotions() || !needFish() || !needBarrowsTele() || !needRings() || !needAmmo() || !needVarrockTele() || !needSalamander() || !needHelmet() || !needSpades() || !needBond()) {
                            getGrandExchange().close();
                        }
                    }
                } else if (getInventory().contains("Coins")) {
                    getGrandExchange().open();
                    sleepUntil(() -> getGrandExchange().isOpen(), 8000);
                }

                if (!getInventory().contains("Coins")) {
                    if (getBank().isOpen()) {
                        if (!getInventory().isEmpty()) {
                            getBank().depositAllItems();
                            sleepUntil(() -> getInventory().isEmpty(), 1200 + API.sleep());
                        }

                        getBank().withdrawAll("Coins");
                        sleepUntil(() -> getInventory().contains("Coins"), 2200);

                        if (getBank().getWithdrawMode() != BankMode.NOTE) {
                            getBank().setWithdrawMode(BankMode.NOTE);
                            sleep(API.sleep());
                        }

                        for (String item : API.sellables) {
                            if (getBank().contains(item)) {
                                getBank().withdrawAll(item);
                                sleepUntil(() -> !getBank().contains(item), 2200);
                            }
                        }

                        if (getInventory().contains("Coins")) {
                            getBank().close();
                        }
                    } else if (getWalking().shouldWalk(Calculations.random(4, 7))) {
                        getBank().openClosest();
                    }
                }
            } else if (getWalking().shouldWalk(Calculations.random(4, 7))) {
                getWalking().walk(API.grandExchange.getCenter().getRandomizedTile(2));
            }
        }
        return API.sleep();
    }

    private boolean canGE() {
        return getGrandExchange().isOpen() || needPotions() || needFish() || needBarrowsTele() || needRings() || needAmmo() || needVarrockTele() || needSalamander() || needHelmet() || needSpades() || needBond();
    }

    private boolean containsItemAlready(String item) {
        return Arrays.asList(getGrandExchange().getItems()).stream().filter(i -> i != null && i.getName().equals(item)).findFirst().isPresent();
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
        if (curr <= max/(Calculations.random(10,14))) {
            return true;
        } else if (curr <= max/3 && API.grandExchange.contains(getLocalPlayer())) {
            if (API.ammoMin == 11000) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean inventoryContainsSellables() {
        for (String item : API.sellables) {
            if (getInventory().contains(item)) {
                return true;
            }
        }
        return false;
    }

    private boolean bankContainsSellables() {
        for (String item : API.sellables) {
            if (getBank().contains(item)) {
                return true;
            }
        }
        return false;
    }

    private void collect() {
        API.status = "Collecting...";
        if (getGrandExchange().isReadyToCollect()) {
            if (getGrandExchange().collect()) {
                sleepUntil(() -> !getGrandExchange().isReadyToCollect(), 2400 + API.sleep());
            }
        }
    }

    private void forceCollect() {
        getGrandExchange().collect();
        sleep(650, 1600);
    }

    private boolean freeSlotAvailable() {
        return getGrandExchange().isOpen() && getGrandExchange().getFirstOpenSlot() != -1;
    }

    private int slotsTakenCount() {
        int count = 0;
        for (int i = 0; i <= 7; i++) {
            if (getGrandExchange().slotContainsItem(i)) {
                count++;
            }
        }
        return count;
    }

    private int setPriceModifier(int price) {
        if (price == -1) {
            return price;
        }

        double modifier = 0;
        if (price <= 300) {
            modifier = 5.0;
        } else if (price <= 600) {
            modifier = 1.60;
        } else if (price <= 5000) {
            modifier = 2.0;
        } else if (price <= 15000) {
            modifier = 0.25;
        }
        return price + (int)(price*modifier);
    }
}
