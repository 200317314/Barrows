package core.nodes;

import core.API;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.path.impl.LocalPath;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.PathObstacle;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Locatable;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.util.Direction;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class CryptTraversalNode extends TaskNode {
    private Timer blackTimer = null, doorTimer = null;
    private Tile currTile = null, currTileDoor = null;

    @Override
    public boolean accept() {
        return !isFightingBrother() && !isFightingMob() && canCryptTraverse() && !API.canAttack() && !canEat() && API.barrowsCrypt.contains(getLocalPlayer()) || overideChestOpen() && API.barrowsCrypt.contains(getLocalPlayer());
    }

    @Override
    public int execute() {
        if (API.cryptChestArea.contains(getLocalPlayer())) {
            API.status = "Opening chest...";
            GameObject chest = getGameObjects().closest(o -> o != null && o.getName().equals("Chest"));
            if (chest != null) {
                if (chest.hasAction("Open")) {
                    if (chest.interact("Open")) {
                        sleepUntil(() -> !chest.hasAction("Open") || isFightingBrother() || getGameObjects().closest(o -> o != null && o.getName().equals("Chest")).hasAction("Search"), 2200 + API.sleep());
                    }
                } else if (chest.hasAction("Search") && !API.charInteractingIsBarrows() && API.getRewardsPotential() != 0 && API.getRewardsPotential() >= API.rewardsPotential && !isFightingBrother() || (chest.hasAction("Search") && overideChestOpen())) {
                    sleep(150, 450);
                    if (!isFightingBrother()) {
                        if (chest.interact("Search")) {
                            API.dharoksSlain = false;
                            API.guthansSlain = false;
                            API.karilsSlain = false;
                            API.toragsSlain = false;
                            API.veracsSlain = false;
                            API.ahrimsSlain = false;
                            API.cryptEntrance = -1;
                            API.chestsOpened += 1;
                            API.resetRewardsPotential();
                            API.resetBarrowsDeque();
                            sleepUntil(() -> API.getRewardsPotential() == 0, 2200 + API.sleep());
                        }
                    }
                }
            }
        } else {
            API.status = "Traversing crypt...";
            LocalPath<Tile> path = getWalking().getAStarPathFinder().calculate(getLocalPlayer().getTile(), API.cryptChestArea.getCenter());
            try {
                Locatable next = path.next(Calculations.random(5, 7));
                //PathObstacle obsFirst = path.getObstacleForTile(next.getTile());

                /*if (!getLocalPlayer().isMoving() && blackTimer != null && blackTimer.finished() && currTile != null && getLocalPlayer().getTile().equals(currTile)) {
                    if (obsFirst.getObstacle(this).isPresent()) {
                        getWalking().walkOnScreen(path.next());
                    } else {
                        if (rng == 1) {
                            getCamera().rotateToEntity(getGameObjects().closest(f -> Objects.nonNull(f) && f.hasAction("Open")));
                        } else {
                            API.adjustPitch();
                        }
                    }

                    blackTimer = null;
                    currTile = null;
                } else if (!getLocalPlayer().isMoving() && blackTimer == null) {
                    blackTimer = new Timer(4000 + API.sleep());
                    currTile = getLocalPlayer().getTile();
                }

                if (blackTimer != null && blackTimer.finished() && !getLocalPlayer().getTile().equals(currTile)) {
                    blackTimer = null;
                    currTile = null;
                }*/

                if (next != null) {
                    PathObstacle obs = path.getObstacleForTile(next.getTile());
                    if (obs != null) {
                        if (obs.getObstacle(this).isPresent()) {
                            GameObject object = obs.getObstacle(this).get();
                            if (object.interactForceRight((obs.getAction().isPresent() ? obs.getAction().get() : object.getActions()[0]))) {
                                //checkDoorStatus();
                                sleepUntil(
                                        () -> {
                                            Optional<Tile> endTile = obs.getEndTile();
                                            if (isFightingBrother() || canSolvePuzzle())
                                                return true;
                                            else if (!getLocalPlayer().isMoving())
                                                return true;
                                            return endTile.filter(tile -> tile.distance() <= 0).isPresent();
                                        }, Math.round(object.getTile().distance() * 2400 + API.sleep()));
                                sleepUntil(() -> canSolvePuzzle() || !obs.equals(path.getObstacleForTile(next.getTile())) || obs == null, 1460 + API.sleep());
                                //return canSolvePuzzle() ? 0 : Calculations.random(550, 750);
                            }
                        }
                    } else {
                        boolean onScreen = getMap().isTileOnScreen(next.getTile());
                        if ((onScreen || getCamera().rotateToTile(next.getTile())) && getWalking().walkOnScreen(next.getTile())) {
                            //MethodProvider.sleep(600);
                            Tile dest = getClient().getDestination();
                            MethodProvider.sleepUntil(() -> !getLocalPlayer().isMoving() || canSolvePuzzle()
                                            || isFightingBrother() || (dest == null || dest.distance() <= 4) || getWalking().shouldWalk(4),
                                    dest != null ? Math.round(dest.distance() * 1500 + API.sleep()) : Calculations.random(1200, 3000));
                        }
                    }
                }
            } catch (Exception e) {
                GameObject door = getGameObjects().closest(f -> Objects.nonNull(f) && f.hasAction("Open"));
                //checkDoorStatus();
                if (door != null && door.interact("Open")) {
                    return Calculations.random(75, 175);
                }
            }
            return (canSolvePuzzle()) ? 0 : Calculations.random(250, 550);
        }

        if (canSolvePuzzle()) {
            return 0;
        } else {
            return API.sleep();
        }
    }

    private void checkDoorStatus() {
        if (!getLocalPlayer().isMoving() && doorTimer != null && doorTimer.finished() && currTileDoor != null && getLocalPlayer().getTile().equals(currTileDoor)) {
            getCamera().rotateToEntity(getGameObjects().closest(f -> Objects.nonNull(f) && f.hasAction("Open")));

            doorTimer = null;
            currTileDoor = null;
        } else if (!getLocalPlayer().isMoving() && doorTimer == null) {
            doorTimer = new Timer(5000 + API.sleep());
            currTile = getLocalPlayer().getTile();
        }

        if (doorTimer != null && doorTimer.finished() && !getLocalPlayer().getTile().equals(currTile)) {
            doorTimer = null;
            currTile = null;
        }
    }

    private boolean inCrypt() {
        return API.northCrypt.contains(getLocalPlayer()) || API.eastCrypt.contains(getLocalPlayer()) || API.southCrypt.contains(getLocalPlayer()) || API.westCrypt.contains(getLocalPlayer());
    }

    private boolean canCryptTraverse() {
        return API.barrowsCrypt.contains(getLocalPlayer()) && !API.charInteractingIsBarrows() && !(API.charInteractingIsCryptMob() && API.getRewardsPotential() < API.rewardsPotential) && !(getLocalPlayer().isInCombat() && API.getRewardsPotential() < API.rewardsPotential)
                && !(getLocalPlayer().isInCombat() && API.charInteractingIsBarrows());
    }

    private boolean canEat() {
        return API.hasFood() && getCombat().getHealthPercent() <= 69;
    }

    private boolean overideChestOpen() {
        return !API.charInteractingIsBarrows() && !(API.charInteractingIsCryptMob() && API.getRewardsPotential() < API.rewardsPotential) && API.cryptChestArea.contains(getLocalPlayer()) && !isFightingBrother();
    }

    private boolean canSolvePuzzle() {
        Widget puzzleWidget = getWidgets().getWidget(25);
        return puzzleWidget != null && puzzleWidget.isVisible();
    }

    private boolean isFightingBrother() {
        Character withMe = getLocalPlayer().getCharacterInteractingWithMe();
        if (withMe == null)
            return false;

        NPC b = getNpcs().closest(f -> Objects.nonNull(f)
                && Objects.nonNull(f.getInteractingCharacter())
                && Objects.nonNull(f.getName())
                && getMap().canReach(f)
                && Arrays.asList(API.barrowsBrothersFull).contains(f.getName())
                && f.getInteractingCharacter().getName().equals(getLocalPlayer().getName()));

        if (b != null) {
            return true;
        }

        return false;
    }

    private boolean isFightingMob() {
        Character withMe = getLocalPlayer().getCharacterInteractingWithMe();
        if (withMe == null)
            return false;

        NPC b = getNpcs().closest(f -> Objects.nonNull(f)
                && Objects.nonNull(f.getInteractingCharacter())
                && Objects.nonNull(f.getName())
                && Arrays.asList(API.cryptMonsters).contains(f.getName())
                && getMap().canReach(f)
                && f.getInteractingCharacter().getName().equals(getLocalPlayer().getName()));

        if (b != null && API.getRewardsPotential() < API.rewardsPotential) {
            return true;
        }

        return false;
    }
}
