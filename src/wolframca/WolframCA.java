package wolframca;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.Timer;

public class WolframCA extends JPanel {
    final int[] ruleSet = {30, 45, 50, 57, 62, 70, 73, 75, 86, 89, 90, 99,
            101, 105, 109, 110, 124, 129, 133, 135, 137, 139, 141, 164,170, 232};
    byte[][] cells;
    int rule = 0;

    /**
     * Wolfram CA Constructor
     */
    public WolframCA() {
        Dimension dim = new Dimension(900, 450);
        setPreferredSize(dim);
        setBackground(Color.white);
        setFont(new Font("SansSerif", Font.BOLD, 28));

        cells = new byte[dim.height][dim.width];
        cells[0][dim.width / 2] = 1;

        // Dupa 5000ms, merge spre urmatoare regula din setul de reguli, predefinite sus
        new Timer(5000, (ActionEvent e) -> {
            rule++;
            if (rule == ruleSet.length)
                rule = 0;
            repaint();
        }).start();
    }

    /**
     * Rules
     *
     * << - Left Shift
     * >> - Right Shift
     * x << y => x * 2^y
     * x >> y => x / 2^y
     *
     * lhs - left side
     * mid - middle side
     * rhs - right side
     *
     * @param lhs
     * @param mid
     * @param rhs
     * @return (byte)
     */
    private byte rules(int lhs, int mid, int rhs) {
        int idx = (lhs << 2 | mid << 1 | rhs);
        return (byte) (ruleSet[rule] >> idx & 1);
    }

    /**
     * Draw Cellular Automata
     *
     * Afisare CA
     *
     * @param g
     */
    void drawCa(Graphics2D g) {
        g.setColor(Color.black);
        for (int r = 0; r < cells.length - 1; r++) {
            for (int c = 1; c < cells[r].length - 1; c++) {
                byte lhs = cells[r][c - 1];
                byte mid = cells[r][c];
                byte rhs = cells[r][c + 1];
                cells[r + 1][c] = rules(lhs, mid, rhs); // next generation
                if (cells[r][c] == 1) {
                    g.fillRect(c, r, 1, 1);
                }
            }
        }
    }

    /**
     * Draw Legend
     *
     * Afiseaza regula care este aplicata
     *
     * @param g
     */
    void drawLegend(Graphics2D g) {
        String s = String.valueOf(ruleSet[rule]);
        int sw = g.getFontMetrics().stringWidth(s);

        g.setColor(Color.white);
        g.fillRect(16, 5, 55, 30);

        g.setColor(Color.darkGray);
        g.drawString(s, 16 + (55 - sw) / 2, 30);
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawCa(g);
        drawLegend(g);
    }

    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Wolfram CA");
            f.setResizable(false);
            f.add(new WolframCA(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
