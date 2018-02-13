<template>
  <div id="index">
    <header>東京工業大学航空会社</header>
    <el-alert show-icon type="info" :closable="false" title="飛行機予約システムへようこそ" description="右上のメニューから飛行機の予約と、予約の確認できます"></el-alert>
  </div>
</template>

<script>
export default {
  name: 'index'
}
</script>

<style scoped>
  header {
    color: #fff;
    background-color: #8bf;
    font-size: 3em;
    padding: 1.5em;
    font-weight: bold;
  }
</style>
