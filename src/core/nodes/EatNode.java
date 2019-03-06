package core.nodes;

import core.API;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Arrays;
import java.util.Objects;

public class EatNode extends TaskNode {
    @Override
    public int priority() {
        return 2;
    }

    @Override
    public boolean accept() {
        //System.out.println("canEat: " + canEat());
        return canEat();
    }

    @Override
    public int execute() {
        API.status = "Eating food...";
        if (getTabs().isOpen(Tab.INVENTORY)) {
            getInventory().get("Monkfish").interact("Eat");
        } else {
            getTabs().open(Tab.INVENTORY);
            return API.sleep();
        }
        return 1000 + API.sleep();
    }

    private boolean canEat() {
        return API.hasFood() && getCombat().getHealthPercent() <= (60 + Calculations.random(1,10)) && !canPrayer();
    }

    private boolean canPrayer() {
        return API.getPrayer() != null && !getPrayer().isActive(API.getPrayer()) && getLocalPlayer().isInCombat() && isFightingBrother() && API.getPrayerPoints() > 1;
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
