<template>
  <header>
    <h1>航空券の予約</h1>
    <el-steps :active="now" finish-status="success">
      <el-step title="経路の検索"></el-step>
      <el-step title="座席の選択"></el-step>
      <el-step title="予約完了"></el-step>
    </el-steps>
  </header>
</template>

<script>
export default {
  name: 'Step',
  props: ["now"]
}
</script>

<style scoped>
  header {
    margin: 2em auto;
    width: 100%;
    max-width: 1000px;
  }
</style>
