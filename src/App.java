import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.event.*;
import java.io.*;


public class App extends JFrame implements ActionListener, KeyListener {
    JTextArea textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu editMenu;
    JMenuItem openItem, saveItem, exitItem, saveAsItem;
    JMenuItem find;
    ImageIcon Icon = new ImageIcon(getClass().getResource("/resources/icons/icon3.png"));
    Image icon = Icon.getImage();

    private File currentFile;
    private boolean isTextChanged = false;
    
    public App() {
        initUI();
        addActionEvents();
        setUndecorated(false);
        setTitle("ZenWrite");
        setIconImage(icon);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (currentFile != null || !textArea.getText().isEmpty() && isTextChanged) {
                    warning();
                } else {
                    System.exit(0); 
                }
            }
        });

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                isTextChanged = true;
            }
            public void removeUpdate(DocumentEvent e) {
                isTextChanged = true;
            }
            public void insertUpdate(DocumentEvent e) {
                isTextChanged = true;
            }
        });
    }
    
    private void initUI() {

        // -----FONTS--------
        Font robotoFontRegular = fontLoader("/resources/fonts/Roboto-Regular.ttf",12f);
        Font consola = fontLoader("/resources/fonts/CONSOLA.TTF", 14f);
        Font consolas = fontLoader("/resources/fonts/Consolas.ttf", 14f);
        Font consolai = fontLoader("/resources/fonts/consolai.ttf", 14f);
        Font consolaz = fontLoader("/resources/fonts/consolaz.ttf", 14f);
        Font consolab = fontLoader("/resources/fonts/CONSOLAB.TTF", 14f);

        textArea = new JTextArea();
        textArea.addKeyListener(this);
        textArea.setBackground(new Color(31,31,31));
        textArea.setForeground(Color.WHITE);
        textArea.setFont(consolab);

        
        //-----------------SCROLL PANEL-----------------

        scrollPane = new JScrollPane(textArea);
        scrollPane.getVerticalScrollBar().setBackground(new Color(31,31,31));
        scrollPane.getHorizontalScrollBar().setBackground(new Color(31,31,31));
        scrollPane.setBorder(null);

        add(scrollPane);
        
        //-------------- MENU BAR------------------------

        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(31,31,31));

        fileMenu = new JMenu("File");
        fileMenu.setFont(robotoFontRegular);

        fileMenu.setForeground(Color.WHITE);

        editMenu = new JMenu("Edit");
        editMenu.setForeground(Color.WHITE);
        //---------- FILEMENU ITEMS-------------------------
        openItem = new JMenuItem("Open          Ctrl+O");
        openItem.setIcon(null);
        saveItem = new JMenuItem("Save            Ctrl+S");
        saveItem.setIcon(null);
        saveAsItem = new JMenuItem("Save As..     Ctrl+Alt+S");
        exitItem = new JMenuItem("Exit              Esc");
        exitItem.setIcon(null);

        //---------- EDITMENU ITEMS-------------------
        find = new JMenuItem("Find       Ctrl+F");
        find.setIcon(null);
        

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        editMenu.add(find);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }
    
    private void addActionEvents() {
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        exitItem.addActionListener(this);
        find.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openItem) {
            openFile();
        } else if (e.getSource() == saveItem) {
            saveFile();
        }else if (e.getSource() == saveAsItem) {
            saveAsFile();
        } else if (e.getSource() == exitItem) {
            System.exit(0);
        } else if (e.getSource() == find){
            findInFile();
        }
    }

    private void openFile() {
        if (isTextChanged) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to save changes?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (result) {
                case JOptionPane.YES_OPTION:
                    saveFile();
                    // Fall through to next case to open a new file
                case JOptionPane.NO_OPTION:
                    actuallyOpenFile();
                    break;
                case JOptionPane.CANCEL_OPTION:
                    // Do nothing if user cancels
                    break;
            }
        } else {
            actuallyOpenFile();
        }
    }

    // Method to open and read a file
    private void actuallyOpenFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
                isTextChanged = false; // Reset the flag
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //save a file
    private void saveFile() {
        if (currentFile != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
                textArea.write(writer);
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile(); 
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
                    textArea.write(writer);
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        isTextChanged = false;
    }

    private void saveAsFile(){

        JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile(); 
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
                    textArea.write(writer);
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
    }

    // method to load fonts 

    public static Font fontLoader(String fontPath, float fontSize) {
        try {
            InputStream is = App.class.getResourceAsStream(fontPath);
            if (is == null) {
                System.err.println("Font file not found at " + fontPath);
                return null;
            }
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontSize);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // prompts the user for the word to find , case sensitive, if found selects the text. 
    private void findInFile() {
        
        String searchTerm = JOptionPane.showInputDialog(this, "Enter text to find:", "Find", JOptionPane.PLAIN_MESSAGE);
        if (searchTerm != null && !searchTerm.isEmpty()) {
           
            String textAreaContent = textArea.getText();
            
            int index = textAreaContent.indexOf(searchTerm);
            
            if (index != -1) {
                // If found, select the text
                textArea.requestFocus();
                textArea.select(index, index + searchTerm.length());
            } else {
                // Not found, show a message
                JOptionPane.showMessageDialog(this, "Text not found.", "Find", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      
        if ( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F){
            findInFile();

        }
        if ( e.isControlDown() && e.isAltDown() && e.getKeyCode() == KeyEvent.VK_S){
            saveAsFile();

        }
        if ( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
            saveFile();

        }
        if ( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O){
            openFile();

        }
        if (  e.getKeyCode() == KeyEvent.VK_ESCAPE){
         //   System.exit(0);

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void warning() {
        if (!isTextChanged) {
            System.exit(0);
            return;
        }
        
        String[] options = {"Save", "Don't Save", "Cancel"};
        int selection = JOptionPane.showOptionDialog(
                this, "Do you want to save the File?", "ZenWrite",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
    
        switch (selection) {
            case 0: // Save
                saveFile();
                System.exit(0);
                break;
            case 1: // Don't Save
                System.exit(0);
                break;
            case 2: // Cancel
                break;
            default:
                break;
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.put("MenuItem.iconTextGap", 0);

                // Set FlatDarkLaf theme
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            new App();
        });
    }

}
