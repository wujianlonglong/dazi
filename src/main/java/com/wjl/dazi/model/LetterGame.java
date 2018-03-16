package com.wjl.dazi.model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class LetterGame extends JPanel {

    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 654;// 界面宽度
    public static final int HEIGTH = 600;// 界面高度
    private Timer timer; // 定时器
    private int interVal = 1000 / 100;// 时间间隔,10毫秒
    private Letter[] letters = {}; // 存放的字母
    private int outOfBoundNumber;// 记录丢掉的字母个数
    private int hitNumbers = 0; // 按中的字母个数
    public static BufferedImage background; // 背景图
    public static BufferedImage gameover; // 背景图
    // 游戏状态
    private int state;
    public static final int RUNNING = 0;// 运行状态
    public static final int GAME_OVER = 1; // 结束状态

    static { // 加载静态资源
        try {
            System.out.println(LetterGame.class.getResource(""));
            System.out.println(LetterGame.class.getResource("/"));
            background = ImageIO.read(LetterGame.class
                    .getResource("/tzz.jpg"));
            gameover = ImageIO.read(LetterGame.class
                    .getResource("/CR.jpg"));
        } catch (Exception e) {
            System.err.println("图片加载失败！");
            e.printStackTrace();
        }
    }

    public LetterGame() {
    }

    // 进数索引
    int enterIndex = 0;

    /**
     * 字母进入面板的方法
     *
     * @time 2016年6月29日 上午10:38:51
     * @author Lichao
     */
    public void enterAction() {
        enterIndex++;
        if (enterIndex % 30 == 0) {
            Letter letter = nextOne();// 每300毫秒执行一次
            letters = Arrays.copyOf(letters, letters.length + 1);
            letters[letters.length - 1] = letter;
        }
    }

    /**
     * 步进方法
     *
     * @time 2016年6月29日 上午10:40:58
     * @author Lichao
     */
    public void stepAction() {
        for (int i = 0; i < letters.length; i++) {
            letters[i].step();
        }
    }

    /**
     * 定时运行方法
     *
     * @time 2016年6月29日 上午11:12:35
     * @author Lichao
     */
    public void action() {

        state = RUNNING;
        this.repaint();
        /**
         * 键盘监听事件
         */
        KeyAdapter keyAdapter = new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                int index = -1;
                String keyPressed = e.getKeyChar() + "";
                for (int i = 0; i < letters.length; i++) {
                    Letter letter = letters[i];
                    if (keyPressed.equalsIgnoreCase(letter.getNum())) {
                        hitNumbers++;
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    Letter temp = letters[index];
                    letters[index] = letters[letters.length - 1];
                    letters[letters.length - 1] = temp;
                    letters = Arrays.copyOf(letters, letters.length - 1);
                }
            }

        };
        /** 添加鼠标事件 */
        this.addKeyListener(keyAdapter);
        // 这两句用来监听键盘
        this.setFocusable(true);
        this.requestFocus();
        mouseAction();

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (state == RUNNING) {
                    enterAction();
                    stepAction();
                    outOfBoundAction();
                }
                checkGameOverAction();

                repaint();
            }
        }, interVal, interVal);

    }

    /**
     * 出界操作
     *
     * @time 2016年6月29日 下午12:30:17
     * @author Lichao
     */
    public void outOfBoundAction() {
        int index = 0;
        Letter[] lettersInPanel = new Letter[letters.length];
        for (int i = 0; i < letters.length; i++) {
            Letter letter = letters[i];
            if (!letter.outOfBound()) {
                lettersInPanel[index++] = letter;
            } else {
                outOfBoundNumber++;
            }
        }

        letters = Arrays.copyOf(lettersInPanel, index);
    }

    /**
     * 判断游戏是否结束
     *
     * @time 2016年6月29日 下午1:38:24
     * @author Lichao
     */
    private void checkGameOverAction() {
        if (isGameOver()) {
            state = GAME_OVER;
        }
    }

    /**
     * 随机生成字母
     *
     * @return
     * @time 2016年6月29日 上午10:35:46
     * @author Lichao
     */
    protected Letter nextOne() {
        Random random = new Random();
        Letter letter = new Letter();
        letter.setNum(String.valueOf((char) (random.nextInt(26) + 'A')));

        letter.setX(random.nextInt(WIDTH - 25));
        letter.setY(50);
        letter.setColor(getRandColor());
        return letter;
    }

    /**
     * 生成随机颜色
     *
     * @return
     * @time 2016年6月29日 上午10:13:06
     * @author Lichao
     */
    public static Color getRandColor() {
        Random random = new Random();
        Color color = new Color(random.nextInt(255), random.nextInt(255),
                random.nextInt(255));
        return color;
    }

    /**
     * 重写父类方法
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        paintNumber(g);
        paintState(g);
        paintScore(g);
    }

    /**
     * 画图形
     *
     * @param g
     * @time 2016年6月29日 上午9:10:08
     * @author Lichao
     */
    private void paintNumber(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        for (int i = 0; i < letters.length; i++) {
            Letter letter = letters[i];
            g.setColor(letter.getColor());
            g.drawString(letter.getNum(), letter.getX(), letter.getY());
        }

    }

    /**
     * 画状态
     *
     * @param g
     * @time 2016年6月29日 下午1:17:46
     * @author Lichao
     */
    private void paintState(Graphics g) {
        switch (state) {
            case GAME_OVER:
                g.drawImage(gameover, 0, 0, null);
                break;
        }
    }

    /**
     * 添加鼠标事件
     *
     * @time 2016年6月29日 下午2:05:00
     * @author Lichao
     */
    private void mouseAction() {
        /** 鼠标监听事件 */
        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                // 单击右键退出
                if (event.getButton() == MouseEvent.BUTTON3
                        && state == GAME_OVER) {
                    System.exit(0);

                }
                // 单击左键重新开始
                if (event.getButton() == MouseEvent.BUTTON1
                        && state == GAME_OVER) {
                    init();
                }
            }
        };

        this.addMouseListener(mouse);
    }

    /**
     * 初始化游戏
     *
     * @time 2016年6月29日 下午2:09:10
     * @author Lichao
     */
    protected void init() {

        this.state = RUNNING;
        this.outOfBoundNumber = 0;
        this.letters = new Letter[]{};
        this.repaint();
    }

    /**
     * 判断游戏是否结束
     *
     * @return
     * @time 2016年6月29日 下午1:19:07
     * @author Lichao
     */
    private boolean isGameOver() {
        return this.outOfBoundNumber > 5;
    }

    private void paintScore(Graphics g) {
        g.setColor(new Color(0xFF0000)); // 设置颜色(0xFF0000为纯红)
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20)); // 设置字体(Font.SANS_SERIF为字体,Font.BOLD为字体样式,20为字号)
        g.drawString("SCORE: " + hitNumbers, 10, 25); // 画分
        g.drawString("MISS: " + outOfBoundNumber, 10, 45); // 画丢失数
    }

    public static void startGame() {
        JFrame frame = new JFrame("傻逼打字游戏");
        LetterGame game = new LetterGame();
        frame.add(game);
        frame.setBackground(new Color(111, 168, 220));
        frame.setSize(WIDTH, HEIGTH); // 设置窗口的大小
        frame.setAlwaysOnTop(true); // 设置窗口总在最上面
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置默认关闭操作(窗口关闭时退出程序)
        frame.setLocationRelativeTo(null); // 设置窗口起始位置(居中)
        frame.setVisible(true); // 1.设置窗口可见 2.尽快调用paint()方法
        frame.setResizable(false);
        game.action();
    }

}