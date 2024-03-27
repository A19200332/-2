
import $ from 'jquery';
export default {
    state: {
        bots: [

        ]
    },
    getters: {
    },
    mutations: {
        updatebots(state, da) {
            for (let bot in da) {
                state.bots.push(bot);
            }
        }
    },
    actions: {
        getlist(context, data) {
            $.ajax({
                url: "http://localhost:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + data.user.token,
                },
                success(resp) {
                    context.commit("updatebots", resp);
                    data.success(resp);
                },
                error() {
                    //data.error(resp);
                }

            })
        }
    },
    modules: {

    }
}
