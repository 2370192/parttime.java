// Main.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Main extends Object{
    private JFrame frame;
    private JLabel earningsLabel;
    private JLabel workTimeLabel;
    private JButton startButton;
    private JButton stopButton;
    private JTextField hourlyWageField;
    private PartTime partTime;

    public Main() {
        frame = new JFrame("Part Time Earnings Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new GridLayout(5, 10));

        earningsLabel = new JLabel("今日の稼ぎ: 0 円");
        workTimeLabel = new JLabel("今日の総労働時間: 0 秒");
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        hourlyWageField = new JTextField("1064");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                startEarningsTracker();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                stopEarningsTracker();
            }
        });

        frame.add(new JLabel("時給:"));
        frame.add(hourlyWageField);
        frame.add(earningsLabel);
        frame.add(workTimeLabel);
        frame.add(startButton);
        frame.add(stopButton);

        partTime = new PartTime();
    }

    private void startEarningsTracker() {
        double hourlyWage = Double.parseDouble(hourlyWageField.getText());
        partTime.setHourlyWage(hourlyWage);
        partTime.startEarningsTimer(new PartTimeListener() {
            @Override
            public void onEarningsUpdated(double earnings, double workTime) {
                updateLabels(earnings, workTime);
            }
        });
    }

    private void stopEarningsTracker() {
        double earnings = partTime.stopEarningsTimer();
        DecimalFormat earningsDf = new DecimalFormat("#.#");
        earningsDf.setRoundingMode(RoundingMode.DOWN); // 切り捨てに変更
        String roundedEarnings = earningsDf.format(earnings);
        earningsLabel.setText("今日の稼ぎ: " + roundedEarnings + " 円");
    }    

    private void updateLabels(double earnings, double workTime) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DecimalFormat workTimeDf = new DecimalFormat("#.#");
                workTimeDf.setRoundingMode(RoundingMode.DOWN); // 切り捨てに変更
                String roundedWorkTime = workTimeDf.format(workTime);
                
                earningsLabel.setText("今日の稼ぎ: " + earnings + " 円");
                workTimeLabel.setText("今日の総労働時間: " + roundedWorkTime + " 秒");
            }
        });
    }
    

    public void display() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main main = new Main();
                main.display();
            }
        });
    }
}
