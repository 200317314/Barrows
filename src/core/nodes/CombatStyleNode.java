package core.nodes;

import core.API;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Arrays;
import java.util.Objects;

public class CombatStyleNode extends TaskNode {
    @Override
    public boolean accept() {
        return canChangeStyle();
    }

    @Override
    public int execute() {
        Character barrowsBrother = getLocalPlayer().getCharacterInteractingWithMe();
        Widget styles = getWidgets().getWidget(593);
        if (getTabs().isOpen(Tab.COMBAT)) {
            API.status = "Changing styles...";
            if (barrowsBrother != null && barrowsBrother.getName().equals("Ahrim the Blighted") && getPlayerSettings().getConfig(43) != 1) {
                if (styles != null) {
                    styles.getChild(7).interact();
                    sleepUntil(() -> !canChangeStyle(), 2200 + API.sleep());
                }
            } else if (barrowsBrother != null && !barrowsBrother.getName().equals("Ahrim the Blighted") && getPlayerSettings().getConfig(43) != 2) {
                styles.getChild(11).interact();
                sleepUntil(() -> !canChangeStyle(), 2200 + API.sleep());
            }
        } else {
            getTabs().open(Tab.COMBAT);
        }
        return API.sleep();
    }

    private boolean canChangeStyle() {
        /*Character barrowsBrother = getLocalPlayer().getCharacterInteractingWithMe();
        if (barrowsBrother != null) {
            if (barrowsBrother.getName().equals("Ahrim the Blighted") && getPlayerSettings().getConfig(43) != 1) {
                return true;
            } else if (barrowsBrother != null && !barrowsBrother.getName().equals("Ahrim the Blighted") && getPlayerSettings().getConfig(43) != 2) {
                return true;
            }
        }*/

        NPC barrowsBro = getNpcs().closest(n -> Objects.nonNull(n) && getMap().canReach(n) && Arrays.asList(API.barrowsBrothersFull).contains(n.getName()) && n.getInteractingCharacter().getName().equals(getLocalPlayer().getName()));
        if (barrowsBro != null) {
            if (barrowsBro.getName().equals("Ahrim the Blighted") && getPlayerSettings().getConfig(43) != 1) {
                return true;
            } else if (barrowsBro != null && !barrowsBro.getName().equals("Ahrim the Blighted") && getPlayerSettings().getConfig(43) != 2) {
                return true;
            }
        }
        return false;
    }
}
