import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";
export class Snake extends AcGameObject {
    constructor(info, gamemap) {
        super();
        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        this.cells = [new Cell(info.r, info.c)];
        this.speed = 5;
        this.direction = -1;
        this.next_cell = null;
        this.status = "idle"; ///idle 静止 move 移动 die 死亡
        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];
        this.eps = 1e-2; ///允许的误差

        this.step = 0;

        this.eye_direction = 0;
        if (this.id == 1) this.eye_direction = 2;
        this.eye_dx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1]
        ];
        this.eye_dy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1]
        ];
    }
    nextstep() { ///将蛇的状态变为走下一步
        const d = this.direction;
        this.eye_direction = d;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1;
        this.status = "move";
        this.step++;

        const k = this.cells.length;
        for (let i = k; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }

    }
    set_direction(d) {
        this.direction = d;
    }
    check_tail_increasing() {
        if (this.step <= 10) return true;
        if (this.step % 3 === 1) return true;
        return false;
    }
    start() {

    }

    move() {

        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < this.eps) {
            this.cells[0] = this.next_cell;
            this.next_cell = null;
            this.status = "idle";
            if (!this.check_tail_increasing()) {
                this.cells.pop();
            }
        } else {
            const move_distance = this.speed * this.timedelta / 1000;
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;
            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const now = this.cells[k - 1], tar = this.cells[k - 2];
                const ddx = tar.x - now.x;
                const ddy = tar.y - now.y;
                const ddistance = Math.sqrt(ddx * ddx + ddy * ddy);
                this.cells[k - 1].x += move_distance * ddx / ddistance;
                this.cells[k - 1].y += move_distance * ddy / ddistance;
            }

        }
    }
    update() {

        if (this.status === "move") {
            this.move();
        }
        this.render();

    }
    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        ctx.fillStyle = this.color;
        if (this.status === "die") {
            ctx.fillStyle = "white";
        }
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2, 0, Math.PI * 2);
            ctx.fill();
        }
        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i], b = this.cells[i - 1];
            if (Math.abs(a.x - b.x) <= this.eps && Math.abs(a.y - b.y) <= this.eps) continue;
            else if (Math.abs(a.x - b.x) <= this.eps) {
                ctx.fillRect((a.x - 0.5) * L, Math.min(a.y, b.y) * L, L, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.5) * L, Math.abs(a.x - b.x) * L, L);
            }
        }
        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i++) {
            const xxx = (this.cells[0].x + 0.2 * this.eye_dx[this.eye_direction][i]) * L;
            const yyy = (this.cells[0].y + 0.2 * this.eye_dy[this.eye_direction][i]) * L;
            ctx.beginPath();
            ctx.arc(xxx, yyy, 0.05 * L, 0, 2 * Math.PI);
            ctx.fill();
        }
    }
}