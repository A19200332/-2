<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card has-margin-top">
                    <div class="card-body">
                        <img :src="$store.state.user.photo">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card has-margin-top">
                    <div class="card-header">
                        <span class="headertext">我的bot</span>
                        <button type="button" class="btn btn-outline-secondary float-end " data-bs-toggle="modal"
                            data-bs-target="#add-bot">
                            创建bot
                        </button>
                        <div class="modal fade" id="add-bot" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="exampleModalLabel">创建bot</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <form @submit.prevent="addbot">
                                            <div class="mb-3">
                                                <label for="add-bot-title" class="form-label">名称</label>
                                                <input v-model="title" type="text" class="form-control"
                                                    id="add-bot-title" placeholder="请输入bot名称">
                                            </div>
                                            <div class="mb-3">
                                                <label for="add-bot-description" class="form-label">简介</label>
                                                <textarea v-model="description" class="form-control"
                                                    id="add-bot-description" rows="3" placeholder="请输入bot简介"></textarea>
                                            </div>
                                            <div class="mb-3">
                                                <label for="add-bot-content" class="form-label">代码</label>
                                                <!-- <textarea v-model="content" class="form-control" id="add-bot-content"
                                                    rows="15" placeholder="请编写bot代码"></textarea> -->
                                                <VAceEditor v-model:value="content" @init="editorInit" lang="c_cpp"
                                                    theme="textmate" style="height: 300px" />
                                            </div>


                                            <div class="modal-footer">
                                                <div class="error-massage">
                                                    {{ error_massage }}
                                                </div>
                                                <button type="submit" class="btn btn-primary">创建</button>
                                                <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">取消</button>

                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right:10px;"
                                            :data-bs-target="'#update-bot' + bot.id" data-bs-toggle="modal">修改</button>
                                        <button type="button" class="btn btn-danger" data-bs-target="#delete-bot"
                                            data-bs-toggle="modal" @click="fuzhi(bot)">删除</button>



                                        <!--update modal-->
                                        <div class="modal fade" :id="'update-bot' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="exampleModalLabel">修改bot</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <form @submit.prevent="updatebot(bot)">
                                                            <div class="mb-3">
                                                                <label for="add-bot-title" class="form-label">名称</label>
                                                                <input v-model="bot.title" type="text"
                                                                    class="form-control" id="add-bot-title"
                                                                    placeholder="请输入bot名称">
                                                            </div>
                                                            <div class="mb-3">
                                                                <label for="add-bot-description"
                                                                    class="form-label">简介</label>
                                                                <textarea v-model="bot.description" class="form-control"
                                                                    id="add-bot-description" rows="3"
                                                                    placeholder="请输入bot简介"></textarea>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label for="add-bot-content"
                                                                    class="form-label">代码</label>
                                                                <!-- <textarea v-model="bot.content" class="form-control"
                                                                    id="add-bot-content" rows="15"
                                                                    placeholder="请编写bot代码"></textarea> -->
                                                                <VAceEditor v-model:value="bot.content"
                                                                    @init="editorInit" lang="c_cpp" theme="textmate"
                                                                    style="height: 300px" />
                                                            </div>


                                                            <div class="modal-footer">
                                                                <div class="error-massage">
                                                                    {{ error_massage }}
                                                                </div>
                                                                <button type="submit"
                                                                    class="btn btn-primary">保存修改</button>
                                                                <button type="button" class="btn btn-secondary"
                                                                    data-bs-dismiss="modal">取消</button>

                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!--update modal-->



                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <!--  delete modal-->
    <div class="modal fade" id="delete-bot" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body headertext">
                    确认删除该bot?
                </div>
                <div class="modal-footer">
                    <div>{{ error_massage }}</div>
                    <button type="button" class="btn btn-danger" @click="deletebot">确认</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>

                </div>
            </div>
        </div>
    </div>
    <!--  delete modal-->


</template>
  
<script>

import { useStore } from 'vuex';
import { reactive } from 'vue';
import { ref } from 'vue';
import $ from 'jquery';
import { Modal } from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
export default {
    name: "UserBotIndexView",
    components: {
        VAceEditor,
    },
    setup() {
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
        const store = useStore();
        let bots = ref([]);
        let title = ref('');
        let description = ref('');
        let content = ref('');
        let error_massage = ref('');
        let nowbot = reactive({

        });
        const fuzhi = (bot) => {
            nowbot = bot;
        }
        const updatebot = (bot) => {
            $.ajax({
                url: "http://localhost:3000/user/bot/update/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                success(resp) {
                    if (resp.error_massage === "success") {
                        getlist();
                        Modal.getInstance('#update-bot' + bot.id).hide();
                        console.log(resp);
                    } else {
                        error_massage.value = resp.error_massage;
                        console.log(resp);
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }
        const deletebot = () => {
            $.ajax({
                url: "http://localhost:3000/user/bot/remove/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    bot_id: nowbot.id,
                },
                success(resp) {
                    if (resp.error_massage !== "success") {
                        error_massage.value = resp.error_massage;
                    } else {
                        getlist();
                        Modal.getInstance("#delete-bot").hide();
                        console.log(resp);
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }
        const getlist = () => {
            $.ajax({
                url: "http://localhost:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },

                success(resp) {
                    bots.value = resp;
                    console.log(resp);
                },
                error(resp) {

                    console.log(resp);
                }

            });
        }
        getlist();
        const addbot = () => {
            error_massage.value = "";
            $.ajax({
                url: "http://localhost:3000/user/bot/add/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    title: title.value,
                    description: description.value,
                    content: content.value,
                },
                success(resp) {
                    console.log(resp);
                    if (resp.error_massage !== "success") {
                        error_massage.value = resp.error_massage;
                    }
                    else {
                        Modal.getInstance("#add-bot").hide();
                        getlist();
                        title.value = "";
                        description.value = "";
                        content.value = "";
                    }
                },
                error(resp) {

                    console.log(resp);
                }
            });
        }
        return {
            bots,
            addbot,
            deletebot,
            fuzhi,
            updatebot,
            nowbot,
            title,
            description,
            content,
            error_massage,
        }
    }
}
</script>
<style scoped>
img {
    width: 100%
}

.has-margin-top {
    margin-top: 20px;
}

.headertext {
    font-size: 20px;
    font-weight: bolder;
    text-align: center;
}

.error-massage {
    color: red;
}
</style>