<template>
  <div id="check">
    <el-card class="box-card form-card">
      <div slot="header" class="clearfix">
        <span>予約の確認</span>
      </div>
      <el-form label-width="80px">
        <el-form-item label="予約者名">
          <el-input placeholder="お名前" v-model="name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="check">確認</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card" v-if="!tickets.length">予約がありません</el-card>
    <el-card class="box-card" v-for="ticket in tickets" :key="ticket.code">
      <div>
        {{ticket.date}} {{ticket.flight.no}}便({{ticket.flight.dept}} ▶ {{ticket.flight.dest}})
        <strong>{{String.fromCharCode(parseInt(ticket.row) + "A".charCodeAt(0))}}-{{parseInt(ticket.col) + 1}}席</strong>
        <el-button class="cancel" type="danger" @click="cancel(ticket.code)">キャンセル</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import API from "../lib/api";

export default {
  name: 'Check',
  data() {
    return {
      name: "",
      tickets: []
    }
  },
  methods: {
    async check() {
      if(this.name == ""){
        return this.$message({
          type: 'error',
          message: '予約者名が入力されていません',
        });
      }
      this.tickets = await API.checkTicket(this.name);
    },
    async cancel(code) {
      await API.cancelTicket(code);
      this.$message({
        type: 'success',
        message: '予約をキャンセルしました',
      });
      await this.check();
    }
  }
}
</script>

<style scoped>
  .el-card {
    margin: .5em auto;
    width: 100%;
    max-width: 1000px;
  }
  .form-card {
    margin: 2em auto;
    background-color: #f7f7ff;
  }
  .cancel {
    position: relative;
    top: -10px;
    float: right;
  }
</style>
