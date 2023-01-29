const getters = {
  isMobile: state => state.app.isMobile,
  lang: state => state.app.lang,
  theme: state => state.app.theme,
  color: state => state.app.color,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  nickname: state => state.user.name,
  welcome: state => state.user.welcome,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  userInfo: state => state.user.info,
  menus: state => state.permission.menus,
  routers: state => state.permission.routers,
  multiTab: state => state.app.multiTab,
  cachedViews: state => state.tagsView.cachedViews,
  drawingItems: state => state.app.drawingItems,
  drawingItemsVersion: state => state.app.drawingItemsVersion,
  idGlobal: state => state.app.idGlobal,
  treeNodeId: state => state.app.treeNodeId,
  formConf: state => state.app.formConf,
  lang: state => state.app.lang
}

export default getters
