<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <h3>座席の選択</h3>
      <h4>{{flight.date}} {{flight.time}}発 {{flight.raw.no}}便 ({{flight.dd}})</h4>
    </div>
    <div class="seat">
      <div class="seat-row" v-for="(_, r) in 9">
        <div class="seat-cell" v-for="(_, c) in 30" :class="{keep: seat[r][c] == 1, reserved: seat[r][c] == 2}" @click="toggle(r, c)"></div>
      </div>
    </div>
    <p><small>← 前方 / 後方 →</small></p>
    <div class="regends">
      <div><div class="seat-cell"></div> 空き</div>
      <div><div class="seat-cell keep"></div> 選択中</div>
      <div><div class="seat-cell reserved"></div> 予約済み</div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'ReservationFlight',
  props: ['flight',　'reserved'],
  data() {
    return {
      keep: [],
    }
  },
  computed: {
    seat() {
      const seat = "_".repeat(9).split("").map(_ => "_".repeat(30).split("").map(_ => 0));
      this.keep.forEach(([r, c]) => seat[r][c] = 1);
      this.reserved.forEach(([r, c]) => seat[r][c] = 2);
      return seat;
    }
  },
  methods: {
    toggle(r, c) {
      if(this.reserved.some(([rr, cc]) => rr == r && cc == c)){
        return;
      }
      const index = this.keep.findIndex(([rr, cc]) => rr == r && cc == c);
      this.$emit("update", [this.flight.date, this.flight.raw.code, r, c].join("\u001F"), index == -1);
      if(index == -1){
        this.keep.push([r, c]);
      }else{
        this.keep.splice(index, 1);
      }
    }
  }
}
</script>

<style scoped>
  .seat {
    display: flex;
    flex-direction: column;
  }
  .seat-row {
    display: flex;
  }
  .seat-row:nth-child(3n) {
    margin-bottom: 10px;
  }
  .seat-cell {
    height: 20px;
    width: 20px;
    margin: 1px;
    border: 1px solid #aaa;
    cursor: pointer;
  }
  .seat-cell.keep {
    background-color: lightblue;
  }
  .seat-cell.reserved {
    background-color: lightgray;
    cursor: default;
  }
  .regends {
    display: flex;
  }
  .regends > div {
    margin-right: 1.2em;
  }
  .regends .seat-cell {
    display: inline-block;
  }
</style>
