<template>
  <div id="search">
    <step :now="0"></step>
    <el-card class="box-card form-card">
      <div slot="header" class="clearfix">
        <span>経路の検索</span>
      </div>
      <el-form label-width="120px">
        <el-form-item label="搭乗日時">
          <el-date-picker type="datetime" placeholder="日時" v-model="date"></el-date-picker>
        </el-form-item>
        <el-form-item label="出発地">
          <el-select v-model="departure" placeholder="出発地を選択">
            <el-option v-for="ap in airports" :key="ap" :label="ap" :value="ap"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="到着地">
          <el-select v-model="destination" placeholder="到着地を選択">
            <el-option v-for="ap in airports" :key="ap" :label="ap" :value="ap"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">検索</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <route-view v-for="(route, no) in routes" :route="route" :no="no" :key="JSON.stringify(route)"></route-view>
  </div>
</template>

<script>
import API from "../lib/api";

import Step from "./Step";
import SearchRoute from "./SearchRoute";

export default {
  name: 'Search',
  components: {
    "step": Step,
    "route-view": SearchRoute
  },
  data() {
    return {
      date: null,
      departure: null,
      destination: null,

      airports: [],
      routes: [],
    }
  },
  async created() {
    for(let ap of await API.getAirports()){
      this.airports.push(ap);
    }
  },
  methods: {
    async search() {
      if(this.date == null || this.departure == null || this.destination == null){
        return this.$message({
          type: 'error',
          message: '入力されてない項目があります',
        });
      }
      if(this.date < new Date){
        return this.$message({
          type: 'error',
          message: '過去の航空券は予約できません',
        });
      }
      this.routes = (await API.findRoutes(
        this.departure,
        this.destination,
        60 * this.date.getHours() + this.date.getMinutes()
      ))
      .split("----- route -----").slice(1).map(r => r.trim().split("\n"))
      .map(route => {
        let now = new Date(this.date.getTime());
        const entry = {
          time: route[0].split(" ")[1],
          miles: route[1].split(" ")[1],
          fare: route[2].split(" ")[1],
        };
        entry.cost = entry.time * entry.miles * entry.fare;
        entry.data = route.slice(3).map(line => {
          const obj = API.parseFlight(line);
          const date = `${now.getFullYear()}/${("0" + (now.getMonth() + 1)).substr(-2)}/${("0" + now.getDate()).substr(-2)}`;
          const time = `${("0" + now.getHours()).substr(-2)}:${("0" + now.getMinutes()).substr(-2)}`;
          now = new Date(now.getTime() + 60 * obj.ftime * 1000);
          return {
            date, time,
            step: /^\d+$/.test(obj.no) ? `搭乗(${obj.no}便)` : obj.no,
            dd: obj.dept != obj.dest ? `${obj.dept}空港 ▶ ${obj.dest}空港` : `${obj.dept}空港`,
            timecost: `${parseInt(obj.ftime / 60)}時間${obj.ftime % 60}分`,
            raw: obj,
          };
        });
        return entry;
      })
      .sort((a, b) => a.cost > b.cost)
      .slice(0, 3);
    }
  }
}
</script>

<style scoped>
  .el-card {
    margin: 2em auto;
    width: 100%;
    max-width: 1000px;
  }
  .form-card {
    background-color: #f7f7ff;
  }
</style>
