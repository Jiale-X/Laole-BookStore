<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="bookName" placeholder="请输入书名" suffix-icon="el-icon-search" style="width: 250px"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="select" filterable placeholder="请选择时间" style="margin-left: 5px">
        <el-option
            v-for="item in selects"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-input v-model="userName" placeholder="请输入用户名" suffix-icon="el-icon-search" style="width: 250px;margin-left: 5px"
                @keyup.enter.native="loadPost"  v-if="user.account=='admin'"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'rgba(56,54,54,0.2)'}"
              border>
      <el-table-column prop="id" label="Id" width="60" v-if="user.account=='admin'">
      </el-table-column>
      <el-table-column prop="bookId" label="书籍ID" width="60">
      </el-table-column>
      <el-table-column prop="bookName" label="书名" width="300">
      </el-table-column>
      <el-table-column prop="userAccount" label="用户ID" width="120" v-if="user.account=='admin'">
      </el-table-column>
      <el-table-column prop="userName" label="用户姓名" width="160" v-if="user.account=='admin'">
      </el-table-column>
      <el-table-column prop="number" label="数量" width="80">
      </el-table-column>
      <el-table-column prop="price" label="单价" width="100">
      </el-table-column>
      <el-table-column prop="money" label="总费用" width="100">
      </el-table-column>
      <el-table-column prop="time" label="时间" >
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[ 5, 8, 10, 15]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

  </div>

</template>

<script>
export default {
  name: "Record",
  data() {
    return {
      user : JSON.parse(sessionStorage.getItem('CurUser')),
      tableData: [],
      pageNum:1,
      pageSize:10,
      total:0,
      bookName:'',
      userName:'',
      userAccount:'',
      select:'',
      selects: [{
        value: '1',
        label: '今年'
      }, {
        value: '2',
        label: '今月'
      }, {
        value: '3',
        label: '今日'
      }]
    }
  },
  methods:{
    resetForm() {
      this.$refs.form.resetFields();
      this.form.id=''
    },
    handleSizeChange(val) {
      window.console.log(`每页 ${val} 条`);
      this.pageNum=1
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      window.console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    resetParam(){
      this.bookName=''
      this.userName=''
      this.select=''
    },
    load(){
      this.pageNum=1//去除在非1页查询时为空的现象
      this.loadPost()
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/record/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          bookName:this.bookName,
          userName:this.userName,
          userAccount:this.userAccount,
          select:this.select
        }
      }).then(res=>res.data).then(res=>{
        window.console.log(res)
        if (res.code==200){
          this.tableData=res.data
          this.total=res.total
        }else {
          alert("获取失败")
        }

      })
    }
  },
  beforeMount() {
    if (this.user.account!='admin'){
      this.userAccount=this.user.account
    }
    this.loadPost()
  }
}
</script>

<style scoped>

</style>