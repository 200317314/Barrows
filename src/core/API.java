package core;

import core.ge.GrandExchancePrice;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.walking.path.impl.LocalPath;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import java.awt.*;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;

public class API {
    private static MethodContext ctx;
    private static final SecureRandom secureRandom = new SecureRandom();
    public static String helmet = "Zamorak mitre", amulet = "Occult necklace", chest = "Dragon chainbody", staff = "Black salamander", shield = "Rune kiteshield", legs = "Dragon platelegs", gloves = "Mystic gloves", ammo = "Harralander tar", ring = "Ring of dueling";

    public static String[] sellables = {"Blood rune", "Mind rune", "Chaos rune", "Death rune", "Bolt rack", "Ahrim's hood", "Ahrim's robetop", "Ahrim's robeskirt", "Ahrim's staff", "Dharok's helm",
            "Dharok's platebody", "Dharok's platelegs", "Dharok's greataxe", "Guthan's helm", "Guthan's platebody", "Guthan's chainskirt", "Guthan's warspear", "Karil's coif", "Karil's leathertop",
            "Karil's leatherskirt", "Karil's crossbow", "Torag's helm", "Torag's platebody", "Torag's platelegs", "Torag's hammers", "Verac's helm", "Verac's brassard", "Verac's plateskirt",
            "Verac's flail", "Super restore(3)", "Super restore(2)", "Super restore(1)"};

    public final static String[] barrowsBrothersFull = {"Verac the Defiled", "Torag the Corrupted", "Karil the Tainted", "Guthan the Infested", "Dharok the Wretched", "Ahrim the Blighted"};
    public final static String[] barrowsBrothers = {"Karil the Tainted", "Dharok the Wretched", "Ahrim the Blighted"};
    public final static String[] cryptMonsters = {"Bloodworm", "Skeleton", "Crypt rat", "Crypt spider", "Giant crypt rat", "Giant crypt spider"};

    public static boolean dharoksSlain = false, guthansSlain = false, karilsSlain = false, toragsSlain = false, veracsSlain = false, ahrimsSlain = false;
    //1 dh 2 gu 3 ka 4 to 5 ve 6 ah
    public static int cryptEntrance = -1;

    public static Deque<BarrowsBrother> barrowsBrotherDeque = new ArrayDeque<>();
    public static BarrowsBrother catacombBrother = null;
    public static BarrowsBrother target = null;

    public static boolean deathSleep = false;

    public static int supplyCostCount = 0;

    public static int chestsOpened = 0;
    public static int bloodRunes = 0, mindRunes = 0, chaosRunes = 0, deathRunes = 0, boltRack = 0, ahrimHood = 0, ahrimRobe = 0, ahrimLegs = 0, ahrimStaff = 0,
                        dharoksHelm = 0, dharoksChest = 0, dharoksLegs = 0, dharoksAxe = 0, guthansHelm = 0, guthansChest = 0, guthansLegs = 0, guthansSpear = 0,
                        karilsCoif = 0, karilsChest = 0, karilsLegs = 0, karilsBow = 0, toragsHelm = 0, toragsChest = 0, toragsLegs = 0, toragsHams = 0,
                        veracsHelm = 0, veracsChest = 0, veracsLegs = 0, veracsFlail = 0, coins = 0;

    public static int bloodRunesPrice = 245, mindRunesPrice = 4, chaosRunesPrice = 102, deathRunesPrice = 236, boltRackPrice = 49, ahrimHoodPrice = 46961, ahrimRobePrice = 1892735, ahrimLegsPrice = 1663604,
            ahrimStaffPrice = 113446, dharoksHelmPrice = 333046, dharoksChestPrice = 1304286, dharoksLegsPrice = 1180312, dharoksAxePrice = 419347, guthansHelmPrice = 1188215,
            guthansChestPrice = 803350, guthansLegsPrice = 397686, guthansSpearPrice = 1105554, karilsCoifPrice = 52757, karilsChestPrice = 1901683, karilsLegsPrice = 171316,
            karilsBowPrice = 148287, toragsHelmPrice = 94816, toragsChestPrice = 222949, toragsLegsPrice = 269059, toragsHamsPrice = 120958, veracsHelmPrice = 304409,
            veracsChestPrice = 204750, veracsLegsPrice = 1036329, veracsFlailPrice = 147554;

    public static Timer logoutTimer = null, shouldSleep = null, shouldBreak = null;
    public static String status = "Loading...";
    public static String user = "-";
    public static int world = -1, memDays = -1;
    public static int BOND_COUNT = -1;
    public static int geBuySellCount = 1;
    public static boolean muling = false;
    public static int goldMuled = 0;
    public static int muleGoldAt = 4000000;
    public static int maxPotions = 200, maxFish = 2000, maxBarrowsTele = 200, maxRings = 60, maxAmmo = 30000, maxVarrockTele = 100;
    public static int potions = -1, fish = -1, barrowsTele = -1, rings = -1, ammoMin = -1, varrockTele = -1, coinsAmt = -1, spades = -1;
    public static int potionsPrice = 13900, fishPrice = 900, ringPrice = 1750, ammoPrice = 50, varrockTelePrice = 1250, barrowsTelePrice = 10000, salamanderPrice = 5000, runeFullHelmPrice = 50000, bondPrice = 5000000;

    public static final Area clanWarsArea = new Area(3345, 3180, 3393, 3140);
    public static final Area inClanWarsArea = new Area(3318, 4759, 3335, 4743);
    public static final Area clanWarsPortal = new Area(3344, 3170, 3358, 3153);
    public static final Area grandExchange = new Area(3159, 3495, 3170, 3485);

    public static final Area cryptChestArea = new Area(3546, 9700, 3557, 9689);
    public static final Area barrowsArea = new Area(3544, 3317, 3586, 3265);
    public static final Area barrowsCrypt = new Area(3522, 9726, 3581, 9664);
    public static final Area dharoksHill = new Area(3574, 3299, 3576, 3297);
    public static final Area guthansHill = new Area(3576, 3283, 3578, 3281);
    public static final Area karilsHill = new Area(3564, 3276, 3566, 3274);
    public static final Area toragsHill = new Area(3552, 3284, 3554, 3281);
    public static final Area veracsHill = new Area(3555, 3298, 3557, 3296);
    public static final Area ahrimsHill = new Area(3563, 3290, 3566, 3288);

    public static final Area dharoksTomb = new Area(3548, 9720, 3560, 9709, 3);
    public static final Area guthansTomb = new Area(3532, 9709, 3545, 9698, 3);
    public static final Area karilsTomb = new Area(3545, 9689, 3557, 9677, 3);
    public static final Area toragsTomb = new Area(3563, 9692, 3575, 9681, 3);
    public static final Area veracsTomb = new Area(3567, 9711, 3579, 9701, 3);
    public static final Area ahrimsTomb = new Area(3550, 9704, 3561, 9693, 3);

    //DebugAreas
    public static final Area northCrypt = new Area(3546, 9717, 3557, 9706);
    public static final Area eastCrypt = new Area(3563, 9700, 3574, 9689);
    public static final Area southCrypt = new Area(3546, 9683, 3557, 9672);
    public static final Area westCrypt = new Area(3529, 9700, 3540, 9689);

    public API(MethodContext ctx) {
        this.ctx = ctx;
    }

    public static int sleep() {
        return (int) Calculations.nextGaussianRandom(400, 200);
    }

    private static boolean contains(String name) {
        return ctx.getEquipment().contains(i -> i != null && i.getName().contains(name)) || ctx.getInventory().contains(i -> i != null && i.getName().contains(name) && !i.isNoted());
    }

    public static boolean hasHelmet() {
        return contains(helmet);
    }

    public static boolean hasAmulet() {
        return contains(amulet);
    }

    public static boolean hasChest() {
        return contains(chest);
    }

    public static boolean hasStaff() {
        return contains(staff);
    }

    public static boolean hasLegs() {
        return contains(legs);
    }

    public static boolean hasRing() {
        return contains(ring);
    }

    public static boolean hasAmmo() {
        return contains(ammo);
    }

    public static boolean hasFood() {
        return contains("Monkfish");
    }

    public static boolean hasPotions() {
        return contains("Super restore");
    }

    public static boolean hasTeleports() {
        return contains("Barrows teleport");
    }

    public static boolean hasSpade() {
        return contains("Spade");
    }

    public static boolean hasSupplies() {
        return hasHelmet() && hasAmulet() && hasChest() && hasStaff() && hasLegs()&& hasRing() && hasFood() && hasPotions() && hasSpade();
    }

    public static int getPrayerPoints() {
        WidgetChild wc = ctx.getWidgets().getWidget(160).getChild(15);
        return Integer.parseInt(wc.getText());
    }

    public static int getRewardsPotential() {
        Widget wc = ctx.getWidgets().getWidget(24);

        if (wc != null && wc.isVisible()) {
            String cut1 = wc.getChild(10).getText().split(": ")[1];
            String percent = cut1.split("%")[0];
            return Integer.parseInt(percent);
        } else {
            return 0;
        }
    }

    public static Prayer getPrayer() {
        /*Character barrowsBrother = ctx.getLocalPlayer().getCharacterInteractingWithMe();
        if (barrowsBrother != null && Arrays.asList(API.barrowsBrothersFull).contains(barrowsBrother.getName())) {
            if (barrowsBrother.getName().equals("Ahrim the Blighted")) {
                return Prayer.PROTECT_FROM_MAGIC;
            } else if (barrowsBrother.getName().equals("Karil the Tainted")) {
                return Prayer.PROTECT_FROM_MISSILES;
            } else {
                return Prayer.PROTECT_FROM_MELEE;
            }
        }*/

        NPC attackingMe = ctx.getNpcs().closest(n -> Objects.nonNull(n) && Objects.nonNull(n.getInteractingCharacter()) && Arrays.asList(API.barrowsBrothersFull).contains(n.getName()) && n.getInteractingCharacter().getName().equals(ctx.getLocalPlayer().getName()));
        if (attackingMe != null) {
            if (attackingMe.getName().equals("Ahrim the Blighted")) {
                return Prayer.PROTECT_FROM_MAGIC;
            } else if (attackingMe.getName().equals("Karil the Tainted")) {
                return Prayer.PROTECT_FROM_MISSILES;
            } else {
                return Prayer.PROTECT_FROM_MELEE;
            }
        }
        return null;
    }

    public static void updateBarrowsCounter() {
        if (dharoksTomb.contains(ctx.getLocalPlayer())) {
            dharoksSlain = true;
        }

        if (guthansTomb.contains(ctx.getLocalPlayer())) {
            guthansSlain = true;
        }

        if (karilsTomb.contains(ctx.getLocalPlayer())) {
            karilsSlain = true;
        }

        if (toragsTomb.contains(ctx.getLocalPlayer())) {
            toragsSlain = true;
        }

        if (veracsTomb.contains(ctx.getLocalPlayer())) {
            veracsSlain = true;
        }

        if (ahrimsTomb.contains(ctx.getLocalPlayer())) {
            ahrimsSlain = true;
        }
    }

    public static boolean inTomb() {
        if (dharoksTomb.contains(ctx.getLocalPlayer())) {
            return true;
        }

        if (guthansTomb.contains(ctx.getLocalPlayer())) {
            return true;
        }

        if (karilsTomb.contains(ctx.getLocalPlayer())) {
            return true;
        }

        if (toragsTomb.contains(ctx.getLocalPlayer())) {
            return true;
        }

        if (veracsTomb.contains(ctx.getLocalPlayer())) {
            return true;
        }

        if (ahrimsTomb.contains(ctx.getLocalPlayer())) {
            return true;
        }
        return false;
    }

    public static boolean enterCrypt() {
        return dharoksSlain && guthansSlain && karilsSlain && toragsSlain && veracsSlain && ahrimsSlain;
    }

    public static Area cryptEntranceHill(int cryptId) {
        if (cryptId == 1) {
            return dharoksHill;
        } else if (cryptId == 2) {
            return guthansHill;
        } else if (cryptId == 3) {
            return karilsHill;
        } else if (cryptId == 4) {
            return toragsHill;
        } else if (cryptId == 5) {
            return veracsHill;
        } else if (cryptId == 6) {
            return ahrimsHill;
        }
        return null;
    }

    public static Area cryptEntranceTomb(int cryptId) {
        if (cryptId == 1) {
            return dharoksTomb;
        } else if (cryptId == 2) {
            return guthansTomb;
        } else if (cryptId == 3) {
            return karilsTomb;
        } else if (cryptId == 4) {
            return toragsTomb;
        } else if (cryptId == 5) {
            return veracsTomb;
        } else if (cryptId == 6) {
            return ahrimsTomb;
        }
        return null;
    }

    public static boolean charInteractingIsBarrows() {
        return ctx.getLocalPlayer().getCharacterInteractingWithMe() != null && Arrays.asList(barrowsBrothersFull).contains(ctx.getLocalPlayer().getCharacterInteractingWithMe().getName()) && ctx.getMap().canReach(ctx.getLocalPlayer().getCharacterInteractingWithMe());
    }

    public static boolean charInteractingIsCryptMob() {
        return ctx.getLocalPlayer().getCharacterInteractingWithMe() != null && Arrays.asList(cryptMonsters).contains(ctx.getLocalPlayer().getCharacterInteractingWithMe().getName()) && ctx.getMap().canReach(ctx.getLocalPlayer().getCharacterInteractingWithMe());
    }

    public static void setLoggerDisabled() {
        Utils.getScript().getRandomManager().disableSolver(RandomEvent.LOGIN);
    }

    public static void setLoggerEnabled() {
        Utils.getScript().getRandomManager().enableSolver(RandomEvent.LOGIN);
    }

    public static String getGold(int gold) {
        DecimalFormat df = new DecimalFormat("##.##");
        if (gold >= 1000000) {
            return df.format(((double)gold/1000000.00)) + "m";
        } else if (gold < 1000000) {
            return df.format((gold/1000)) + "k";
        }
        return "0k";
    }

    public static String getExp(int gold) {
        DecimalFormat df = new DecimalFormat("##.####");
        if (gold >= 1000000) {
            return df.format(((double)gold/1000000.00)) + "m";
        } else if (gold < 1000000) {
            return df.format((gold/1000)) + "k";
        }
        return "0k";
    }

    public static long getRunTimeTimer() {
        //2.33 hrs
        int minutes = Calculations.random(10, 30) + 100;
        return minutes*60*1000;
    }

    public static long getBreakTimeTimer() {
        //
        int minutes = Calculations.random(5, 14) + 10;
        return minutes*60*1000;
    }

    public static long getSleepTimeTimer() {
        int minutes = Calculations.random(15, 75) + 400;
        return minutes*60*1000;
    }

    public static long getDeathTimeTimer() {
        int minutes = secureRandom.nextInt(242 + Calculations.random(1, 17));
        return minutes*60*1000;
    }

    public static long getShouldSleepTimer() {
        //avg 9.3hrs
        int minutes = Calculations.random(20, 80) + 500;
        return minutes*60*1000;
    }

    public static boolean canAttack() {
        NPC monster = ctx.getNpcs().closest(n -> n != null && !n.isInCombat() && ctx.getMap().canReach(n) && ctx.getLocalPlayer().getCharacterInteractingWithMe() == n
                && (Arrays.asList(API.barrowsBrothersFull).contains(n.getName()) || (Arrays.asList(API.cryptMonsters).contains(n.getName())) && API.getRewardsPotential() < 78));
        return monster != null && !ctx.getLocalPlayer().isInCombat() && ctx.getLocalPlayer().getCharacterInteractingWithMe() == null && ctx.getMap().canReach(monster) || canReAttack();
    }

    private static boolean canReAttack() {
        return !ctx.getLocalPlayer().isInCombat() && ctx.getLocalPlayer().getCharacterInteractingWithMe() != null && ctx.getMap().canReach(ctx.getLocalPlayer().getCharacterInteractingWithMe())
                && (Arrays.asList(barrowsBrothersFull).contains(ctx.getLocalPlayer().getCharacterInteractingWithMe().getName())
                || (Arrays.asList(cryptMonsters).contains(ctx.getLocalPlayer().getCharacterInteractingWithMe().getName()) && API.getRewardsPotential() < 78));
    }

    public static boolean canAdjustPitch() {
        return ctx.getCamera().getPitch() < 300;
    }

    public static void adjustPitch() {
        int pitch = secureRandom.nextInt(82) + 301;
        ctx.getCamera().rotateToPitch(pitch);
    }

    public static boolean shouldWalk() {
        int distance = secureRandom.nextInt(2) + 4;
        return ctx.getWalking().shouldWalk(distance) && ctx.getWalking().getDestination().distance(ctx.getLocalPlayer()) > distance;
    }

    public static void drawBox(int a, int b, int c, int d,  Graphics g) {
        g.setColor(new Color(25,25,25,150));
        g.fillRect(a, b, c, d);
        g.setColor(Color.BLACK);
        g.draw3DRect(a +2, b +2, c -4, d -4, false);
        g.setColor(Color.DARK_GRAY);
        g.draw3DRect(a -0, b -0, c +0, d +0, false);
    }

    public static Color getWarningColor(int curr, int max) {
        if (curr <= max/4) {
            return Color.RED;
        } else if (curr <= max/2) {
            return Color.ORANGE;
        } else {
            return Color.GREEN;
        }
    }

    public static void countSupplies() {
        API.bloodRunes += ctx.getInventory().count("Blood rune");
        API.mindRunes += ctx.getInventory().count("Mind rune");
        API.chaosRunes += ctx.getInventory().count("Chaos rune");
        API.deathRunes += ctx.getInventory().count("Death rune");
        API.boltRack += ctx.getInventory().count("Bolt rack");
        API.ahrimHood += ctx.getInventory().count("Ahrim's hood");
        API.ahrimRobe += ctx.getInventory().count("Ahrim's robetop");
        API.ahrimLegs += ctx.getInventory().count("Ahrim's robeskirt");
        API.ahrimStaff += ctx.getInventory().count("Ahrim's staff");
        API.dharoksHelm += ctx.getInventory().count("Dharok's helm");
        API.dharoksChest += ctx.getInventory().count("Dharok's platebody");
        API.dharoksLegs += ctx.getInventory().count("Dharok's platelegs");
        API.dharoksAxe += ctx.getInventory().count("Dharok's greataxe");
        API.guthansHelm += ctx.getInventory().count("Guthan's helm");
        API.guthansChest += ctx.getInventory().count("Guthan's platebody");
        API.guthansLegs += ctx.getInventory().count("Guthan's chainskirt");
        API.guthansSpear += ctx.getInventory().count("Guthan's warspear");
        API.karilsCoif += ctx.getInventory().count("Karil's coif");
        API.karilsChest += ctx.getInventory().count("Karil's leathertop");
        API.karilsLegs += ctx.getInventory().count("Karil's leatherskirt");
        API.karilsBow += ctx.getInventory().count("Karil's crossbow");
        API.toragsHelm += ctx.getInventory().count("Torag's helm");
        API.toragsChest += ctx.getInventory().count("Torag's platebody");
        API.toragsLegs += ctx.getInventory().count("Torag's platelegs");
        API.toragsHams += ctx.getInventory().count("Torag's hammers");
        API.veracsHelm += ctx.getInventory().count("Verac's helm");
        API.veracsChest += ctx.getInventory().count("Verac's brassard");
        API.veracsLegs += ctx.getInventory().count("Verac's plateskirt");
        API.veracsFlail += ctx.getInventory().count("Verac's flail");
        //API.coins += getInventory().count("Coins");
        API.cryptEntrance = -1;
        API.resetBarrowsDeque();

        potions = ctx.getBank().count("Super restore(4)");
        fish = ctx.getBank().count("Monkfish");
        barrowsTele = ctx.getBank().count("Barrows teleport");
        rings = ctx.getBank().count("Ring of dueling(8)");
        ammoMin = ctx.getEquipment().count(ammo) + ctx.getBank().count(ammo);
        varrockTele = ctx.getBank().count("Varrock teleport");
        coinsAmt = ctx.getBank().count("Coins");
        spades = ctx.getBank().count("Spade") + ctx.getInventory().count("Spade");
        BOND_COUNT = ctx.getBank().count("Old school bond") + ctx.getInventory().count("Old school bond");

        if (!API.goldForBond() && clanWarsArea.contains(ctx.getLocalPlayer()) && ctx.getBank().count("Coins") > (muleGoldAt + suppliesGold() + 1) && !muling) {
            muling = true;
            MuleConnector.sendMuleRequest(ctx.getLocalPlayer().getName(), ctx.getClient().getCurrentWorld());
        } else if (muling && ctx.getBank().count("Coins") < (muleGoldAt + suppliesGold() + 1)) {
            muling = false;
        }
    }

    public static int suppliesGold() {
        int saveGold = 0;

        if (API.potions < API.maxPotions) {
            saveGold += (API.maxPotions - API.potions) * API.potionsPrice;
        }

        if (API.fish < API.maxFish) {
            saveGold += (API.maxFish - API.fish) * API.fishPrice;
        }

        if (API.barrowsTele < API.maxBarrowsTele) {
            saveGold += (API.maxBarrowsTele - API.barrowsTele) * API.barrowsTelePrice;
        }
        return saveGold + 100000;
    }

    public static void sendWorkerStatus() {
        if (ctx.getClient().getGameState() == GameState.LOGGED_IN) {
            MuleConnector.sendWorkerStatus(user, world, getProfit(), chestsOpened, true, status);
        } else {
            MuleConnector.sendWorkerStatus(user, world, getProfit(), chestsOpened, false, status);
        }
    }

    public static int getProfit() {
        int profit = 0;
        profit += API.bloodRunes * API.bloodRunesPrice;
        profit += API.mindRunes * API.mindRunesPrice;
        profit += API.chaosRunes * API.chaosRunesPrice;
        profit += API.deathRunes * API.deathRunesPrice;
        profit += API.boltRack * API.boltRackPrice;
        profit += API.ahrimHood * API.ahrimHoodPrice;
        profit += API.ahrimRobe * API.ahrimRobePrice;
        profit += API.ahrimLegs * API.ahrimLegsPrice;
        profit += API.ahrimStaff * API.ahrimStaffPrice;
        profit += API.dharoksHelm * API.dharoksHelmPrice;
        profit += API.dharoksChest * API.dharoksChestPrice;
        profit += API.dharoksLegs * API.dharoksLegsPrice;
        profit += API.dharoksAxe * API.dharoksAxePrice;
        profit += API.guthansHelm * API.guthansHelmPrice;
        profit += API.guthansChest * API.guthansChestPrice;
        profit += API.guthansLegs * API.guthansLegsPrice;
        profit += API.guthansSpear * API.guthansSpearPrice;
        profit += API.karilsCoif * API.karilsCoifPrice;
        profit += API.karilsChest * API.karilsChestPrice;
        profit += API.karilsLegs * API.karilsLegsPrice;
        profit += API.karilsBow * API.karilsBowPrice;
        profit += API.toragsHelm * API.toragsHelmPrice;
        profit += API.toragsChest * API.toragsChestPrice;
        profit += API.toragsLegs * API.toragsLegsPrice;
        profit += API.toragsHams * API.toragsHamsPrice;
        profit += API.veracsHelm * API.veracsHelmPrice;
        profit += API.veracsChest * API.veracsChestPrice;
        profit += API.veracsLegs * API.veracsLegsPrice;
        profit += API.veracsFlail * API.veracsFlailPrice;
        profit -= API.supplyCostCount;
        //profit += API.coins;
        return profit;
    }

    public static void resetBarrowsDeque() {

        barrowsBrotherDeque.clear();
        target = null;
        catacombBrother = null;

        barrowsBrotherDeque.add(BarrowsBrother.DHAROK);
        barrowsBrotherDeque.add(BarrowsBrother.AHRIM);
        barrowsBrotherDeque.add(BarrowsBrother.KARIL);
        barrowsBrotherDeque.add(BarrowsBrother.GUTHAN);
        barrowsBrotherDeque.add(BarrowsBrother.TORAG);
        barrowsBrotherDeque.add(BarrowsBrother.VERAC);
    }

    public static boolean needsSupplies() {
        return needPotions() || needFish() || needBarrowsTele() || needRings() || needAmmo() || needVarrockTele() || needSalamander() || needHelmet();
    }

    private static boolean needPotions() {
        return API.potions != -1 && needItems(API.potions, API.maxPotions);
    }

    private static boolean needFish() {
        return API.fish != -1 && needItems(API.fish, API.maxFish);
    }

    private static boolean needBarrowsTele() {
        return API.barrowsTele != -1 && needItems(API.barrowsTele, API.maxBarrowsTele);
    }

    private static boolean needRings() {
        return API.rings != -1 && needItems(API.rings, API.maxRings);
    }

    private static boolean needAmmo() {
        return API.ammoMin != -1 && needItems(API.ammoMin, API.maxAmmo);
    }

    private static boolean needVarrockTele() {
        return API.varrockTele != -1 && needItems(API.varrockTele, API.maxVarrockTele);
    }

    private static boolean needSalamander() {
        return !ctx.getInventory().contains(API.staff) && !ctx.getEquipment().contains(API.staff);
    }

    private static boolean needHelmet() {
        return !ctx.getInventory().contains(API.helmet) && !ctx.getEquipment().contains(API.helmet);
    }

    private static boolean needItems(int curr, int max) {
        if (curr <= max/(Calculations.random(10,14))) {
            return true;
        } else if (curr <= max/3 && API.grandExchange.contains(ctx.getLocalPlayer())) {
            if (API.ammoMin == 11000) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean goldForBond() {
        return (ctx.getBank().count("Coins") >= API.bondPrice || ctx.getInventory().count("Coins") >= API.bondPrice || BOND_COUNT > 0) && ctx.getClient().getMembershipLeft() != -1 && ctx.getClient().getMembershipLeft() <= 2;
    }

    public static LocalPath<Tile> getLocalPathAlgo() {
        Area[] possibleAreas = {northCrypt, eastCrypt, southCrypt, westCrypt};

        for (Area area : possibleAreas) {
            if (!area.contains(ctx.getLocalPlayer())) {
                LocalPath path = ctx.getWalking().getAStarPathFinder().calculate(ctx.getLocalPlayer().getTile(), area.getCenter());
                if (path.size() != 0) {
                    return path;
                }
            }
        }
        return null;
    }

    public static void resetGeBuySell() {
        geBuySellCount = Calculations.random(1, 5);
    }

    private static String getPrefix() {
        if (ctx != null) {
            if (ctx.getSkills().getRealLevel(Skill.DEFENCE) >= 60) {
                return "Dragon";
            }
        }
        return "Rune";
    }

    /*
        public static int bloodRunesPrice = 245, mindRunesPrice = 4, chaosRunesPrice = 102, deathRunesPrice = 236, boltRackPrice = 49, ahrimHoodPrice = 46961, ahrimRobePrice = 1892735, ahrimLegsPrice = 1663604,
            ahrimStaffPrice = 113446, dharoksHelmPrice = 333046, dharoksChestPrice = 1304286, dharoksLegsPrice = 1180312, dharoksAxePrice = 419347, guthansHelmPrice = 1188215,
            guthansChestPrice = 803350, guthansLegsPrice = 397686, guthansSpearPrice = 1105554, karilsCoifPrice = 52757, karilsChestPrice = 1901683, karilsLegsPrice = 171316,
            karilsBowPrice = 148287, toragsHelmPrice = 94816, toragsChestPrice = 222949, toragsLegsPrice = 269059, toragsHamsPrice = 120958, veracsHelmPrice = 304409,
            veracsChestPrice = 204750, veracsLegsPrice = 1036329, veracsFlailPrice = 147554;
     */

    public static void updateSellablesPrices() {
        status = "Updating prices...";
        bloodRunesPrice = GrandExchancePrice.getPrice(565);
        mindRunesPrice = GrandExchancePrice.getPrice(558);
        chaosRunesPrice = GrandExchancePrice.getPrice(562);
        deathRunesPrice = GrandExchancePrice.getPrice(560);
        boltRackPrice = GrandExchancePrice.getPrice(4740);
        ahrimHoodPrice = GrandExchancePrice.getPrice(4708);
        ahrimRobePrice = GrandExchancePrice.getPrice(4712);
        ahrimLegsPrice = GrandExchancePrice.getPrice(4714);
        ahrimStaffPrice = GrandExchancePrice.getPrice(4710);
        dharoksHelmPrice = GrandExchancePrice.getPrice(4716);
        dharoksChestPrice = GrandExchancePrice.getPrice(4720);
        dharoksLegsPrice = GrandExchancePrice.getPrice(4722);
        dharoksAxePrice = GrandExchancePrice.getPrice(4718);
        guthansHelmPrice = GrandExchancePrice.getPrice(4724);
        guthansChestPrice = GrandExchancePrice.getPrice(4728);
        guthansLegsPrice = GrandExchancePrice.getPrice(4730);
        guthansSpearPrice = GrandExchancePrice.getPrice(4726);
        karilsCoifPrice = GrandExchancePrice.getPrice(4732);
        karilsChestPrice = GrandExchancePrice.getPrice(4736);
        karilsLegsPrice = GrandExchancePrice.getPrice(4738);
        karilsBowPrice = GrandExchancePrice.getPrice(4734);
        toragsHelmPrice = GrandExchancePrice.getPrice(4745);
        toragsChestPrice = GrandExchancePrice.getPrice(4749);
        toragsLegsPrice = GrandExchancePrice.getPrice(4751);
        toragsHamsPrice = GrandExchancePrice.getPrice(4747);
        veracsHelmPrice = GrandExchancePrice.getPrice(4753);
        veracsChestPrice = GrandExchancePrice.getPrice(4757);
        veracsLegsPrice = GrandExchancePrice.getPrice(4759);
        veracsFlailPrice = GrandExchancePrice.getPrice(4755);
    }

    public static int rewardsPotential = 77;

    public static void resetRewardsPotential() {
        rewardsPotential = Calculations.random(75, 83);
    }
}
