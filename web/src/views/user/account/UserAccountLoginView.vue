<template>
    <MyContainer>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password"
                            placeholder="请输入密码">
                    </div>
                    <div class="error_massage">{{ error_massage }}</div>
                    <button type="submit" class="btn btn-primary mybutton">提交</button>

                </form>
            </div>
        </div>
    </MyContainer>
</template>
<script>
import MyContainer from '@/components/MyContainer.vue';
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '../../../router/index';
export default {
    name: "UserAccountLoginView",
    components: {
        MyContainer,
    },
    setup() {
        const store = useStore();
        const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo", {
                success() {
                    router.push({ name: "home" });
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }
        let username = ref('');
        let password = ref('');
        let error_massage = ref('');
        const login = () => {
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: "home" });
                            console.log(store.state.user);
                            // username.value = '';
                            // password.value = '';
                            // error_massage.value = '';
                        },
                        error() {
                            console.log(store.state.user);
                        }
                    });

                },
                error() {
                    error_massage.value = '用户名或密码错误';

                }
            });
        }
        return {
            username,
            password,
            error_massage,
            login,
        }
    }
}
</script>
<style scoped>
.mybutton {

    width: 100%;
}

.error_massage {
    color: red;
}
</style>