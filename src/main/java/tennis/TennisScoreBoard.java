package tennis;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static java.lang.Math.abs;

public class TennisScoreBoard extends JFrame {

    private static final int COLUMN_FOR_POINTS = 3;
    private static final int COLUMN_FOR_GAMES = 2;
    private static final int COLUMN_FOR_SETS = 1;
    private static final int ROW_FOR_PLAYER1 = 0;
    private static final int ROW_FOR_PLAYER2 = 1;
    private static final int SETS_TO_WIN_MATCH = 2;
    private static final Integer DEUCE_POINTS = 3;
    private static final String PLAYER_ONE = "Player 1";
    private static final String PLAYER_TWO = "Player 2";
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private Boolean tieBreak = false;
    private Boolean gameOver = false;

    public TennisScoreBoard(TennisPlayer p1, TennisPlayer p2) {
        initializeFrame();
        initializeTable();
        initializeButtons(p1, p2);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("Score Board");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLayout(new BorderLayout());
    }

    private void initializeTable() {
        Object[][] data = {
                {PLAYER_ONE, "0", "0", "0"},
                {PLAYER_TWO, "0", "0", "0"}
        };
        String[] columnNames = {"Player", "Sets", "Games", "Points"};

        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel) {
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };
        table.setRowSelectionAllowed(false);

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                table.setRowHeight(row, (getPreferredSize().height + 20));
                component.setFont(new Font("Arial", Font.BOLD, 24));

                switch (column) {
                    case 1:
                        component.setForeground(Color.decode("#019920"));
                        break;
                    case 2:
                        component.setForeground(Color.BLUE);
                        break;
                    case 3:
                        component.setForeground(Color.BLACK);
                        break;
                    default:
                        component.setForeground(Color.RED);
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

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initializeButtons(TennisPlayer p1, TennisPlayer p2) {

        JButton buttonPlayer1 = new JButton("Point for Player 1");
        buttonPlayer1.setPreferredSize(new Dimension(220, 30));
        buttonPlayer1.addActionListener(e -> {
            scoreUpdate(p1, p2, PLAYER_ONE);
        });

        JButton buttonPlayer2 = new JButton("Point for Player 2");
        buttonPlayer2.setPreferredSize(new Dimension(220, 30));
        buttonPlayer2.addActionListener(e -> {
            scoreUpdate(p1, p2, PLAYER_TWO);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonPlayer1);
        buttonPanel.add(buttonPlayer2);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void scoreUpdate(TennisPlayer p1, TennisPlayer p2, String pointForPlayer) {
        tieBreak = (p1.getNumberOfGamesWon() == 6 && p2.getNumberOfGamesWon() == 6);
        if(Boolean.TRUE.equals(tieBreak)) {
            frame.setTitle("Score Board      --- tiebreak ---");
        } else frame.setTitle("Score Board");

        if (Boolean.TRUE.equals(gameOver)) return;

        switch (pointForPlayer) {
            case PLAYER_ONE:
                p1.addPointWon();
                break;
            case PLAYER_TWO:
                p2.addPointWon();
                break;
        }

        int numPointsDiff = abs(p1.getPointsWon() - p2.getPointsWon());

        if (numPointsDiff == 0 && Boolean.FALSE.equals(tieBreak) && p1.getPointsWon() > 2) {
                updateDeucePointsOnBoard();
                return;
        }

        switch (pointForPlayer) {
            case PLAYER_ONE:
                if (p1.getPointsWon() <= 3)                 {
                    updatePointsOnBoard(p1, p2);
                } else {
                    if (Boolean.TRUE.equals(tieBreak)) {
                        if (p1.getPointsWon() < 7) {
                            updatePointsOnBoard(p1, p2);
                        } else {
                            if (numPointsDiff >= 2) {
                                updateGamesWon(p1, p2, PLAYER_ONE);
                            } else {
                                updatePointsOnBoard(p1, p2);
                            }
                        }
                    }
                    else { // not in tieBreak
                        if (numPointsDiff >= 2) {
                            updateGamesWon(p1, p2, PLAYER_ONE);
                        } else {
                            updatePointsOnBoard(p1, p2);
                        }
                    }
                }
                break;

            case PLAYER_TWO:
                if (p2.getPointsWon() <= 3) {
                    updatePointsOnBoard(p1, p2);
                } else {
                    if (Boolean.TRUE.equals(tieBreak)) {
                        if (p2.getPointsWon() < 7) {
                            updatePointsOnBoard(p1, p2);
                        } else {
                            if (numPointsDiff >= 2) {
                                updateGamesWon(p1, p2, PLAYER_TWO);
                            } else {
                                updatePointsOnBoard(p1, p2);
                            }
                        }
                    } else { // not in tieBreak
                        if (numPointsDiff >= 2) {
                            updateGamesWon(p1, p2, PLAYER_TWO);
                        } else {
                            updatePointsOnBoard(p1, p2);
                        }
                    }
                }
                break;
        }
    }

    private void updateGamesWon(TennisPlayer p1, TennisPlayer p2, String gameForPlayer) {
        switch (gameForPlayer) {
            case PLAYER_ONE:
                p1.addGameWon();
                break;
            case PLAYER_TWO:
                p2.addGameWon();
                break;
        }

        int numGamesDiff = abs(p1.getNumberOfGamesWon() - p2.getNumberOfGamesWon());

        switch (gameForPlayer) {
            case PLAYER_ONE:
                if (p1.getNumberOfGamesWon() < 6) {
                    initPointsWon(p1, p2);
                    updatePointsOnBoard(p1, p2);
                    updateGamesOnBoard(p1, p2);
                } else { // p1.getNumberOfGamesWon() >= 6
                    if (numGamesDiff < 2) {
                        initPointsWon(p1, p2);
                        updatePointsOnBoard(p1, p2);
                        if(Boolean.TRUE.equals(tieBreak) && p1.getNumberOfGamesWon() == 7) {
                            updateSetsWon(p1, p2, PLAYER_ONE);
                        }
                        else updateGamesOnBoard(p1, p2);
                    } else {
                        initPointsWon(p1, p2);
                        updatePointsOnBoard(p1, p2);
                        updateGamesOnBoard(p1, p2);
                        updateSetsWon(p1, p2, PLAYER_ONE);
                    }
                }
                break;

            case PLAYER_TWO:
                if (p2.getNumberOfGamesWon() < 6) {
                    initPointsWon(p1, p2);
                    updatePointsOnBoard(p1, p2);
                    updateGamesOnBoard(p1, p2);
                } else { // p2.getNumberOfGamesWon() >= 6
                    if (numGamesDiff < 2) {
                        initPointsWon(p1, p2);
                        updatePointsOnBoard(p1, p2);
                        if(Boolean.TRUE.equals(tieBreak) && p2.getNumberOfGamesWon() == 7) {
                            updateSetsWon(p1, p2, PLAYER_TWO);
                        }
                        else updateGamesOnBoard(p1, p2);
                    } else {
                        initPointsWon(p1, p2);
                        updatePointsOnBoard(p1, p2);
                        updateGamesOnBoard(p1, p2);
                        updateSetsWon(p1, p2, PLAYER_TWO);
                    }
                }
                break;
        }
    }

    private void updateSetsWon(TennisPlayer p1, TennisPlayer p2, String setForPlayer) {

        switch (setForPlayer) {
            case PLAYER_ONE:
                p1.addSetWon();
                break;
            case PLAYER_TWO:
                p2.addSetWon();
                break;
        }

        initPointsWon(p1, p2);
        updatePointsOnBoard(p1, p1);
        initGamesWon(p1, p2);
        updateGamesOnBoard(p1, p2);
        updateSetsOnBoard(p1, p2);

        if (p1.getNumberOfSetsWon() == SETS_TO_WIN_MATCH) {
            gameOver = true;
            JOptionPane.showMessageDialog(frame, "Player 1 is the Winner !");
        } else if (p2.getNumberOfSetsWon() == SETS_TO_WIN_MATCH) {
            gameOver = true;
            JOptionPane.showMessageDialog(frame, "Player 2 is the Winner !");
        }
    }

    private void initPointsWon(TennisPlayer p1, TennisPlayer p2) {
        p1.setNumberOfPointsWon(0);
        p2.setNumberOfPointsWon(0);
    }

    private void updatePointsOnBoard(TennisPlayer p1, TennisPlayer p2) {

        if (p1.getPointsWon() >= p2.getPointsWon()) {
            changeTableValue(ROW_FOR_PLAYER1, COLUMN_FOR_POINTS, prepareScorePointsToShow(p1.getPointsWon()));
        }
        else { //  p1.getPointsWon() < p2.getPointsWon()
            if (p1.getPointsWon() > DEUCE_POINTS.shortValue()) {
                if(Boolean.FALSE.equals(tieBreak)) {
                    changeTableValue(ROW_FOR_PLAYER1, COLUMN_FOR_POINTS, prepareScorePointsToShow(DEUCE_POINTS));
                }
                else changeTableValue(ROW_FOR_PLAYER1, COLUMN_FOR_POINTS, prepareScorePointsToShow(p1.getPointsWon()));
            }
            else changeTableValue(ROW_FOR_PLAYER1, COLUMN_FOR_POINTS, prepareScorePointsToShow(p1.getPointsWon()));
        }

        if (p2.getPointsWon() >= p1.getPointsWon()) {
            changeTableValue(ROW_FOR_PLAYER2, COLUMN_FOR_POINTS, prepareScorePointsToShow(p2.getPointsWon()));
        }
        else { // p2.getPointsWon() < p1.getPointsWon()
            if (p2.getPointsWon() > DEUCE_POINTS.shortValue()) {
                if(Boolean.FALSE.equals(tieBreak)) {
                    changeTableValue(ROW_FOR_PLAYER2, COLUMN_FOR_POINTS, prepareScorePointsToShow(DEUCE_POINTS));
                }
                else changeTableValue(ROW_FOR_PLAYER2, COLUMN_FOR_POINTS, prepareScorePointsToShow(p2.getPointsWon()));
            }
            else changeTableValue(ROW_FOR_PLAYER2, COLUMN_FOR_POINTS, prepareScorePointsToShow(p2.getPointsWon()));
        }
    }

    private void updateDeucePointsOnBoard() {
        changeTableValue(ROW_FOR_PLAYER1, COLUMN_FOR_POINTS, prepareScorePointsToShow(DEUCE_POINTS));
        changeTableValue(ROW_FOR_PLAYER2, COLUMN_FOR_POINTS, prepareScorePointsToShow(DEUCE_POINTS));
    }

    private void initGamesWon(TennisPlayer p1, TennisPlayer p2) {
        p1.setNumberOfGamesWon(0);
        p2.setNumberOfGamesWon(0);
    }

    private void updateGamesOnBoard(TennisPlayer p1, TennisPlayer p2) {
        changeTableValue(ROW_FOR_PLAYER1, COLUMN_FOR_GAMES, p1.getNumberOfGamesWon().toString());
        changeTableValue(ROW_FOR_PLAYER2, COLUMN_FOR_GAMES, p2.getNumberOfGamesWon().toString());
    }

    private void updateSetsOnBoard(TennisPlayer p1, TennisPlayer p2) {
        changeTableValue(ROW_FOR_PLAYER1, COLUMN_FOR_SETS, p1.getNumberOfSetsWon().toString());
        changeTableValue(ROW_FOR_PLAYER2, COLUMN_FOR_SETS, p2.getNumberOfSetsWon().toString());
    }

    private String prepareScorePointsToShow(Integer points) {

        if (Boolean.TRUE.equals(tieBreak)) {
            return points.toString();
        }

        switch (points) {
            case 0:
                return "0";
            case 1:
                return "15";
            case 2:
                return "30";
            case 3:
                return "40";
            default:
                return "Ad";
        }
    }

    public void changeTableValue(int rowIndex, int columnIndex, Object newValue) {
        tableModel.setValueAt(newValue, rowIndex, columnIndex);
    }
}
