import { mapState } from 'vuex'

const baseMixin = {
  computed: {
    ...mapState({
      layout: state => state.app.layout,
      navTheme: state => state.app.theme,
      primaryColor: state => state.app.color,
      colorWeak: state => state.app.weak,
      fixedHeader: state => state.app.fixedHeader,
      fixedSidebar: state => state.app.fixedSidebar,
      contentWidth: state => state.app.contentWidth,

      isMobile: state => state.app.isMobile,
      sideCollapsed: state => state.app.sideCollapsed,
      multiTab: state => state.app.multiTab,
      hideFooter: state => state.app.hideFooter,
      drawingItems: state => state.app.drawingItems,
      drawingItemsVersion: state => state.app.drawingItemsVersion,
      idGlobal: state => state.app.idGlobal,
      treeNodeId: state => state.app.treeNodeId,
      formConf: state => state.app.formConf,
      lang: state => state.app.lang
    }),
    isTopMenu() {
      return this.layout === 'topmenu'
    }
  },
  methods: {
    isSideMenu() {
      return !this.isTopMenu
    }
  }
}

export {
  baseMixin
}
