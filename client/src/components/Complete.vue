<template>
  <div id="check">
    <step :now="3"></step>
    <el-alert show-icon type="success" :closable="false" title="予約が完了しました" description="左上のメニューから予約を確認できます"></el-alert>
  </div>
</template>

<script>
import Step from "./Step";

export default {
  name: 'Complete',
  components: {
    "step": Step
  },
  created() {
    sessionStorage.clear();
  }
}
</script>

<style scoped>
  .el-alert {
    margin: 2em auto;
    width: 100%;
    max-width: 1000px;
  }
</style>
