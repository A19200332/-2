<template>
    <PlayGround v-if="$store.state.pk.status==='playing' " />
    <MatchGround v-if="$store.state.pk.status==='matching'" />
    <ResultBoard v-if="$store.state.pk.loser!=='none'" />
</template>
<script>
import PlayGround from "@/components/PlayGround.vue";
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
import MatchGround from "../../components/MatchGround.vue";
import ResultBoard from "../../components/ResultBoard.vue";

export default {
    name: "PkIndexView",
    components: {
        PlayGround,
        MatchGround,
        ResultBoard,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}/`;
        let socket = null;
        onMounted(() => {
            socket = new WebSocket(socketUrl);

            store.commit("updateopponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            });
            socket.onopen = () => {
                console.log("connected!");
                store.commit("updateSocket", socket);
            }
            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                console.log(data);
                if (data.event === "start-matching") {
                    store.commit("updateopponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    store.commit("updategame", data.game);
                    setTimeout(() => {
                        store.commit("updatestatus", "playing");
                    }, 1000)
                } else if (data.event === "move") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);

                } else if (data.event === "result") {
                    store.commit("updateloser", data.loser);
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    if (data.loser === "all" || data.loser === "A") {
                        snake0.status = "die";
                    }
                    if (data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }

                }
            }
            socket.onclose = () => {
                console.log("disconnected!");
            }
        });
        onUnmounted(() => {
            socket.close();
        });
        return {
            store,
        }
    },
}
</script>
<style>

</style>