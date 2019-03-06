package core.nodes;

import core.API;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Arrays;
import java.util.Objects;

public class PotionNode extends TaskNode {
    @Override
    public int priority() {
        return 99;
    }

    @Override
    public boolean accept() {
        //System.out.println("canPotion: " + canPotion());
        return canPotion();
    }

    @Override
    public int execute() {
        if (getTabs().isOpen(Tab.INVENTORY)) {
            API.status = "Drinking potion...";
            //getInventory().get(i -> i != null && i.getName().contains("Super restore")).interact("Drink");
            if (getInventory().get(i -> i != null && i.getName().contains("Super restore")).interact("Drink")) {
                sleepUntil(() -> API.getPrayerPoints() > 14, API.sleep());
            }
        } else {
            getTabs().open(Tab.INVENTORY);
            return API.sleep();
        }
        return 0;
    }

    private boolean canPotion() {
        return API.hasPotions() && API.getPrayerPoints() <= 14 && isFightingBrother();
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
}
