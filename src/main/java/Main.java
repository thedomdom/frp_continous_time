import nz.sodium.Cell;
import nz.sodium.StreamSink;
import nz.sodium.Unit;
import nz.sodium.time.SecondsTimerSystem;
import nz.sodium.time.TimerSystem;
import swidgets.SLabel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        setUpLogic();
        loop();
    }

    private static void setUpLogic() {
        JFrame frame = new JFrame("Continuous Time 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        TimerSystem timerSystem = new SecondsTimerSystem();
        Cell<Double> time = timerSystem.time;

        //v(t) = g*t
        Cell<Double> velocity = time.map(seconds -> 9.81 * seconds);
        //s(t) = 1/2 * g * t^2
        Cell<Double> distance = time.map(seconds -> 0.5 * 9.81 * seconds * seconds);

        SLabel lblSeconds = new SLabel(time.map(value -> Double.toString(value) + " s"));
        SLabel lblSpeed = new SLabel(velocity.map(value -> Double.toString(value) + " m/s"));
        SLabel lblDistance = new SLabel(distance.map(value -> Double.toString(value) + " m"));

        frame.add(lblSeconds);
        frame.add(lblSpeed);
        frame.add(lblDistance);

        frame.setSize(400, 160);
        frame.setVisible(true);
    }

    private static void loop() throws InterruptedException {
        long systemSampleRate = 1000L;
        StreamSink<Unit> sMain = new StreamSink<>();
        while(true) {
            //jede Transaktion aktualisiert TimerSystem.time (Sodium spezifisch) -> send löst Transaktion aus
            sMain.send(Unit.UNIT);
            Thread.sleep(systemSampleRate);
        }
    }
}
