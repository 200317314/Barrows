import core.API;
import core.MuleConnector;
import core.Utils;
import core.nodes.*;
import org.dreambot.api.Client;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.api.utilities.Timer;

import java.awt.*;

@ScriptManifest(name = "DynaBarrows_Travis_License", author = "7804364", version = 8.10D, description = "makes mad money.", category = Category.COMBAT)
public class Main extends TaskScript {
    private final API api = new API(this);
    private final Timer runTime = new Timer();
    private final Utils utils = new Utils(this);
    private MuleConnector muleConnector;

    @Override
    public void onStart() {
        API.shouldBreak = new Timer(API.getRunTimeTimer());
        API.shouldSleep = new Timer(API.getShouldSleepTimer());
        API.resetBarrowsDeque();
        API.resetGeBuySell();

        API.updateSellablesPrices();

        PassableObstacle pb = new PassableObstacle("Door", "Open", null, null, null);
        getWalking().getAStarPathFinder().addObstacle(pb);

        addNodes(new GearNode());
        addNodes(new BankNode());
        addNodes(new EatNode());
        addNodes(new PotionNode());
        addNodes(new PrayerNode());
        addNodes(new AttackNode());
        //addNodes(new TraversalNode());
        addNodes(new NewTraversalNode());
        addNodes(new PuzzleNode());
        addNodes(new ClanWarsNode());
        addNodes(new TeleportNode());
        addNodes(new CryptTraversalNode());
        //addNodes(new ChestWidgetNode());
        addNodes(new CombatStyleNode());
        addNodes(new BreakNode());
        addNodes(new GeNode());
        addNodes(new MuleNode());
        addNodes(new ItemSelectNode());
        addNodes(new StatusNode());
        addNodes(new TimerNode());
        addNodes(new BondNode());

        //getSkillTracker().start(Skill.RANGED);
        //getSkillTracker().start(Skill.MAGIC);

        muleConnector = new MuleConnector(this);
        //new Thread(new MuleConnector()).start();
    }

    /*@Override
    public void onPaint(Graphics2D g) {
        //Status
        API.drawBox(5, 20, 140, 60, g);
        g.setColor(Color.WHITE);
        g.drawString(Utils.getScript().getManifest().name() + " v" + Utils.getScript().getVersion(), 10, 32);
        g.setColor(Color.GRAY);
        g.drawString("RunTime: " + Timer.formatTime(runTime.elapsed()), 10, 46);
        if (API.logoutTimer != null) {
            g.setColor(Color.RED);
            g.drawString("Break/Sleep: " + Timer.formatTime(API.logoutTimer.remaining()), 10, 60);
        } else {
            g.setColor(Color.GREEN);
            g.drawString("Break/Sleep: Awake", 10, 60);
        }
        g.setColor(Color.GRAY);
        g.drawString(API.status, 10, 74);

        //Profit
        API.drawBox(5, 84, 140, 44, g);
        g.setColor(Color.GRAY);
        g.drawString("Profit: " + API.getGold(getProfit()) + "(" + API.getGold(runTime.getHourlyRate(getProfit())) + ")", 10, 96);
        g.drawString("Chests: " + API.chestsOpened + "(" + runTime.getHourlyRate(API.chestsOpened) + ")", 10, 110);
        g.drawString("Muled: " + API.getGold(API.goldMuled) + "[" + API.getGold(API.coinsAmt) + "]", 10, 124);

        //Supplies
        API.drawBox(5, 132, 140, 86, g);
        g.setColor(API.getWarningColor(API.fish, API.maxFish));
        g.drawString("MonkFish: " + API.fish, 10, 144);
        g.setColor(API.getWarningColor(API.potions, API.maxPotions));
        g.drawString("Super Restore: " + API.potions, 10, 158);
        g.setColor(API.getWarningColor(API.barrowsTele, API.maxBarrowsTele));
        g.drawString("Barrows Teleport: " + API.barrowsTele, 10, 172);
        g.setColor(API.getWarningColor(API.rings, API.maxRings));
        g.drawString("Ring of dueling: " + API.rings, 10, 186);
        g.setColor(API.getWarningColor(API.ammoMin, API.maxAmmo));
        g.drawString("Harralander tar: " + API.ammoMin, 10, 200);
        g.setColor(API.getWarningColor(API.varrockTele, API.maxVarrockTele));
        g.drawString("Varrock teleport: " + API.varrockTele, 10, 214);

        //Levels
        g.setColor(Color.GRAY);
        API.drawBox(5, 222, 180, 32, g);
        g.setColor(Color.GREEN);
        g.drawString("Magic: " + getSkills().getRealLevel(Skill.MAGIC) + "(+" + getSkillTracker().getGainedLevels(Skill.MAGIC) + ")" + " Exp: " + getSkillTracker().getGainedExperience(Skill.MAGIC) + "(" + API.getExp(getSkillTracker().getGainedExperiencePerHour(Skill.MAGIC)) + ")", 10, 234);
        g.drawString("Ranged: " + getSkills().getRealLevel(Skill.RANGED) + "(+" + getSkillTracker().getGainedLevels(Skill.RANGED) + ")" + " Exp: " + getSkillTracker().getGainedExperience(Skill.RANGED) + "(" + API.getExp(getSkillTracker().getGainedExperiencePerHour(Skill.RANGED)) + ")", 10, 248);
    }*/

    private int getProfit() {
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

    @Override
    public void onExit() {
        MuleConnector.stop();
        MuleConnector.sendWorkerStatus(getLocalPlayer().getName(), 0, 0, -1, false, "BANNED");
        Client.setRenderingDisabled(false);
    }

    /*@Override
    public void onAutoMessage(Message message) {

    }

    @Override
    public void onPrivateInfoMessage(Message message) {

    }

    @Override
    public void onClanMessage(Message message) {

    }

    @Override
    public void onGameMessage(Message message) {
        if (message != null) {
            if (message.getMessage().contains("You don't find anything.")) {
                API.updateBarrowsCounter();
            }
        }
    }

    @Override
    public void onPlayerMessage(Message message) {

    }

    @Override
    public void onTradeMessage(Message message) {

    }

    @Override
    public void onPrivateInMessage(Message message) {

    }

    @Override
    public void onPrivateOutMessage(Message message) {

    }*/
}
