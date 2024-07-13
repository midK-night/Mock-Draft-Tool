package org.example;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class homePage extends JFrame implements ActionListener {
    JButton runDraft;
    JButton updateADP;
    JButton savedDrafts;

    public static void main(String[] args) {
        homePage frame = new homePage();
        frame.homePageBuilder();
    }

    public void homePageBuilder() {
        JFrame frame = new JFrame();

        frame.setSize(1000, 600);
        frame.setTitle("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        runDraft = new mainButton("Run Draft", 150, 200);
        updateADP = new mainButton("Update ADP", 400, 200);
        savedDrafts = new mainButton("Saved Drafts", 650, 200);

        runDraft.addActionListener(this);
        updateADP.addActionListener(this);
        savedDrafts.addActionListener(this);

        frame.add(runDraft);
        frame.add(updateADP);
        frame.add(savedDrafts);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Scanner console = new Scanner(System.in);
        //todo figure out a way to not? close scanner cuz if closed then system.in gets closed as well
        if (e.getSource() == runDraft) {
            try {
                DraftRunner.main(console);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == updateADP) {
            try {
                ADP_Updater.main();
                System.out.println("Updated ADP!\n");
            } catch (IOException | URISyntaxException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == savedDrafts) {
            try {
                TeamReader.main(console);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
