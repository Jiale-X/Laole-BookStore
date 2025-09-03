<template>
  <div style="display: flex;line-height: 60px">
    <div style="flex: 1;text-align: center;font-size: 28px">欢迎来到老乐书城</div>
    <el-dropdown>
      <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="layout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

  </div>

</template>

<script>
export default {
  data(){
    return {
      user:JSON.parse(sessionStorage.getItem('CurUser'))
    }
  },
  name: "Header",
  methods:{
    toUser(){
      window.console.log("to_user")
      this.$router.push('Home')
    },
    layout(){
      window.console.log("layout")
      this.$confirm('您确定要退出登录吗?', '提示', {
        confirmButtonText: '确定',  //确认按钮的文字显示
        type: 'warning',
        center: true, //文字居中显示

      })
          .then(() => {
            this.$message({
              type:'success',
              message:'退出登录成功'
            })

            this.$router.push("/")
            sessionStorage.clear()
          })
          .catch(() => {
            this.$message({
              type:'info',
              message:'已取消退出登录'
            })
          })
    }

  },
  created() {
    this.$router.push('/Home')
  }
}
</script>

<style scoped>

</style>