package tennis;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static java.lang.Math.abs;

public class TennisScoreBoard extends JFrame {

    private static final int COLUMNFORPOINTS = 3;
    private static final int COLUMNFORGAMES = 2;
    private static final int COLUMNFORSETS = 1;
    private static final int ROWFORPLAYER1 = 0;
    private static final int ROWFORPLAYER2 = 1;
    private static final int SETSTOWINMATCH = 2;
    private static final Integer DEUCEPOINTS = 3;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private Boolean tieBreak = false;

    private Boolean gameOver = false;
    public TennisScoreBoard(TennisPlayer p1, TennisPlayer p2) {

        // Set the title of the frame
        //super("Score Board");

        // Sample data
        Object[][] data = {
                {"Player 1", "0", "0", "0"},
                {"Player 2", "0", "0", "0"}
        };

        // Column names
        String[] columnNames = {"Player", "Sets", "Games", "Points"};

        frame = new JFrame("Score Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        // Create a DefaultTableModel
        tableModel = new DefaultTableModel(data, columnNames);

        // Create a JTable with the model
        table = new JTable(tableModel) {
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };
        table.setRowSelectionAllowed(false);

        // Create a custom cell renderer for the table header
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        Font headerFont = new Font("Arial", Font.BOLD, 16);
        table.getTableHeader().setFont(headerFont);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set the preferred height of the cell to auto
                table.setRowHeight(row, (getPreferredSize().height + 20));
                component.setFont(new Font("Arial", Font.BOLD, 24));

                switch( column ) {
                    case 1: component.setForeground(Color.decode("#019920"));
                        break;

                    case 2: component.setForeground(Color.BLUE);
                        break;

                    case 3: component.setForeground(Color.BLACK);
                        break;

                    default: component.setForeground(Color.RED);
                        component.setFont(new Font("Arial", Font.BOLD, 18));
                        break;
                }
                return component;
            }
        };

        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Create a Button 1
        JButton buttonP1 = new JButton("Point for Player 1");
        buttonP1.setPreferredSize(new Dimension(220, 30));

        // Add an ActionListener to the button 1
        buttonP1.addActionListener(e -> {
            ScoreUpdate( p1, p2, "Player 1");
            //JOptionPane.showMessageDialog(frame, "Player 1 clicked!");
        });

        // Create a Button 2
        JButton buttonP2 = new JButton("Point for Player 2");
        buttonP2.setPreferredSize(new Dimension(220, 30));

        // Add an ActionListener to the button 2
        buttonP2.addActionListener(e -> {
            ScoreUpdate( p1, p2, "Player 2");
            //JOptionPane.showMessageDialog(frame, "Player 2 clicked!");
        });

        // Create a JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonP1);
        buttonPanel.add(buttonP2);

        // Set layout manager of the frame to BorderLayout
        frame.setLayout(new BorderLayout());

        // Add the table to the center and buttons to the south
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Method to change the value in a specific cell
    public void changeTableValue(int rowIndex, int columnIndex, Object newValue) {
        tableModel.setValueAt(newValue, rowIndex, columnIndex);
    }

    private void ScoreUpdate( TennisPlayer p1, TennisPlayer p2, String pointForPlayer ) {

        tieBreak = ( p1.getNumberOfGamesWon() == 6 && p2.getNumberOfGamesWon() == 6 ) ? true : false;

        if ( gameOver ) return;

        switch(pointForPlayer) {
            case "Player 1":
                    p1.AddPointWon();
                break;
            case "Player 2":
                    p2.AddPointWon();
                break;
        }

        int numPointsDiff = abs(p1.getPointsWon()-p2.getPointsWon());

        //Check for Deuce
        if ( numPointsDiff == 0 ) {
            if (p1.getPointsWon() > 2) {
                UpdateDeucePointsOnBoard();
                return;
            }
        }

        switch(pointForPlayer) {
            case "Player 1":
                    if( p1.getPointsWon() <= 3 ) {
                        UpdatePointsOnBoard(p1, p2);
                        return;
                    }
                    else {
                        if( tieBreak ) {
                            if( p1.getPointsWon() < 7 ) {
                                UpdatePointsOnBoard(p1, p2);
                                return;
                            }
                            else {
                                if( numPointsDiff >= 2 ) {
                                    // player wins tieBreak
                                    UpdateGamesWon(p1, p2, "Player 1");
                                    return;
                                }
                            }
                        }
                        else {
                            if( numPointsDiff >= 2 ) {
                                UpdateGamesWon(p1, p2, "Player 1");
                                return;
                            }
                            else {
                                UpdatePointsOnBoard(p1, p2);
                                return;
                            }
                        }
                    }
                break;

            case "Player 2":
                    if( p2.getPointsWon() <= 3 ) {
                        UpdatePointsOnBoard(p1, p2);
                        return;
                    }
                    else {
                        if( tieBreak ) {
                            if( p2.getPointsWon() < 7 ) {
                                UpdatePointsOnBoard(p1, p2);
                                return;
                            }
                            else {
                                if( numPointsDiff >= 2 ) {
                                    // player wins tieBreak
                                    UpdateGamesWon(p1, p2, "Player 2");
                                    return;
                                }
                            }
                        }
                        else {
                            if( numPointsDiff >= 2 ) {
                                UpdateGamesWon(p1, p2, "Player 2");
                                return;
                            }
                            else {
                                UpdatePointsOnBoard(p1, p2);
                                return;
                            }
                        }
                    }
                break;

            default: return;
        }
    }

    private void UpdateGamesWon(TennisPlayer p1, TennisPlayer p2, String gameForPlayer) {

        switch(gameForPlayer) {
            case "Player 1":
                p1.AddGameWon();
                break;
            case "Player 2":
                    p2.AddGameWon();
                break;
        }

        int numGamesDiff = abs(p1.getNumberOfGamesWon() - p2.getNumberOfGamesWon());

        switch(gameForPlayer) {
            case "Player 1":
                    if( p1.getNumberOfGamesWon() < 6 ) {
                        InitPointsWon(p1, p2);
                        UpdatePointsOnBoard(p1, p2);
                        UpdateGamesOnBoard(p1, p2);
                        return;
                    }
                    else if( p1.getNumberOfGamesWon() >= 6 ) {
                        if( numGamesDiff < 2 ) {
                            UpdateGamesOnBoard(p1, p2);
                            return;
                        }
                        else {
                            InitPointsWon(p1, p2);
                            UpdatePointsOnBoard(p1, p2);
                            UpdateGamesOnBoard(p1, p2);
                            UpdateSetsWon(p1, p2, "Player 1");
                        }
                    }
                break;

            case "Player 2":
                if( p2.getNumberOfGamesWon() < 6 ) {
                    InitPointsWon(p1, p2);
                    UpdatePointsOnBoard(p1, p2);
                    UpdateGamesOnBoard(p1, p2);
                    return;
                }
                else if( p2.getNumberOfGamesWon() >= 6 ) {
                    if( numGamesDiff < 2 ) {
                        UpdateGamesOnBoard(p1, p2);
                        return;
                    }
                    else {
                        InitPointsWon(p1, p2);
                        UpdatePointsOnBoard(p1, p2);
                        UpdateGamesOnBoard(p1, p2);
                        UpdateSetsWon(p1, p2, "Player 2");
                    }
                }
                break;
        }
    }

    private void UpdateSetsWon(TennisPlayer p1, TennisPlayer p2, String setForPlayer) {

        switch(setForPlayer) {
            case "Player 1":
                    p1.AddSetWon();
                break;

            case "Player 2":
                    p2.AddSetWon();
                break;
        }

        UpdateSetsOnBoard(p1, p2);
        InitPointsWon(p1, p2);
        UpdatePointsOnBoard(p1, p1);
        InitGamesWon(p1, p2);
        UpdateGamesOnBoard(p1, p2);

        if( p1.getNumberOfSetsWon() > 1) {
            // Player 1 wins the match
            gameOver = true;
            JOptionPane.showMessageDialog(frame, "Player 1 is the Winner !");
        }
        else if( p2.getNumberOfSetsWon() > 1) {
            // Player 2 wins the match
            gameOver = true;
            JOptionPane.showMessageDialog(frame, "Player 2 is the Winner !");
        }
        return;
    }

    private void InitPointsWon(TennisPlayer p1, TennisPlayer p2) {
        p1.setNumberOfPointsWon(0);
        p2.setNumberOfPointsWon(0);
    }

    private void UpdatePointsOnBoard(TennisPlayer p1, TennisPlayer p2) {
        if(p1.getPointsWon() >= p2.getPointsWon()) {
            changeTableValue(ROWFORPLAYER1, COLUMNFORPOINTS, PrepareScorePointsToShow(p1.getPointsWon()));
        }
        else if(p1.getPointsWon() < p2.getPointsWon() ) {
            if( p1.getPointsWon() > DEUCEPOINTS.shortValue()) {
                changeTableValue(ROWFORPLAYER1, COLUMNFORPOINTS, PrepareScorePointsToShow(DEUCEPOINTS));
            }
            else changeTableValue(ROWFORPLAYER1, COLUMNFORPOINTS, PrepareScorePointsToShow(p1.getPointsWon()));
        }

        if(p2.getPointsWon() >= p1.getPointsWon()) {
            changeTableValue(ROWFORPLAYER2, COLUMNFORPOINTS, PrepareScorePointsToShow(p2.getPointsWon()));
        }
        else if(p2.getPointsWon() < p1.getPointsWon() ) {
            if( p2.getPointsWon() > DEUCEPOINTS.shortValue()) {
                changeTableValue(ROWFORPLAYER2, COLUMNFORPOINTS, PrepareScorePointsToShow(DEUCEPOINTS));
            }
            else changeTableValue(ROWFORPLAYER2, COLUMNFORPOINTS, PrepareScorePointsToShow(p2.getPointsWon()));
        }
    }

    private void UpdateDeucePointsOnBoard() {
        changeTableValue(ROWFORPLAYER1, COLUMNFORPOINTS, PrepareScorePointsToShow(DEUCEPOINTS));
        changeTableValue(ROWFORPLAYER2, COLUMNFORPOINTS, PrepareScorePointsToShow(DEUCEPOINTS));
    }
    private void InitGamesWon(TennisPlayer p1, TennisPlayer p2) {
        p1.setNumberOfGamesWon(0);
        p2.setNumberOfGamesWon(0);
    }

    private void UpdateGamesOnBoard(TennisPlayer p1, TennisPlayer p2) {
        changeTableValue(ROWFORPLAYER1, COLUMNFORGAMES, p1.getNumberOfGamesWon().toString());
        changeTableValue(ROWFORPLAYER2, COLUMNFORGAMES, p2.getNumberOfGamesWon().toString());
    }

    private void UpdateSetsOnBoard(TennisPlayer p1, TennisPlayer p2) {
        changeTableValue(ROWFORPLAYER1, COLUMNFORSETS, p1.getNumberOfSetsWon().toString());
        changeTableValue(ROWFORPLAYER2, COLUMNFORSETS, p2.getNumberOfSetsWon().toString());
    }

    private String PrepareScorePointsToShow(Integer points) {

        if( tieBreak ) {
            return points.toString();
        }

        switch(points) {
            case 0: return "0";
            case 1: return "15";
            case 2: return "30";
            case 3: return "40";
            default: return "Ad";
        }
    }

}
