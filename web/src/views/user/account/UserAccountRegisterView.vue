<template>
    <MyContainer>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password"
                            placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">确认密码</label>
                        <input v-model="confirmPassword" type="password" class="form-control" id="confirmPassword"
                            placeholder="请再次输入密码">
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
import $ from 'jquery';
import { ref } from 'vue';
import router from '../../../router/index';
export default {
    name: "UserAccountRegisterView",
    components: {
        MyContainer,
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let confirmPassword = ref('');
        let error_massage = ref('');
        const register = () => {
            error_massage.value = "";
            $.ajax({
                url: "http://localhost:3000/user/account/register/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    username: username.value,
                    password: password.value,
                    confirmPassword: confirmPassword.value,
                },
                success(resp) {
                    if (resp.error_massage === "success") {
                        router.push({ name: "login" });
                    } else {
                        error_massage.value = resp.error_massage;
                    }
                },

            });
        }
        return {
            username,
            password,
            confirmPassword,
            error_massage,
            register,
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