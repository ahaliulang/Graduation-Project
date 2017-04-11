# Graduation-Project
广州医科大学云课堂的简化Android APP，具有自动缓存、搜索、用户管理等功能，作为本人的毕业设计，全部过程包括服务器的搭建都是独立自主完成。
# 项目支持的Android特性
- CardView
- RecyclerView
- SnackBar
- Shared Element Transition
- SwipeRefreshLayout
- CoordinatorLayout
- AppBarLayout
- CollapsingToolbarLayout
# 项目使用的开源库
- [vitamio](https://github.com/yixia/VitamioBundle)
- [Realm](https://realm.io/)
- [Square / Retrofit](https://github.com/square/retrofit)
- [bumptech/glide](https://github.com/bumptech/glide)
- [drakeet/MultiType](https://github.com/drakeet/MultiType)
- [google/gson](https://github.com/google/gson)
- [hdodenhof/CircleImageView](https://github.com/hdodenhof/CircleImageView)
# 预览
<img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E4%B8%AA%E4%BA%BA%E8%B5%84%E6%96%99.jpg" width="30%" height = "30%"> <img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E5%85%B3%E4%BA%8E.jpg" width="30%" height = "30%"> <img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E5%88%86%E7%B1%BB1.jpg" width="30%" height = "30%">
<img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E5%88%86%E7%B1%BB2.jpg" width="30%" height = "30%"> <img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E6%9C%AA%E7%99%BB%E9%99%86.jpg" width="30%" height = "30%"> <img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E7%99%BB%E9%99%86.jpg" width="30%" height = "30%"> <img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E6%90%9C%E7%B4%A2%E5%90%8E.jpg" width="30%" height = "30%"> <img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E7%99%BB%E9%99%86%E6%88%90%E5%8A%9F.jpg" width="30%" height = "30%">
<img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E8%A7%86%E9%A2%91%E8%AF%A6%E6%83%85.jpg" width="30%" height = "30%"> <img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E9%A6%96%E9%A1%B5.jpg" width="30%" height = "30%"> <img src="https://github.com/ahaliulang/Graduation-Project/blob/master/screenshot/%E8%A7%86%E9%A2%91%E6%92%AD%E6%94%BE.jpg" width="60%" height = "60%">
# 功能架构
主要分为三大模块
- 首页
通过一个列表(RecyclerView对课程信息的加载)，实现上拉刷新，下拉加载
- 分类 
上部分循环滚动显示热门课程，下面对课程信息对分类显示
- 我的
用户管理，包括个人信息的查看与修改，密码修改，个人登陆，头像修改等功能

服务器与客户端之间的数据交互主要是通过json传输，对实时性不强的数据进行自动缓存处理，主要是缓存到realm数据库中。由于有网络交互，做了一些弱网无网状态下的处理
