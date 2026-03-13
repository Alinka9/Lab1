package ui;

import model.Mission;
import parser.MissionParser;
import parser.ParserFactory;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MissionAnalyzerUI extends JFrame {
    private JTextArea resultArea;
    private JButton openButton;
    private JLabel statusLabel;
    private ParserFactory parserFactory;
    
    public MissionAnalyzerUI() {
        parserFactory = new ParserFactory();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("🔮 Анализатор проклятых миссий");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Верхняя панель с кнопкой
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(30, 30, 40));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        openButton = new JButton("📂 Открыть файл миссии");
        openButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        openButton.setBackground(new Color(14, 34, 135));
        openButton.setForeground(Color.BLUE);
        openButton.setFocusPainted(false);
        openButton.addActionListener(e -> openFile());
        
        statusLabel = new JLabel("Готов к анализу");
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        topPanel.add(openButton);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(statusLabel);
        
        // Центральная область с результатом
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBackground(new Color(20, 20, 30));
        resultArea.setForeground(new Color(200, 200, 220));
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 50, 150), 2));
        
        // Добавляем компоненты
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Настройки окна
        setSize(900, 700);
        setLocationRelativeTo(null);
    }
    
private void openFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Выберите файл с данными миссии");
    
    // Устанавливаем каталог по умолчанию - папка "files" внутри проекта
    String projectPath = System.getProperty("user.dir");
    File defaultDirectory = new File(projectPath + File.separator + "files");
    
    // Проверяем, существует ли папка, если нет - создаем её
    if (!defaultDirectory.exists()) {
        defaultDirectory.mkdirs();
    }
    
    fileChooser.setCurrentDirectory(defaultDirectory);
    
    // Фильтры для файлов
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Поддерживаемые форматы (JSON, XML, TXT)", "json", "xml", "txt");
    fileChooser.setFileFilter(filter);
    
    int result = fileChooser.showOpenDialog(this);
    
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        processFile(selectedFile);
    }
}
    
    private void processFile(File file) {
        try {
            statusLabel.setText("🔄 Анализ файла: " + file.getName());
            statusLabel.setForeground(Color.YELLOW);
            
            MissionParser parser = parserFactory.getParser(file);
            
            if (parser == null) {
                showError("Не удалось определить формат файла");
                return;
            }
            
            Mission mission = parser.parse(file);
            
            if (mission == null) {
                showError("Не удалось распарсить файл");
                return;
            }
            
            // Показываем результат
            resultArea.setText("");
            resultArea.append(mission.toString());
            
            // Дополнительная статистика
            resultArea.append("\n📊 СТАТИСТИКА МИССИИ:\n");
            resultArea.append(String.format("   Всего магов: %d\n", mission.getSorcerers().size()));
            resultArea.append(String.format("   Всего техник: %d\n", mission.getTechniques().size()));
            
            int totalDamage = mission.getTechniques().stream()
                    .mapToInt(t -> t.getDamage()).sum();
            resultArea.append(String.format("   Общий урон от техник: %,d¥\n", totalDamage));
            
            statusLabel.setText("✅ Анализ завершен: " + file.getName());
            statusLabel.setForeground(new Color(100, 255, 100));
            
        } catch (Exception e) {
            showError("Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showError(String message) {
        resultArea.setText("❌ ОШИБКА: " + message);
        statusLabel.setText("❌ Ошибка");
        statusLabel.setForeground(new Color(255, 100, 100));
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Игнорируем
            }
            new MissionAnalyzerUI().setVisible(true);
        });
    }
}