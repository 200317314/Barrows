package core.nodes;

import core.API;
import core.BarrowsBrother;
import core.ClientRender;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Arrays;
import java.util.Objects;

public class TraversalNode extends TaskNode {
    @Override
    public boolean accept() {
        return canTraversal() && (API.inTomb() || API.barrowsArea.contains(getLocalPlayer()) ||  (API.enterCrypt() && (API.inTomb() || API.barrowsArea.contains(getLocalPlayer()))));
    }

    @Override
    public int execute() {
        API.status = "Traversing...";
        if (API.barrowsArea.contains(getLocalPlayer())) {
                //Dharoks
                if (!API.dharoksSlain && !API.dharoksTomb.contains(getLocalPlayer())) {
                    if (API.dharoksHill.contains(getLocalPlayer())) {
                        if (getTabs().isOpen(Tab.INVENTORY)) {
                            getInventory().get("Spade").interact("Dig");
                            sleep(2000 + API.sleep());
                        } else {
                            getTabs().open(Tab.INVENTORY);
                        }
                    } else if (shouldWalk(5) && !API.dharoksHill.contains(getLocalPlayer())) {
                        Tile tile = API.dharoksHill.getRandomTile();
                        if (tile.distance(getLocalPlayer()) <= 5) {
                            getWalking().walkOnScreen(tile);
                            sleepUntil(() -> API.dharoksHill.contains(getLocalPlayer()), 2200 + API.sleep());
                        } else {
                            getWalking().walk(tile);
                        }
                    }
                }

                //Ahrims
                if (!API.ahrimsSlain && !API.ahrimsTomb.contains(getLocalPlayer()) && API.dharoksSlain) {
                    if (API.ahrimsHill.contains(getLocalPlayer())) {
                        if (getTabs().isOpen(Tab.INVENTORY)) {
                            getInventory().get("Spade").interact("Dig");
                            sleep(2000 + API.sleep());
                        } else {
                            getTabs().open(Tab.INVENTORY);
                        }
                    } else if (shouldWalk(5)&& !API.ahrimsHill.contains(getLocalPlayer())) {
                        Tile tile = API.ahrimsHill.getRandomTile();
                        if (tile.distance(getLocalPlayer()) <= 5) {
                            getWalking().walkOnScreen(tile);
                            sleepUntil(() -> API.ahrimsHill.contains(getLocalPlayer()), 2200 + API.sleep());
                        } else {
                            getWalking().walk(tile);
                        }
                    }
                }

                //Karils
                if (!API.karilsSlain && !API.karilsTomb.contains(getLocalPlayer()) && API.dharoksSlain && API.ahrimsSlain) {
                    if (API.karilsHill.contains(getLocalPlayer())) {
                        if (getTabs().isOpen(Tab.INVENTORY)) {
                            getInventory().get("Spade").interact("Dig");
                            sleep(2000 + API.sleep());
                        } else {
                            getTabs().open(Tab.INVENTORY);
                        }
                    } else if (shouldWalk(5) && !API.karilsHill.contains(getLocalPlayer())) {
                        Tile tile = API.karilsHill.getRandomTile();
                        if (tile.distance(getLocalPlayer()) <= 5) {
                            getWalking().walkOnScreen(tile);
                            sleepUntil(() -> API.karilsHill.contains(getLocalPlayer()), 2200 + API.sleep());
                        } else {
                            getWalking().walk(tile);
                        }
                    }
                }

                //Guthans
                if (!API.guthansSlain && !API.guthansTomb.contains(getLocalPlayer()) && API.dharoksSlain && API.ahrimsSlain && API.karilsSlain) {
                    if (API.guthansHill.contains(getLocalPlayer())) {
                        if (getTabs().isOpen(Tab.INVENTORY)) {
                            getInventory().get("Spade").interact("Dig");
                            sleep(2000 + API.sleep());
                        } else {
                            getTabs().open(Tab.INVENTORY);
                        }
                    } else if (shouldWalk(5) && !API.guthansHill.contains(getLocalPlayer())) {
                        Tile tile = API.guthansHill.getRandomTile();
                        if (tile.distance(getLocalPlayer()) <= 5) {
                            getWalking().walkOnScreen(tile);
                            sleepUntil(() -> API.guthansHill.contains(getLocalPlayer()), 2200 + API.sleep());
                        } else {
                            getWalking().walk(tile);
                        }
                    }
                }

                //Torags
                if (!API.toragsSlain && !API.toragsTomb.contains(getLocalPlayer()) && API.dharoksSlain && API.ahrimsSlain && API.karilsSlain && API.guthansSlain) {
                    if (API.toragsHill.contains(getLocalPlayer())) {
                        if (getTabs().isOpen(Tab.INVENTORY)) {
                            getInventory().get("Spade").interact("Dig");
                            sleep(2000 + API.sleep());
                        } else {
                            getTabs().open(Tab.INVENTORY);
                        }
                    } else if (shouldWalk(5) && !API.toragsHill.contains(getLocalPlayer())) {
                        Tile tile = API.toragsHill.getRandomTile();
                        if (tile.distance(getLocalPlayer()) <= 5) {
                            getWalking().walkOnScreen(tile);
                            sleepUntil(() -> API.toragsHill.contains(getLocalPlayer()), 2200 + API.sleep());
                        } else {
                            getWalking().walk(tile);
                        }
                    }
                }

                //Veracs
                if (!API.veracsSlain && !API.veracsTomb.contains(getLocalPlayer()) && API.dharoksSlain && API.ahrimsSlain && API.karilsSlain && API.guthansSlain && API.toragsSlain) {
                    if (API.veracsHill.contains(getLocalPlayer())) {
                        if (getTabs().isOpen(Tab.INVENTORY)) {
                            getInventory().get("Spade").interact("Dig");
                            sleep(2000 + API.sleep());
                        } else {
                            getTabs().open(Tab.INVENTORY);
                        }
                    } else if (shouldWalk(5) && !API.veracsHill.contains(getLocalPlayer())) {
                        Tile tile = API.veracsHill.getCenter().getRandomizedTile(2);
                        if (tile.distance(getLocalPlayer()) <= 5) {
                            getWalking().walkOnScreen(tile);
                            sleepUntil(() -> API.veracsHill.contains(getLocalPlayer()), 2200 + API.sleep());
                        } else {
                            getWalking().walk(tile);
                        }
                    }
                }
        }

        if (API.enterCrypt() && API.cryptEntrance != -1) {
            if (inTombSlain() && !API.cryptEntranceTomb(API.cryptEntrance).contains(getLocalPlayer()) && getLocalPlayer().getCharacterInteractingWithMe() == null && !API.canAttack()) {
                GameObject stairs = getGameObjects().closest(o -> o != null && o.getName().equals("Staircase"));
                stairs.interact("Climb-up");
                sleepUntil(() -> !inTombSlain(), 2200 + API.sleep());
            } else {
                if (API.cryptEntranceTomb(API.cryptEntrance).contains(getLocalPlayer())) {
                    if (!getDialogues().inDialogue()) {
                        getGameObjects().closest(o -> o != null && o.getName().equals("Sarcophagus")).interact("Search");
                        sleepUntil(() -> getDialogues().inDialogue(), 2200 + API.sleep());
                    } else {
                        if (getDialogues().canContinue()) {
                            getDialogues().clickContinue();
                            sleepUntil(() -> !getDialogues().canContinue(), 2200 + API.sleep());
                        } else {
                            Widget crypt = getWidgets().getWidget(219);
                            if (crypt != null) {
                                crypt.getChild(0).getChild(1).interact();
                                sleepUntil(() -> API.barrowsCrypt.contains(getLocalPlayer()), 2000 + API.sleep());
                            }
                        }
                    }
                } else {
                    if (API.cryptEntranceHill(API.cryptEntrance).contains(getLocalPlayer())) {
                        if (getTabs().isOpen(Tab.INVENTORY)) {
                            getInventory().get("Spade").interact("Dig");
                            sleep(2000 + API.sleep());
                        } else {
                            getTabs().open(Tab.INVENTORY);
                        }
                    } else if (shouldWalk(5) && !API.cryptEntranceHill(API.cryptEntrance).contains(getLocalPlayer())) {
                        Tile tile = API.cryptEntranceHill(API.cryptEntrance).getRandomTile();
                        if (tile.distance(getLocalPlayer()) <= 6) {
                            getWalking().walkOnScreen(tile);
                            sleepUntil(() -> API.cryptEntranceHill(API.cryptEntrance).contains(getLocalPlayer()), 2200 + API.sleep());
                        } else {
                            getWalking().walk(tile);
                        }
                    }
                }
            }
        }

        if (inTombSlain() && !getLocalPlayer().isInCombat() && !API.enterCrypt() && !API.canAttack() && !isFightingBrother()) {
            GameObject stairs = getGameObjects().closest(o -> o != null && o.getName().equals("Staircase"));
            if (!API.charInteractingIsBarrows() && !API.canAttack() && getLocalPlayer().getCharacterInteractingWithMe() == null && !getLocalPlayer().isInCombat()) {
                stairs.interact("Climb-up");
                sleepUntil(() -> !inTombSlain(), 2200 + API.sleep());
            }
        }

        if (inTomb()) {
            if (getGameObjects().closest(o -> o != null && o.getName().equals("Sarcophagus")).interact("Search")) {
                API.updateBarrowsCounter();
                sleepUntil(() -> isFightingBrother() && getLocalPlayer().isInCombat() && API.canAttack() || getDialogues().canContinue() || isFightingBrother(), 6200 + API.sleep());
                sleep(API.sleep());
            }
            /*getGameObjects().closest(o -> o != null && o.getName().equals("Sarcophagus")).interact("Search");
            API.updateBarrowsCounter();
            sleepUntil(() -> getLocalPlayer().getCharacterInteractingWithMe() != null && getLocalPlayer().isInCombat() && API.canAttack(), 2200 + API.sleep());
            sleep(API.sleep());*/
        }

        if (getDialogues().canContinue()) {
            setCryptEntrance();
            API.updateBarrowsCounter();
            sleepUntil(() -> API.cryptEntrance != -1, 2200 + API.sleep());
        }
        return API.sleep();
    }

    private boolean canTraversal() {
        return API.hasSupplies() && !API.charInteractingIsBarrows();
    }

    private boolean inTombSlain() {
        if (API.dharoksTomb.contains(getLocalPlayer()) && API.dharoksSlain) {
            return true;
        }

        if (API.guthansTomb.contains(getLocalPlayer()) && API.guthansSlain) {
            return true;
        }

        if (API.karilsTomb.contains(getLocalPlayer()) && API.karilsSlain) {
            return true;
        }

        if (API.toragsTomb.contains(getLocalPlayer()) && API.toragsSlain) {
            return true;
        }

        if (API.veracsTomb.contains(getLocalPlayer()) && API.veracsSlain) {
            return true;
        }

        if (API.ahrimsTomb.contains(getLocalPlayer()) && API.ahrimsSlain) {
            return true;
        }
        return false;
    }

    private boolean inTomb() {
        if (API.dharoksTomb.contains(getLocalPlayer()) && !API.dharoksSlain) {
            return true;
        }

        if (API.guthansTomb.contains(getLocalPlayer()) && !API.guthansSlain) {
            return true;
        }

        if (API.karilsTomb.contains(getLocalPlayer()) && !API.karilsSlain) {
            return true;
        }

        if (API.toragsTomb.contains(getLocalPlayer()) && !API.toragsSlain) {
            return true;
        }

        if (API.veracsTomb.contains(getLocalPlayer()) && !API.veracsSlain) {
            return true;
        }

        if (API.ahrimsTomb.contains(getLocalPlayer()) && !API.ahrimsSlain) {
            return true;
        }
        return false;
    }

    private void setCryptEntrance() {
        Widget enterCrypt = getWidgets().getWidget(229);

        if (enterCrypt != null && enterCrypt.getChild(1) != null && enterCrypt.isVisible()) {
            if (enterCrypt.getChild(1).getText().equals("You've found a hidden tunnel, do you want to enter?")) {
                if (API.dharoksTomb.contains(getLocalPlayer())) {
                    API.cryptEntrance = 1;
                }

                if (API.guthansTomb.contains(getLocalPlayer())) {
                    API.cryptEntrance = 2;
                }

                if (API.karilsTomb.contains(getLocalPlayer())) {
                    API.cryptEntrance = 3;
                }

                if (API.toragsTomb.contains(getLocalPlayer())) {
                    API.cryptEntrance = 4;
                }

                if (API.veracsTomb.contains(getLocalPlayer())) {
                    API.cryptEntrance = 5;
                }

                if (API.ahrimsTomb.contains(getLocalPlayer())) {
                    API.cryptEntrance = 6;
                }
            }
        }
    }

    private boolean shouldWalk(int dist) {
        return (getWalking().getDestination() != null) ? getWalking().shouldWalk(dist) && getWalking().getDestination().distance() > dist : getWalking().shouldWalk(dist);
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
}
