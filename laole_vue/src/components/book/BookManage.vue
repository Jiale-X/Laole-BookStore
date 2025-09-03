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
      <el-button type="primary" style="margin-left: 5px" @click="add">新增</el-button>
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
          <el-button type="primary" @click="mod(scope.row)">编辑</el-button>
          <el-button type="primary" @click="increase(scope.row)">上架</el-button>
          <el-button type="primary" @click="decrease(scope.row)">下架</el-button>
          <el-popconfirm title="确定撤架吗？" @confirm="del(scope.row.id)" style="margin-left: 5px">
            <el-button slot="reference" size="small" type="danger" >撤架</el-button>
          </el-popconfirm>
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
        title="书籍信息"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="书名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-col :span="20">
            <el-input v-model="form.author"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="出版社" prop="publisher">
          <el-col :span="20">
            <el-input v-model="form.publisher"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-col :span="20">
            <el-input v-model="form.price"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="书籍上新下架"
        :visible.sync="DialogVisible"
        width="30%"
        center>
      <el-form ref="form1" :rules="rules" :model="form1" label-width="80px">
        <el-form-item label="书名" >
          <el-col :span="20">
            <span>{{form1.name}}</span>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="number">
          <el-col :span="20">
            <el-input v-model="form1.number"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="DialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveNum">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
export default {
  name: "BookManage",
  data() {
    return {
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
      centerDialogVisible:false,
      DialogVisible:false,
      form:{
        id:'',
        name:'',
        author:'',
        publisher:'',
        number:'',
        price:''
      },
      form1:{
        id:'',
        name:'',
        number:'',
        action:'0'
      },
      rules: {
        name: [
          { required:true,message:'请输入书名' , trigger: 'blur' }
        ],
        author: [
          { required:true,message:'请输入作者' , trigger: 'blur' }
        ],
        publisher:[
          { required:true,message:'请输入出版社' , trigger: 'blur' }
        ],
        number:[
          { required:true,message:'请输入数量' , trigger: 'blur' },
          {pattern: /^([1-9]\d{0,2})$/,message: '数量必须为正整数字',trigger: "blur"}
        ],
        price:[
          { required:true,message:'请输入价格' , trigger: 'blur' },
          {pattern: /^(?!0$)(0|[1-9]\d*)(\.\d{1,2})?$/,message: '价格必须为正数，且最多保留两位小数',trigger: "blur"}
        ],
        count:[
          { required:true,message:'请输入数量' , trigger: 'blur' },
          {pattern: /^(0|[1-9]\d{0,2})$/,message: '数量必须为非0整数',trigger: "blur"}
        ]
      }
    }
  },
  methods:{
    resetForm() {
      this.$refs.form.resetFields();
      this.form.id=''
    },
    del(id){
      window.console.log(id)
      this.$axios.get(this.$httpUrl+'/book/del?id='+id).then(res=>res.data).then(res=>{
        window.console.log(res)
        if (res.code==200){
          this.$message({
            message:"操作成功",
            type:"success"
          });
          this.loadPost()
        }else {
          this.$message({
            message:"操作失败",
            type:"error"
          })
        }

      })
    },
    mod(row){
      window.console.log(row)
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        this.form.id=row.id
        this.form.name=row.name
        this.form.author=row.author
        this.form.publisher=row.publisher
        this.form.number=row.number
        this.form.price=row.price
      })
    },
    increase(row){
      window.console.log(row)
      this.DialogVisible=true
      this.$nextTick(()=>{
        this.form1.id=row.id
        this.form1.name=row.name
        this.form1.number=''
        this.form1.action='1'
      })
    },
    decrease(row){
      window.console.log(row)
      this.DialogVisible=true
      this.$nextTick(()=>{
        this.form1.id=row.id
        this.form1.name=row.name
        this.form1.number=''
        this.form1.action='2'
      })
    },
    saveNum(){
      this.$refs.form1.validate((valid)=>{
        if(valid){
          this.$axios.post(this.$httpUrl+'/book/updateNum',this.form1).then(res=>res.data).then(res=>{
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
                message:"操作失败:下架数超过库存数",
                type:"error"
              })
            }

          })
        }else {
          return false
        }
      });


    },
    add(){
      this.centerDialogVisible=true
      this.form.number='0'
      this.$nextTick(()=>{
        this.resetForm()
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/book/update',this.form).then(res=>res.data).then(res=>{
        window.console.log(res)
        if (res.code==200){
          this.$message({
            message:"操作成功",
            type:"success"
          });
          this.centerDialogVisible=false
          this.loadPost()
        }else {
          this.$message({
            message:"操作失败",
            type:"error"
          })
        }

      })
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/book/save',this.form).then(res=>res.data).then(res=>{
        window.console.log(res)
        if (res.code==200){
          this.$message({
            message:"操作成功",
            type:"success"
          });
          this.centerDialogVisible=false
          this.loadPost()
        }else {
          this.$message({
            message:"操作失败",
            type:"error"
          })
        }

      })
    },
    save(){
      this.$refs.form.validate((valid)=>{
        if(valid){
          if(this.form.id){
            this.doMod()
          }else {
            this.doSave()
          }
        }else {
          window.console.log("error submit!!")
          return false
        }
      });

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
    // loadGet(){
    //   this.$axios.get(this.$httpUrl+'/user/list').then(res=>res.data).then(res=>{
    //     window.console.log(res)
    //   })
    // },
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
    // this.loadGet();
    this.loadPost()
  }
}
</script>

<style scoped>

</style>