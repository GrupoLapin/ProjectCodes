package curvabolistica2;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BalisticCurve extends JFrame {

    private static final double G = 9.8; 
    private int animationSpeed = 5; //velocidade da animaçãos
    private static int size = 900, ballDiameter = 10;
    private double startX, startY, ballX, ballY;
    private double xSpeed, ySpeed, lastPointX, lastPointY, radius = 100;
    private double angleStop = 110 * Math.PI/180, angle, time, deltaTime = 0.01 ; //ângulo em que a bola deve parar e realizar o lançamento; ângulo em que o programa está no momento; tempo em segundos
    private List<Point2D> curvePoints= new ArrayList<>();
    private Timer timer;

    BalisticCurve(){

        super("Lançamento Obliquo");
        DrawBoard board  = new DrawBoard();
        add(board, BorderLayout.CENTER);

        ballX= lastPointX = startX = 400;//mover x para a direita ou esquerda(horizontal)
        ballY = lastPointY = startY = size - 600;//mover y para cima ou para baixo(vertical)
        getUserInput();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        timer = new Timer(animationSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                board.moveBall();
                board.repaint();
                if(! inBounds()) {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void getUserInput() {

        //double angle = 45;//angulo fornecido pelo usuario
        double speed = 62.8;//velocidade fornecida pelo usuario
        xSpeed = radius * Math.cos(angleStop);
        ySpeed = radius * Math.sin(angleStop);
    }

    private boolean inBounds() {

        
        if((ballX < 0) || (ballX > (getWidth()))
                || ( ballY  > (getHeight() - ballDiameter) ) ) {
            return false;
        }

        return true;
    }

    class DrawBoard extends JPanel {

        public DrawBoard() {
            setPreferredSize(new Dimension(size, size));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.RED);
            g2d.fillOval((int)ballX,(int)ballY,ballDiameter,ballDiameter);

            if((Math.abs(lastPointX - ballX)>=1) && (Math.abs(lastPointY - ballY)>=1) ) {
                curvePoints.add(new Point2D.Double(ballX, ballY));
                lastPointX = ballX; lastPointY = ballY;
            }

            drawCurve(g2d);
        }

        private void drawCurve(Graphics2D g2d) {

            g2d.setColor(Color.BLUE);
            for(int i=0; i < (curvePoints.size()-1); i++) {

                Point2D from = curvePoints.get(i);
                Point2D to = curvePoints.get(i+1);
                g2d.drawLine((int)from.getX(),(int)from.getY(), (int)to.getX(), (int)to.getY());
            }
        }

        private void moveBall() {
            
            if(time < angleStop){ //Tempo = ângulo em que o aplicativo está rodando, logo enquanto o tempo for menor que o ângulo de parada, a bola continua rodando no braço robótico

            ballX = startX + radius * Math.cos(time); //Velocidade em que a bola deve girar de acordo com o raio colocado pelo usuário
            ballY = startY - radius * Math.sin(time); //Velocidade em que a bola deve girar de acordo com o raio colocado pelo usuário
            
            } else {
                
            ballX = startX + (xSpeed * time);
            ballY = startY - ((ySpeed * time) -(0.5 * G * Math.pow(time, 2))) ;

            }

            time += deltaTime;

        }
    }

    public static void main(String[] args) {
        
              

        new BalisticCurve();
        
        System.out.println();

    }
}