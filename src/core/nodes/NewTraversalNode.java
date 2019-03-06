package core.nodes;

import core.API;
import core.BarrowsBrother;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NewTraversalNode extends TaskNode {
    @Override
    public boolean accept() {
        return canTraversal();
    }

    @Override
    public int execute() {
        if (API.target != null && !inWrongCrypt()) {
            if (API.barrowsArea.contains(getLocalPlayer())) {
                if (API.target.getHillTopArea().contains(getLocalPlayer())) {
                    if (getTabs().isOpen(Tab.INVENTORY)) {
                        getInventory().get("Spade").interact("Dig");
                        API.status = "Digging...";
                        sleepUntil(() -> API.target.getCryptArea().contains(getLocalPlayer()), 2400 + API.sleep());
                    } else {
                        getTabs().open(Tab.INVENTORY);
                    }
                } else if (getWalking().shouldWalk(Calculations.random(4, 6))) {
                    Tile tile = API.target.getHillTopArea().getRandomTile();
                    if (tile.distance(getLocalPlayer()) <= 6 && !API.target.getHillTopArea().contains(getWalking().getDestination())) {
                        if (getWalking().walkOnScreen(tile)) {
                            API.status = "Traversing hill...";
                            sleepUntil(() -> API.target.getHillTopArea().contains(getLocalPlayer()), 2200 + API.sleep());
                        }
                    } else if (!API.target.getHillTopArea().contains(getWalking().getDestination())) {
                        API.status = "Traversing hill...";
                        if (getWalking().walk(tile)) {
                            sleep(100, 250);
                        }
                    }
                }
            } else if (API.target.getCryptArea().contains(getLocalPlayer())) {
                if (isCatacombBrother()) {
                    if (!getDialogues().inDialogue()) {
                        getGameObjects().closest(o -> o != null && o.getName().equals("Sarcophagus")).interact("Search");
                        API.status = "Searching...";
                        sleepUntil(() -> getDialogues().inDialogue(), 2200 + API.sleep());
                    } else {
                        API.status = "Entering crypt...";
                        if (getDialogues().canContinue()) {
                            getDialogues().clickContinue();
                            sleepUntil(() -> !getDialogues().canContinue(), 2200 + API.sleep());
                        } else {
                            Widget crypt = getWidgets().getWidget(219);
                            if (crypt != null) {
                                crypt.getChild(1).getChild(1).interact();
                                API.resetBarrowsDeque();
                                sleepUntil(() -> API.barrowsCrypt.contains(getLocalPlayer()), 2000 + API.sleep());
                            }
                        }
                    }
                } else {
                    if (getGameObjects().closest(o -> o != null && o.getName().equals("Sarcophagus")).interact("Search")) {
                        sleepUntil(() -> continueDialogOpen() || isFightingBrother(), 4200 + API.sleep());

                        if (continueDialogOpen()) {
                            API.catacombBrother = API.target;
                            API.barrowsBrotherDeque.addLast(API.target);
                        }
                        API.target = null;
                    }
                }
            }
        } else if (!inWrongCrypt()) {
            if (API.barrowsBrotherDeque.isEmpty()) {
                API.resetBarrowsDeque();
            } else {
                API.target = API.barrowsBrotherDeque.pop();
            }
        }

        if (inWrongCrypt() && !isFightingBrother()) {
            API.status = "Exiting crypt...";
            if (getGameObjects().closest(o -> o != null && o.getName().equals("Staircase")).interact("Climb-up")) {
                sleepUntil(() -> !inWrongCrypt(), 2400 + API.sleep());
            }
        }
        return API.sleep();
    }

    private boolean canTraversal() {
        return API.hasSupplies() && !isFightingBrother() && (API.barrowsArea.contains(getLocalPlayer()) || inCrypt());
    }

    private boolean isFightingBrother() {
        Character withMe = getLocalPlayer().getCharacterInteractingWithMe();
        if (withMe == null)
            return false;

        NPC b = getNpcs().closest(f -> Objects.nonNull(f)
                && Objects.nonNull(f.getInteractingCharacter())
                && Objects.nonNull(f.getName())
                && Arrays.asList(API.barrowsBrothersFull).contains(f.getName())
                && f.getInteractingCharacter().getName().equals(getLocalPlayer().getName()));

        if (b != null) {
            return true;
        }

        return false;
    }

    private boolean continueDialogOpen() {
        Widget cont = getWidgets().getWidget(229);
        return cont != null && cont.isVisible();
    }

    private boolean isCatacombBrother() {
        return API.catacombBrother != null && API.target != null && API.catacombBrother.equals(API.target);
    }

    private boolean inCrypt() {
        for (BarrowsBrother barrowsBrother : BarrowsBrother.values()) {
            if (barrowsBrother.getCryptArea().contains(getLocalPlayer())) {
                return true;
            }
        }
        return false;
    }

    private boolean inWrongCrypt() {
        for (BarrowsBrother barrowsBrother : BarrowsBrother.values()) {
            if (barrowsBrother.getCryptArea().contains(getLocalPlayer()) && !barrowsBrother.equals(API.target)) {
                return true;
            }
        }
        return false;
    }
}
