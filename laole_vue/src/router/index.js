
import VueRouter from 'vue-router';

const routes = [
    {
        path:'/',
        name:'login',
        component:()=>import('../components/Login')
    },
    {
        path:'/Register',
        name:'register',
        component:()=>import('../components/Register')
    },
    {
        path: '/Index',
        name: 'index',
        component:()=>import('../components/Index'),
        children:[
            {
                path: '/Home',
                name: 'home',
                meta:{
                    title:'主页'
                },
                component:()=>import('../components/Home')
            },
            {
                path: '/User',
                name: 'user',
                meta:{
                    title:'用户管理'
                },
                component:()=>import('../components/user/UserManage')
            },
            {
                path: '/BookManage',
                name: 'bookmanage',
                meta:{
                    title:'书籍管理'
                },
                component:()=>import('../components/book/BookManage.vue')
            },
            {
                path: '/BookStore',
                name: 'bookstore',
                meta:{
                    title:'书城'
                },
                component:()=>import('../components/book/BookStore')
            },
            {
                path: '/Record',
                name: 'record',
                meta: {
                    title: '记录'
                },
                component:()=>import('../components/record/Record')
            }
        ]
    }

]

const router = new VueRouter({
    mode:'history',
    routes
})
// export function resetRouter() {
//     router.matcher = new VueRouter({
//         mode:'history',
//         routes: []
//     }).matcher
// }
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}
export  default router;