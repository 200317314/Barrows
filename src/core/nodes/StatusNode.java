package core.nodes;

import core.API;
import org.dreambot.api.data.GameState;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Arrays;
import java.util.Objects;

public class StatusNode extends TaskNode {
    private Timer timer = new Timer(timerTime);
    private static long timerTime = 30000; // 30 seconds

    @Override
    public int priority() {
        return 99;
    }

    @Override
    public boolean accept() {
        return canStatus();
    }

    @Override
    public int execute() {
        if (getClient().getGameState() == GameState.LOGGED_IN) {
            if (!isFightingBrother() && !isFightingMob()) {
                API.memDays = getClient().getMembershipLeft();
                if (API.user.equals("-")) {
                    API.user = getLocalPlayer().getName();
                }

                API.world = getClient().getCurrentWorld();
            }    else {
                timer = new Timer(timerTime);
            }
        }

        timer = new Timer(timerTime);
        API.sendWorkerStatus();
        return 0;
    }

    private boolean canStatus() {
        return timer != null && timer.finished();
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

        if (b != null && API.getRewardsPotential() < 78) {
            return true;
        }

        return false;
    }
}
