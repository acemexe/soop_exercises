//this file presents two basic examples of the creation and usage of complex data types
//until not told otherwise remove the code below before creating your solutions

import static java.lang.IO.*;  //including package IO to be able to use simple print()
import static java.lang.IO.println;
import static term.term.*;     //includes package term (createElements() functions were moved there)


int randomize(int x) {
    int randomnumber = (int)(Math.random() * x + 1);
    return randomnumber;
}
final double GRAVITY = 0.15;

final boolean MOVEMENT_SQ_Y = true;
final boolean Y_DIRECTION = false;
final int MAX_X = 280;
final int MAX_Y = 78;
final int SAFE_X = MAX_X - 1;
final int SAFE_Y = MAX_Y - 1;
final int MIDDLE_X = MAX_X / 2;
final int MIDDLE_Y = MAX_Y / 2;

public class Square {
    int x;
    int y;
    int old_x;
    int old_y;
    int direction;
    int color;
    int speed;
    int size;
    double vy;
    double yReal;
}

public class Player {
    int x;
    int y;
    int old_x;
    int old_y;
    int color;
    int sound;
    int score;
    String up_key;
    String down_key;
    String left_key;
    String right_key;
    int control;
    int target_index;
}

Square makeSquare(int x, int y, int old_x, int old_y,
                  int direction, int color, int speed, int size) {
    Square s = new Square();
    s.x = x;
    s.y = y;
    s.old_x = old_x;
    s.old_y = old_y;
    s.direction = direction;
    s.color = color;
    s.speed = speed;
    s.size = size;
    s.vy = 0.0;
    s.yReal = y;

    return s;
}

Player makePlayer(int x, int y, int old_x, int old_y,  int color, int sound, int score,
                  String up_key, String down_key, String left_key, String right_key, int control) {
    Player p = new Player();
    p.x = x;
    p.y = y;
    p.old_x = old_x;
    p.old_y = old_y;
    p.color = color;
    p.sound = sound;
    p.score = score;
    p.up_key = up_key;
    p.down_key = down_key;
    p.left_key = left_key;
    p.right_key = right_key;
    p.control = control;
    p.target_index = -1;
    return p;
}

Player pl1 = makePlayer(60, 15, 60, 15, ltgreen, 600, 0, "arrow_up", "arrow_dn", "arrow_lt", "arrow_rt", 0);
Player pl2 = makePlayer(MAX_Y, 15, MAX_Y, 15, yellow, 500, 0, "w", "s", "a", "d", 0);
Player pl3 = makePlayer(40, 15, MAX_Y, 15, green, 200, 0, "w", "s", "a", "d", 0);
Player[] players = {pl1, pl2, pl3};

int coll_frame = 0;
int coll_sq = 0;

void draw_horiz_line_c(int x1, int x2, int y, char c) {
    gotoxy(x1,y);

    int numberofchars = x2 - x1;
    String chars = "";
    for  (int i = 1; i <= numberofchars; i++) {
        chars += c;
    }
    print(chars);
}

void draw_vert_line_c(int x, int y1, int y2, char c) {
    gotoxy(x,y1);

    int numberofchars = y2 - y1;
    for   (int i = 1; i <= numberofchars; i++) {
        print(c);
        gotoxy(x,y1 + i);
    }
}

void draw_frame_c(int x1, int y1, int x2, int y2, char c) {
    draw_horiz_line_c(x1,x2,y1,c);
    draw_vert_line_c(x1,y1,y2,c);
    draw_vert_line_c(x2,y1,y2,c);
    draw_horiz_line_c(x1,x2 +1 ,y2, c);
}

int guard_pos_x(int n, int size) {
    if (n <= 1) {

        return 2;
    }
    else if (n >= SAFE_X - size) {

        return SAFE_X - size;
    }
    else {
        return n;
    }
}

int guard_pos_y(int n, int size) {
    if (n <= 1) {

        return 2;
    }
    else if (n >= SAFE_Y - size) {

        return SAFE_Y - size;
    }
    else {
        return n;
    }

}

boolean isColliding(int x1, int y1, int size1, int x2, int y2, int size2) {

    int leftA   = x1;
    int rightA  = x1 + size1;
    int topA    = y1;
    int bottomA = y1 + size1;

    int leftB   = x2;
    int rightB  = x2 + size2;
    int topB    = y2;
    int bottomB = y2 + size2;


    if (rightA < leftB || rightB < leftA || bottomA < topB || bottomB < topA) {
        return false;
    }
    coll_sq += 1;
    return true;
}

int randomNewDirection(int currentDir) {
    int dir = currentDir;
    while (dir == currentDir) {
        dir = randomize(8);
    }
    return dir;
}

int changedir (int pos_x, int pos_y, int size,  int direction, Square[] all_squares, int index) {
    int newdir = direction;
    //top left corner
    // 1 up 2 down 3 left 4 right 5 diag up left 6 diag up right 7 diag down left 8 diag down right
    int [] posdirs = null;
    if (pos_x == 2 && pos_y == 2 && (direction == 1 || direction == 3 || direction == 7 || direction == 5 || direction == 6)) { //top left
        posdirs = new int[]{2, 4, 8};
        if (!(Y_DIRECTION)) posdirs = new int[]{4};

    } else if (pos_x == 2 && pos_y == SAFE_Y - size && (direction == 2 || direction == 3 || direction == 5 || direction == 7 || direction == 8)) { // bottom left
        posdirs = new int[]{1, 4, 6};
        if (!(Y_DIRECTION))  posdirs = new int[]{4};
    } else if (pos_x == SAFE_X - size && pos_y == 2 && (direction == 1 || direction == 4 || direction == 5 || direction == 6 || direction == 8)) { // top right
        posdirs = new int[]{2, 3, 7};
        if  (!(Y_DIRECTION))  posdirs = new int[]{3};
    } else if (pos_x == SAFE_X - size && pos_y == SAFE_Y - size && (direction == 2 || direction == 4 || direction == 6 || direction == 7 || direction == 8)) { // bottom right
        posdirs = new int[]{1, 3, 5};
        if (!(Y_DIRECTION))  posdirs = new int[]{3};
    } else if (pos_x == SAFE_X - size && (direction == 4 || direction == 6 || direction == 8)) { // right edge
        posdirs = new int[]{1, 2, 3, 5, 7};
        if (!(Y_DIRECTION))   posdirs = new int[]{3};
    } else if (pos_x == 2 && (direction == 3 || direction == 5 || direction == 7)) { // left edge
        posdirs = new int[]{1, 2, 4, 6, 8};
        if (!(Y_DIRECTION))  posdirs = new int[]{4};
    } else if (pos_y == 2 && (direction == 1 || direction == 5 || direction == 6)) { // top edge
        posdirs = new int[]{2, 3, 4, 7, 8};
        if (!(Y_DIRECTION))  posdirs = new int[]{3,4};
    } else if (pos_y == SAFE_Y - size && (direction == 2 || direction == 7 || direction == 8)) { // bottom edge
        posdirs = new int[]{1, 3, 4, 5, 6};
        if (!(Y_DIRECTION))  posdirs = new int[]{3,4};
    }

    if (posdirs != null) {
        newdir = posdirs[randomize(posdirs.length) - 1];
        coll_frame += 1;
    }

    return newdir;

}

Square[] initialize_squares(int n_squares) {
    Square[] squares = new Square[n_squares];
    for (int i=0; i<n_squares; i++) {

        int randomdir = randomize(8);
        int randomcolor = randomize(12);
        int randomspeed = randomize(5);
        int randomsize = randomize(6) - 1;
        int randomx = randomize((SAFE_X - 1) - randomsize) + 1 ;
        int randomy = randomize((SAFE_Y -1) - randomsize) + 1;
        squares[i] = makeSquare(randomx, randomy, randomx, randomy, randomdir, randomcolor, randomspeed, randomsize);
        squares[i].yReal = squares[i].y;
        squares[i].vy = 0;

        //println(squares[i].x + " " + squares[i].y + " " + squares[i].color + " " + squares[i].speed + " " + squares[i].speed +  " " + squares[i].direction + " " + squares[i].size);
    }
    return squares;
}

Square[] update_squares(Square[] squares, Player[] players) {
    boolean[] dead = new boolean[squares.length];

    for (int i = 0; i < squares.length; i++) {
        Square curr_square = squares[i];
        int old_x = curr_square.x;
        int old_y = curr_square.y;
        int direction = curr_square.direction;
        int color = curr_square.color;
        int speed = curr_square.speed;
        int size = curr_square.size;
        int new_x = 98;
        int new_y = 20;
        boolean coll = false;
        double vy = curr_square.vy;
        double yReal = curr_square.yReal;
        int top = 2;
        int bottom = SAFE_Y - size;
        if (yReal == 0.0) {
            yReal = old_y;
        }

        for (int j = 0; j < players.length; j++) {
            Player curr_player = players[j];
            if (isColliding(curr_player.x, curr_player.y, 0, old_x, old_y, size)) {
                dead[i] = true;

                if (size == 0) {
                    curr_player.score += 4;
                } else {
                    curr_player.score += 1;
                }

                sound(curr_player.sound, 10);
                draw_frame_c(old_x, old_y, old_x + size, old_y + size, ' ');
                players[j] = curr_player;

                coll = true;
            }
        }
        if (coll) {
            continue;
        }
        int yspeed = 0;
        switch(direction) {
            case 1: //up
                new_x = guard_pos_x(old_x, size);
                new_y = guard_pos_y(old_y - speed, size);
                if (MOVEMENT_SQ_Y) yspeed = -speed;
                break;
            case 2: //down
                new_x = guard_pos_x(old_x, size);
                new_y = guard_pos_y(old_y + speed, size);
                if (MOVEMENT_SQ_Y) yspeed = speed;
                break;
            case 3: //left
                new_x = guard_pos_x(old_x - speed, size);
                new_y = guard_pos_y(old_y, size);
                break;
            case 4: //right
                new_x = guard_pos_x(old_x + speed, size);
                new_y = guard_pos_y(old_y, size);
                break;
            case 5: //diag up left
                new_x = guard_pos_x(old_x - speed, size);
                new_y = guard_pos_y(old_y - speed, size);
                //yspeed = -speed;
                break;
            case 6: //diag up right
                new_x = guard_pos_x(old_x + speed, size);
                new_y = guard_pos_y(old_y - speed, size);
                if (MOVEMENT_SQ_Y) yspeed = -speed;
                break;
            case 7: //diag down left
                new_x = guard_pos_x(old_x - speed, size);
                new_y = guard_pos_y(old_y + speed, size);
                if (MOVEMENT_SQ_Y) yspeed = speed;
                break;
            case 8: //diag down right
                new_x = guard_pos_x(old_x + speed, size);
                new_y = guard_pos_y(old_y + speed, size);
                if (MOVEMENT_SQ_Y) yspeed = speed;
                break;
        }

        int new_direction = changedir(new_x, new_y, size , curr_square.direction, squares, i);







        for (int j = 0; j < squares.length; j++) {
            if (j == i) continue;

            Square other = squares[j];

            if (isColliding(new_x, new_y, size, other.x, other.y, other.size)) {

                new_direction = randomNewDirection(new_direction);

                switch(new_direction) {
                    case 1: // up
                        new_x = guard_pos_x(old_x, size);
                        new_y = guard_pos_y(old_y - speed, size);
                        if (MOVEMENT_SQ_Y) yspeed = -speed;

                        break;
                    case 2: // down
                        new_x = guard_pos_x(old_x, size);
                        new_y = guard_pos_y(old_y + speed, size);
                        if (MOVEMENT_SQ_Y) yspeed = speed;

                        break;
                    case 3: // left
                        new_x = guard_pos_x(old_x - speed, size);
                        new_y = guard_pos_y(old_y, size);
                        break;
                    case 4: // right
                        new_x = guard_pos_x(old_x + speed, size);
                        new_y = guard_pos_y(old_y, size);
                        break;
                    case 5: // diag up left
                        new_x = guard_pos_x(old_x - speed, size);
                        new_y = guard_pos_y(old_y - speed, size);
                        if (MOVEMENT_SQ_Y) yspeed = -speed;

                        break;
                    case 6: // diag up right
                        new_x = guard_pos_x(old_x + speed, size);
                        new_y = guard_pos_y(old_y - speed, size);
                        if (MOVEMENT_SQ_Y) yspeed = -speed;

                        break;
                    case 7: // diag down left
                        new_x = guard_pos_x(old_x - speed, size);
                        new_y = guard_pos_y(old_y + speed, size);
                        if (MOVEMENT_SQ_Y) yspeed = speed;

                        break;
                    case 8: // diag down right
                        new_x = guard_pos_x(old_x + speed, size);
                        new_y = guard_pos_y(old_y + speed, size);
                        if (MOVEMENT_SQ_Y) yspeed = speed;

                        break;
                }

                break;
            }
        }

        vy += GRAVITY;
        yReal += yspeed + vy;

        if (yReal > bottom) {
            yReal = bottom;
            vy = -vy;
        } else if (yReal < top) {
            yReal = top;
            vy = -vy;
        }


        new_y = (int)Math.round(yReal);


        boolean overlap = false;
        for (int j = 0; j < squares.length; j++) {
            if (j == i || dead[j]) continue;

            Square other = squares[j];
            if (isColliding(new_x, new_y, size, other.x, other.y, other.size)) {
                overlap = true;
                break;
            }
        }

        if (overlap) {
            vy = -vy;
            yReal = old_y;
            new_y = old_y;
        }



        Square updated = makeSquare(new_x, new_y, old_x, old_y, new_direction, color, speed, size);


        updated.vy = vy;
        updated.yReal = yReal;
        squares[i] = updated;
    }



    int aliveCount = 0;
    for (int i = 0; i < squares.length; i++) {
        if (!dead[i]) {
            aliveCount++;
        }
    }

    Square[] newSquares = new Square[aliveCount];
    int idx = 0;
    for (int i = 0; i < squares.length; i++) {
        if (!dead[i]) {
            newSquares[idx++] = squares[i];
        }
    }

    return newSquares;
}


Player update_player(Player player, String key, Square[] squares) {
    if (player.control == 0) {
        //human mode
        if (key.equals(player.up_key)) {
            int old_x = player.x;
            int old_y = player.y;
            int x = guard_pos_x(old_x, 0);
            int y = guard_pos_y(old_y - 1, 0);
            player.x = x;
            player.y = y;
            player.old_x = old_x;
            player.old_y = old_y;
        }
        if (key.equals(player.down_key)) {
            int old_x = player.x;
            int old_y = player.y;
            int x = guard_pos_x(old_x, 0);
            int y = guard_pos_y(old_y + 1, 0);
            player.x = x;
            player.y = y;
            player.old_x = old_x;
            player.old_y = old_y;
        }
        if (key.equals(player.left_key)) {
            int old_x = player.x;
            int old_y = player.y;
            int x = guard_pos_x(old_x - 1, 0);
            int y = guard_pos_y(old_y, 0);
            player.x = x;
            player.y = y;
            player.old_x = old_x;
            player.old_y = old_y;
        }
        if (key.equals(player.right_key)) {
            int old_x = player.x;
            int old_y = player.y;
            int x = guard_pos_x(old_x + 1, 0);
            int y = guard_pos_y(old_y, 0);
            player.x = x;
            player.y = y;
            player.old_x = old_x;
            player.old_y = old_y;
        }
    } else if (player.control == 1) {
        //nearest mode
        if (squares.length > 0) {
            int nearest_square = 0;
            int nearest_dist = Integer.MAX_VALUE;

            for (int i = 0; i < squares.length; i++) {
                Square s = squares[i];
                int dx = Math.abs(player.x - s.x);
                int dy = Math.abs(player.y - s.y);
                int _dist = dx + dy;
                if (_dist < nearest_dist) {
                    nearest_dist = _dist;
                    nearest_square = i;
                }
            }

            Square target = squares[nearest_square];

            int old_x = player.x;
            int old_y = player.y;
            int new_x = player.x;
            int new_y = player.y;

            int dx = target.x - player.x;
            int dy = target.y - player.y;

            if (Math.abs(dx) >= Math.abs(dy)) {
                if (dx > 0) new_x = guard_pos_x(player.x + 1, 0);
                else if (dx < 0) new_x = guard_pos_x(player.x - 1, 0);
                new_y = guard_pos_y(player.y, 0);
            } else {
                if (dy > 0) new_y = guard_pos_y(player.y + 1, 0);
                else if (dy < 0) new_y = guard_pos_y(player.y - 1, 0);
                new_x = guard_pos_x(player.x, 0);
            }

            player.old_x = old_x;
            player.old_y = old_y;
            player.x = new_x;
            player.y = new_y;
        }
    } else if (player.control == 2) {
        //random selected square mode
        if (squares.length > 0) {

            if (player.target_index < 0 || player.target_index >= squares.length) {
                player.target_index = randomize(squares.length) - 1;
            }

            Square target = squares[player.target_index];

            int old_x = player.x;
            int old_y = player.y;
            int new_x = player.x;
            int new_y = player.y;

            int dx = target.x - player.x;
            int dy = target.y - player.y;

            if (Math.abs(dx) >= Math.abs(dy)) {
                if (dx > 0) new_x = guard_pos_x(player.x + 1, 0);
                else if (dx < 0) new_x = guard_pos_x(player.x - 1, 0);
                new_y = guard_pos_y(player.y, 0);
            } else {
                if (dy > 0) new_y = guard_pos_y(player.y + 1, 0);
                else if (dy < 0) new_y = guard_pos_y(player.y - 1, 0);
                new_x = guard_pos_x(player.x, 0);
            }

            player.old_x = old_x;
            player.old_y = old_y;
            player.x = new_x;
            player.y = new_y;

            if (player.x == target.x && player.y == target.y) {
                player.target_index = -1;
            }
        }
    }
    return player;
}

void draw_squares(Square[] squares) {
    for (int i=0; i<squares.length; i++) {
        Square curr_square = squares[i];
        int x = curr_square.x;
        int y = curr_square.y;
        int old_x = curr_square.old_x;
        int old_y = curr_square.old_y;
        int color = curr_square.color;
        int size = curr_square.size;
        setfgcolor(color);

        draw_frame_c(old_x, old_y, old_x + size, old_y + size, ' ');
        draw_frame_c(x, y, x + size, y + size, '█');
        setfgcolor(white);
    }

}

void draw_player(Player player) {
    gotoxy(player.old_x, player.old_y);
    print(' ');
    gotoxy(player.x, player.y);
    setfgcolor(player.color);
    print('#');
    setfgcolor(white);
}


void load_control_modes(Player[] players) {
    try {
        BufferedReader br = new BufferedReader(new FileReader("conf.cfg"));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("Player")) {
                String[] parts = line.split(":");
                if (parts.length != 2) continue;

                String left = parts[0].trim();
                String right = parts[1].trim();

                if (left.startsWith("Player")) {
                    String numStr = left.replace("Player", "").trim();
                    int index = Integer.parseInt(numStr) - 1;
                    int value = Integer.parseInt(right);

                    if (index >= 0 && index < players.length) {
                        players[index].control = value;
                    }
                }
            }
        }

        br.close();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}


void unlimitedSquares(int n_squares) {

    long timems = 60L * 1000000000;
    int remaining_sq = n_squares;
    int eaten_sq = 0;
    int delaycount = 0;
    coll_sq = 0;
    coll_frame = 0;
    Square[] squares = initialize_squares(n_squares);
    draw_frame_c(1,1, MAX_X, MAX_Y,'*');
    cursor_hide();


    load_control_modes(players);

    boolean quit = false;
    boolean pause = false;
    while (true) {
        long start = System.nanoTime();
        if (quit) {
            break;
        }
        String key = "none";
        if (keypressed()) {
            key = readkeystr();
            if (key.equals("q") || key.equals("Q")) {
                quit = true;
                break;
            }
        }
        draw_squares(squares);


        for (int i=0; i < players.length; i++) {
            players[i] = update_player(players[i], key, squares);
            draw_player(players[i]);
        }


        delaycount++;
        if (delaycount % 1 == 0) {
            squares = update_squares(squares, players);
        }
        eaten_sq = n_squares - squares.length;
        remaining_sq = n_squares - eaten_sq;

        gotoxy(2, MAX_Y);
        long end = System.nanoTime();;

        timems -= end - start;

        String seconds = String.format("%.2f", timems / 1000000000.0);;
        System.out.printf("| Time remaining: " + seconds + "s Square collisions: " + coll_sq + " Frame collisions: " + coll_frame + " Score P1: " + players[0].score + " Score P2: " + players[1].score + " Alive Squares: " + remaining_sq + " | ");

        String scores = "| Scores:";
        for (int i=0; i <= players.length; i++) {
            if (players.length == i) {
                scores = scores + " |";
            } else {
                scores = scores + " P" + (i + 1) + ": " + players[i].score;
            }

        }
        gotoxy(2,1);
        print(scores);
        if (remaining_sq == 0) {
            gotoxy(MIDDLE_X, MIDDLE_Y - 3);
            print("-------------------------");
            gotoxy(MIDDLE_X, MIDDLE_Y - 2);
            print("|      YOU WON !        |");
            gotoxy(MIDDLE_X, MIDDLE_Y - 2);
            print(" |");
            gotoxy(MIDDLE_X, MIDDLE_Y - 1);
            print("-------------------------");
            delay(4000);
            break;
        }

        if (timems <= 0) {
            gotoxy(MIDDLE_X, MIDDLE_Y - 3);
            print("--------------");
            gotoxy(MIDDLE_X, MIDDLE_Y -2);
            print("| GAME OVER ! |");
            gotoxy(MIDDLE_X, MIDDLE_Y -1);
            print("--------------");
            delay(4000);
            break;
        }


    }
    cursor_show();

}


void main() {
    clrscr();
    sound(600, 10);
    print("How many squares? : ");
    int sq = new Scanner(System.in).nextInt();


    clrscr();
    unlimitedSquares(sq);
    clrscr();


    //println("Press any key to continue...");

}
