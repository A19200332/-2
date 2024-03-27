import store from "@/store";
import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";
export class GameMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.in_walls = 20;
        this.gamemap = store.state.pk.gamemap;
        this.L = 0;
        this.rows = 13;
        this.cols = 14;
        this.walls = [];
        this.snakes = [
            new Snake({ id: 0, color: "#F94848", r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 1, color: "#4876EC", r: 1, c: this.cols - 2 }, this),

        ];
    }
    if_ready() {
        for (const snake of this.snakes) {
            if (snake.direction === -1) return false;
            if (snake.status !== "idle") return false;
        }
        return true;
    }
    // check_if_connect(g, sx, sy, ex, ey) {
    //     if (sx == ex && sy == ey) return true;
    //     g[sx][sy] = true;
    //     let vx = [1, 0, 0, -1], vy = [0, 1, -1, 0];
    //     for (let i = 0; i < 4; i++) {
    //         let tx = sx + vx[i];
    //         let ty = sy + vy[i];
    //         if (!g[tx][ty] && this.check_if_connect(g, tx, ty, ex, ey)) return true;
    //     }
    //     return false;
    // }
    create_walls() {
        // const g = [];
        // for (let r = 0; r < this.rows; r++) {
        //     g[r] = [];
        //     for (let c = 0; c < this.cols; c++) {
        //         g[r][c] = false;
        //     }
        // }
        // for (let r = 0; r < this.rows; r++) {
        //     g[r][0] = g[r][this.cols - 1] = true;
        // }
        // for (let c = 0; c < this.rows; c++) {
        //     g[0][c] = g[this.rows - 1][c] = true;
        // }
        // for (let i = 0; i < this.in_walls / 2; i++) {
        //     for (let j = 0; j < 1000; j++) {
        //         let r = parseInt(Math.random() * this.rows);
        //         let c = parseInt(Math.random() * this.cols);
        //         if (r > c || g[r][c]) continue;
        //         if (c == 1 && r == this.rows - 2 || c == this.cols - 2 && r == 1) continue;
        //         g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
        //         break;
        //     }
        // }
        // const copy_g = JSON.parse(JSON.stringify(g));
        // if (!this.check_if_connect(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false;
        const g = this.gamemap;
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c] === 1) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
        return true;
    }
    add_listening_events() {

        this.ctx.canvas.focus();
        // const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;
            if (e.key === "w") d = 0;
            else if (e.key === "d") d = 1;
            else if (e.key === "s") d = 2;
            else if (e.key === "a") d = 3;
            if (d >= 0) {
                store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d,
                }));
                // this.snakes[0].direction = store.state.pk.a_direction;
                // this.snakes[1].direction = store.state.pk.b_direction;
                // this.snakes[0].status = store.state.pk.a_snake_status;
                // this.snakes[1].status = store.state.pk.b_snake_status;
            }
        });
    }
    start() {
        this.create_walls();
        this.add_listening_events();
    }
    updatesize() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;

    }
    next_step() {
        for (const snake of this.snakes) {
            snake.nextstep();
        }
    }
    check_valid(cell) {
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c) return false;
        }
        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {
                k--;
            }
            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) return false;
            }
        }
        return true;
    }
    update() {
        this.updatesize();
        if (this.if_ready()) {
            this.next_step();
        }
        this.reander();
    }
    reander() {

        const even_color = "#ADE457", odd_color = "#A5DE4E";
        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if ((i + j) % 2 == 0) this.ctx.fillStyle = even_color;
                else this.ctx.fillStyle = odd_color;
                this.ctx.fillRect(j * this.L, i * this.L, this.L, this.L);
            }

        }
    }
}