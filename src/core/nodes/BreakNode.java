package core.nodes;

import core.API;
import core.MuleConnector;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.Timer;

public class BreakNode extends TaskNode {
    @Override
    public int priority() {
        return 999999;
    }

    @Override
    public boolean accept() {
        return canBreak() && !API.muling || canLogin() && !API.muling || canSleep() && !API.muling || canDebug() && !API.muling || API.deathSleep;
    }

    @Override
    public int execute() {
        API.status = "Breaking...";
        API.resetGeBuySell();
        if (API.deathSleep && getClient().getGameState() == GameState.LOGGED_IN) {
            if (!getGrandExchange().isOpen()) {
                API.setLoggerDisabled();
                getTabs().logout();
                API.shouldBreak = null;
                API.logoutTimer = new Timer(API.getDeathTimeTimer());
                API.deathSleep = false;
                MuleConnector.sendWorkerStatus(API.user, API.world, API.coinsAmt, API.chestsOpened, false, "Death sleep...");
            } else {
                getGrandExchange().close();
            }
        }

        if (canBreak() && !canSleep()) {
            sleep(10000 + API.sleep());
            worldHop();
            API.setLoggerDisabled();
            getTabs().logout();
            API.shouldBreak = null;
            API.logoutTimer = new Timer(API.getBreakTimeTimer());
            MuleConnector.sendWorkerStatus(API.user, API.world, API.coinsAmt, API.chestsOpened, false, "Breaking...");
        }

        if (canSleep()) {
            sleep(10000 + API.sleep());
            worldHop();
            API.setLoggerDisabled();
            getTabs().logout();
            API.shouldSleep = null;
            API.logoutTimer = new Timer(API.getSleepTimeTimer());
            MuleConnector.sendWorkerStatus(API.user, API.world, API.coinsAmt, API.chestsOpened, false, "Sleeping...");
        }

        if (canDebug()) {
            API.setLoggerDisabled();
            getTabs().logout();
        }

        if (canLogin()) {
            API.setLoggerEnabled();
            API.logoutTimer = null;

            if (API.shouldBreak == null) {
                API.shouldBreak = new Timer(API.getRunTimeTimer());
            }

            if (API.shouldSleep == null) {
                API.shouldSleep = new Timer(API.getShouldSleepTimer());
                API.shouldBreak = new Timer(API.getRunTimeTimer());
            }
        }
        return 0;
    }

    private void worldHop() {
        World world = getWorlds().getRandomWorld(f -> f.isMembers() && !f.isDeadmanMode() && !f.isPVP() && f.getMinimumLevel() == 0
                && f.getID() != 318 && f.getID() != getClient().getCurrentWorld() && !f.isTournamentWorld());
        getWorldHopper().hopWorld(world);
        sleepUntil(() -> getClient().getGameState() != GameState.HOPPING && getClient().getGameState() != GameState.LOADING && getClient().getCurrentWorld() == world.getID(), 9000);
        sleep(650,2000);
    }

    private boolean canBreak() {
        return API.clanWarsArea.contains(getLocalPlayer()) && API.shouldBreak != null && API.shouldBreak.finished() && getClient().getGameState() == GameState.LOGGED_IN && API.logoutTimer == null;
    }

    private boolean canSleep() {
        return API.clanWarsArea.contains(getLocalPlayer()) && API.shouldSleep != null && API.shouldSleep.finished() && getClient().getGameState() == GameState.LOGGED_IN && API.logoutTimer == null;
    }

    private boolean canDebug() {
        return API.logoutTimer != null && !API.logoutTimer.finished() && getClient().getGameState() == GameState.LOGGED_IN;
    }

    private boolean canLogin() {
        return getClient().getGameState() != GameState.LOGGED_IN && API.logoutTimer != null && API.logoutTimer.finished();
    }
}
