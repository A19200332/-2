<template>
    <div class="gamemap" ref="parent">
        <canvas ref="canvas" tabindex="0">

        </canvas>
    </div>
</template>
<script>
import { GameMap } from '@/assets/scripts/GameMap';
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';
export default {
    name: "GameMap",
    setup() {
        let parent = ref(null);
        let canvas = ref(null);
        const store = useStore();
        onMounted(() => {
            store.commit("updategameObject",
                new GameMap(canvas.value.getContext("2d"), parent.value, store)
            );
        })
        return {
            parent,
            canvas,
        }
    }
}
</script>
<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>