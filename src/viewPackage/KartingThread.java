package viewPackage;

import controllerPackage.ApplicationController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class KartingThread extends Thread {

    private ApplicationController controller;
    private MenuWindow menuWindow;
    private volatile boolean running = true; // Contrôle du fonctionnement global du thread
    private volatile boolean active = true;  // Contrôle de l'animation (true = active, false = en pause)
    private volatile int currentFrame = 1;   // Frame actuelle de l'animation
    private final int totalFrames = 22;      // Nombre total de frames

    public KartingThread(MenuWindow menuWindow) {
        this.controller = new ApplicationController();
        this.menuWindow = menuWindow;
    }

    public void run() {
        while (running) {
            if (active) {
                loadFrame(currentFrame); // Charger l'image de la frame actuelle

                currentFrame++;
                if (currentFrame > totalFrames) {
                    currentFrame = 1; // Revenir à la première frame après la dernière
                }

                try {
                    Thread.sleep(100); // Ajustez le délai pour la vitesse d'animation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Réactiver le flag d'interruption
                    return; // Quitter si le thread est interrompu
                }
            } else {
                // Si l'animation est en pause, attendre avant de vérifier à nouveau
                synchronized (this) {
                    try {
                        wait(); // Attendre jusqu'à ce que l'animation soit activée
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Réactiver le flag d'interruption
                        return; // Quitter si le thread est interrompu
                    }
                }
            }
        }
    }

    private void loadFrame(int frameNumber) {
        try {
            // Format du nom de fichier avec des zéros initiaux
            String fileName = String.format("frame-%02d.png", frameNumber);
            BufferedImage img = ImageIO.read(new File("animation/" + fileName));
            ImageIcon icon = new ImageIcon(img);
            SwingUtilities.invokeLater(() -> menuWindow.updateImageIcon(icon));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void pauseAnimation() {
        active = false; // Mettre le flag d'animation à false pour la pause
    }

    public synchronized void resumeAnimation() {
        active = true; // Mettre le flag d'animation à true pour reprendre l'animation
        notify(); // Réveiller le thread si en pause
    }

    public synchronized void stopAnimation() {
        running = false; // Arrêter le thread
        active = false;  // Assurez-vous que l'animation est également arrêtée
        notify(); // Réveiller le thread s'il est en pause
    }
}
