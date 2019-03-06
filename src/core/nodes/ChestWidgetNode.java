package core.nodes;

import core.API;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.TaskNode;

public class ChestWidgetNode extends TaskNode {
    @Override
    public int priority() {
        return 5;
    }

    @Override
    public boolean accept() {
        return canCloseWidget();
    }

    @Override
    public int execute() {
        Widget chestWidget = getWidgets().getWidget(155);

        if (chestWidget != null) {
            chestWidget.getChild(1).getChild(11).interact();
            sleepUntil(() -> chestWidget == null, 1200 + API.sleep());
        }
        return API.sleep();
    }

    private boolean canCloseWidget() {
        Widget chestWidget = getWidgets().getWidget(155);
        return chestWidget != null && chestWidget.isVisible();
    }
}
