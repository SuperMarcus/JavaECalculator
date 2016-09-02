package com.marcuszhou.demo.java;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class DemoJ extends JFrame {
    private JPanel designerPane;
    private JTextArea Output;
    private JLabel digitsLabel;
    private JButton startButton;
    private JProgressBar progressBar1;
    private JLabel noticeLabel;
    private JLabel precision;
    private Thread thread;
    volatile private boolean running = false;

    private MathContext context;
    private BigDecimal a = BigDecimal.ZERO, b = BigDecimal.ZERO, c = BigDecimal.ONE;

    public static void main(String args[]){
        new DemoJ(args);
    }

    public DemoJ(String[] args){
        int precision = args.length > 0 ? Integer.parseInt(args[0]) : 10000;

        System.out.println("[*] Math-E Calculate by Marcus Z.(https://github.com/SuperMarcus)");
        System.out.println("[*] For this time, "+precision+" digits of 'e' will be calculated. Change it in the argument passed to the program.");

        context = new MathContext(precision, RoundingMode.HALF_EVEN);

        thread = new Thread(() -> {
            System.out.println("[*] Compute thread running...");
            while(true){
                while(running){
                    a = a.add(BigDecimal.ONE.divide(c = c.multiply(b = b.add(BigDecimal.ONE, context), context), context), context);
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        startButton.addActionListener(e -> {
            running = !running;
            System.out.println("[!] "+(running ? "Compute will stop immediately" : "Compute will starts immediately"));
            startButton.setText(running ? "Stop" : "Continue");
        });

        noticeLabel.setText("Press Start");

        new Thread(() -> {
            while(true){
                try {
                    Output.setText(a.add(BigDecimal.ONE).toString());
                    digitsLabel.setText(b.toString());
                    if(b.compareTo(BigDecimal.TEN) == -1){
                        progressBar1.setIndeterminate(false);
                        progressBar1.setValue(b.intValue());
                        if(running){noticeLabel.setText("Calculation might be tough at first, will take about one minute...");}
                    }else{
                        progressBar1.setValue(10);
                        noticeLabel.setText(running ? "Calculating..." : "Press Continue");
                        progressBar1.setIndeterminate(running);
                    }
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try{
            com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
        }catch (Exception ignored){}

        progressBar1.setMinimum(0);
        progressBar1.setMaximum(5);

        this.precision.setText(Integer.toString(precision));

        Output.setLineWrap(true);
        Output.setEditable(false);
        add(designerPane);
        setTitle("'e' Calculator");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 90);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
