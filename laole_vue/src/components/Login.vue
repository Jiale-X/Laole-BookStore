<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <h1 class="login-title">用户登录</h1>
        <el-form :model="loginForm" label-width="100px"
                 :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="account">
            <el-input style="width: 200px" type="text" v-model="loginForm.account"
                      autocomplete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="width: 200px" type="password" v-model="loginForm.password"
                      show-password autocomplete="off" size="small" @keyup.enter.native="confirm"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="confirmAdmin" :disabled="confirm_disabled">管理员登录</el-button>
            <el-button type="primary" @click="confirmUser" :disabled="confirm_disabled">用户登录</el-button>
          </el-form-item>
        </el-form>
        <div class="register-link">
          <span>还没有账号？</span>
          <el-button type="text" @click="toRegister">立即注册</el-button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: "Login",
  data(){
    return{
      confirm_disabled:false,
      loginForm:{
        account:'',
        password:''
      },
      rules:{
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输密码', trigger: 'blur' }
        ],
      }
    }
  },
  methods:{
    toRegister() {
      // 跳转到注册页面
      this.$router.push('/Register');
    },
    confirmAdmin(){
      this.confirm_disabled=true;
      this.$refs.loginForm.validate((valid) => {
        if (valid) { //valid成功为true，失败为false
          //去后台验证用户名密码
          this.$axios.post(this.$httpUrl+'/admin/login',this.loginForm).then(res=>res.data).then(res=>{
            window.console.log(res)
            if(res.code==200){
              //存储
              sessionStorage.setItem("CurUser",JSON.stringify(res.data))
              // window.console.log(res.data.menu)
              // this.$store.commit("setMenu",res.data.menu)

              this.$router.replace('/Index');
            }else{

              this.confirm_disabled=false;
              alert('校验失败，用户名或密码错误！');
              return false;
            }
          });
        } else {
          this.confirm_disabled=false;
          window.console.log('校验失败');
          return false;
        }
      });

    },
    confirmUser(){
      this.confirm_disabled=true;
      this.$refs.loginForm.validate((valid) => {
        if (valid) { //valid成功为true，失败为false
          //去后台验证用户名密码
          this.$axios.post(this.$httpUrl+'/user/login',this.loginForm).then(res=>res.data).then(res=>{
            window.console.log(res)
            if(res.code==200){
              //存储
              sessionStorage.setItem("CurUser",JSON.stringify(res.data))
              // window.console.log(res.data.menu)
              // this.$store.commit("setMenu",res.data.menu)

              this.$router.replace('/Index');
            }else{

              this.confirm_disabled=false;
              alert('校验失败，用户名或密码错误！');
              return false;
            }
          });
        } else {
          this.confirm_disabled=false;
          window.console.log('校验失败');
          return false;
        }
      });

    }
  }
}
</script>

<style scoped>
.loginBody {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: #B3C0D1;
}
.loginDiv {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -200px;
  margin-left: -250px;
  width: 450px;
  height: 330px;
  background: #fff;
  border-radius: 5%;

}
.login-title {
  margin: 20px 0;
  text-align: center;
}
.login-content {
  width: 400px;
  height: 250px;
  position: absolute;
  top: 25px;
  left: 25px;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}
</style>