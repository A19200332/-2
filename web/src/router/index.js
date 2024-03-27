import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from "../views/pk/PkIndexView.vue";
import RanklistIndexView from "../views/ranklist/RanklistIndexView.vue";
import RecordIndexView from "../views/record/RecordIndexView.vue";
import UserBotIndexView from "../views/user/bot/UserBotIndexView.vue";
import NotFoundView from "../views/error/NotFound.vue";
import UserAccountLoginView from "../views/user/account/UserAccountLoginView";
import UserAccountRegisterView from "../views/user/account/UserAccountRegisterView";
import store from '../store/index';
const routes = [
  {
    path: "/user/account/login/",
    name: "login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/register/",
    name: "register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/",
    name: "home",
    component: PkIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/pk/",
    name: "pk",
    component: PkIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist",
    component: RanklistIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/bot/",
    name: "userbot",
    component: UserBotIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/notfound/",
    name: "notfound",
    component: NotFoundView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    name: "notfound",
    component: NotFoundView,
    meta: {
      requestAuth: false,
    }
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})
router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({ name: "login" });
  } else {
    next();
  }
})
export default router
