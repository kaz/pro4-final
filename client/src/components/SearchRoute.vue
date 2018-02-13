<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>経路{{1 + no}}</span>
      <el-button type="primary" @click="select">この経路で予約する</el-button>
    </div>
    <div>運　　賃: {{route.fare}}円</div>
    <div>飛行距離: {{route.miles}}マイル</div>
    <div>所要時間: {{parseInt(route.time / 60)}}時間{{route.time % 60}}分</div>
    <el-table :data="route.data">
      <el-table-column prop="date" label="日付" width="120"></el-table-column>
      <el-table-column prop="time" label="時刻" width="80"></el-table-column>
      <el-table-column prop="step" label="行程" width="100"></el-table-column>
      <el-table-column prop="timecost" label="所要時間" width="150"></el-table-column>
      <el-table-column prop="dd" label="出発/到着"></el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import API from "../lib/api";

export default {
  name: 'SearchRoute',
  props: ['route', 'no'],
  methods: {
    async select() {
      sessionStorage.reservations = JSON.stringify(await API.checkTicket(""));
      sessionStorage.route = JSON.stringify(this.route);
      this.$router.push("/reservation");
    }
  }
}
</script>

<style scoped>
  .el-button {
    position: relative;
    top: -10px;
    float: right;
  }
  .el-table {
    width: 100%;
  }
</style>
