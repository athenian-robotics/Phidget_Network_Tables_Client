package com.arc852;
import com.phidget22.*;
import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.math.WPIMathJNI;
import edu.wpi.first.networktables.*;
import edu.wpi.first.util.CombinedRuntimeLoader;
import edu.wpi.first.util.WPIUtilJNI;

import java.io.IOException;

public class Main {
    private static final int HUB_PORT = 0;
    public static final int ENC_RESOLUTION = 96;
    public static final double ENC_TO_RADS = 1.0 / ENC_RESOLUTION * 2 * Math.PI;
    private static Encoder enc;

    public static void main(String[] args) throws IOException, PhidgetException {
        NetworkTablesJNI.Helper.setExtractOnStaticLoad(false);
        WPIUtilJNI.Helper.setExtractOnStaticLoad(false);
        WPIMathJNI.Helper.setExtractOnStaticLoad(false);
        CameraServerJNI.Helper.setExtractOnStaticLoad(false);

        enc = new Encoder();
        enc.setHubPort(HUB_PORT);

        CombinedRuntimeLoader.loadLibraries(Main.class, "wpiutiljni", "wpimathjni", "ntcorejni",
                "cscorejnicvstatic");
        new Main().run();    }

    private void run() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("phidget");

        enc.addPositionChangeListener(
                posEvent ->
                        table.putValue("dial", NetworkTableValue.makeDouble(posEvent.getPositionChange()*ENC_TO_RADS))
        );
    }
}