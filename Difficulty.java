package mini_galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Difficulty {
   private int hIndex; // � ��ư�� ���������� �˷��ִ� ����
   private int scoreMagnification; // ���ھ� ���� ���� ����
   private int _healthPoint; // ���� ���۽� ĳ�������� �Ѱ��� ü�°�
   private int _missileStack; // ���� ���۽� ĳ�������� �Ѱ��� ��ȭ�鿡 ����ִ� �ִ� �Ѿ˰���

   public static boolean gameStart; // ���ӽ����� Ȯ���ϴ� ����
   public static int healthPoint;
   public static int missileStack;

   private Toolkit tk;
   private int btnX; // �̹����� �⺻ x��
   private int btnY; // �̹����� �⺻ y��

   private Image start; // ���ӽ��� ��ư
   private Image btnHP; // �ִ�ü�� ������ư
   private Image btnMS; // �ִ��Ѿ� ������ư
   private Image arrow; // ȭ��ǥ �̹���
   private Image arrowB; // ȭ��ǥ �̹���
   private Image num; // ���� �̹���
   private Image score; // ���� ����ǥ�� �̹���

   private final int BTN_X; // �̹��� ���� ������ ���
   private final int BTN_Y; // �̹��� ���� ������ ���

   static {
      gameStart = false;
      healthPoint = 4;
      missileStack = 1;
   }

   public Difficulty() {
      hIndex = 0;
      scoreMagnification = 124 - (_healthPoint) + (_missileStack * -20); // ���ھ� ����. �⺻�� 100(x2)
      _healthPoint = 4;
      _missileStack = 1;

      btnX = 900;
      btnY = 300;

      BTN_X = 102;
      BTN_Y = 24;

      tk = Toolkit.getDefaultToolkit();
      start = tk.getImage("res/btn_start.png");
      btnHP = tk.getImage("res/btn_hp.png");
      btnMS = tk.getImage("res/btn_bul.png");
      arrow = tk.getImage("res/ico_arr.png");
      arrowB = tk.getImage("res/black_arrow.png");
      num = tk.getImage("res/ic_num.png");
      score = tk.getImage("res/score.png");
   }

   public void update() { // â�� ������ ������°� �ִϸ��̼�ȭ
      btnX -= 4;
   }

   public void draw(Graphics g, GalagaCanvas roleCanvas) {
      btn(g, roleCanvas); // ��ư
      arrow(g, roleCanvas); // ȭ��ǥ �̹��� ����
      score(g, roleCanvas); // ���� ��� �� �̹��� ���
   }

   private void score(Graphics g, GalagaCanvas roleCanvas) {
      // ���� ǥ�� �̹���
      scoreShow();

      g.drawImage(score, btnX, btnY + 400, btnX + 85, btnY + 19 + 400, 0, 0, 85, 19, roleCanvas);

      g.drawImage(num, btnX + 100, btnY + 400, btnX + 120, btnY + 419, 0 + ((scoreMagnification * 2) / 100 * 22), 0,
            22 + ((scoreMagnification * 2) / 100 * 22), 19, roleCanvas); // ���ھ� 100���ڸ�

      g.drawImage(num, btnX + 120, btnY + 400, btnX + 140, btnY + 419,
            0 + (((scoreMagnification * 2) % 100) / 10 * 22), 0, 22 + (((scoreMagnification * 2) % 100) / 10 * 22),
            19, roleCanvas); // ���ھ� 10���ڸ�

      g.drawImage(num, btnX + 140, btnY + 400, btnX + 160, btnY + 419, 0 + ((scoreMagnification * 2) % 10 * 22), 0,
            22 + ((scoreMagnification * 2) % 10 * 22), 19, roleCanvas); // ���ھ� 1���ڸ�
   }

   private void arrow(Graphics g, GalagaCanvas roleCanvas) {
      g.drawImage(arrow, btnX - 30, btnY + (hIndex * 100), btnX - 12, btnY + 24 + (hIndex * 100), 0, 0, 11, 17,
            roleCanvas);

      g.drawImage(arrowB, btnX + 170, btnY - 10, btnX + 140, btnY + 34, 0, 0, 30, 44, roleCanvas);// bae ����
      g.drawImage(arrowB, btnX + 240, btnY - 10, btnX + 270, btnY + 34, 0, 0, 30, 44, roleCanvas);// bae ����

      g.drawImage(num, btnX + 190, btnY - 5, btnX + 220, btnY + 30, 0 + (_healthPoint * 22), 0,
            22 + (_healthPoint * 22), 19, roleCanvas);

      g.drawImage(arrowB, btnX + 170, btnY + 90, btnX + 140, btnY + 134, 0, 0, 30, 44, roleCanvas);// bae ����
      g.drawImage(arrowB, btnX + 240, btnY + 90, btnX + 270, btnY + 134, 0, 0, 30, 44, roleCanvas);// bae ����

      g.drawImage(num, btnX + 190, btnY + 95, btnX + 220, btnY + 130, 0 + (_missileStack * 22), 0,
            22 + (_missileStack * 22), 19, roleCanvas);
   }

   private void btn(Graphics g, GalagaCanvas roleCanvas) {
      if (hIndex == 0) { // ü������
         g.drawImage(btnHP, btnX, btnY, btnX + BTN_X, btnY + BTN_Y, 0, BTN_Y, BTN_X, BTN_Y * 2, roleCanvas);
      } else {
         g.drawImage(btnHP, btnX, btnY, btnX + BTN_X, btnY + BTN_Y, 0, 0, BTN_X, BTN_Y, roleCanvas);
      }

      if (hIndex == 1) { // �̻��� ����
         g.drawImage(btnMS, btnX, btnY + 100, btnX + BTN_X, btnY + BTN_Y + 100, 0, BTN_Y, BTN_X, BTN_Y * 2,
               roleCanvas);
      } else {
         g.drawImage(btnMS, btnX, btnY + 100, btnX + BTN_X, btnY + BTN_Y + 100, 0, 0, BTN_X, BTN_Y, roleCanvas);
      }

      if (hIndex == 2) { // ���ӽ���
         g.drawImage(start, btnX, btnY + 200, btnX + BTN_X, btnY + BTN_Y + 200, 0, BTN_Y, BTN_X, BTN_Y * 2,
               roleCanvas);
      } else {
         g.drawImage(start, btnX, btnY + 200, btnX + BTN_X, btnY + BTN_Y + 200, 0, 0, BTN_X, BTN_Y, roleCanvas);
      }
   }

   public void move(Direction direction) {
      switch (direction) {
      case LEFT:
         if (hIndex == 0 && _healthPoint > 1) {
            _healthPoint--;
            healthPoint = _healthPoint;
         }
         if (hIndex == 1 && _missileStack > 1) {
            _missileStack--;
            missileStack = _missileStack;
         }
         break;

      case UP:
         if (hIndex == 1 || hIndex == 2) {
            hIndex--;
         }
         break;

      case RIGHT:
         if (hIndex == 0 && _healthPoint < 5) {
            _healthPoint++;
            healthPoint = _healthPoint;
         }
         if (hIndex == 1 && _missileStack < 3) {
            _missileStack++;
            missileStack = _missileStack;
         }
         break;

      case DOWN:
         if (hIndex == 1 || hIndex == 0) {
            hIndex++;
         }
         break;

      case SELECT:
         if (hIndex != 2) {
            gameStart = true;
         } else {
            gameStart = false;
         }
         break;

      default:
         break;
      }
   }

   public int scoreMagnification() {
      scoreShow();
      return scoreMagnification;
   }

   private int scoreShow() {
      scoreMagnification = 124 - (_healthPoint) + (_missileStack * (-20));
      return scoreMagnification * 2;
   }
}
