<template>
  <NavBar />
  <router-view />
</template>
<script>
import NavBar from './components/NavBar.vue';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";

export default {
  components: {
    NavBar,
  },
  setup() {
    // $.ajax({
    //   url: "http://localhost:3000/user/account/token/",
    //   type: "post",
    //   data: {
    //     username: "rxek",
    //     password: "xxz846",
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }

    // });
    // $.ajax({
    //   url: "http://localhost:3000/user/account/info/",
    //   type: "get",
    //   headers: {
    //     Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzYmFkYWNhYjg1OGY0ZGU5YjVmYzIwZTUwZTM4OTJmZCIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY2MDkwNzUxMSwiZXhwIjoxNjYyMTE3MTExfQ.s5yYU0h77i1vUlkHvgCFXgjctoWcz_OcRr001Q2ZPsk"
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }

    // })
  }

}
</script>
<style>
body {
  background-image: url("@/assets/images/1_59b8f162fd-background.png");
  background-size: cover;
}
</style>
