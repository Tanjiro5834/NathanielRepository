package hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class HangMan {
    private JFrame frame;
    private JLabel wordLabel;
    private JLabel guessedLettersLabel;
    private JPanel letterButtonsPanel;
    private JButton[] letterButtons;
    private JButton hintButton;
    private JLabel hintLabel;
    private JLabel firstThreeLettersLabel;
    
    private Random random = new Random();
    private final List<String> randomWords = new ArrayList<>(Arrays.asList(
    	    "AWESOME", "COOL", "DEFENSE", "BEAUTIFUL", "LOOK", "COKE", "PHONE",
    	    "FANTASTIC", "WONDERFUL", "MAGIC", "SUNSHINE", "HAPPINESS", "ADVENTURE",
    	    "FRIENDSHIP", "JOURNEY", "INSPIRATION", "COURAGE", "DISCOVERY", "VICTORY", "DREAM"
    	));
    private String selectedWord;
    private StringBuilder underscoreString;
    private Set<Character> guessedLetters;

    int allowedGuesses = 10;

    public HangMan() {
        frame = new JFrame("HangMan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(430, 400);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        ImageIcon icon = new ImageIcon("icons/hangman_logo.png");
        frame.setIconImage(icon.getImage());

        wordLabel = new JLabel("Word: _ _ _ _ _", SwingConstants.CENTER);
        wordLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
       

        guessedLettersLabel = new JLabel("Guessed Letters: ", SwingConstants.CENTER);
        guessedLettersLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        
        hintButton = new JButton("Hint");
        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hint(); 
            }
        });

        letterButtonsPanel = new JPanel();
        letterButtonsPanel.setLayout(new GridLayout(4, 7));
        
        letterButtons = new JButton[26];
        for(char letter = 'A'; letter <= 'Z'; letter++){
            letterButtons[letter - 'A'] = new JButton(String.valueOf(letter));
            letterButtons[letter - 'A'].addActionListener(new LetterButtonListener());
            letterButtonsPanel.add(letterButtons[letter - 'A']);
        }
        
        letterButtonsPanel.add(hintButton);
        
        frame.add(wordLabel, BorderLayout.NORTH);
        frame.add(guessedLettersLabel, BorderLayout.CENTER);
        frame.add(letterButtonsPanel, BorderLayout.SOUTH);
        
        random = new Random();
        guessedLetters = new HashSet<>();
        selectRandomWord();
        createUnderscoreString();


        frame.setVisible(true);
    }

    private class LetterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            button.setEnabled(false);

            char guessedLetter = button.getText().charAt(0);
            boolean newGuess = guessLetter(guessedLetter);

            wordLabel.setText("Word: " + getUnderscoreString());
            updateGuessedLettersDisplay();
            
            if(getUnderscoreString().replace(" ", "").equals(selectedWord)) {
            	int decision = JOptionPane.showConfirmDialog(frame, "Congratulations! You've guessed the word.\nContinue playing?",
                        selectedWord, JOptionPane.YES_NO_OPTION);
                if(decision == JOptionPane.YES_OPTION){
                    resetGame();
                }else{
                    frame.dispose();
                }
            }
        }
    }

    private void updateScoreString(char guessedLetter) {
        for(int i = 0; i < selectedWord.length(); i++){
            if(guessedLetter == selectedWord.charAt(i)){
                underscoreString.setCharAt(i * 2, guessedLetter);
            }
        }
    }

    public boolean guessLetter(char letter) {
        if(!guessedLetters.contains(letter)){
            guessedLetters.add(letter);
            updateScoreString(letter);
            
            wordLabel.setText("Word: " + getUnderscoreString());
            
            if(getUnderscoreString().replace(" ", "").equals(selectedWord)){
            	int decision = JOptionPane.showConfirmDialog(frame, "Congratulations! You've guessed the word.\nContinue playing?"
            			, selectedWord, JOptionPane.YES_NO_OPTION);
                if(decision == JOptionPane.YES_OPTION){
                	 resetGame();
                }else{
                	frame.dispose();
                }
            }
            return true;
        }
        return false; 
    }
    
    private void hint(){
		char letter1 = selectedWord.charAt(0), letter2 = selectedWord.charAt(1), letter3 = selectedWord.charAt(2);
		firstThreeLettersLabel = new JLabel("Hint: " + letter1 + " " + letter2 + " " + letter3);
		firstThreeLettersLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		frame.add(firstThreeLettersLabel, BorderLayout.CENTER);  
	    frame.revalidate();
	    frame.repaint();
    }
    
    private void resetGame() {
        guessedLetters.clear();
        selectRandomWord();
        createUnderscoreString();

        wordLabel.setText("Word: ".concat(getUnderscoreString()));
        for(JButton btn : letterButtons){
            btn.setEnabled(true);
        }
        
        firstThreeLettersLabel.setText("Hint: ");
    }

    private void createUnderscoreString() {
        underscoreString = new StringBuilder();
        for(int i = 0; i < selectedWord.length(); i++){
            underscoreString.append("_ ");
        }
    }

    public void selectRandomWord() {
    	int index = random.nextInt(randomWords.size());
		selectedWord = randomWords.get(index);
		System.out.println("Word: " + selectedWord);
    }

    public String getSelectedWord() {
        return selectedWord;
    }

    public String getUnderscoreString() {
        return underscoreString.toString().trim();
    }

    public Set<Character> getGuessedLetters() {
        return guessedLetters;
    }

    private void updateGuessedLettersDisplay() {
        StringBuilder guessedLetters = new StringBuilder("Guessed Letters: ");
        for(char letter : getGuessedLetters()){
            guessedLetters.append(letter).append(" "); 
        }
        guessedLettersLabel.setText(guessedLetters.toString().trim());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HangMan::new);
    }
}

