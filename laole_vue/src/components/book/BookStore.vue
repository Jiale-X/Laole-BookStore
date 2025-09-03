<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="请输入书名" suffix-icon="el-icon-search" style="width: 200px"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="number" filterable placeholder="请选择数量有无" style="margin-left: 5px">
        <el-option
            v-for="item in numbers"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'rgba(56,54,54,0.2)'}"
              border>
      <el-table-column prop="id" label="Id" width="60">
      </el-table-column>
      <el-table-column prop="name" label="书名" width="280">
      </el-table-column>
      <el-table-column prop="author" label="作者" width="160">
      </el-table-column>
      <el-table-column prop="publisher" label="出版社" width="160">
      </el-table-column>
      <el-table-column prop="number" label="数量" width="100">
      </el-table-column>
      <el-table-column prop="price" label="单价" width="100">
      </el-table-column>
      <el-table-column prop="operate" label="操作" >
        <template slot-scope="scope">
          <el-button type="primary" @click="buy(scope.row)">购买</el-button>
        </template>
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
    <el-dialog
        title="书籍购买"
        :visible.sync="DialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="书名" prop="bookName">
          <el-col :span="20">
            <span>{{form.bookName}}</span>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="number">
          <el-col :span="20">
            <el-input v-model="form.number"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="金额" prop="money">
          <el-col :span="20">
            <span>{{form.money=Math.round(form.number*form.price*100)/100}}</span>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="DialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="update">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
export default {
  name: "BookManage",
  data() {
    return {
      user : JSON.parse(sessionStorage.getItem('CurUser')),
      tableData: [],
      pageNum:1,
      pageSize:10,
      total:0,
      name:'',
      number:"",
      numbers: [{
        value: '1',
        label: '有'
      }, {
        value: '0',
        label: '无'
      }],
      DialogVisible:false,
      form:{
        id:'',
        bookName:'',
        number:'',
        price:'',
        money:'',
        userId:''
      },
      rules: {
        number:[
          { required:true,message:'请输入数量' , trigger: 'blur' },
          {pattern: /^([1-9]\d{0,2})$/,message: '数量必须为正整数字',trigger: "blur"}
        ]
      }
    }
  },
  methods:{
    resetForm() {
      this.$refs.form.resetFields();
    },
    buy(row){
      window.console.log(row)
      this.DialogVisible=true
      this.form.id=row.id
      this.form.bookName=row.name
      this.form.number=''
      this.form.price=row.price
      this.form.money=0
      this.form.userId=this.user.id
    },
    update(){
      this.$refs.form.validate((valid)=>{
        if(valid){
          this.updateBuy()
        }else {
          window.console.log("error submit!!")
          return false
        }
      });
    },
    updateBuy(){
      this.$axios.post(this.$httpUrl+'/book/updateBuy',this.form).then(res=>res.data).then(res=>{
        window.console.log(res)
        if (res.code==200){
          this.updateBalance()
          this.saveRecord()
          this.DialogVisible=false
          this.loadPost()
        }else if(res.code==401){
          this.$message({
            message:"购买数量过多",
            type:"error"
          })
        }else if(res.code==402){
          this.$message({
            message:"余额不足",
            type:"error"
          })
        }else {
          this.$message({
            message:"操作失败",
            type:"error"
          })
        }
      })
    },
    updateBalance(){
      this.$axios.post(this.$httpUrl+'/user/updateBuy', {
        id:this.user.id,
        balance:this.user.balance-this.form.money
      }).then(res=>res.data).then(res=>{
        window.console.log(res)
        // 更新当前组件中的用户余额
        this.user.balance = this.user.balance - this.form.money;

        // 同步更新 sessionStorage 中的用户信息
        let currentUser = JSON.parse(sessionStorage.getItem('CurUser'));
        currentUser.balance = this.user.balance;
        sessionStorage.setItem('CurUser', JSON.stringify(currentUser));

        // 触发全局事件，通知其他组件余额已更新
        this.$emit('balance-updated', this.user.balance);

      })
    },
    saveRecord(){
      this.$axios.post(this.$httpUrl+'/record/save', {
        bookId:this.form.id,
        bookName:this.form.bookName,
        userAccount:this.user.account,
        userName:this.user.name,
        number:this.form.number,
        price:this.form.price,
        money:this.form.money
      }).then(res=>res.data).then(res=>{
        window.console.log(res)
        if (res.code==200){
          this.$message({
            message:"操作成功",
            type:"success"
          });
          this.DialogVisible=false
          this.loadPost()
        }else {
          this.$message({
            message:"操作失败",
            type:"error"
          })
        }

      })
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
      this.name=''
      this.number=''
    },
    load(){
      this.pageNum=1//去除在非1页查询时为空的现象
      this.loadPost()
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/book/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          number:this.number
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
    this.loadPost()
  }
}
</script>

<style scoped>

</style>