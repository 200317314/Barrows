package core;

import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.login.LoginUtility;
import org.dreambot.api.utilities.Timer;

import java.io.*;
import java.net.Socket;

public class MuleConnector {
    private static MethodProvider ctx = null;
    /*private static Socket muleServer;
    private static OutputStream output;
    private static PrintWriter writer;*/


    public MuleConnector(MethodProvider ctx) {
        this.ctx = ctx;
        /*try {
            muleServer = new Socket("localhost", 6969);

            output = muleServer.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static void stop() {
        /*try {
            muleServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /*public static void sendRequest(String username, int world) {
        writer.println(username + ":" + world);
    }*/

    public static void sendMuleRequest(String username, int world) {
        Socket muleServer = null;
        OutputStream output = null;
        PrintWriter writer = null;

        try {
            muleServer = new Socket("localhost", 6969);

            output = muleServer.getOutputStream();
            writer = new PrintWriter(output, true);

            writer.println("mule:" + username + ":" + world);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (muleServer != null) {
                    muleServer.close();
                }

                if (output != null) {
                    output.close();
                }

                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendWorkerStatus(String username, int world, int gold, int chests, boolean online, String status) {
        Socket muleServer = null;
        OutputStream output = null;
        PrintWriter writer = null;

        try {
            muleServer = new Socket("localhost", 6969);

            output = muleServer.getOutputStream();
            writer = new PrintWriter(output, true);

            writer.println("status:" + username  + ":" + world + ":" + gold + ":" + chests + ":" + online + ":" + status);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (muleServer != null) {
                    muleServer.close();
                }

                if (output != null) {
                    output.close();
                }

                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
