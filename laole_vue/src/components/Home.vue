<template>
  <div style="text-align: center;background-color: #f1f1f3;height: 100%;padding: 0px;margin: 0px;">
    <h1 style="font-size: 50px;">{{'欢迎你！'+user.name}}</h1>
    <el-descriptions  title="个人中心" :column="2" size="40" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-custom"></i>
          账号
        </template>
        {{user.account}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          电话
        </template>
        {{user.phone}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          性别
        </template>
        <el-tag
            :type="user.sex == '1' ? 'primary' : 'danger'"
            disable-transitions><i :class="user.sex==1?'el-icon-male':'el-icon-female'"></i>{{user.sex==1?"男":"女"}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          角色
        </template>
        <el-tag
            type="success"
            disable-transitions>{{user.account=='admin'?"管理员":'用户'}}</el-tag>

      </el-descriptions-item>
      <el-descriptions-item v-if="user.account!='admin'">
        <template slot="label">
          <i class="el-icon-eleme"></i>
          余额
        </template>
        {{Math.round(user.balance*100)/100}}
      </el-descriptions-item>
      <el-descriptions-item v-if="user.account!='admin'">
        <template slot="label">
          <i class="el-icon-s-operation"></i>
          操作
        </template>
        <el-button  type="primary" @click="charge">充值</el-button>
      </el-descriptions-item>
    </el-descriptions>

    <DateUtils></DateUtils>
    <el-dialog
        title="充值"
        :visible.sync="DialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="金额" prop="balance">
          <el-col :span="20">
            <el-input v-model="form.balance"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="DialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateBalance">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import DateUtils from "./DateUtils";
export default {
  name: "Home",
  components: {DateUtils},
  data() {

    return {
      user:{},
      DialogVisible:false,
      form:{
        balance:'',
      },
      rules: {
        balance:[
          { required:true,message:'请输入价格' , trigger: 'blur' },
          {pattern: /^(?!0$)(0|[1-9]\d*)(\.\d{1,2})?$/,message: '充值必须为正数，且最多保留两位小数',trigger: "blur"}
        ]
      }
    }
  },
  computed:{

  },
  methods:{
    resetForm() {
      this.$refs.form.resetFields();
    },
    init(){
      this.user = JSON.parse(sessionStorage.getItem('CurUser'))
    },
    charge(){
      this.DialogVisible=true
      this.$nextTick(()=>{
        this.resetForm()
      })
      // this.form.balance=0
    },
    updateBalance(){
      this.$refs.form.validate((valid)=>{
        if (valid){
          this.$axios.post(this.$httpUrl+'/user/updateBalance', {
            id:this.user.id,
            balance:this.form.balance,
            sex:this.user.sex,
            age:this.user.age
          }).then(res=>res.data).then(res=>{
            window.console.log(res)
            if (res.code==200){
              this.$message({
                message:"充值成功",
                type:"success"
              });
              this.DialogVisible=false
            }else {
              this.$message({
                message:"充值失败",
                type:"error"
              })
            }
            // 更新当前组件中的用户余额
            this.user.balance = +this.user.balance + +this.form.balance;

            // 同步更新 sessionStorage 中的用户信息
            let currentUser = JSON.parse(sessionStorage.getItem('CurUser'));
            currentUser.balance = this.user.balance;
            sessionStorage.setItem('CurUser', JSON.stringify(currentUser));

            // 触发全局事件，通知其他组件余额已更新
            this.$emit('balance-updated', this.user.balance);

          })
        }else return false;
      });

    },
  },
  created(){
    this.init()
  }
}
</script>

<style scoped>
.el-descriptions{
  width:90%;

  margin: 0 auto;
  text-align: center;
}
</style>