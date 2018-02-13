<template>
  <div id="app">
    <el-menu mode="horizontal" :default-active="this.$route.path" @select="handleSelect">
      <el-menu-item index="/">トップページ</el-menu-item>
      <el-menu-item index="/search">航空券の予約</el-menu-item>
      <el-menu-item index="/check">予約の確認</el-menu-item>
    </el-menu>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  methods: {
    handleSelect (key) {
      this.$router.push(key)
    }
  }
}
</script>

<style>
</style>
