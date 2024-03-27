import { createStore } from 'vuex'
import ModuleUser from './user'
import ModuleBot from "./bot"
import ModulePk from "./pk"
export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {

  },
  modules: {
    user: ModuleUser,
    bot: ModuleBot,
    pk: ModulePk,
  }
})
