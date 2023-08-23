import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadingBar extends JFrame {

    private static final String FOLDER_PATH = "deletar";

    //construtor
    public LoadingBar(){
        super("Loading Bar");
        setSize(563, 392);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addGuiComponents();
    }

    private void addGuiComponents(){
        JButton deleteButton = new JButton("Delete Files");
        deleteButton.setFont(new Font("Dialog", Font.BOLD, 48));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File folder = new File(FOLDER_PATH);
                if(folder.isDirectory()){
                    File[] deletar = folder.listFiles();
                    if(deletar.length > 0){
                        deleteFiles(deletar);
                    }
                    else
                    {
                        showResultDialog("No files found");
                    }
                }
            }
        });
        add(deleteButton, BorderLayout.CENTER);
    }

    private void showResultDialog(String message){
        JDialog resultDialog = new JDialog(this, "Result", true);
        resultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        resultDialog.setSize(150, 150);
        resultDialog.setLocationRelativeTo(this);
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultDialog.add(messageLabel, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Dialog", Font.BOLD, 18));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultDialog.dispose();
            }
        });
        resultDialog.add(confirmButton, BorderLayout.SOUTH);
        resultDialog.setVisible(true);
    }

    private void deleteFiles(File[] deletar) {
        JDialog loadingDialog = new JDialog(this, "Deleting files", true);
        loadingDialog.setSize(150, 130);
        loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        loadingDialog.setLocationRelativeTo(this);

        JProgressBar loadingBar = new JProgressBar();
        loadingBar.setFont(new Font("Dialog", Font.BOLD, 18));
        loadingBar.setValue(0);
        loadingBar.setForeground(Color.green);

        Thread deleteFilesThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int totalFiles = deletar.length;
                int filesDeleted = 0;
                int progress;
                for(File f : deletar){
                    if(f.delete()){
                        filesDeleted++;
                        progress = (int)((((double)filesDeleted/totalFiles) * 100));

                        try {
                            Thread.sleep(60);
                        } catch(InterruptedException interruptedException){
                            interruptedException.printStackTrace();
                        }
                        loadingBar.setValue(progress);
                    }
                }

                if(loadingDialog.isVisible()){
                    loadingDialog.dispose();
                }

                showResultDialog("Files deleted");
            }

        });

        deleteFilesThread.start();
        loadingBar.setStringPainted(true);
        loadingDialog.add(loadingBar, BorderLayout.CENTER);
        loadingDialog.setVisible(true);
    }
}
