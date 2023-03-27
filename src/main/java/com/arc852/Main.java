package com.arc852;
import com.phidget22.*;
import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.math.WPIMathJNI;
import edu.wpi.first.networktables.*;
import edu.wpi.first.util.CombinedRuntimeLoader;
import edu.wpi.first.util.WPIUtilJNI;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    private static DoublePublisher dpl;
    private static final int HUB_PORT = 0;
    public static final int ENC_RESOLUTION = 96;
    public static final double ENC_TO_RADS = 1.0 / ENC_RESOLUTION * 2 * Math.PI;

    public static void main(String[] args) throws IOException, PhidgetException, InterruptedException {
      NetworkTablesJNI.Helper.setExtractOnStaticLoad(false);
      WPIUtilJNI.Helper.setExtractOnStaticLoad(false);
      WPIMathJNI.Helper.setExtractOnStaticLoad(false);
      CameraServerJNI.Helper.setExtractOnStaticLoad(false);

      Encoder enc = new Encoder();
      enc.setHubPort(HUB_PORT);

      CombinedRuntimeLoader.loadLibraries(Main.class, "wpiutiljni", "wpimathjni", "ntcorejni",
          "cscorejnicvstatic");


      NetworkTableInstance inst = NetworkTableInstance.getDefault();
      inst.startClient4("phidget with ur balls");
      inst.setServer("localhost");
      inst.startDSClient();
      var topic = inst.getDoubleTopic("phidget/dial");
      topic.setPersistent(true);
      dpl = topic.publish();
      dpl.setDefault(0.0);

      AtomicReference<Double> pos = new AtomicReference<>((double) 0);

      enc.addPositionChangeListener(posEvent -> {
        pos.updateAndGet(v -> v + posEvent.getPositionChange() * ENC_TO_RADS);
        dpl.set(pos.get());
      });

      enc.open();

      Thread.currentThread().join();
    }

}