import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

import filter.FilterByTimeFrame;

import java.util.*;


@SuppressWarnings("serial")
public class TivooViewer extends JFrame {

    private static String myTitle = "Tivoo";
    private static JFileChooser myFileOpener = new JFileChooser();
    private static JFileChooser myFileSaver = new JFileChooser();

    private TivooSystem myModel;
    private JTextField myMessage;


    public TivooViewer(TivooSystem model) {

        myModel = model;
        JPanel panel = (JPanel) getContentPane();
        panel.setLayout(new BorderLayout());
        panel.add(makeMessageBar(), BorderLayout.SOUTH);
        panel.add(makeStartButton(), BorderLayout.CENTER);
        makeMenuBar();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(myTitle);
        pack();
        setVisible(true);
    }

    private JPanel makeMessageBar() {
        JPanel p = new JPanel(new BorderLayout());
        myMessage = new JTextField(30);
        myMessage.setEditable(false);
        p.setBorder(BorderFactory.createTitledBorder("Message"));
        p.add(myMessage, BorderLayout.CENTER);
        return p;
    }
    
    private JButton makeStartButton() {
        JButton startButton = new JButton("Start"); 
        startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed (ActionEvent e) {
                start();
            }
        });
        return startButton;
    }

    
    private void makeMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.add(makeFileMenu());
        bar.add(makeFilterMenu());
        bar.add(makeOutputMenu());
        setJMenuBar(bar);
    }

    private JMenu makeFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem itemOpen = new JMenuItem("Open File");
        itemOpen.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                openFile();
            }
        });
        fileMenu.add(itemOpen);

        fileMenu.addSeparator();

        JMenuItem itemQuit = new JMenuItem("Quit");
        itemQuit.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                exit();
            }
        });
        fileMenu.add(itemQuit);

        return fileMenu;
    }

    private JMenu makeFilterMenu() {
        JMenu filterMenu = new JMenu("Set Filter");

        JCheckBoxMenuItem itemFilterByKeyWord = new JCheckBoxMenuItem("Filter by Keyword");
        itemFilterByKeyWord.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                boolean isSelected = aButton.getModel().isSelected();
                if (isSelected) {
                    filterByKeyWord();
                }
                else {
                    showError("Action Denied", "");
                }
            }
        });
        filterMenu.add(itemFilterByKeyWord);

        JCheckBoxMenuItem itemFilterByTimeFrame = new JCheckBoxMenuItem("Filter by Time Frame");
        itemFilterByTimeFrame.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                boolean isSelected = aButton.getModel().isSelected();
                if (isSelected) {
                    filterByTimeFrame();
                }
                else {
                    showError("Action Denied", "");
                }
            }
        });
        filterMenu.add(itemFilterByTimeFrame);

        return filterMenu;
    }

    private JMenu makeOutputMenu() {
        JMenu outputMenu = new JMenu("Set Output");

        JCheckBoxMenuItem itemOutputSummary = new JCheckBoxMenuItem("Output Summary&Details");
        itemOutputSummary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                boolean isSelected = aButton.getModel().isSelected();
                if (isSelected) {
                    outputSummaryAndDetailPages();
                }
                else {
                    showError("Action Denied", "");
                }
            }
        });
        outputMenu.add(itemOutputSummary);

        return outputMenu;
    }
    
    
    private void openFile() {
        int returnVal = myFileOpener.showOpenDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        showMessage("loading..");
        File file = myFileOpener.getSelectedFile();
        myModel.loadFile(file);
        showMessage("file loaded");
    }

    private void filterByKeyWord() {
        String keyword = JOptionPane.showInputDialog("Please enter keyword:");
        if (keyword == null) {
            return;
        }
        myModel.addFilterByKeyword(keyword);
        showMessage("filter ready");
    }

    private void filterByTimeFrame() {
        String start = JOptionPane.showInputDialog("Please enter start time: ", FilterByTimeFrame.getDefaultDateFormat());
        if (start == null) {
            return;
        }
        String end = JOptionPane.showInputDialog("Please enter end time: ", FilterByTimeFrame.getDefaultDateFormat());
        if (end == null) {
            return;
        }
        myModel.addFilterByTimeFrame(start, end);
        showMessage("filter ready");
    }

    private void outputSummaryAndDetailPages() {
        int returnval = myFileSaver.showSaveDialog(null);
        if (returnval != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = myFileSaver.getSelectedFile();
        myModel.addSummaryAndDetailPagesWriter(file.getPath());
        showMessage("output ready");
    }

    private void start() {
        myModel.perform();
        showMessage("succeeded!");
    }
    
    private void exit() {
        System.exit(0);
    }
    
    
    public void showMessage(String s) {
        myMessage.setText(s);
    }

    public void showInfo(String s) {
        JOptionPane.showMessageDialog(this, s, "Tivoo Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String word, String error){
        JOptionPane.showMessageDialog(this, word, error,
                JOptionPane.ERROR_MESSAGE);
    }



}