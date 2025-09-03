<template>
  <div class="registerBody">
    <div class="registerDiv">
      <div class="register-content">
        <h1 class="register-title">用户注册</h1>
        <el-form :model="registerForm" label-width="100px"
                 :rules="rules" ref="registerForm">
          <el-form-item label="账号" prop="account">
            <el-input style="width: 200px" type="text" v-model="registerForm.account"
                      autocomplete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="width: 200px" type="password" v-model="registerForm.password"
                      show-password autocomplete="off" size="small"></el-input>
          </el-form-item>

          <el-form-item label="姓名" prop="name">
            <el-input style="width: 200px" type="text" v-model="registerForm.name"
                      autocomplete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <el-radio-group v-model="registerForm.sex">
              <el-radio label="1">男</el-radio>
              <el-radio label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input style="width: 200px" type="text" v-model="registerForm.age"
                      autocomplete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input style="width: 200px" type="text" v-model="registerForm.phone"
                      autocomplete="off" size="small"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="register" :disabled="register_disabled">注册</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="login-link">
          <span>想起账户？</span>
          <el-button type="text" @click="toLogin">返回登录</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    let checkAge = (rule, value, callback) => {
      if(value>150){
        callback(new Error('年龄输⼊过⼤'));
      }else{
        callback();
      }
    };
    let checkDuplicate = (rule,value,callback)=>{
      this.$axios.get(this.$httpUrl+"/user/findByAccount?account="+this.registerForm.account).then(res=>res.data).then(res=>{
        if(res.code!=200){
          callback()
        }else{
          callback(new Error('账号已经存在'));
        }
      })
    };
    return {
      register_disabled: false,
      registerForm: {
        account: '',
        password: '',
        name: '',
        sex:'0',
        age:'',
        phone:'',
        balance:'0',
      },
      rules: {
        account: [
          { required:true,message:'请输入账号' , trigger: 'blur' },
          { min:3, max:8, message: '长度在3到8个字符',trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        name: [
          { required:true,message:'请输入名字' , trigger: 'blur' }
        ],
        password: [
          { required:true,message:'请输入密码' , trigger: 'blur' },
          { min:3, max:8, message: '长度在3到8个字符',trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输⼊年龄', trigger: 'blur'},
          {min: 1, max: 3, message: '⻓度在 1 到 3 个位', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/,message: '年龄必须为正整数字',trigger: "blur"},
          {validator:checkAge,trigger: 'blur'}
        ],
        phone: [
          {required: true,message: "⼿机号不能为空",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输⼊正确的⼿机号码", trigger: "blur"}
        ]
      }
    }
  },
  methods: {
    toLogin(){
      this.$router.push('/')
    },
    register() {
      this.register_disabled = true;
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          // 发送注册请求
          this.$axios.post(this.$httpUrl + '/user/register',this.registerForm).then(res => res.data).then(res => {
            if (res.code == 200) {
              this.$message({
                message: '注册成功！',
                type: 'success'
              });
              // 注册成功后跳转到登录页
              this.$router.push('/');
            } else {
              this.$message({
                message:"注册失败",
                type:"error"
              })
              this.register_disabled = false;
            }
          }).catch(error => {
            this.$message({
              message: '注册失败，请重试！',
              type: 'error'
            });
            window.console.error(error);
            this.register_disabled = false;
          });
        } else {
          this.register_disabled = false;
          window.console.log("error submit!!")
          return false
        }
      });
    },
    resetForm() {
      this.$refs.registerForm.resetFields();
    }
  }
}
</script>

<style scoped>
.registerBody {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: #B3C0D1;
}
.registerDiv {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -250px;
  margin-left: -250px;
  width: 450px;
  height: 500px;
  background: #fff;
  border-radius: 5%;
}
.register-title {
  margin: 20px 0;
  text-align: center;
}
.register-content {
  width: 400px;
  position: absolute;
  top: 25px;
  left: 25px;
}
.login-link {
  text-align: center;
  margin-top: 20px;
}
</style>
