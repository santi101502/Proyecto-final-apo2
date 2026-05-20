import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class Enemy {
    int x = 0;
    int y = 220;
    int health = 100;
    int speed = 2;

    void move() {
        x += speed;
    }
}

class Tower {
    int x, y;
    int range = 180;
    int damage = 1;

    public Tower(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void attack(Enemy enemy) {
        double distance = Math.sqrt(
                Math.pow(enemy.x - x, 2) +
                Math.pow(enemy.y - y, 2)
        );

        if (distance < range) {
            enemy.health -= damage;
        }
    }
}

class GamePanel extends JPanel implements ActionListener {

    ArrayList<Tower> towers = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();

    Timer timer;

    public GamePanel() {

        // 🔹 Dos torres (requisito cumplido)
        towers.add(new Tower(250, 150));
        towers.add(new Tower(550, 300));

        // 🔹 Enemigos iniciales
        enemies.add(new Enemy());

        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setBackground(new Color(34, 139, 34));

        // Camino
        g.setColor(new Color(194, 178, 128));
        g.fillRect(0, 220, 900, 60);

        // Torres
        g.setColor(Color.BLUE);
        for (Tower t : towers) {
            g.fillRect(t.x, t.y, 40, 40);
        }

        // Enemigos
        for (Enemy e : enemies) {

            g.setColor(Color.RED);
            g.fillRect(e.x, e.y, 40, 40);

            // Vida
            g.setColor(Color.BLACK);
            g.fillRect(e.x, e.y - 10, 40, 5);

            g.setColor(Color.GREEN);
            g.fillRect(e.x, e.y - 10, Math.max(e.health / 2, 0), 5);

            // 🔥 EVENTO DE DEFENSA (líneas de ataque)
            for (Tower t : towers) {
                double dist = Math.sqrt(
                        Math.pow(e.x - t.x, 2) +
                        Math.pow(e.y - t.y, 2)
                );

                if (dist < t.range) {
                    g.setColor(Color.YELLOW);
                    g.drawLine(t.x + 20, t.y + 20, e.x + 20, e.y + 20);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < enemies.size(); i++) {

            Enemy enemy = enemies.get(i);
            enemy.move();

            // Torres atacan
            for (Tower t : towers) {
                t.attack(enemy);
            }

            // eliminar enemigo y respawn
            if (enemy.health <= 0) {
                enemies.remove(i);
                enemies.add(new Enemy());
                break;
            }
        }

        repaint();
    }
}

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Tower Defense - Primera Versión");

        GamePanel panel = new GamePanel();

        frame.add(panel);
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}