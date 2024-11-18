package com.brainboost.frames;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//CONNECT TO ATTEMPTS DB!!
public class Leaderboard extends JPanel
{
    private JButton firstButton, previousButton, nextButton, lastButton, moreButton;//buttons for navigating pages
    private JButton[] pageButtons;//buttons for pages
    private JTable table;
    private DefaultTableModel tableModel;//manager for table data
    private int currentPage = 0;//start at page 1 or index 0
    private int tableRows = 10;//number of rows in table
    private int tableColumns = 3;//number of columns in table
    private int totalRows = 100;//total number of rows in database(testing 100 rows) RECONNECT TO ATTEMOPTS DB SIZE
    private int totalPages;//total number of pages from total rows
    private int currentPagesGroup = 0;//current buttons displaying the pages of the group, ex buttons1,2,3,4 is group 1,and buttons 5,6,7,8 is group 2

    public Leaderboard(JFrame previousFrame)
    {
        // title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel title = new JLabel("Leaderboard");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        titlePanel.add(title);
        
        //leaderboard panel 
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));

        //table for leaderboard
        tableModel = new DefaultTableModel();//
        tableModel.setColumnIdentifiers(new String[]{"ID","Name", "Score"});
        table = new JTable(tableModel);
        table.setRowHeight(30);

        leaderboardPanel.add(new JScrollPane(table));
        
        totalPages = (int) Math.ceil((double) totalRows / (double) tableRows); //calaulate total number of pages

        //Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        //init 4 buttons for pages (1,2,3,4)
        pageButtons = new JButton[4];
        for(int i = 0; i < pageButtons.length; i++)
        {
            pageButtons[i] = new JButton(Integer.toString(i + 1));
            final int j = i;
            pageButtons[i].addActionListener(e -> loadPage(j + currentPagesGroup));
        }
        
        firstButton = new JButton("First");
        firstButton.addActionListener(e -> loadPage(0));
        previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> loadPage(currentPage - 1));
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> loadPage(currentPage + 1));
        lastButton = new JButton("Last");
        lastButton.addActionListener(e -> loadPage(totalPages - 1));
        
        loadPage(0);//load first page

        moreButton = new JButton("...");
        moreButton.addActionListener(e -> loadNextPageGroup());

        //add all buttons to buttonsPanel
        buttonsPanel.add(firstButton);
        buttonsPanel.add(previousButton);
        for(JButton button : pageButtons)
        {
            buttonsPanel.add(button);
        }
        buttonsPanel.add(moreButton);
        buttonsPanel.add(nextButton);
        buttonsPanel.add(lastButton);

         //return to menu button
        JPanel returnMenuPanel = new JPanel();
        JButton returnToMenuButton = new JButton("Return to Menu");
        returnToMenuButton.addActionListener(e -> {
            previousFrame.setContentPane(new MenuFrame(previousFrame));
            previousFrame.revalidate();
        });
        returnMenuPanel.add(returnToMenuButton);

        //main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(leaderboardPanel);
        mainPanel.add(buttonsPanel);
        mainPanel.add(returnMenuPanel);
        add(mainPanel);
    }

    private void loadPage(int page)
    {
        //out of bounds
        if(page<0||page>=totalPages)
        {
            return;
        }
        currentPage = page;//set current page
        //load page logic
        tableModel.setRowCount(0);//clear table
        //load new data
        int start = page * tableRows;//start index of table
        int end = Math.min(start + tableRows, totalRows);//Math min chooses the smaller value if either the total rows is smaller than the number of table rows for the current page
        for(int i = start; i < end; i++)
        {
            Object[] row = new Object[tableColumns];
            row[0] = i;
            row[1] = "Name";
            row[2] = 100;
            tableModel.addRow(row);
        }
        Dimension d = table.getPreferredSize();
        table.setPreferredScrollableViewportSize(d);
        updatePageButtons();
    }

    private void updatePageButtons()
    {
        //update page buttons
        for(int i = 0; i < pageButtons.length; i++)
        {
            int pageIndex = i + currentPagesGroup;//beginning index of current page group
            if(pageIndex < totalPages)
            {
                pageButtons[i].setText(Integer.toString(pageIndex + 1));
                pageButtons[i].setEnabled(true);
            }
            else
            {
                pageButtons[i].setText("");
                pageButtons[i].setEnabled(false);
            }
            
        }
        firstButton.setEnabled(currentPage > 0);
        previousButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(currentPage < totalPages - 1);
        lastButton.setEnabled(currentPage < totalPages - 1);
    }
    private void loadNextPageGroup()
    {
        currentPagesGroup += pageButtons.length;
        if(currentPagesGroup >= totalPages)
        {
            currentPagesGroup = 0;
        }
        updatePageButtons();
    }
}
