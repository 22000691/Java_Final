package DavinciCode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DaVinciCodeGame extends JFrame {
    private List<Tile> userTiles; //유저 타일 저장
    private List<Tile> computerTiles; //컴퓨터 타일 저장
    private List<Tile> tiles; //타일 목록 저장
    private boolean[] computerTilesRevealed; //컴퓨터타일 공개여부
    private JPanel gamePanel;
    private JTextArea gameLog; //text
    private boolean userTurn;
    private Tile pickedTile;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DaVinciCodeGame::new);
    }

    public DaVinciCodeGame() {
        setTitle("Da Vinci Code Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeGame(); //게임 초기화

        setVisible(true);
    }

    private void initializeGame() {
        tiles = initializeTiles();
        userTiles = new ArrayList<>();
        computerTiles = new ArrayList<>();
        computerTilesRevealed = new boolean[4];

        Collections.shuffle(tiles); //타일 섞기
        distributeTiles(tiles, userTiles, computerTiles); //4개씩

        sortTiles(userTiles); //타일 순서대로
        sortTiles(computerTiles); //타일 순서대로

        gamePanel = new JPanel();
        gameLog = new JTextArea(10, 50);
        gameLog.setEditable(false);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(startButton, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(gameLog), BorderLayout.CENTER);
        mainPanel.add(gamePanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void startGame() {
        gameLog.append("Game started!\n");
        userTurn = true; //user부터 게임 시작
        playTurn();
    }

    private void playTurn() {
        if (userTiles.isEmpty() || computerTiles.isEmpty()) {
            endGame(); //user or computer list empty면 endgame
            return;
        }
    
        if (userTurn) {
            gameLog.append("\nUser turn\n");
        } else {
            gameLog.append("\nComputer turn\n");
        }
        pickedTile = pickOne(); //각자 turn에 타일을 하나씩 뽑음
        if (pickedTile == null) { //남은 타일 없으면 endgame
            gameLog.append("No remain tile");
            return;
        }
        if (userTurn) {
            gameLog.append("Picked tile: " + pickedTile + "\n"); //뽑은 타일은 유저만 공개
            sortTiles(userTiles);
            updateUserTilesLog();
            updateComputerTilesLog();
            userGuessTile();
        } else {
            sortTiles(computerTiles);
            updateComputerTilesLog();
            computerGuessTile();
        }
    }

    private Tile pickOne() { //tiles리스트에 맨 앞에꺼 가져옴
        if (tiles.isEmpty()) {
            gameLog.append("No more tiles to pick.\n");
            return null;
        }
        return tiles.remove(0);
    }

    private void userGuessTile() {
        // 유저가 컴퓨터꺼 맞출때
        JTextField indexField = new JTextField(5);
        JTextField valueField = new JTextField(5);
        JTextField colorField = new JTextField(5);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Index:"));
        inputPanel.add(indexField);
        inputPanel.add(new JLabel("Value:"));
        inputPanel.add(valueField);
        inputPanel.add(new JLabel("Color (0 for Black, 1 for White):"));
        inputPanel.add(colorField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel,
                "Guess the computer's tile", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int index = Integer.parseInt(indexField.getText());
            int value = Integer.parseInt(valueField.getText());
            int color = Integer.parseInt(colorField.getText());
            boolean correct = guessTile(computerTiles, index, value, color);
            if (correct) {
                userTiles.add(pickedTile);
                gameLog.append("Correct guess!\n");
                openComputerTile(index);
                updateComputerTilesLog();
                userTurn = askStopOrContinue();
            } else {
                gameLog.append("Wrong guess.\n");
                openUserTile();
                userTurn = false;
            }
            playTurn();
        }
    }

    private boolean askStopOrContinue() { //맞췄을때 한번더 맞출지 아니면 그만할지 선택
        int result = JOptionPane.showConfirmDialog(null,
                "Do you want to continue guessing?",
                "Continue?",
                JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    private void openComputerTile(int index) {
        computerTilesRevealed[index] = true;
        Tile tile = computerTiles.get(index);
        gameLog.append("Opened computer's tile: " + tile + "\n");
    }

    private void openUserTile() {
        //유저가 한개 받았던거 open
        gameLog.append("Opened your tile: " + pickedTile + "\n");
    }

    private void computerGuessTile() {
        // 컴퓨터가 랜덤으로 하나 선택해서 맞추기
        int index = (int) (Math.random() * userTiles.size()); //index
        int val = (int) (Math.random() * 12); //value
        int col = (int) (Math.random() * 2); //col

        gameLog.append("Computer guesses your tile at index " + index + ": " + val + "\n");
        boolean correct = guessTile(userTiles, index, val,col);
        if (correct) {
            computerTiles.add(pickedTile);
            gameLog.append("Computer guessed correctly!\n");
        } else {
            gameLog.append("Computer guessed incorrectly.\n");
        }
        userTurn = true;
        playTurn();
    }

    // 타일 initialize
    private List<Tile> initializeTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            tiles.add(new Tile(i, 0)); // Black
            tiles.add(new Tile(i, 1)); // White
        }
        return tiles;
    }

    // 4개씩 나눠주기
    private void distributeTiles(List<Tile> tiles, List<Tile> userTiles, List<Tile> computerTiles) {
        for (int i = 0; i < 4; i++) {
            userTiles.add(tiles.remove(0));
            computerTiles.add(tiles.remove(0));
        }
    }

    // 오름차순으로 sort
    private void sortTiles(List<Tile> tiles) {
        tiles.sort((t1, t2) -> Integer.compare(t1.value, t2.value));
    }

    // 타일 맞추기
    private boolean guessTile(List<Tile> tiles, int index, int value, int color) {
        if (index >= 0 && index < tiles.size()) {
            Tile tile = tiles.get(index);
            if (tile.value == value && tile.color == color) {
                tiles.remove(index);
                return true;
            }
        }
        return false;
    }

    private void updateUserTilesLog() {
        gameLog.append("Your tiles: " + userTiles + "\n");
    }

    private void updateComputerTilesLog() {
        StringBuilder sb = new StringBuilder("Computer tiles: [");
        for (int i = 0; i < computerTiles.size(); i++) {
            if (computerTilesRevealed[i]) {
                sb.append(computerTiles.get(i));
            } else {
                sb.append("(  )");
            }
            if (i < computerTiles.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]\n");
        gameLog.append(sb.toString());
    }

    private void endGame() {
        gameLog.append("Game over!\n");
    }

    static class Tile {
        int value;
        int color;

        Tile(int value, int color) {
            this.value = value;
            this.color = color;
        }

        @Override
        public String toString() {
            return "(" + value + "," + (color == 0 ? "Black" : "White") + ")";
        }
    }
}