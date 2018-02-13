<template>
  <div id="reservation">
    <step :now="1"></step>
    <flight-seat v-on:update="update" v-for="flight in flights" :key="flight.code" :flight="flight" :reserved="flight.reserved"></flight-seat>
    <el-card class="box-card form-card">
      <div slot="header" class="clearfix">
        <span>予約の確認</span>
      </div>
      <div class="tickets">
        <strong>チケット一覧</strong>
        <div v-if="!tickets.length">座席予約なし</div>
        <div v-for="ticket in ticketsDetail">{{ticket.detail}} <strong>{{ticket.seat}}</strong></div>
      </div>
      <el-form label-width="70px">
        <el-form-item label="予約者名">
          <el-input placeholder="お名前" v-model="name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="execute">予約を実行</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import API from "../lib/api";

import Step from "./Step";
import ReservationFlight from "./ReservationFlight";

export default {
  name: 'Reservation',
  components: {
    "step": Step,
    "flight-seat": ReservationFlight
  },
  data() {
    return {
      name: "",
      tickets: [],
    };
  },
  computed: {
    reservations() {
      return JSON.parse(sessionStorage.reservations);
    },
    routes() {
      return JSON.parse(sessionStorage.route);
    },
    flights() {
      return this.routes.data.filter(r => /^\d+$/.test(r.raw.no)).map(f => {
        f.reserved = this.reservations.filter(r => r.date == f.date && r.flight.code == f.raw.code).map(r => [r.row, r.col]);
        return f;
      });
    },
    ticketsDetail() {
      return this.tickets.map(t => t.split("\u001F")).map(([date, code, r, c]) => {
        const flight = this.routes.data.find(r => r.raw.code == code);
        return {
          seat: `${String.fromCharCode(parseInt(r) + "A".charCodeAt(0))}-${parseInt(c) + 1}席`,
          detail: `${flight.date} ${flight.time}発 ${flight.raw.no}便 (${flight.dd})`
        };
      });
    }
  },
  methods: {
    update(ticket, rm) {
      if(rm){
        this.tickets.push(ticket);
      }else{
        this.tickets = this.tickets.filter(t => t != ticket);
      }
    },
    async execute() {
      if(this.name == ""){
        return this.$message({
          type: 'error',
          message: '予約者名が入力されていません',
        });
      }
      if(!this.tickets.length){
        return this.$message({
          type: 'error',
          message: '座席が選択されていません',
        });
      }
      await API.reserveTicket(this.name, this.tickets);
      this.$router.push("/complete");
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
  .tickets {
    margin-bottom: 2em;
  }
</style>
