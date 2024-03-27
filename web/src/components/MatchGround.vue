<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-name">
                    {{store.state.user.username}}
                </div>
            </div>
            <div class="col-4">
                <div class="user-select-bot">
                    <select v-model="bot_id" class="form-select" aria-label="Default select example">
                        <option selected value="-1">亲自出马</option>
                        <option :value="bot.id" v-for="bot in bots" :key="bot.id">{{bot.title}}</option>
                    </select>
                </div>
            </div>
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-name">
                    {{store.state.pk. opponent_username}}
                </div>
            </div>
            <div class="col-12" style="text-align: center;padding-top: 15vh;">
                <button type="button" class="btn btn-warning btn-lg" @click="change">{{btn_context}}</button>
            </div>
        </div>
    </div>
</template>
<script>
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';
export default {
    name: "MatchGround",
    setup() {
        const store = useStore();
        let btn_context = ref('开始匹配');
        let bots = ref([]);
        let bot_id = ref("-1");
        const change = () => {
            if (btn_context.value === "开始匹配") {
                btn_context.value = "取消";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: bot_id.value,
                }));
            } else {
                btn_context.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        }
        let getlist = () => {
            $.ajax({
                url: "http://localhost:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                    console.log(resp)
                },
                error() {
                    //data.error(resp);
                }

            })
        }
        getlist();
        return {
            store,
            btn_context,
            change,
            getlist,
            bots,
            bot_id,
        }
    }


}
</script>
<style scoped>
.matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-color: rgba(50, 50, 50, 0.5)
}

div.user-photo {
    text-align: center;
    padding-top: 10vh;

}

div.user-photo>img {
    border-radius: 50%;
    width: 20vh;
}

div.user-name {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
    padding-top: 2vh;
}

div.user-select-bot {
    padding-top: 20vh;

}

div.user-select-bot>select {
    width: 80%;
    margin: 0 auto;

}
</style>