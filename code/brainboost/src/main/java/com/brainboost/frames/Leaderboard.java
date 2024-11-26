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

import com.brainboost.ServerAPI;
import com.brainboost.User;

public class Leaderboard extends JPanel
{
    private final JButton firstButton, previousButton, nextButton, lastButton, moreButton;//buttons for navigating pages
    private final JButton[] pageButtons;//buttons for pages
    private final JTable table;
    private final DefaultTableModel tableModel;//manager for table data
    private int currentPage = 0;//start at page 1 or index 0
    private final int tableRows = 10;//number of rows per page in the table
    private final int tableColumns = 3;//number of columns in table

    private String[] leaderboardArray;//array of data from attempts db
    private int totalRows;//total number of rows in table
    private int totalPages;//total number of pages from total rows

    private int currentPagesGroup = 0;//current buttons displaying the pages of the group, ex buttons1,2,3,4 is group 1,and buttons 5,6,7,8 is group 2

    public Leaderboard(JFrame previousFrame, User user)
    {
        getLeaderboard(user.getQuiz_id());//get leaderboard values from the quiz id the user is currently taking
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
        tableModel.setColumnIdentifiers(new String[]{"Place","Name", "Score"});
        table = new JTable(tableModel);
        table.setRowHeight(30);

        leaderboardPanel.add(new JScrollPane(table));

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
            previousFrame.setContentPane(new MenuFrame(previousFrame,user));
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
    //loads the leaderboard data for the current page
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
        

        //CHECK FOR ERROR DURING TESTING!!!
        for(int i = start; i < end; i++)
        {
            Object[] row = new Object[tableColumns];
            row[0] = i;
            row[1] = leaderboardArray[i].split(",")[0];
            row[2] = leaderboardArray[i].split(",")[1];
            tableModel.addRow(row);
        }
        //CHECK FOR ERROR DURING TESTING!!!

        Dimension d = table.getPreferredSize();
        table.setPreferredScrollableViewportSize(d);
        updatePageButtons();
    }

    //updates the gui's page buttons 
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
    //Move to the next group of pages. If the next page is outside the bounds of the total pages, loop back to the first page group. 
    private void loadNextPageGroup()
    {
        currentPagesGroup += pageButtons.length;
        if(currentPagesGroup >= totalPages)
        {
            currentPagesGroup = 0;
        }
        updatePageButtons();
    }
    //gets the leaderboard data from server and updates the gui
    private void getLeaderboard(int quiz_id)
    {
    try {
        String leaderboard = ServerAPI.sendMessage("printLeaderboard," + quiz_id);
        leaderboardArray = leaderboard.split("\n");
        totalRows = leaderboardArray.length;
        totalPages = (int) Math.ceil((double) totalRows / tableRows);
        

    } catch (Exception ex) {
        System.out.println("Error getting leaderboard: " + ex.getMessage());
    }
    }
}
