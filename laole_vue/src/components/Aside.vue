<template>
  <el-menu
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      style="height: 100%"
      default-active="/Home"
      router>
    <el-menu-item index="/Home">
      <i class="el-icon-s-home"></i>
      <span>主页</span>
    </el-menu-item>
    <el-menu-item index="/User" v-if="user.account=='admin'">
      <i class="el-icon-user-solid"></i>
      <span>用户管理</span>
    </el-menu-item>
    <el-menu-item index="/BookManage" v-if="user.account=='admin'">
      <i class="el-icon-s-management"></i>
      <span>书籍管理</span>
    </el-menu-item>
    <el-menu-item index="/BookStore" v-if="user.account!='admin'">
      <i class="el-icon-s-management"></i>
      <span>书城</span>
    </el-menu-item>
    <el-menu-item index="/Record">
      <i class="el-icon-s-data"></i>
      <span>购买记录</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return{
      user : JSON.parse(sessionStorage.getItem('CurUser'))
    }
  }
}
</script>

<style scoped>

</style>