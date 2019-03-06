package core.nodes;

import org.dreambot.api.script.TaskNode;

public class ItemSelectNode extends TaskNode {
    @Override
    public int priority() {
        return 99999999;
    }

    @Override
    public boolean accept() {
        return canDeselect();
    }

    @Override
    public int execute() {
        getInventory().deselect();
        return 0;
    }

    private boolean canDeselect() {
        return getInventory().isItemSelected();
    }
}
