package factory.codegen.app;

import com.fuscard.commons.FuscardXMLException;
import com.fuscard.commons.XMLDocument;
import factory.Application;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author John Mwai
 */
public class CreditsPlayer {

    private JPanel creditsPanel;
    private String creditPath;
    private Thread player;
    private PlayerDelegate playerDelegate;
    private boolean pause = true;
    private JButton jbplay, jbpause, jbnext, jbrestart, jbprevious;

    public synchronized boolean isPause() {
        return pause;
    }

    public void next() {
        jbplay.setEnabled(false);
        jbpause.setEnabled(true);
        setPause(false);
        if (player != null) {
            playerDelegate.next();
            player.interrupt();
        }
    }

    public void previous() {
        jbplay.setEnabled(false);
        jbpause.setEnabled(true);
        setPause(false);
        if (player != null) {
            playerDelegate.previous();
            player.interrupt();
        }
    }

    public void restart() {
        jbplay.setEnabled(false);
        jbpause.setEnabled(true);
        setPause(false);
        if (player != null) {
            playerDelegate.restart();
            player.interrupt();
        }
    }

    protected synchronized void checkPlay() throws InterruptedException {
        if (isPause()) {
            wait();
        }
    }

    public synchronized void setPause(boolean pause) {
        this.pause = pause;
        notifyAll();
        jbnext.setEnabled(true);
        jbrestart.setEnabled(true);
        jbprevious.setEnabled(true);
        if (pause) {
            jbplay.setEnabled(true);
            jbpause.setEnabled(false);

        } else {
            creditsPanel.setBackground(Color.BLACK);
            jbpause.setEnabled(true);
            jbplay.setEnabled(false);
        }
    }

    public CreditsPlayer(JButton play, JButton next, JButton pause,
            JButton restart, JButton previous) {
        debug("Creating credit player...");
        setUpButtons(play, next, pause, restart, previous);
        this.creditsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                debug("in credit panel paint component");
                CreditsPlayer.this.draw(g);
            }
        };
        creditsPanel.setBackground(new Color(234, 234, 234));
        creditsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(153, 153, 153)));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initiateCredits();
            }
        });
    }

    private void setUpButtons(JButton play, JButton next,
            JButton pause, JButton restart, JButton previous) {
        jbpause = pause;
        jbplay = play;
        jbnext = next;
        //
        jbprevious = previous;
        jbrestart = restart;
        jbrestart.setEnabled(false);
        jbprevious.setEnabled(false);
        //
        jbplay.setEnabled(true);
        jbpause.setEnabled(false);
        jbnext.setEnabled(false);

    }

    public CreditsPlayer(JPanel creditsPanel, JButton play, JButton next,
            JButton pause, JButton restart, JButton previous) {
        setUpButtons(play, next, pause, restart, previous);
        this.creditsPanel = creditsPanel;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initiateCredits();
            }
        });
    }

    public JPanel getCreditsPanel() {
        return creditsPanel;
    }

    public void draw(Graphics g) {
        debug("Drawing from credit player...");

        if (playerDelegate != null) {
            playerDelegate.draw(g);
        }
    }

    private void initiateCredits() {
        debug("Initiating credits...");
        try {
            creditPath = Application.getInstance().getCreditsPath();
            play();
        } catch (FuscardXMLException ex) {
            Logger.getLogger(CreditsPlayer.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public void stop() {
        if (player != null) {
            player.interrupt();
            try {
                player.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    private void play() throws FuscardXMLException {
        debug("Playing ... ");
        try {
            XMLDocument xml = new XMLDocument(creditPath);
            xml.loadFromFile();
            NodeList nodes = xml.getNodes("root/credits/credit");
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            credits = new LinkedList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                Credit c = new Credit();
                credits.add(c);
                String nameexp = "Name", orderexp = "order", roleexp = "role", narrativeexp = "narrative";
                String nameval = (String) xPath.evaluate(nameexp, n,
                        XPathConstants.STRING);
                String orderval = (String) xPath.evaluate(orderexp, n,
                        XPathConstants.STRING);
                String roleval = (String) xPath.evaluate(roleexp, n,
                        XPathConstants.STRING);
                String narrativeval = (String) xPath.evaluate(narrativeexp, n,
                        XPathConstants.STRING);
                c.order = Integer.parseInt(orderval.trim());
                c.name = nameval;
                c.narrative = narrativeval;
                c.role = roleval;
            }
            debug("Found " + credits.size() + " credits");
            //Sort the credits
            Collections.sort(credits);

            playerDelegate = new PlayerDelegate(credits, creditsPanel, this);
            player = new Thread(playerDelegate);

            player.start();
        } catch (XPathExpressionException exp) {
            throw new FuscardXMLException(exp);
        }
    }
    private LinkedList<Credit> credits;

    protected class Credit implements Comparable<Credit> {

        String name;
        int order;
        String role;
        String narrative;

        @Override
        public int compareTo(Credit o) {
            if (o == null) {
                throw new NullPointerException("Expecting a Credit. Null found");
            }
            if (order < o.order) {
                return -1;
            }
            if (order > o.order) {
                return 1;
            }
            if (!equals(o)) {
                throw new IllegalStateException(
                        "Equal order implies equal credit");
            }
            return 0;
        }
    }

    public static class PlayerDelegate implements Runnable {

        private LinkedList<Credit> credits;
        protected JPanel panel;
        private Graphics g;
        private Color nameColor = Color.lightGray;
        private Color roleColor = Color.cyan;
        private Color narrativeColor = Color.GRAY.brighter();
        private Font nameFont = new Font("Copperplate Gothic Light", Font.BOLD,
                16);
        private Font roleFont = new Font("Monotype Corsiva",
                Font.ITALIC + Font.BOLD, 16);
        private Font narrativeFont = new Font("Plantagenet Cherokee", Font.PLAIN,
                14);
        private Font currentFont;
        private Color currentColor;
        int ydefault = 50, xdefault = 20;
        int XOffset = xdefault, YOffset = ydefault;
        int VERTICAL_SPACING = 5;
        protected int updateInterval = 10;
        private int characterReleaseInterval = 30;
        private FlyingChar fc;
        private LinkedList<Line> lines = new LinkedList<>();
        private Line currentLine;
        private final Object lineLock = new Object();
        private final CreditsPlayer creditsPlayer;

        public CreditsPlayer getCreditsPlayer() {
            return creditsPlayer;
        }

        public Graphics getGraphics() {
            if (g == null) {
                throw new IllegalStateException("No graphics");
            }
            return g;
        }

        protected PlayerDelegate(
                LinkedList<Credit> credits, JPanel creditsPanel,
                CreditsPlayer creditsPlayer) {
            debug("Creating player delegate...");
            this.credits = credits;
            panel = creditsPanel;
            this.creditsPlayer = creditsPlayer;
        }

        LinkedList<Line> getLines() {
            synchronized (lineLock) {
                return new LinkedList<>(lines);
            }
        }

        void addLine(Line l) {
            synchronized (lineLock) {
                lines.add(l);
            }
        }

        void setLines(LinkedList<Line> ls) {
            synchronized (lineLock) {
                lines = ls;
            }
        }

        @Override
        public void run() {
            play();
        }
        private boolean previous, next, restart;
        private final Object locationLock = new Object();

        private void next() {
            setLines(new LinkedList<Line>());
            panel.repaint();
            XOffset = xdefault;
            YOffset = ydefault;
            setNext(true);
        }

        private void previous() {
            setLines(new LinkedList<Line>());
            panel.repaint();
            XOffset = xdefault;
            YOffset = ydefault;
            setPrevious(true);
        }

        private void restart() {
            setLines(new LinkedList<Line>());
            panel.repaint();
            XOffset = xdefault;
            YOffset = ydefault;
            setRestart(true);
        }

        public boolean isNext() {
            synchronized (locationLock) {
                return next;
            }
        }

        public void setNext(boolean next) {
            synchronized (locationLock) {
                this.next = next;
            }
        }

        public boolean isPrevious() {
            synchronized (locationLock) {
                return previous;
            }
        }

        public void setPrevious(boolean previous) {
            synchronized (locationLock) {
                this.previous = previous;
            }
        }

        public boolean isRestart() {
            synchronized (locationLock) {
                return restart;
            }
        }

        public void setRestart(boolean restart) {
            synchronized (locationLock) {
                this.restart = restart;
            }
        }

        private void play() {
            debug("Player delegate playing...");

            while (true) {
                if (credits.size() == 0) {
                    debug("No credits to play....exiting player.");
                    break;
                }
                int i = 0;
                for (; i < credits.size();) {

                    try {
                        play(credits.get(i));
                        Thread.sleep(3000);
                        if (i == credits.size() - 1) {
                            creditsPlayer.setPause(true);
                        }
                        pushUp();
                    } catch (InterruptedException ex) {
                        if (isNext()) {
                            setNext(false);
                            i++;
                            if (i >= credits.size()) {
                                i = 0;
                            }
                            continue;
                        }
                        if (isPrevious()) {
                            setPrevious(false);
                            i--;
                            if (i < 0) {
                                i = credits.size() - 1;
                            }
                            continue;
                        }
                        if (isRestart()) {
                            setRestart(false);
                            i = 0;
                            continue;
                        }
                        debug("Player delegate stopped");
                        if (fc != null) {
                            fc.stop();
                        }
                        if (lines != null) {
                            for (Line l : getLines()) {
                                for (FlyingChar f : l.chars) {
                                    f.stop();
                                }
                            }
                        }
                        return;
                    }
                    i++;
                }
            }
        }

        private class Line {

            LinkedList<FlyingChar> chars = new LinkedList<>();
            final Object charLock = new Object();

            LinkedList<FlyingChar> getChars() {
                synchronized (charLock) {
                    return new LinkedList<>(chars);
                }
            }

            void addChar(FlyingChar _char) {
                synchronized (charLock) {
                    chars.add(_char);
                }
            }

            void draw(Graphics g) {
                for (FlyingChar f : getChars()) {
                    f.draw(g);
                }
            }

            int getExtremeY() {
                int y = 0;
                for (FlyingChar f : getChars()) {
                    if (f.getTarget().y > y) {
                        y = f.getTarget().y;
                    }
                }
                return y;
            }
        }

        public void draw(Graphics g) {
            debug("Drawing in player delegate...");
            if (fc != null) {
                debug("Drawing..." + fc.getC());
                fc.draw(g);
            }
            for (Line l : getLines()) {
                l.draw(g);
            }
        }

        private void pushUp() throws InterruptedException {
            for (Line l : getLines()) {
                int distance = l.getExtremeY();
                int n = (int) ((double) Speed.FAST.time / updateInterval);
                for (int i = 0; i <= n; i++) {
                    creditsPlayer.checkPlay();
                    for (FlyingChar fc : l.getChars()) {

                        fc.setY((int) (distance + (double) i / n * (-5 - distance)));
                        panel.repaint();

                    }
                    Thread.sleep(updateInterval);
                }
            }
            setLines(new LinkedList<Line>());
            panel.repaint();
            XOffset = xdefault;
            YOffset = ydefault;
        }

        private void play(Credit credit) throws InterruptedException {
            debug("Playing credit for " + credit.name);
            currentLine = new Line();
            addLine(currentLine);
            currentFont = nameFont;
            currentColor = nameColor;
            flyIn(credit.name.trim(), Direction.EAST, Speed.SLOW);
            Thread.sleep(100);
            nextLine();
            currentFont = roleFont;
            currentColor = roleColor;
            flyIn(credit.role.trim(), Direction.WEST, Speed.MEDIUM);
            Thread.sleep(100);
            nextLine();
            currentFont = narrativeFont;
            currentColor = narrativeColor;
            flyIn(credit.narrative.trim(), Direction.SOUTH, Speed.FAST);

        }

        public void resetGraphics() {
            Graphics gg = panel.getGraphics();
            if (gg != null) {
                g = gg;
            }
        }

        private void flyIn(String what, Direction from, Speed speed) throws InterruptedException {
            debug(
                    "Flying in " + what + " from " + from + " at speed: " + speed);
            //Remove multiple spaces
            what = what.replaceAll("\\s{2,}", " ");

            StringTokenizer st = new StringTokenizer(what, " ", true);
            String[] words = new String[st.countTokens()];
            for (int i = 0; i < words.length; i++) {
                words[i] = st.nextToken();
            }
            resetGraphics();

            getGraphics().setFont(currentFont);
            FontMetrics fontMetrics = getGraphics().getFontMetrics();
            int h = fontMetrics.getHeight();
            for (String s : words) {
                debug("Dealing with word: '" + s + "'");
                int w = fontMetrics.stringWidth(s);
                Point p = nextAvailableLocation(new Dimension(w, h));
                //All the characters of this word are going to fit in the space allocated

                Point origin;
                switch (from) {
                    case EAST:
                        origin = new Point(panel.getWidth(),
                                (int) ((double) panel.getHeight() / 2));
                        break;
                    case WEST:
                        origin = new Point(0,
                                (int) ((double) panel.getHeight() / 2));
                        break;
                    case NORTH:
                        origin = new Point((int) ((double) panel.getWidth() / 2),
                                0);
                        break;
                    default://South
                        origin = new Point((int) ((double) panel.getWidth() / 2),
                                panel.getHeight());
                        break;
                }

                Point dest = new Point(p.x, p.y + h);

                char[] chars = s.toCharArray();

                for (char c : chars) {
                    checkPlay();
                    int cw = fontMetrics.charWidth(c);
                    fc = new FlyingChar(c, new Point(origin), this);
                    fc.setColor(currentColor);
                    fc.setFont(currentFont);
                    fc.fly(speed, new Point(dest));
                    pushCharacter(fc);
                    Thread.sleep(characterReleaseInterval);
                    dest.x += cw;

                }


            }

        }

        void checkPlay() throws InterruptedException {
            try {
                creditsPlayer.checkPlay();
            } catch (InterruptedException ex) {
                if (creditsPlayer.isPause()) {
                    throw new InterruptedException();
                }
            }
        }

        private void pushCharacter(FlyingChar c) {
            if (currentLine != null) {
                currentLine.addChar(c);
            }
        }

        private void nextLine() {
            YOffset = currentLine.getExtremeY() + VERTICAL_SPACING;
            XOffset = 20;
            Line l = new Line();
            addLine(l);
            currentLine = l;
        }

        private Point nextAvailableLocation(Dimension textDimensions) {
            debug("Next available " + textDimensions.width);
            Point res = new Point();
            res.x = XOffset;
            XOffset += textDimensions.width;
            if (XOffset > (panel.getWidth() - 10)) {
                nextLine();
                res.x = 10;
                XOffset = 10 + textDimensions.width;
            }
            res.y = YOffset;
            return res;
        }
    }

    public static enum Direction {

        EAST,
        WEST,
        NORTH,
        SOUTH
    }

    public static enum Speed {

        FAST(500),
        MEDIUM(750),
        SLOW(1000);
        protected int time;

        private Speed(int time) {
            this.time = time;
        }
    }
    private final static boolean debug_mode = false;

    private static void debug(String s) {
        if (debug_mode) {
            System.out.println(s);
        }
    }
}
