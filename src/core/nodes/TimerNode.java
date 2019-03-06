package core.nodes;

import core.ClientRender;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.Timer;

public class TimerNode extends TaskNode {
    private Timer timer = new Timer(timerTime);
    private static long timerTime = 60000; // 30 seconds
    @Override
    public int priority() {
        return 999999999;
    }

    @Override
    public boolean accept() {
        return timer != null && timer.finished();
    }

    @Override
    public int execute() {
        MethodProvider.log("Starting render...");
        ClientRender.setRender(this);
        timer = new Timer(timerTime);
        return 0;
    }
}
