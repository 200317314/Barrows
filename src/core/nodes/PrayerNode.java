package core.nodes;

import core.API;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Arrays;
import java.util.Objects;

public class PrayerNode extends TaskNode {
    //private final String[] barrowsBrothers = {"Verac the Defiled", "Torag the Corrupted", "Karil the Tainted", "Guthan the Infested", "Dharok the Wretched", "Ahrim the Blighted"};
    @Override
    public int priority() {
        return 100;
    }

    @Override
    public boolean accept() {
        //System.out.println("canPrayer: " + canPrayer() + " canDeactivate: " + canDeactivate());
        return canPrayer() || canDeactivate();
    }

    @Override
    public int execute() {
        if (getTabs().isOpen(Tab.PRAYER)) {
            API.status = "Using prayer...";
            if (canPrayer()) {
                getPrayer().toggle(true, API.getPrayer());
                sleepUntil(() -> getPrayer().isActive(API.getPrayer()), 1200 + API.sleep());
            }

            if (canDeactivate()) {
                if (getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
                    getPrayer().toggle(false, Prayer.PROTECT_FROM_MELEE);
                    sleepUntil(() -> !getPrayer().isActive(Prayer.PROTECT_FROM_MELEE), 1200 + API.sleep());
                }

                if (getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
                    getPrayer().toggle(false, Prayer.PROTECT_FROM_MAGIC);
                    sleepUntil(() -> !getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC), 1200 + API.sleep());
                }

                if (getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
                    getPrayer().toggle(false, Prayer.PROTECT_FROM_MISSILES);
                    sleepUntil(() -> !getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES), 1200 + API.sleep());
                }
            }
        } else {
            getTabs().open(Tab.PRAYER);
        }
        return API.sleep();
    }

    private boolean canPrayer() {
        return API.getPrayer() != null && !getPrayer().isActive(API.getPrayer()) && getLocalPlayer().isInCombat() && isFightingBrother() && API.getPrayerPoints() > 1;
    }

    private boolean canDeactivate() {
        return isPrayerActive() && !isFightingBrother();
    }

    private boolean charInteractingIsBarrows() {
        return getLocalPlayer().getCharacterInteractingWithMe() != null && Arrays.asList(API.barrowsBrothers).contains(getLocalPlayer().getCharacterInteractingWithMe().getName());
    }

    private boolean isFightingBrother() {
        Character withMe = getLocalPlayer().getCharacterInteractingWithMe();
        if (withMe == null)
            return false;

        NPC b = getNpcs().closest(f -> Objects.nonNull(f)
                && Objects.nonNull(f.getInteractingCharacter())
                && Objects.nonNull(f.getName())
                && Arrays.asList(API.barrowsBrothers).contains(f.getName())
                && f.getInteractingCharacter().getName().equals(getLocalPlayer().getName()));

        if (b != null) {
            return true;
        }

        return false;
    }

    private boolean isPrayerActive() {
        return getPrayer().isActive(Prayer.PROTECT_FROM_MELEE) || getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC) || getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES);
    }
}
