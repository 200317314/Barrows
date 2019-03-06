package core.nodes;

import core.API;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class PuzzleNode extends TaskNode {
    private Widget puzzleWidget;
    @Override
    public int priority() {
        return 99;
    }

    @Override
    public boolean accept() {
        return canSolvePuzzle();
    }

    @Override
    public int execute() {
        API.status = "Solving puzzle...";
        if (puzzleWidget != null && puzzleWidget.isVisible()) {
            if (correctPuzzleWC() != null) {
                correctPuzzleWC().interact();
            }
        }
        return 0;
    }

    private boolean canSolvePuzzle() {
        puzzleWidget = getWidgets().getWidget(25);
        return puzzleWidget != null && puzzleWidget.isVisible();
    }

    private WidgetChild correctPuzzleWC() {
        if (puzzleWidget != null) {
            for (WidgetChild wc : puzzleWidget.getChildren()) {
                if (wc.getDisabledMediaType() == 6731 || wc.getDisabledMediaType() == 6725 || wc.getDisabledMediaType() == 6713 || wc.getDisabledMediaType() == 6719) {
                    return wc;
                }
            }
        }
        return null;
    }
}
