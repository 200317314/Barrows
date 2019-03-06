package core.nodes;

import core.API;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Arrays;
import java.util.Objects;

public class AttackNode extends TaskNode {

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public boolean accept() {
        //System.out.println("canAttack: " + (canAttack() && !getLocalPlayer().isInCombat()));
        return canAttack() && !canPrayer() && !canPotion();
    }

    @Override
    public int execute() {
        if (getLocalPlayer().getCharacterInteractingWithMe() != null && !canPrayer() && !canPotion() || canReAttack()) {
            API.status = "Attacking mob...";
            getLocalPlayer().getCharacterInteractingWithMe().interact("Attack");
            sleepUntil(() -> getLocalPlayer().isInCombat(), 2200 + API.sleep());
        }
        return API.sleep();
    }

    /*private boolean canAttack() {
        return API.charInteractingIsBarrows() || (API.charInteractingIsCryptMob() && API.getRewardsPotential() < 78);
    }*/

    private boolean canAttack() {
        NPC monster = getNpcs().closest(n -> n != null && !n.isInCombat() && getMap().canReach(n) && getLocalPlayer().getCharacterInteractingWithMe() == n
                && (Arrays.asList(API.barrowsBrothersFull).contains(n.getName()) || (Arrays.asList(API.cryptMonsters).contains(n.getName())) && API.getRewardsPotential() < API.rewardsPotential));
        return monster != null && !getLocalPlayer().isInCombat() && getLocalPlayer().getCharacterInteractingWithMe() == null && getMap().canReach(monster) || canReAttack();
    }

    private boolean canReAttack() {
        return !getLocalPlayer().isInCombat() && getLocalPlayer().getCharacterInteractingWithMe() != null && getMap().canReach(getLocalPlayer().getCharacterInteractingWithMe())
                && isFightingBrotherAll()
                || !getLocalPlayer().isInCombat() && getLocalPlayer().getCharacterInteractingWithMe() != null && getMap().canReach(getLocalPlayer().getCharacterInteractingWithMe())
                && isFightingMob();
    }

    private boolean canPrayer() {
        return API.getPrayer() != null && !getPrayer().isActive(API.getPrayer()) && getLocalPlayer().isInCombat() && getLocalPlayer().getCharacterInteractingWithMe() != null && API.charInteractingIsBarrows();
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

    private boolean isFightingBrotherAll() {
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
