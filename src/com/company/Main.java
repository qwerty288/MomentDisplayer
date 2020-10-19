package com.company;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/*
 This program is used to display a random image in a file when a button is clicked. Includes an intro screen, containing
 a message contained within multiple text panes and a button that allows the user to go to the main screen.
 */
public class Main {
    final static File file = new File("moments");
    static File[] moments = file.listFiles();
    static int momentNumber = -1;

    static int maxImageHeight;
    static int maxImageWidth;

    static int pityCounter = 0;


    public static void main(String[] args) {
        // Create basic GUI objects for the intro screen.
        JFrame frame = new JFrame();
        JPanel introPanel = new JPanel();

        // Create the text for the top line of the intro screen.
        JTextPane toText = new JTextPane();
        toText.setText("To the user,");
        StyledDocument toTextStyle = toText.getStyledDocument();
        SimpleAttributeSet toTextAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(toTextAlign, StyleConstants.ALIGN_LEFT);
        toTextStyle.setParagraphAttributes(0, toTextStyle.getLength(), toTextAlign, false);
        toText.setFont(toText.getFont().deriveFont(72f));

        // Create the text for the middle lines of the intro screen.
        JTextPane messageText = new JTextPane();
        messageText.setText("This is the intro message shown before the main program is loaded. Click the start " +
                "button below to start the main program");
        StyledDocument messageTextStyle = messageText.getStyledDocument();
        SimpleAttributeSet messageTextAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(messageTextAlign, StyleConstants.ALIGN_CENTER);
        messageTextStyle.setParagraphAttributes(0, messageTextStyle.getLength(), messageTextAlign, false);
        messageText.setFont(messageText.getFont().deriveFont(72f));

        // Create the text for the bottom line of the intro screen.
        JTextPane fromText = new JTextPane();
        fromText.setText("From Ahmed");
        StyledDocument fromTextStyle = fromText.getStyledDocument();
        SimpleAttributeSet fromTextAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(fromTextAlign, StyleConstants.ALIGN_RIGHT);
        fromTextStyle.setParagraphAttributes(0, fromTextStyle.getLength(), fromTextAlign, false);
        fromText.setFont(fromText.getFont().deriveFont(72f));

        // Create the button that will display the main screen when clicked.
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete the intro screen.
                frame.remove(introPanel);

                // Create basic GUI objects for the main screen.
                JPanel mainPanel = new JPanel();
                JLabel momentLabel = new JLabel();

                // Create the button that will display a new moment when clicked.
                JButton getMomentButton = new JButton("Get random moment");
                getMomentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        momentLabel.setIcon(getRandomMoment());
                        // JOptionPane.showMessageDialog(frame,"hi");

                        int randomInteger = (int) (Math.random() * 10);
                        pityCounter++;

                        if ((randomInteger == 0) || (pityCounter == 15)) {
                            File messageFile = new File("messages.txt");
                            BufferedReader reader;
                            try {
                                reader = new BufferedReader(new FileReader(messageFile));
                                String message = reader.readLine();
                                randomInteger = (int) (Math.random() * 3);
                                int counter = 0;

                                while (message != null) {
                                    if (counter == randomInteger) {
                                        JOptionPane.showMessageDialog(frame, message);
                                    }
                                    message = reader.readLine();
                                    counter++;
                                }
                                reader.close();
                            } catch (IOException err) {
                                err.printStackTrace();
                            }
                        }
                    }
                });

                // Set up the remaining GUI objects
                maxImageHeight = (int) (
                        GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight()
                                - getMomentButton.getPreferredSize().getHeight() - 20);
                maxImageWidth = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() - 20;
                momentLabel.setIcon(getRandomMoment());

                momentLabel.setPreferredSize(new Dimension(maxImageHeight, maxImageWidth));

                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
                momentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                getMomentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(momentLabel);
                mainPanel.add(getMomentButton);

                frame.add(mainPanel);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    // Displays two messages when the user tries to exit the program.
                    public void windowClosing(WindowEvent event) {
                        JOptionPane.showMessageDialog(frame,"This message...");
                        JOptionPane.showMessageDialog(frame,"... is shown just before the user leaves the program.");
                        frame.dispose();
                        System.exit(0);
                    }
                });
                frame.pack();
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

                frame.setVisible(true);

            }
        });

        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.PAGE_AXIS));
        toText.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageText.setAlignmentX(Component.CENTER_ALIGNMENT);
        toText.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        introPanel.add(toText);
        introPanel.add(messageText);
        introPanel.add(fromText);
        introPanel.add(startButton);

        frame.add(introPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setVisible(true);
    }

    /*
     Gets a new random moment from the list of files in the directory 'moments' and displays it on the screen.
     @return A new random moment, resized to fit the window if needed.
     */
    private static ImageIcon getRandomMoment() {
        int randomInteger;

        // Pick any moment other than the one already displayed
        do {
            randomInteger = (int) (Math.random() * moments.length);
        } while (randomInteger == momentNumber);

        // ImageIcon icon = new ImageIcon("moments/IMG_6981.jpg");
        momentNumber = randomInteger;
        ImageIcon momentIcon = new ImageIcon(moments[randomInteger].getPath());

        // Get the image's original height and width
        int imageHeight = momentIcon.getIconHeight();
        int imageWidth = momentIcon.getIconWidth();

//        System.out.println(maxImageHeight);
//        System.out.println(maxImageWidth);
//        System.out.println(imageHeight);
//        System.out.println(imageWidth);

        // If the picture is bigger than the space that it has been given in width or height, adjust the image so that
        // it fits in the space while keeping its aspect ratio.
        if (imageHeight > maxImageHeight) {
            imageWidth = imageWidth * maxImageHeight / imageHeight;
            imageHeight = maxImageHeight;
        }

        if (imageWidth > maxImageWidth) {
            imageHeight = imageHeight * maxImageWidth / imageWidth;
            imageWidth = maxImageWidth;
        }

        // Returns the resized image.
        return new ImageIcon(momentIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
    }
}
