import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.awt.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static javafx.scene.paint.Color.rgb;


public class Panel extends Application {

    private static int s(int n) {
        int w = 2560, h = 1440;
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            w = (int)screenSize.getWidth();
            h = (int)screenSize.getHeight();

        }
        catch (ExceptionInInitializerError ignored) {

        }
        int c = Math.max(w, h) / Math.min(w, h);
        int nw = w / c;
        int nh = w / c * 49 / 41;
        return (int) (0.86 * ((double) n / (double) 2400.0F * (double) Math.max(nw, nh)));
    }
    Font font;

    {
        try {
            font = Font.loadFont(new FileInputStream("clearsansbold.ttf"),s(50));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    static Group root = new Group();
    static StackPane[] stackPanes = new StackPane[16];
    static Scene scene = new Scene(root,s(1015),s(1195));
    static int score = 0;
    static boolean getLost;
    static Text scoreText;
    static long[] values = {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    static Rectangle[] rect = new Rectangle[16];
    static Text[] texts = new Text[16];
    static Rectangle[] cell = new Rectangle[16];
    static Color[] colors = {rgb(238, 228, 218), rgb(237, 224, 200), rgb(242, 177, 121), rgb(245, 149, 99), rgb(246, 124, 95), rgb(246, 94, 59), rgb(237, 207, 114), rgb(237, 204, 97), rgb(237, 200, 80), rgb(237, 197, 63), rgb(237, 194, 46), rgb(181, 135, 180), rgb(168, 96, 169), rgb(170, 85, 168), rgb(162, 82, 164), rgb(163, 73, 163), rgb(120, 120, 193), rgb(105, 107, 196), rgb(91, 97, 199), rgb(63, 71, 210), rgb(63, 71, 204), rgb(146, 145, 141), rgb(106, 104, 101), rgb(72, 72, 70)};

    public static Color getBackground(long val) {
        return colors[(int) Math.round(Math.log(val) / Math.log(2) - 1)];
    }

    public static Color getForeground(long val) {
        if (val < 8) return rgb(119, 110, 101);
        else return rgb(249, 246, 242);
    }

    @Override
    public void start(Stage stage) {
        Text nameText = new Text("2048");
        nameText.setFill(Color.WHITE);
        nameText.setFont(Font.font("Clear Sans", s(150)));
        StackPane namePane = new StackPane(nameText);
        namePane.setLayoutX(s(30));
        namePane.setLayoutY(s(5));

        Rectangle name = new Rectangle();
        name.setLayoutX(s(400));
        name.setWidth(s(500));
        name.setHeight(s(100));
        scoreText = new Text("Рекорд: " + score);
        scoreText.setFont(font);
        scoreText.setFill(Color.WHITE);
        name.setFill(rgb(39, 39, 39));
        name.setArcHeight(s(30));
        name.setArcWidth(s(30));
        StackPane scorePane = new StackPane(name, scoreText);
        scorePane.setLayoutX(s(470));
        scorePane.setLayoutY(s(25));

        scene.setRoot(root);
        stage.setTitle("2048");
        ObservableList<Node> j = ((Group) scene.getRoot()).getChildren();
        scene.setFill(rgb(15, 15, 15));

        Rectangle bg = new Rectangle(s(40), s(220), s(930), s(930));
        bg.setFill(rgb(60, 60, 60));
        bg.setArcWidth(s(30));
        bg.setArcHeight(s(30));

        Rectangle lost = new Rectangle(s(40), s(220), s(930), s(930));
        lost.setFill(rgb(210, 210, 210, 0));
        lost.setArcWidth(s(30));
        lost.setArcHeight(s(30));
        Text loseText = new Text("Вы проиграли");
        loseText.setFill(rgb(0, 0, 0, 0));
        loseText.setFont(Font.font("Clear Sans", s(100)));
        StackPane losePane = new StackPane(lost, loseText);
        losePane.setLayoutX(s(40));
        losePane.setLayoutY(s(220));

        j.add(namePane);
        j.add(scorePane);

        Button button = new Button("Рестарт");
        button.setOnAction(actionEvent -> {
            lost.setFill(rgb(210, 210, 210, 0));
            loseText.setFill(rgb(0, 0, 0, 0));
            values = new long[]{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            move_gen();
            move_gen();
            getLost = false;
            score = 0;
            Update_Board();
        });
        button.setLayoutX(s(525));
        button.setLayoutY(s(135));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: rgb(79,79,79); -fx-border-radius: "+s(30)+"; -fx-font-family: 'Clear Sans'; -fx-font-size: "+s(30)+"px;");
        button.setPrefWidth(s(400));
        button.setPrefHeight(s(60));
        j.add(bg);
        for (int a = 0; a < 4; a++)
            for (int b = 0; b < 4; b++) {
                int c = a + 4 * b;
                cell[c] = new Rectangle();
                cell[c].setX(s(50 + 30 + (195 + 30) * (3 - b) - 10));
                cell[c].setY(s(250 + 30 + (195 + 30) * (3 - a) - 30));
                cell[c].setWidth(s(195));
                cell[c].setHeight(s(195));
                cell[c].setFill(rgb(80, 80, 80));
                cell[c].setArcWidth(s(30));
                cell[c].setArcHeight(s(30));
                j.add(cell[c]);
            }
        for (int a = 0; a < 4; a++)
            for (int b = 0; b < 4; b++) {
                int c = a + 4 * b;

                rect[c] = new Rectangle(s(50 + 30 + (195 + 30) * (3 - b) - 10), s(250 + 30 + (195 + 30) * (3 - a) - 30), s(195), s(195));
                if (values[1 + c] != 0) rect[c].setFill(getBackground(values[1 + c]));
                else rect[c].setFill(rgb(0, 0, 0, 0));
                rect[c].setArcWidth(s(30));
                rect[c].setArcHeight(s(30));
                rect[c].setLayoutY(s(250 + 30 + (195 + 30) * (3 - a) - 30));
                rect[c].setLayoutX(s(50 + 30 + (195 + 30) * (3 - b) - 10));
                j.add(rect[c]);

                texts[c] = new Text();
                texts[c].setFont(Font.font("Clear Sans", s(100)));
                setFont(c);
                if (values[1 + c] != 0) texts[c].setText(String.valueOf(values[1 + c]));
                else texts[c].setText("");
                texts[c].setX(rect[a + 4 * b].getWidth() / 2);
                texts[c].setY(rect[a + 4 * b].getHeight() / 2);
                texts[c].setFill(getForeground(values[1 + c]));

                stackPanes[c] = new StackPane(rect[c], texts[c]);
                stackPanes[c].setLayoutX(rect[c].getLayoutX());
                stackPanes[c].setLayoutY(rect[c].getLayoutY());
                j.add(stackPanes[c]);
            }
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case W, UP -> move_up();
                case A, LEFT -> move_left();
                case S, DOWN -> move_down();
                case D, RIGHT -> move_right();
            }
            Update_Board();
            if (getLost) {
                lost.setFill(rgb(210, 210, 210, 0.5));
                loseText.setFill(Color.WHITE);
            }
        });
        stage.setScene(scene);
        stage.show();
        j.add(losePane);
        j.add(button);
    }

    private static void setFont(int c) {
        int d;
        if (values[1 + c] < 10) d = 90;
        else if (values[1 + c] < 100 && values[1 + c] > 9) d = 80;
        else if (values[1 + c] < 1000 && values[1 + c] > 99) d = 70;
        else if (values[1 + c] < 10000 && values[1 + c] > 999) d = 60;
        else if (values[1 + c] < 100000 && values[1 + c] > 9999) d = 50;
        else if (values[1 + c] < 1000000 && values[1 + c] > 99999) d = 45;
        else d = 37;
        texts[c].setFont(Font.font("Clear Sans", s(d)));
    } // ---- метод изменяет размер шрифта в зависимости от значения плитки

    public static void main(String[] args) {
        move_gen();
        move_gen(); // ---- создаёт две первые плитки
        launch();
    }

// ---- ход "→" ----
    static int[] a = {0,0,0};
    static int key = 1;

    private static void move() {
        boolean[] check = {false, false, false, false}; // ---- введён новый массив для проверки хода: если ни одна плитка не изменила своего значения, то новые плитки не будут сгенерированы
        for (int i = 1; i < 5; i++) {
            long[] prev = {values[i], values[i + 4], values[i + 8], values[i + 12]};
            for (int r = 0; r < 12; r += 4)
                for (int q = 12; q > r; q -= 4)
                    if (values[i + r] == 0) {
                        for (int w = r; w < q; w += 4) values[i + w] = values[i + w + 4];
                        values[i + q] = 0;
                    } // ---- сдвигаем все плитки к краю, чтобы между ними не осталось пустот
            for (int r = 0; r < 12; r += 4)
                if (values[i + r] == values[i + r + 4] && values[i + r] != 0) {
                    values[i + r] *= 2;
                    score += values[i + r];
                    for (int q = r + 4; q < 12; q += 4) values[i + q] = values[i + q + 4];
                    values[i + 12] = 0;
                    a[2] += 1;
                } // ---- объединяем плитки
            if (values[i] == prev[0] && values[i + 4] == prev[1] && values[i + 8] == prev[2] && values[i + 12] == prev[3])
                check[i - 1] = true;
        }
        if (!(check[0] && check[1] && check[2] && check[3])) move_gen(); // ---- генерируем новые плитки
        getLost = lost(); // ---- обновляем состояние проигрыш
    }
    public static void move_right() {
        key = 4;
        move();
    }

    static void setA(boolean[] check) {

    }

    // ---- ход "←" ----
    public static void move_left() {
        key = 2;
        values = array_inverse_y(values);
        move();
        values = array_inverse_y(values);
    } // ---- ход влево: отражаем поле по вертикальной оси, делаем ход вправо, снова отражаем поле по вертикальной оси

// ---- ход "↑" ----

    public static void move_up() {
        key = 1;
        values = array_rotate_cw_90(values);
        move();
        values = array_rotate_ccw_90(values);
    } // ---- ход вверх: поворачиваем поле по часовой стрелке, делаем ход вправо, поворачиваем в обратном направлении

// ---- ход "↓" ----

    public static void move_down() {
        key = 3;
        values = array_rotate_ccw_90(values);
        move();
        values = array_rotate_cw_90(values);
    } // ---- ход вверх: поворачиваем поле против часовой стрелки, делаем ход вправо, поворачиваем в обратном направлении

    // ---- обновляем поле ----
    private static void Update_Board() {
        for (int a = 0; a < 4; a++)
            for (int b = 0; b < 4; b++) {
                int c = a + 4 * b; // ---- введена новая переменная, чтобы сократить запись и оптимизировать вычисления
                if (values[1 + c] != 0) rect[c].setFill(getBackground(values[1 + c]));
                else rect[c].setFill(rgb(0, 0, 0, 0)); // ---- обновляет цвета плиток (если 0 то скрывает плитку)
                if (values[1 + c] != 0) texts[c].setText(String.valueOf(values[1 + c]));
                else texts[c].setText(""); // ---- изменяет текст на значение плитки (если 0, то оставляет пустой текст)
                texts[c].setFill(getForeground(values[1 + c])); // ---- обновляет цвет плитки
                setFont(c); // ---- обновляет размер шрифта плитки
                scoreText.setText("Рекорд: " + score); // ---- обновляет значение рекорда
            }
    } // ---- метод обновляет параметры поля: цвета плиток, цвета текста с их значениями, размер шрифта этого текста и сам текст, значение рекорда

// ---- работа с массивами ----

    private static long[] array_inverse_y(long[] a) {
        return new long[]{-1, a[13], a[14], a[15], a[16], a[9], a[10], a[11], a[12], a[5], a[6], a[7], a[8], a[1], a[2], a[3], a[4]};
    } // ---- метод изменяет порядок элементов основного массива так, чтобы на поле ячейки были отражены относительно вертикальной оси

    private static long[] array_rotate_cw_90(long[] a) {
        return new long[]{-1, a[4], a[8], a[12], a[16], a[3], a[7], a[11], a[15], a[2], a[6], a[10], a[14], a[1], a[5], a[9], a[13]};
    } // ---- то же, что и предыдущий метод, но вместо отражения от поворачивает ячейки относительно центра доски на 90 градусов по часовой стрелке

    private static long[] array_rotate_ccw_90(long[] a) {
        return array_rotate_cw_90(array_rotate_cw_90(array_rotate_cw_90(a)));
    } // ---- то же, что и предыдущий метод, но поворот на 90 градусов против часовой стрелки

// ---- генерация плиток ----

    public static void move_gen() {
        try {
            int i = 1 + (int) (Math.random() * (values.length - 1)); // ---- рандомный выбор из свободных плиток
            if (values[i] != 0)
                move_gen(); // ---- повторное воспроизведение метода если ячейка уже занята другой плиткой
            else values[i] = random_tile(); // ---- присвоение выбранной пустой ячейке значения новой плитки (2 или 4)
        } catch (StackOverflowError ignored) {
            // ---- при воспроизведении метода возникает исключение. Игнорируем его
        }
    }

// ---- выбор плитки ----

    public static int random_tile() {
        double rand_tile = Math.random(); // ---- случайное число в промежутке [0 ; 1)
        if (rand_tile >= 0.9)
            return 4; // ---- если число больше либо равно 0.9, то устанавливаем значение вывода 4, иначе - 2. Таким образом плитка со значением 2 будет сгенерирована с 90-процентной вероятностью, а плитка со значением 4 - 10-процентной.
        else return 2;
    }

// ---- обнаружение поражения ----

    static int f = 1; // ---- переменная, обозначающая число соседних неравных плиток

    public static boolean lost() {
        f = 1;
        if (values[16] != 0) {
            for (int i = 0; i < 12; i += 4)
                for (int j = 1; j < 4; j++)
                    if (values[i + j] != values[i + j + 1] && values[i + j] != values[i + j + 4] && values[i + j] != 0)
                        f += 1;
            for (int i = 0; i < 12; i += 4) if (values[i + 4] != values[i + 8] && values[i + 4] != 0) f += 1;
            for (int j = 1; j < 4; j++) if (values[12 + j] != values[13 + j] && values[12 + j] != 0) f += 1;
        } // ---- метод проверяет все соседние ячейки. Если они не равны, то добавляет 1 к f, в ином случае ничего не делает
        return f == 16; // ---- если f = 16 (нет равных соседних ячеек) то на вывод идёт true (случай поражения), иначе - false (есть равные соседние ячейки)
    }
}