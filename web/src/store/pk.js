
export default {
    state: {
        status: 'matching',///matching 表示正在匹配,playing正在玩
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        gamemap: null,
        a_id: 0,
        a_sx: 0,
        a_sy: 0,
        b_id: 0,
        b_sx: 0,
        b_sy: 0,
        gameObject: null,
        loser: "none" //none all A B
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateopponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updatestatus(state, status) {
            state.status = status;
        },
        updategame(state, game) {
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
            state.gamemap = game.game_map;
        },
        updategameObject(state, gameObject) {
            state.gameObject = gameObject;
        },
        updatedirection(state, data) {
            state.a_direction = data.a_direction;
            state.b_direction = data.b_direction;
        },
        updatesnakestatus(state, data) {
            if (data.loser === "all") {
                state.a_snake_status = "die";
                state.b_snake_status = "die";
            } else if (data.loser === "A") {
                state.a_snake_status = "die";
            } else if (data.loser === "B") {
                state.b_snake_status = "die";
            }
        },
        updateloser(state, loser) {
            state.loser = loser;
        }
    },
    actions: {

    },
    modules: {

    }
}
